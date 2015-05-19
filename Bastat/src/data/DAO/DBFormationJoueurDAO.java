package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.FormationJoueur;
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dohko on 12/02/15.
 */
public class DBFormationJoueurDAO extends  BaseDAO {

    public static final String TABLE_NAME = "FORMATION_JOUEUR";

    public static final String FORMATION_ID_FIELD_NAME = "FORMATION_ID";
    public static final String JOUEUR_ID_FIELD_NAME = "JOUEUR_ID";
    public static final String NUMERO_FIELD_NAME = "NUMERO";

    private static final String FORMATION_ID_FIELD_TYPE = "INTEGER";
    private static final String JOUEUR_ID_FIELD_TYPE = "INTEGER";
    private static final String NUMERO_FIELD_TYPE = "TEXT";

    private static final int NUM_COL_FORMATION_ID = 0;
    private static final int NUM_COL_JOUEUR_ID = 1;
    private static final int NUM_COL_NUMERO = 2;

    public static final String CREATE_TABLE_STATEMENT = FORMATION_ID_FIELD_NAME + " " + FORMATION_ID_FIELD_TYPE
            + ", " + JOUEUR_ID_FIELD_NAME + " " + JOUEUR_ID_FIELD_TYPE
            + ", " + NUMERO_FIELD_NAME + " " + NUMERO_FIELD_TYPE
            + ", " + "PRIMARY KEY(" + FORMATION_ID_FIELD_NAME + ", " + JOUEUR_ID_FIELD_NAME + ")"
            + ", " + "FOREIGN KEY (" + FORMATION_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBFormationDAO.TABLE_NAME + "(ID)"
            + ", " + "FOREIGN KEY (" + JOUEUR_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBJoueurDAO.TABLE_NAME + "(ID)";


    public DBFormationJoueurDAO(Context context) {
        super(context);
        super.mDb = this.open();
    }


    public String FormationsJoueurJSON(int formationId,List<Joueur> joueurs){
        StringBuilder str=new StringBuilder();

        for(Joueur j: joueurs) {


                    String formationJson = FormationJoueurJSON(formationId,j.getId());

                    str.append(formationJson);

                }
        return str.toString();
        }







    public String FormationJoueurJSON(int formationId,int joueurId){
        StringBuilder str=new StringBuilder("{\"formationJoueur\" :{");
        Cursor c = super.mDb.query(TABLE_NAME, null,
                FORMATION_ID_FIELD_NAME +" = "+formationId+" AND "+JOUEUR_ID_FIELD_NAME+" = "+joueurId
                , null, null, null, null);
        c.moveToFirst();



        for(int i=0;i<c.getColumnCount();i++) {
            String columnName=c.getColumnName(i), columnValue=c.getString(i);
            str.append("\""+columnName+"\": "+"\""+columnValue+"\""+" ,\n");

        }
        str.append("}\n}\n");

        return str.toString();
    }



    public long insert(FormationJoueur formationJoueur){
        ContentValues values = new ContentValues();

        values.put(FORMATION_ID_FIELD_NAME,formationJoueur.getFormationId());
        values.put(JOUEUR_ID_FIELD_NAME,formationJoueur.getJoueurId());
        values.put(NUMERO_FIELD_NAME,formationJoueur.getNumero());
        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public long insert(int formationId, int joueurId){
        ContentValues values = new ContentValues();

        values.put(FORMATION_ID_FIELD_NAME,formationId);
        values.put(JOUEUR_ID_FIELD_NAME,joueurId);
        values.put(NUMERO_FIELD_NAME,"");
        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int formationId,int joueurId, String numero){
        ContentValues values = new ContentValues();
        values.put(NUMERO_FIELD_NAME, numero);


        return super.mDb.update(TABLE_NAME, values, FORMATION_ID_FIELD_NAME + " = " + formationId+" AND "+JOUEUR_ID_FIELD_NAME+ " = " + joueurId, null);
    }


    public int removeWithId(int formationId,int joueurId){
        return super.mDb.delete(TABLE_NAME, FORMATION_ID_FIELD_NAME + " = " + formationId+" AND "+JOUEUR_ID_FIELD_NAME+ " = " + joueurId, null);
    }

    public FormationJoueur getWithId(int formationId,int joueurId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                FORMATION_ID_FIELD_NAME +" = "+formationId+" AND "+JOUEUR_ID_FIELD_NAME+" = "+joueurId
                , null, null, null, null);
        return cursorToFormationJoueur(c);
    }

    public List<FormationJoueur> getWithId(int formationId){
        Cursor c = super.mDb.query(TABLE_NAME, null,FORMATION_ID_FIELD_NAME + " = " + formationId,
                null, null, null, null);
        return cursorToListFormationJoueur(c);
    }


    public List<FormationJoueur> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListFormationJoueur(c);
    }


    private List<FormationJoueur> cursorToListFormationJoueur(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<FormationJoueur> listeFormationJoueurs = new ArrayList<FormationJoueur>();
        listeFormationJoueurs.clear();

        if (c.moveToFirst()) {
            do {
                FormationJoueur formationJoueur = new FormationJoueur();
                formationJoueur.setFormationId(c.getInt(NUM_COL_FORMATION_ID));
                formationJoueur.setJoueurId(c.getInt(NUM_COL_JOUEUR_ID));
                formationJoueur.setNumero(c.getString(NUM_COL_NUMERO));
                listeFormationJoueurs.add(formationJoueur);
            } while (c.moveToNext());
        }
        c.close();
        return listeFormationJoueurs;
    }


    private FormationJoueur cursorToFormationJoueur(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        FormationJoueur formationJoueur = new FormationJoueur();
        formationJoueur.setFormationId(c.getInt(NUM_COL_FORMATION_ID));
        formationJoueur.setJoueurId(c.getInt(NUM_COL_JOUEUR_ID));
        formationJoueur.setNumero(c.getString(NUM_COL_NUMERO));


        c.close();

        return formationJoueur;
    }








}














