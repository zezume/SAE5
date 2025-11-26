package com.example.jpp.model;

public class Question {
    private Long idQuestion;
    private String intitule;
    private String description;
    private String type; // Using String to simplify, matches "CHECKBOX_RADIO"

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getIntitule() {
        return intitule;
    }

    public void setIntitule(String intitule) {
        this.intitule = intitule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private java.util.List<Reponse> reponses;

    public java.util.List<Reponse> getReponses() {
        return reponses;
    }

    public void setReponses(java.util.List<Reponse> reponses) {
        this.reponses = reponses;
    }
}
