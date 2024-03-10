package com.myfirstmavenproject;

public class Donnee {
    private String nomAppareil;
    private String typeAppareil;
    private int idMicrocontroleur;

    public Donnee(String nomAppareil, String typeAppareil, int idMicrocontroleur) {
        this.nomAppareil = nomAppareil;
        this.typeAppareil = typeAppareil;
        this.idMicrocontroleur = idMicrocontroleur;
    }

    // Getters et setters

    public String getNomAppareil() {
        return nomAppareil;
    }

    public void setNomAppareil(String nomAppareil) {
        this.nomAppareil = nomAppareil;
    }

    public String getTypeAppareil() {
        return typeAppareil;
    }

    public void setTypeAppareil(String typeAppareil) {
        this.typeAppareil = typeAppareil;
    }

    public int getIdMicrocontroleur() {
        return idMicrocontroleur;
    }

    public void setIdMicrocontroleur(int idMicrocontroleur) {
        this.idMicrocontroleur = idMicrocontroleur;
    }
}
