package com.enseirb.pfa.bastats.data.model;

/**
 * Created by dohko on 13/02/15.
 */
public class Departage {
    private static int NO_ID = -1;

    private int pouleId;
    private int equipeId;
    private int points;


    public Departage(){
        pouleId=NO_ID;
        equipeId=NO_ID;
        points=0;


    }



    public int getPouleId() {
        return pouleId;
    }

    public void setPouleId(int pouleId) {
        this.pouleId = pouleId;
    }

    public int getEquipeId() {
        return equipeId;
    }

    public void setEquipeId(int equipeId) {
        this.equipeId = equipeId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
