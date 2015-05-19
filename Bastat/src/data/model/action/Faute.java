package com.enseirb.pfa.bastats.data.model.action;

import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;



public class Faute  extends Action{
    public static final String PERSONELLE="P";
    public static final String TECHNIQUE="T";
    public static final String ANTISPORTIVE="A";
    public static final String DISQUALIFIANTE="D";

    private static int NO_ID = -1;

    private int id;

    private int joueurCible;

    public Faute() {
        setTempsDeJeu(NO_ID);
        setJoueurActeur(NO_ID);
        setCommentaire("");
        setJoueurCible(NO_ID);
    }

    public Faute(TempsDeJeu temps, Joueur joueurActeur, Joueur joueurCible) {
        setTempsDeJeu(temps.getId());
        setJoueurActeur(joueurActeur.getId());
        setCommentaire("");
        setJoueurCible(joueurCible.getId());
    }

    public Faute(TempsDeJeu temps, Joueur joueurActeur) {
        setTempsDeJeu(temps.getId());
        setJoueurActeur(joueurActeur.getId());
        setCommentaire("");
        setJoueurCible(NO_ID);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getJoueurCible() {
        return joueurCible;
    }

    public void setJoueurCible(int joueurCible) {
        this.joueurCible = joueurCible;
    }



    @Override
    public String toString(){
        return "actionId" + getActionId()+ "\tJoueur: "+getJoueurActeur()+
                "\tId: "+getId();
    }
}
