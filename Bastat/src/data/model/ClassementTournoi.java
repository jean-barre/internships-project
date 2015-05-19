package com.enseirb.pfa.bastats.data.model;

import com.enseirb.pfa.bastats.tournoi.algorithme.Classement;

/**
 * Created by rchabot on 13/03/15.
 */
public class ClassementTournoi {
    public static final int NO_ID=-1;

    private int id;
    private int equipeId;
    private int place;
    private String regleEquipe;
    private int tournoiId;
    private int points;

    public ClassementTournoi(){

    }

    public ClassementTournoi(int equipeId, int tournoiId, int place, int points){
        setId(NO_ID);
        setEquipeId(equipeId);
        setTournoiId(tournoiId);
        setPlace(place);
        setPoints(points);
        setRegleEquipe("");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(int equipeId) {
        this.equipeId = equipeId;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getRegleEquipe() {
        return regleEquipe;
    }

    public void setRegleEquipe(String regleEquipe) {
        this.regleEquipe = regleEquipe;
    }

    public int getTournoiId() {
        return tournoiId;
    }

    public void setTournoiId(int tournoiId) {
        this.tournoiId = tournoiId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public String toString(){
        return "Place: "+getPlace()+" Equipe: "+getEquipeId()+" Points:"+getPoints();
    }
}
