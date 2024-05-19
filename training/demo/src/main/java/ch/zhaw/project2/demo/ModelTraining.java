package ch.zhaw.textanalysis.demo;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;
import ai.djl.training.DefaultTrainingConfig;
import ai.djl.training.Trainer;
import ai.djl.training.dataset.ArrayDataset;
import ai.djl.training.dataset.Batch;
import ai.djl.training.listener.TrainingListener;
import ai.djl.training.loss.Loss;
import ai.djl.translate.TranslateException;
import ai.djl.training.evaluator.Accuracy;
import ai.djl.nn.Block;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.training.EasyTrain;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ModelTraining {

    public static void main(String[] args) throws IOException, ModelException, TranslateException, CsvValidationException {
        NDManager manager = NDManager.newBaseManager();

        // Load dataset
        List<String[]> trainData = DataPreparation.loadCSV("C:\\Users\\joele\\Desktop\\MDM_Project2\\Data_Train_cleaned.csv");

        // Convert dataset to NDArray
        float[][] trainFeatures = new float[trainData.size()][];
        float[] trainLabels = new float[trainData.size()];
        for (int i = 0; i < trainData.size(); i++) {
            trainFeatures[i] = preprocessText(trainData.get(i)[1]);
            trainLabels[i] = Float.parseFloat(trainData.get(i)[0]);
        }

        ArrayDataset trainDataset = new ArrayDataset.Builder()
                .setData(manager.create(trainFeatures))
                .optLabels(manager.create(trainLabels))
                .setSampling(32, true)
                .build();

        // Define model
        Block block = new SequentialBlock()
                .add(Linear.builder().setUnits(100).build())
                .add(Linear.builder().setUnits(2).build());

        Model model = Model.newInstance("text-classification");
        model.setBlock(block);

        // Training configuration
        DefaultTrainingConfig config = new DefaultTrainingConfig(Loss.softmaxCrossEntropyLoss())
                .addEvaluator(new Accuracy())
                .addTrainingListeners(TrainingListener.Defaults.logging());

        Trainer trainer = model.newTrainer(config);

        // Training loop
        trainer.initialize(new Shape(32, 100)); // Assuming batch size 32 and feature size 100

        for (int epoch = 0; epoch < 10; epoch++) {
            for (Batch batch : trainer.iterateDataset(trainDataset)) {
                try {
                    EasyTrain.trainBatch(trainer, batch);
                    trainer.step();
                } finally {
                    batch.close();
                }
            }
            trainer.notifyListeners(listener -> listener.onEpoch(trainer));
        }
        model.save(Paths.get("build/model"), "text-classification");
    }

    static float[] preprocessText(String text) {
        // Implement text preprocessing and feature extraction
        return new float[100]; // Dummy implementation
    }
}
