package com.enseirb.pfa.bastats.data.model;

public class Poule {
    private static final int NO_ID = -1;

    public static final int ETAT_EN_ATTENTE=1;
    public static final int ETAT_EN_COURS = 2;
    public static final int ETAT_TERMINE=3;

	private int id;
	private String libelle;
	private int phasePouleId;
	private int etat;
	private int pointsVictoire;
	private int pointsDefaite;
	private int pointsNul;
	private int goalAverageEcartMax;


    //constructor
	public Poule() {

	}

    public Poule(Poule poule){
        setLibelle(poule.getLibelle());
        setPhasePouleId(poule.getPhasePouleId());
        setPointsVictoire(poule.getPointsVictoire());
        setPointsNul(poule.getPointsNul());
        setPointsDefaite(poule.getPointsDefaite());
        setEtat(poule.getEtat());
        setGoalAverageEcartMax(poule.getGoalAverageEcartMax());
        setId(poule.getId());
    }

    public Poule(String name, int phasePouleId, int ptsVictoire, int ptsNul, int ptsDefaite, int etat){
        setLibelle(name);
        setPhasePouleId(phasePouleId);
        setPointsVictoire(ptsVictoire);
        setPointsNul(ptsNul);
        setPointsDefaite(ptsDefaite);
        setEtat(etat);
        setGoalAverageEcartMax(NO_ID);
        setId(NO_ID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getPhasePouleId() {
        return phasePouleId;
    }

    public void setPhasePouleId(int phasePouleId) {
        this.phasePouleId = phasePouleId;
    }

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public int getPointsVictoire() {
        return pointsVictoire;
    }

    public void setPointsVictoire(int pointsVictoire) {
        this.pointsVictoire = pointsVictoire;
    }

    public int getPointsDefaite() {
        return pointsDefaite;
    }

    public void setPointsDefaite(int pointsDefaite) {
        this.pointsDefaite = pointsDefaite;
    }

    public int getPointsNul() {
        return pointsNul;
    }

    public void setPointsNul(int pointsNul) {
        this.pointsNul = pointsNul;
    }

    public int getGoalAverageEcartMax() {
        return goalAverageEcartMax;
    }

    public void setGoalAverageEcartMax(int goalAverageEcartMax) {
        this.goalAverageEcartMax = goalAverageEcartMax;
    }

    @Override
    public String toString(){
        return "Name: "+getLibelle()+" id:"+getId()+" Etat:"+getEtat()
                +"\n V:"+getPointsVictoire()+ " N:"+getPointsNul()+" D:"+getPointsDefaite()
                +"\n liée à la phase: "+getPhasePouleId();
    }
}