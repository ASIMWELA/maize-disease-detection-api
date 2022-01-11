package com.detection.maize.disease.cnnmodel.impl;

import com.detection.maize.disease.cnnmodel.CnnModelService;
import com.detection.maize.disease.disease.payload.GetDiseaseResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

@Service
@Slf4j
public class CnnModelServiceImpl implements CnnModelService {

    @Override
    @SneakyThrows
    public MultiLayerNetwork loadModel() {

        //Where to save the network. Note: the file is in .zip format - can be opened externally
        //Load the model
        File modelLocation = new ClassPathResource("maize-disease-model.zip").getFile();
        return MultiLayerNetwork.load(modelLocation, false);
    }

    @Override
    @SneakyThrows
    public ResponseEntity<GetDiseaseResponse> detectDisease(MultipartFile image) {
        int height = 215;
        int width = 215;
        int channels = 3;

        MultiLayerNetwork model = this.loadModel();

        NativeImageLoader loader = new NativeImageLoader(height, width, channels);

        INDArray inputImage = loader.asMatrix(image.getInputStream());

        log.info(Arrays.toString(inputImage.shape()));

        DataNormalization scalar = new ImagePreProcessingScaler(0, 1);
        scalar.transform(inputImage);

        //Key: [gray_leaf_spot, common_rust, nothern_leaf_blight, heathy]
        INDArray output = model.output(inputImage.reshape(new int[]{1,3,215, 215}));
        log.info("Evaluate model....");
        log.info(output.toString());
        return null;
    }
}
