package com.enseirb.pfa.bastats.data.model;

public class Phase {
    public static final int TYPE_POULE = 1;
    public static final int TYPE_TABLEAU = 2;
    public static final int TYPE_MATCH_LIBRE = 3;

	private int id;
	private int tournoiId;
	private String libelle;
	private String date;
	private int ordreTemporel;
	private int type; // Poule ou Tableau
	//private int specifiqueId;

    //constructor
	public Phase() {

	}

    public Phase(int tournoiId, int ordreTemporel, String nomPhase, int type){
        setTournoiId(tournoiId);
        setOrdreTemporel(ordreTemporel);
        setLibelle(nomPhase);
        setType(type);
        setDate("00/00/00");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTournoiId() {
        return tournoiId;
    }

    public void setTournoiId(int tournoiId) {
        this.tournoiId = tournoiId;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrdreTemporel() {
        return ordreTemporel;
    }

    public void setOrdreTemporel(int ordreTemporel) {
        this.ordreTemporel = ordreTemporel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return getLibelle();
    }
}