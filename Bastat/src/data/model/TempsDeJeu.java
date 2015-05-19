package com.enseirb.pfa.bastats.data.model;

public class TempsDeJeu {

    private static final int NO_ID = -1;

    private int id;
	private int matchId;
	private int numeroQT; // récupérable dans l'interface
    private String chronometre; // récupérable dans l'interface
	private int nbFauteEquipeA; // récupérable dans l'interface ou se calcul
    private int nbFauteEquipeB; // récupérable dans l'interface ou se calcul
    private String libelle;

    //constructor
    public TempsDeJeu(){
        setId(NO_ID);
        setChronometre("");
        setNumeroQT(NO_ID);
        setMatchId(NO_ID);
        setLibelle("");
        setNbFauteEquipeA(NO_ID);
        setNbFauteEquipeB(NO_ID);
    }

	public TempsDeJeu(int numeroQT, String chronometre, int nbFauteEquipeA, int nbFauteEquipeB) {
        this.setNumeroQT(numeroQT);
        this.setChronometre(chronometre);
        this.setNbFauteEquipeA(nbFauteEquipeA);
        this.setNbFauteEquipeB(nbFauteEquipeB);
	}

    public TempsDeJeu(int matchId,int numeroQT, String chronometre, int nbFauteEquipeA, int nbFauteEquipeB) {
        this.setNumeroQT(numeroQT);
        this.setChronometre(chronometre);
        this.setNbFauteEquipeA(nbFauteEquipeA);
        this.setNbFauteEquipeB(nbFauteEquipeB);
        this.matchId=matchId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getNumeroQT() {
        return numeroQT;
    }

    public void setNumeroQT(int numeroQT) {
        this.numeroQT = numeroQT;
    }

    public int getNbFauteEquipeA() {
        return nbFauteEquipeA;
    }

    public void setNbFauteEquipeA(int nbFauteEquipeA) {
        this.nbFauteEquipeA = nbFauteEquipeA;
    }

    public int getNbFauteEquipeB() {
        return nbFauteEquipeB;
    }

    public void setNbFauteEquipeB(int nbFauteEquipeB) {
        this.nbFauteEquipeB = nbFauteEquipeB;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    @Override
    public String toString(){
        return "QT: "+ numeroQT +"\tChrono: "+getChronometre()+"\tFautes A: "+nbFauteEquipeA+
                "\tFautes B: "+getNbFauteEquipeB()+"\tMatchId: " +getMatchId()+"\tId: "+ getId();
    }

    public String getChronometre() {
        return chronometre;
    }

    public void setChronometre(String chronometre) {
        this.chronometre = chronometre;
    }
}