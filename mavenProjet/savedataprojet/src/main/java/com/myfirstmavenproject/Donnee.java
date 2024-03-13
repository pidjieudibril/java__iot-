package com.myfirstmavenproject;

public class Donnee {
    private String nomAppareil;
    private String typeAppareil;
    private int idMicrocontroleur;
    private String etatFonctionnement;
    private String donneApp;

    public Donnee(String nomAppareil, String typeAppareil,String etatFonctionnement, int idMicrocontroleur) {
        this.nomAppareil = nomAppareil;
        this.typeAppareil = typeAppareil;
        this.etatFonctionnement = etatFonctionnement;
        this.idMicrocontroleur = idMicrocontroleur;
    }
  public Donnee(String nomAppareil, String typeAppareil,String etatFonctionnement) {
        this.nomAppareil = nomAppareil;
        this.typeAppareil = typeAppareil;
        this.etatFonctionnement = etatFonctionnement;
        
    }
    // Getters et setters

    public String getNomAppareil() {
        return nomAppareil;
    }

    public void setNomAppareil(String nomAppareil) {
        this.nomAppareil = nomAppareil;
    }

    public String getEtatFonctionnement() {
        return etatFonctionnement;
    }

    public void setEtatFonctionnement(String etatFonctionnement) {
        this.etatFonctionnement = etatFonctionnement;
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
