package com.enseirb.pfa.bastats.data.model;

/**
 * Created by dohko on 13/02/15.
 */
public class PouleMatch {
    private static int NO_ID = -1;

    private int pouleId;
    private int matchId;


    public PouleMatch(){
        pouleId=NO_ID;
        matchId=NO_ID;


    }





    public int getPouleId() {
        return pouleId;
    }

    public void setPouleId(int pouleId) {
        this.pouleId = pouleId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }
}
