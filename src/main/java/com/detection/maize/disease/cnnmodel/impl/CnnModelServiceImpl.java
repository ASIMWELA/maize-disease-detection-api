package com.detection.maize.disease.cnnmodel.impl;

import com.detection.maize.disease.cnnmodel.CnnModelService;
import com.detection.maize.disease.disease.entity.DiseaseEntity;
import com.detection.maize.disease.disease.entity.PrescriptionEntity;
import com.detection.maize.disease.disease.entity.SymptomEntity;
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

@Service
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CnnModelServiceImpl implements CnnModelService {

    private DiseaseRepository diseaseRepository;

    public CnnModelServiceImpl(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }


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

    @Override
    @SneakyThrows
    public ResponseEntity<GetDiseaseResponse> detectDisease(MultipartFile image) {
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
        for (int i = 1; i < outputProbabilities.length; i++) {
            if (outputProbabilities[i] > outputProbabilities[indexOfLarge]) indexOfLarge = i;
        }
        double accuracyProbability = outputProbabilities[indexOfLarge];
        String diseaseName = diseaseTrainedOrder[indexOfLarge];
        DiseaseEntity diseaseEntity = diseaseRepository.findByDiseaseName(diseaseName).orElseThrow(
                () -> new EntityNotFoundException("Oops! seems like our model doesnt recognise the disease\n consider creating an issue in the community")
        );
        List<String> symptoms = diseaseEntity.getSymptoms().stream().map(SymptomEntity::getSymptomDescription).collect(Collectors.toList());

        List<String> prescriptions = diseaseEntity.getPrescriptions().stream().map(PrescriptionEntity::getDiseasePrescription).collect(Collectors.toList());

        return ResponseEntity.ok(GetDiseaseResponse.builder()
                .diseaseName(diseaseEntity.getDiseaseName())
                .accuracy(String.format("%.2f", accuracyProbability * 100))
                .diseaseUuid(diseaseEntity.getUuid())
                .prescriptions(prescriptions)
                .symptoms(symptoms)
                .build());
    }
}
