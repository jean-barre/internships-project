package com.enseirb.pfa.bastats.data.model;

import java.util.List;

public class PhasePoule {

    public static final int NO_ID = -1;

    private int id;
	//private List<Poule> poules;
    private int phaseId;
	private int nbPoule;
	private int nbPeriodeMatch;
	private String dureePeriodeMatch;

    //constructor
	public PhasePoule() {
        setId(NO_ID);
        setPhaseId(NO_ID);
        setDureePeriodeMatch("00:00");
        setNbPoule(0);
        setNbPeriodeMatch(0);
	}

    public PhasePoule(int phaseId, int nbPoules, int nbPeriodes, String dureePeriode) {
        setId(NO_ID);
        setNbPoule(nbPoules);
        setNbPeriodeMatch(nbPeriodes);
        setDureePeriodeMatch(dureePeriode);
        setPhaseId(phaseId);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public int getNbPoule() {
        return nbPoule;
    }

    public void setNbPoule(int nbPoule) {
        this.nbPoule = nbPoule;
    }

    public int getNbPeriodeMatch() {
        return nbPeriodeMatch;
    }

    public void setNbPeriodeMatch(int nbPeriodeMatch) {
        this.nbPeriodeMatch = nbPeriodeMatch;
    }

    public String getDureePeriodeMatch() {
        return dureePeriodeMatch;
    }

    public void setDureePeriodeMatch(String dureePeriodeMatch) {
        this.dureePeriodeMatch = dureePeriodeMatch;
    }
}