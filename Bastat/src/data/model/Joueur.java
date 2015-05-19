package com.enseirb.pfa.bastats.data.model;


public class Joueur {

    public static final int NO_ID = -1;
    public static final String NO_NUM = "";

    private int     id;
    private String  nom;
    private String  prenom;
    private String  pseudo;
    private String numero;
    //private int equipeId;

    public Joueur(){
        this.id = NO_ID;
        this.nom = "";
        this.prenom = "";
        //setEquipeId(NO_ID);
        setNumero("");
    }

    public Joueur(Joueur joueur){
        setId(joueur.getId());
        setNom(joueur.getNom());
        setPrenom(joueur.getPrenom());
        setNumero(joueur.getNumero());
        setPseudo(joueur.getPseudo());
    }

/*    public Joueur(String pseudo, String numero) {
        this.id = NO_ID;
        this.pseudo = pseudo;
        this.nom = "";
        this.prenom = "";
        //setEquipeId(equipeId);
        setNumero(numero);
    }*/

    public Joueur(String nom, String prenom, String numero) {
        this.id = NO_ID;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = "";
        //setEquipeId(equipeId);
        setNumero(numero);
    }

    public Joueur(String nom, String prenom, String pseudo, String numero) {
        this.id = NO_ID;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        //setEquipeId(equipeId);
        setNumero(numero);
    }

    public Joueur(String nom, String prenom) {
        this.id = NO_ID;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = "";
        //etEquipeId(equipeId);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String toString(){
        return "Numéro: "+getNumero()+ "\tNom: "+nom+"\tPrenom: "+prenom+"\tPseudo :"+getPseudo()+"\tId: "+id;
                //+ "\tEquipe: "+getEquipeId();
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    /*public int getEquipeId() {
        return equipeId;
    }*/

    /*public void setEquipeId(int equipeId) {
        this.equipeId = equipeId;
    }*/
}
