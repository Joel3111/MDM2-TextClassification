package ch.zhaw.textanalysis.demo;

import org.springframework.web.bind.annotation.*;
import ai.djl.Model;
import ai.djl.ModelException;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Block;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.core.Linear;
import ai.djl.translate.Batchifier;
import ai.djl.modality.Classifications;
import ai.djl.modality.Classifications.Classification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TextClassificationController {

    private Model model;
    private Translator<String, Classifications> translator;

    public TextClassificationController() throws IOException, ModelException {
        initializeModel();
    }

    private void initializeModel() throws IOException, ModelException {
        String modelPath = "build/model";
        String paramFileName = "text-classification-0000.params";

        if (Files.exists(Paths.get(modelPath, paramFileName))) {
            model = Model.newInstance("text-classification");

            // Manually define the model architecture
            Block block = new SequentialBlock()
                .add(Linear.builder().setUnits(100).build())
                .add(Linear.builder().setUnits(2).build());
            model.setBlock(block);

            // Load the parameters
            model.load(Paths.get(modelPath), paramFileName);

            translator = new Translator<>() {
                @Override
                public NDList processInput(TranslatorContext ctx, String input) {
                    float[] features = preprocessText(input);
                    return new NDList(ctx.getNDManager().create(features));
                }

                @Override
                public Classifications processOutput(TranslatorContext ctx, NDList list) {
                    float[] probabilities = list.singletonOrThrow().toFloatArray();
                    List<String> classNames = List.of("negative", "positive");

                    List<Classification> classificationList = new ArrayList<>();
                    for (int i = 0; i < classNames.size(); i++) {
                        classificationList.add(new Classification(classNames.get(i), probabilities[i]));
                    }

                    return new Classifications(classificationList);
                }

                @Override
                public Batchifier getBatchifier() {
                    return null;
                }
            };
        } else {
            throw new IOException("Model parameter file does not exist.");
        }
    }

    @PostMapping("/predict")
    public Classifications predict(@RequestBody String text) throws TranslateException {
        if (model == null) {
            throw new IllegalStateException("Model not initialized");
        }
        return model.newPredictor(translator).predict(text);
    }

    private float[] preprocessText(String text) {
        // Implement your text preprocessing here
        return new float[100]; // Dummy implementation
    }
}
