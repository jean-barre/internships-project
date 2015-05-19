package com.enseirb.pfa.bastats.data.model.action;

import com.enseirb.pfa.bastats.data.model.TempsDeJeu;
import com.enseirb.pfa.bastats.data.model.Joueur;

public class Shoot extends Action{

    public static int NO_ID = -1;
    public static final int REUSSI = 1;
    public static final int RATE = 0;
    // 4 champs action
    private int     id;




    private int     pts; // 1pts, 2pts ou 3pts
    private int     reussi;

    public Shoot(){
        init();

    }

    public Shoot(Action a){
       super(a);
        init();

    }

    private void init(){

        setPts(NO_ID);
        setReussi(NO_ID);
        setCommentaire("");

    }



    public Shoot(TempsDeJeu tempsDeJeu1, Joueur joueur, int pts, int reussi){
        setTempsDeJeu(tempsDeJeu1.getId());
        setJoueurActeur(joueur.getId());
        setPts(pts);
        setReussi(reussi);
        setCommentaire("");
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int valeur) {
        this.pts = valeur;
    }

    public int getReussi() {
        return reussi;
    }

    public void setReussi(int reussi) {
        this.reussi = reussi;
    }



    @Override
    public String toString(){
        return "actionId" + getActionId()+ "\tJoueur: "+getJoueurActeur()+"\tTir: "+getPts()+"\tRéussi: "
                +getReussi()+"\tId: "+getId();
    }
}
