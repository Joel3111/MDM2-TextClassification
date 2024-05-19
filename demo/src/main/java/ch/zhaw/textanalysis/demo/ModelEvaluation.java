package ch.zhaw.textanalysis.demo;

import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.ndarray.NDList;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.translate.Batchifier;
import java.io.IOException;
import java.nio.file.Paths;

public class ModelEvaluation {

    public static void main(String[] args) throws IOException, ModelException, TranslateException {
        Model model = Model.newInstance("text-classification");
        model.load(Paths.get("build/model"));

        Translator<String, float[]> translator = new Translator<>() {
            @Override
            public NDList processInput(TranslatorContext ctx, String input) {
                float[] features = ModelTraining.preprocessText(input); // Verwenden der public Methode
                return new NDList(ctx.getNDManager().create(features));
            }

            @Override
            public float[] processOutput(TranslatorContext ctx, NDList list) {
                return list.singletonOrThrow().toFloatArray();
            }

            @Override
            public Batchifier getBatchifier() {
                return null;
            }
        };

        // Testen Sie das Modell
        String text = "This is a great place to visit!";
        try (ai.djl.inference.Predictor<String, float[]> predictor = model.newPredictor(translator)) {
            float[] result = predictor.predict(text);
            System.out.println("Prediction: " + result[0]);
        }
    }
}
