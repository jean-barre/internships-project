package com.enseirb.pfa.bastats.data.model;

public class PhaseTableau {

    public static final int EN_COURS = 2;
    public static final int TERMINE = 3;
    public static final int ETAT_EN_ATTENTE=1;


    private int NO_ID = -1;

	private int id;
	private int phaseId;
	private int etat;

	public PhaseTableau() {
	}

    public PhaseTableau(int phaseId){
        setPhaseId(phaseId);
        setId(NO_ID);
        setEtat(EN_COURS);
    }

	public int getPhaseId() {
		return this.phaseId;
	}

	public int getEtat() {
		return this.etat;
	}

	public void setPhaseId(int phaseId) {
		this.phaseId = phaseId;
	}

	public void setEtat(int etat) {
		this.etat = etat;
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}