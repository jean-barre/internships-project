package com.enseirb.pfa.bastats.data.model.action;


import com.enseirb.pfa.bastats.data.model.Joueur;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;

public class Action {
    public static final int TYPE_GENERIQUE= 0;
    public static final int TYPE_SHOOT=1;
    public static final int TYPE_FAUTE=2;
    public static final int TYPE_PASSE=3;
    public static final int TYPE_REBOND= 4;
    public static final int TYPE_CONTRE= 5;
    public static final int TYPE_INTERCEPTION= 6;

    public static final int NO_ID = -1;
	
    private int        id;
    private int        tempsDeJeu;
    private int        joueurActeur;
    private int        type;
    private String     commentaire;
    //private int        specifique;


    //constructor
    
    public Action() {
	    setActionId(NO_ID);
        setTempsDeJeu(NO_ID);
        setJoueurActeur(NO_ID);
    }
    

    public Action(Action a){
	    this.id=a.id;
        this.tempsDeJeu=a.tempsDeJeu;
        this.joueurActeur=a.joueurActeur;
        this.type=a.type;
        this.commentaire=a.commentaire;

    }

    public Action(Joueur acteur, TempsDeJeu tempsDeJeu) {
	    this.setJoueurActeur(acteur.getId());
        this.setTempsDeJeu(tempsDeJeu.getId());
    }


    public int getActionId() {
        return id;
    }

    public void setActionId(int id) {
        this.id = id;
    }

    public int getTempsDeJeu() {
        return tempsDeJeu;
    }

    public void setTempsDeJeu(int tempsDeJeu) {
        this.tempsDeJeu = tempsDeJeu;
    }

    public int getJoueurActeur() {
        return joueurActeur;
    }

    public void setJoueurActeur(int joueurActeur) {
        this.joueurActeur = joueurActeur;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString(){
        return "TempsDeJeuId: "+ getTempsDeJeu() +"\tJoueurActeurId: "+getJoueurActeur()+"\tId: "+getActionId();
    }
}

