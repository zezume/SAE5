package com.example.jpp.model;

public class Questionnaire {
    private Long idQuestionnaire;
    private String titre;
    private String description;

    public Long getIdQuestionnaire() {
        return idQuestionnaire;
    }

    public void setIdQuestionnaire(Long idQuestionnaire) {
        this.idQuestionnaire = idQuestionnaire;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
