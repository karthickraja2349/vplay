package com.vplay.utils;

import java.io.IOException;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.nlp.qa.QAInput;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelZoo;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;

public class QAService {
    private final Predictor<QAInput, String> predictor;

    public QAService() throws IOException, ModelException {
        Criteria<QAInput, String> criteria = Criteria.builder()
                .optApplication(Application.NLP.QUESTION_ANSWER)
                .setTypes(QAInput.class, String.class)
                .optProgress(new ProgressBar())
                .build();
        ZooModel<QAInput, String> model = ModelZoo.loadModel(criteria);
        predictor = model.newPredictor();
    }

    public String answer(String question, String paragraph) throws TranslateException {
        return predictor.predict(new QAInput(question, paragraph));
    }
}


