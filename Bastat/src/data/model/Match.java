package com.enseirb.pfa.bastats.data.model;

import java.sql.Date;

/**
 * Created by rchabot on 25/01/15.
 */
public class Match {
    public static int NO_ID = -1;
    public static int AMICAL = -2;

    public static final int RESULTAT_NUL = 0;
    public static final int MATCH_NON_JOUE = -1;

    private int id;
    private String libelle;
    private String date;
    private int formationEquipeAId;
    private int formationEquipeBId;
    private String regleFormationA;
    private String RegleFormationB;
    private int resultat;
    private int scoreEquipeA;
    private int scoreEquipeB;
    private int phaseId;
    private String arbitreChamp;
    private String arbitreAssistant;

    //Constructor
    public Match(){

    }



    public Match(Match match){
        setId(match.getId());
        setLibelle(match.getLibelle());
        setDate(match.getDate());
        setFormationEquipeA(match.getFormationEquipeA());
        setFormationEquipeB(match.getFormationEquipeB());
        setPhaseId(match.getPhaseId());
        setResultat(match.getResultat());
        setScoreEquipeA(match.getScoreEquipeA());
        setScoreEquipeB(match.getScoreEquipeB());
    }



    public Match(String libelle, String date, int fA, int fB, int phaseId){
        setId(NO_ID);
        setLibelle(libelle);
        setDate(date);
        setFormationEquipeA(fA);
        setFormationEquipeB(fB);
        setPhaseId(phaseId);
        setResultat(MATCH_NON_JOUE);
        setScoreEquipeA(0);
        setScoreEquipeB(0);
    }

    public Match(int formationEquipeAId, int formationEquipeBId){
        setFormationEquipeA(formationEquipeAId);
        setFormationEquipeB(formationEquipeBId);
    }

    public Match(String libelle){
        setLibelle(libelle);
        setFormationEquipeA(NO_ID);
        setFormationEquipeB(NO_ID);
    }
    public Match(String libelle, int phaseId){
        setId(NO_ID);
        setLibelle(libelle);
        setDate(date);
        setFormationEquipeA(NO_ID);
        setFormationEquipeB(NO_ID);
        setPhaseId(phaseId);
        setResultat(MATCH_NON_JOUE);
        setScoreEquipeA(0);
        setScoreEquipeB(0);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getFormationEquipeA() {
        return formationEquipeAId;
    }

    public void setFormationEquipeA(int formationEquipeA) {
        this.formationEquipeAId = formationEquipeA;
    }

    public int getFormationEquipeB() {
        return formationEquipeBId;
    }

    public void setFormationEquipeB(int formationEquipeB) {
        this.formationEquipeBId = formationEquipeB;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getScoreEquipeA() {
        return scoreEquipeA;
    }

    public void setScoreEquipeA(int scoreEquipeA) {
        this.scoreEquipeA = scoreEquipeA;
    }

    public int getScoreEquipeB() {
        return scoreEquipeB;
    }

    public void setScoreEquipeB(int scoreEquipeB) {
        this.scoreEquipeB = scoreEquipeB;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public String getRegleFormationA() {
        return regleFormationA;
    }

    public void setRegleFormationA(String regleFormationA) {
        this.regleFormationA = regleFormationA;
    }

    public String getRegleFormationB() {
        return RegleFormationB;
    }

    public void setRegleFormationB(String regleFormationB) {
        RegleFormationB = regleFormationB;
    }

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    @Override
    public String toString(){
        return "MatchId: "+getId()+" Phase:"+getPhaseId()+" EquipeAid: "+getFormationEquipeA()+" scoreA "+getScoreEquipeA()
        +" vs "+getScoreEquipeB()+" EquipeBid: "+getFormationEquipeB()+" Résultat: "+getResultat()+"\n";
    }

    public String getArbitreChamp() {
        return arbitreChamp;
    }

    public void setArbitreChamp(String arbitreChamp) {
        this.arbitreChamp = arbitreChamp;
    }

    public String getArbitreAssistant() {
        return arbitreAssistant;
    }

    public void setArbitreAssistant(String arbitreAssistant) {
        this.arbitreAssistant = arbitreAssistant;
    }
}
