package com.enseirb.pfa.bastats.data.model;

public class Tournoi {
    private static int    NO_ID = -1;

    private int    id;
    private String libelle;
    private String lieu;
    private int    nbEquipeMax;
    

    //constructor
    public Tournoi() {
        setId(NO_ID);
        setLieu("");
        setNbEquipeMax(NO_ID);
        setLieu("");
    }

    public Tournoi(String libelle, String lieu) {
        setId(NO_ID);
        setLibelle(libelle);
        setLieu(lieu);
        setNbEquipeMax(NO_ID);
    }


    public Tournoi(String libelle, String lieu, int nbEquipeMax) {
        setId(NO_ID);
        setLibelle(libelle);
        setLieu(lieu);
        setNbEquipeMax(nbEquipeMax);
    }

    public Tournoi(Tournoi t){
        setId(t.getId());
        setLibelle(t.getLibelle());
        setLieu(t.getLieu());
        setNbEquipeMax(t.getNbEquipeMax());
    }

    //getter
    public String getLibelle() {
	return this.libelle;
    }

    public String getLieu() {
	return this.lieu;
    }

    public int getNbEquipeMax() {
	return this.nbEquipeMax;
    }

    //setter
    public void setLibelle(String libelle) {
	this.libelle = libelle;
    }

    public void setLieu(String lieu) {
	this.lieu = lieu;
    }

    public void setNbEquipeMax(int nbEquipeMax) {
	this.nbEquipeMax = nbEquipeMax;
    }

    public int getId() {
        return id;
    }

    public void setId(int i){
	this.id = i;
    }


    @Override
    public String toString(){
        return "Tournoi: "+getLibelle()+" ("+getLieu()+")"+"\tId: "+getId();
    }


}
