package com.example.jpp.model;

public class Reponse {
    private Long idReponse;
    private Question question;
    private Utilisateur utilisateur;
    private String reponse;

    public Reponse(Question question, Utilisateur utilisateur, String reponse) {
        this.question = question;
        this.utilisateur = utilisateur;
        this.reponse = reponse;
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
}
