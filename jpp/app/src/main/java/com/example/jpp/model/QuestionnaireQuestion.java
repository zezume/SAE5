package com.example.jpp.model;

public class QuestionnaireQuestion {
    private QuestionnaireQuestionId id;
    private Questionnaire questionnaire;
    private Question question;
    private Integer ordre;

    public QuestionnaireQuestionId getId() {
        return id;
    }

    public void setId(QuestionnaireQuestionId id) {
        this.id = id;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Integer getOrdre() {
        return ordre;
    }

    public void setOrdre(Integer ordre) {
        this.ordre = ordre;
    }
}
