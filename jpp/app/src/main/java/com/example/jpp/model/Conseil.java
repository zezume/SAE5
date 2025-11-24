package com.example.jpp.model;

public class Conseil {
    private Long idConseil;
    private String titre;
    private String description;
    private String type;

    public Long getIdConseil() {
        return idConseil;
    }

    public void setIdConseil(Long idConseil) {
        this.idConseil = idConseil;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
