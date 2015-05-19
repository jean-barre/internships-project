package com.enseirb.pfa.bastats.data.model;

/**
 * Created by dohko on 12/02/15.
 */
public class Formation {
    private static int NO_ID = -1;

    public static final int FORMATION_PAR_DEFAUT = 1;
    public static final int FORMATION_MATCH = 0;
    public static final int LAST = 2;

    private int id;
    private int equipeId;
    private int valeurDefaut;

    public Formation(){
        setEquipeId(NO_ID);
        setId(NO_ID);
        setValeurDefaut(FORMATION_MATCH);
    }

    public Formation(int equipeId, int estValeurParDefaut){
        setEquipeId(equipeId);
        setValeurDefaut(estValeurParDefaut);
        setId(NO_ID);
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

    public int getValeurDefaut() {
        return valeurDefaut;
    }

    public void setValeurDefaut(int valeurDefaut) {
        this.valeurDefaut = valeurDefaut;
    }
}
