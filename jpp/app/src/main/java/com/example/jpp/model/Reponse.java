package com.example.jpp.model;

public class Reponse {
    private Long idReponse;
    private Question question;
    private Utilisateur utilisateur;
    private Boolean bonne;
    private String reponse;

    public Reponse(Question question, Utilisateur utilisateur, String reponse, Boolean bonne) {
        this.question = question;
        this.utilisateur = utilisateur;
        this.reponse = reponse;
        this.bonne = bonne;
    }
    
    // Constructor for backward compatibility if needed, or just update usages
    public Reponse(Question question, Utilisateur utilisateur, String reponse) {
        this(question, utilisateur, reponse, false);
    }

    public Long getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(Long idReponse) {
        this.idReponse = idReponse;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public Boolean getBonne() {
        return bonne;
    }

    public void setBonne(Boolean bonne) {
        this.bonne = bonne;
    }

    @Override
    public String toString() {
        return reponse;
    }
}
