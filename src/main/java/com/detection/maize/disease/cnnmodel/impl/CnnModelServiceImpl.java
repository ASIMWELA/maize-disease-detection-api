package com.detection.maize.disease.cnnmodel.impl;

import com.detection.maize.disease.cnnmodel.CnnModelResponse;
import com.detection.maize.disease.cnnmodel.CnnModelService;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import com.detection.maize.disease.disease.entity.SymptomEntity;
import com.detection.maize.disease.disease.payload.CnnModelSecondProbableDisease;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import com.detection.maize.disease.disease.repositoy.DiseaseRepository;
import com.detection.maize.disease.exception.EntityNotFoundException;
import com.detection.maize.disease.exception.OperationNotAllowedException;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author Augustine Simwela
 *<p>
 * CnnModelService that deals with loading and
 * carrying out the inference of the sent image
 *
 * The class depends on @link DiseaseRe
 */

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CnnModelServiceImpl implements CnnModelService {

    /**
     * Dao for accessing diseases from the database
     */
    private DiseaseRepository diseaseRepository;

    /**
     * CnnModelService constructor that takes disease repository for fetching
     * and returning the disease inference
     *
     * @param diseaseRepository communicates with the database using JPA
     */
    public CnnModelServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }


    /**
     * loads and return the multilayer network located in the resources folder
     * @return {@link MultiLayerNetwork}
     */
    @Override
    @SneakyThrows
    public MultiLayerNetwork loadModel() {
        File modelLocation = new File("model.zip");
        if(!modelLocation.exists()){
            try{
                InputStream ioStream = this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("maize-model.zip");
             FileCopyUtils.copy(IOUtils.toByteArray(ioStream), modelLocation);
            }catch (EOFException exception){
                    exception.printStackTrace();
            }

        }
        //Where to save the network. Note: the file is in .zip format - can be opened externally
        //Load the model
        return ModelSerializer.restoreMultiLayerNetwork(modelLocation, false);
    }

    /**
     * Gets an image and returns the top two diseases
     * with the highest probabilities by using the loaded model
     *
     * Uses loadModel method for loading the method
     *
     * @param image of class  {@link MultipartFile} file
     *
     * @return {@link CnnModelResponse}
     */
    @Override
    @SneakyThrows
    public ResponseEntity<CnnModelResponse> detectDisease(MultipartFile image) {
        int height = 200;
        int width = 200;
        int channels = 3;

        if (image.isEmpty()) {
            throw new IOException("Image cannot be null");
        }
        if (ImageIO.read(image.getInputStream()) == null) {
            throw new OperationNotAllowedException("The file is not an image");
        }

        MultiLayerNetwork model = this.loadModel();


        NativeImageLoader loader = new NativeImageLoader(height, width, channels);

        INDArray inputImage = loader.asMatrix(image.getInputStream());

        DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
        scalar.transform(inputImage);

        String[] diseaseTrainedOrder = {"Gray leaf spot", "Common rust", "Northern leaf blight", "Health"};

        INDArray output = model.output(inputImage.reshape(new int[]{1, 3, width, height}));

        double[] outputProbabilities = Arrays.stream(output.toDoubleVector()).toArray();
        log.info("Probabilities : " + Arrays.toString(outputProbabilities));
        log.info("Key : " + "{Gray leaf spot, Common rust, Northern leaf blight, Health}");
        int indexOfLarge = 0;
        double[] sorted = Arrays.stream(outputProbabilities).toArray();
        double secondPercentage = sorted[sorted.length-2];
        int indexOfSecond = 0;

        for (int i = 1; i < outputProbabilities.length; i++) {
            if (outputProbabilities[i] > outputProbabilities[indexOfLarge]) {
                indexOfLarge = i;
            }
            if(secondPercentage == outputProbabilities[i]){
                indexOfSecond = i;
            }
        }


        //highly probable
        double accuracyProbability = outputProbabilities[indexOfLarge];
        double secondAccuracy = outputProbabilities[indexOfSecond];
        String diseaseName = diseaseTrainedOrder[indexOfLarge];
        DiseaseEntity diseaseEntity = diseaseRepository.findByDiseaseName(diseaseName).orElseThrow(
                () -> new EntityNotFoundException("Oops! seems like our model doesnt recognise the disease\n consider creating an issue in the community")
        );
        List<String> symptoms = diseaseEntity.getSymptoms().stream().map(SymptomEntity::getSymptomDescription).collect(Collectors.toList());
        List<String> prescriptions = diseaseEntity.getPrescriptions().stream().map(PrescriptionEntity::getDiseasePrescription).collect(Collectors.toList());


        //less probable
        String secondDiseaseName = diseaseTrainedOrder[indexOfSecond];
        DiseaseEntity secondDiseaseEntity = diseaseRepository.findByDiseaseName(secondDiseaseName).orElseThrow(
                ()-> new EntityNotFoundException("No disease with the given identifier")
        );

        List<String> symptoms2 = secondDiseaseEntity.getSymptoms().stream().map(SymptomEntity::getSymptomDescription).collect(Collectors.toList());
        List<String> prescriptions2 = secondDiseaseEntity.getPrescriptions().stream().map(PrescriptionEntity::getDiseasePrescription).collect(Collectors.toList());


        CnnModelSecondProbableDisease secondDisease =
                CnnModelSecondProbableDisease.builder()
                        .diseaseName(secondDiseaseEntity.getDiseaseName())
                        .diseaseUuid(secondDiseaseEntity.getUuid())
                        .prescriptions(prescriptions2)
                        .symptoms(symptoms2)
                        .accuracy(String.format("%.2f", secondAccuracy * 100))
                        .build();

        GetDiseaseResponse firstDisease = GetDiseaseResponse.builder()
                .diseaseName(diseaseEntity.getDiseaseName())
                .accuracy(String.format("%.2f", accuracyProbability * 100))
                .diseaseUuid(diseaseEntity.getUuid())
                .prescriptions(prescriptions)
                .symptoms(symptoms)
                .build();
        return  ResponseEntity.ok(
                CnnModelResponse.builder()
                        .firstDisease(firstDisease)
                        .secondDisease(secondDisease)
                        .build()
        );
    }
}
