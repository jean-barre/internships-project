package com.enseirb.pfa.bastats.data.model.action;

import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;

/**
 * Created by dohko on 24/01/15.
 */
public class Passe extends Action{

    private static int NO_ID = -1;

    private int id;
    private int joueurCible;

    public Passe() {
        setTempsDeJeu(NO_ID);
        setJoueurActeur(NO_ID);
        setCommentaire("");
        setJoueurCible(NO_ID);
    }

    public Passe(TempsDeJeu temps, Joueur joueurActeur, Joueur joueurCible) {
        setTempsDeJeu(temps.getId());
        setJoueurActeur(joueurActeur.getId());
        setCommentaire("");
        setJoueurCible(joueurCible.getId());
    }

    public Passe(TempsDeJeu temps, Joueur joueurActeur){
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


