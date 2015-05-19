package com.enseirb.pfa.bastats.data.model;

public class Equipe {
    private static int    NO_ID = -1;

    private int    id;
    private String acronyme;
    private String nom;
    private String couleur;
    private String photo;
    

    //constructor
    public Equipe() {
        setId(NO_ID);
        setCouleur("");
        setPhoto("");
        setCouleur("");
        setAcronyme("");
    }

    // Constructeur par recopie
    public Equipe(Equipe equipe){
        setId(equipe.getId());
        setCouleur(equipe.getCouleur());
        setPhoto(equipe.getPhoto());
        setAcronyme(equipe.getAcronyme());
        setNom(equipe.getNom());
    }

    public Equipe(String nom) {
        setId(NO_ID);
        setNom(nom);
        setAcronyme("");
        setCouleur("");
        setPhoto("");
    }

    public Equipe(String nom, String couleur) {
        setId(NO_ID);
        setNom(nom);
        //setAcronyme(acronyme); // tronquer 3 premières lettre
        setCouleur(couleur);
        setPhoto("");
    }

    public Equipe(String nom, String couleur, String photo) {
	    this.nom = nom;
	    this.couleur = couleur;
	    this.photo = photo;
    }

    //getter
    public String getNom() {
	return this.nom;
    }

    public    String getCouleur() {
	return this.couleur;
    }

    public String getPhoto() {
	return this.photo;
    }

    //setter
    public void setNom(String nom) {
	this.nom = nom;
    }

    public void setCouleur(String couleur) {
	this.couleur = couleur;
    }

    public void setPhoto(String photo) {
	this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int i){
	this.id = i;
    }

    public String getAcronyme() {
        return acronyme;
    }

    public void setAcronyme(String acronyme) {
        this.acronyme = acronyme;
    }

    @Override
    public String toString(){
        return "Equipe: "+getNom()+" ("+getAcronyme()+")";
    }


}
