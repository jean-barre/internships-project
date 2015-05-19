package com.enseirb.pfa.bastats.data.DAO;
 
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.enseirb.pfa.bastats.data.model.Formation;
import com.enseirb.pfa.bastats.data.model.Joueur;

import java.util.ArrayList;
import java.util.List;

public class DBJoueurDAO extends BaseDAO {

    public  static final String TABLE_NAME             = "JOUEUR";

    private static final String ID_FIELD_NAME          = "_ID";
    private static final String NOM_FIELD_NAME         = "NOM";
    private static final String PRENOM_FIELD_NAME      = "PRENOM";
    private static final String PSEUDO_FIELD_NAME      = "PSEUDO";
    private static final String NUMERO_FIELD_NAME      = "NUMERO";

    private static final String ID_FIELD_TYPE          = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String NOM_FIELD_TYPE         = "TEXT";
    private static final String PRENOM_FIELD_TYPE      = "TEXT";
    private static final String PSEUDO_FIELD_TYPE      = "TEXT";
    private static final String NUMERO_FIELD_TYPE      = "TEXT";

    private static final int NUM_COL_ID          = 0;
    private static final int NUM_COL_NOM         = 1;
    private static final int NUM_COL_PRENOM      = 2;
    private static final int NUM_COL_PSEUDO      = 3;
    private static final int NUM_COL_NUMERO      = 4;


    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + NOM_FIELD_NAME         + " " + NOM_FIELD_TYPE 
	+ ", " + PRENOM_FIELD_NAME      + " " + PRENOM_FIELD_TYPE
    + ", " + PSEUDO_FIELD_NAME      + " " + PSEUDO_FIELD_TYPE
    + ", " + NUMERO_FIELD_NAME      + " " + NUMERO_FIELD_TYPE;

    public DBJoueurDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(Joueur joueur){
	    ContentValues values = new ContentValues();

	    values.put(NOM_FIELD_NAME,             joueur.getNom());
	    values.put(PRENOM_FIELD_NAME,          joueur.getPrenom());
        values.put(PSEUDO_FIELD_NAME,          joueur.getPseudo());
        values.put(NUMERO_FIELD_NAME,          joueur.getNumero());

	    return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, Joueur joueur){
	    ContentValues values = new ContentValues();
	    values.put(NOM_FIELD_NAME,             joueur.getNom());
	    values.put(PRENOM_FIELD_NAME,          joueur.getPrenom());
        values.put(PSEUDO_FIELD_NAME,          joueur.getPseudo());
        values.put(NUMERO_FIELD_NAME,          joueur.getNumero());


        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }
 
    public int removeWithId(int id){
	    return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public Joueur getWithId(int id){
	    Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
	    return cursorToJoueur(c);
    }

    public List<Joueur> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListJoueur(c);
    }

    public Joueur getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToJoueur(c);
    }

    public List<Joueur> getJoueursEquipe(int equipeId) {
        String fj = DBFormationJoueurDAO.TABLE_NAME;
        String f = DBFormationDAO.TABLE_NAME;
        String rawQuery = " SELECT *"
                +" FROM "+TABLE_NAME
                +" WHERE "+TABLE_NAME+"."+ID_FIELD_NAME+" IN "
                +" ( SELECT DISTINCT "+fj+"."+DBFormationJoueurDAO.JOUEUR_ID_FIELD_NAME
                +" FROM "+f+" , "+fj
                +" WHERE "+f+"."+DBFormationDAO.EQUIPE_ID_FIELD_NAME+"="+equipeId
                +" AND " +f+"."+DBFormationDAO.ID_FIELD_NAME+" = "+fj+"."+DBFormationJoueurDAO.FORMATION_ID_FIELD_NAME
                +" AND " +f+"."+DBFormationDAO.FORMATION_PAR_DEFAUT_FIELD_NAME+"="+Formation.FORMATION_PAR_DEFAUT+")";
        //Log.d("BDD Match", "Cherche match de l'équipe " + equipeId + " dans la poule " + pouleId);

        Cursor c = super.mDb.rawQuery(rawQuery,
                null);
        return cursorToListJoueur(c);
    }

    public List<Joueur> getLastSelection(int equipeId){
        String fj = DBFormationJoueurDAO.TABLE_NAME;
        String f = DBFormationDAO.TABLE_NAME;
        String rawQuery = " SELECT *"
                +" FROM "+TABLE_NAME
                +" WHERE "+TABLE_NAME+"."+ID_FIELD_NAME+" IN "
                +" ( SELECT DISTINCT "+fj+"."+DBFormationJoueurDAO.JOUEUR_ID_FIELD_NAME
                +" FROM "+f+" , "+fj
                +" WHERE "+f+"."+DBFormationDAO.EQUIPE_ID_FIELD_NAME+"="+equipeId
                +" AND " +f+"."+DBFormationDAO.ID_FIELD_NAME+" = "+fj+"."+DBFormationJoueurDAO.FORMATION_ID_FIELD_NAME
                +" AND " +f+"."+DBFormationDAO.FORMATION_PAR_DEFAUT_FIELD_NAME+"="+Formation.LAST+")";
        //Log.d("BDD Match", "Cherche match de l'équipe " + equipeId + " dans la poule " + pouleId);

        Cursor c = super.mDb.rawQuery(rawQuery,
                null);
        return cursorToListJoueur(c);
    }

    public List<Joueur> getFormationMatch(int formationId){
        String fj = DBFormationJoueurDAO.TABLE_NAME;
        String rawQuery = " SELECT "+TABLE_NAME+".*"
                +" FROM "+TABLE_NAME+" , "+fj
                +" WHERE "+fj+"."+DBFormationJoueurDAO.FORMATION_ID_FIELD_NAME+"="+formationId
                +" AND "+TABLE_NAME+"."+ID_FIELD_NAME+"="+fj+"."+DBFormationJoueurDAO.JOUEUR_ID_FIELD_NAME;
               /* +" ( SELECT DISTINCT "+fj+"."+DBFormationJoueurDAO.JOUEUR_ID_FIELD_NAME
                +" FROM "+fj
                +" WHERE "+fj+"."+DBFormationJoueurDAO.FORMATION_ID_FIELD_NAME+"="+formationId+")";*/

        //Log.d("BDD Match", "Cherche match de l'équipe " + equipeId + " dans la poule " + pouleId);

        Cursor c = super.mDb.rawQuery(rawQuery,
                null);
        return cursorToListJoueur(c);
    }

    private List<Joueur> cursorToListJoueur(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Joueur> listeJoueurs = new ArrayList<Joueur>();
        listeJoueurs.clear();

        if (c.moveToFirst()) {
            do {
                Joueur joueur = new Joueur();
                joueur.setId(c.getInt(NUM_COL_ID));
                joueur.setNom(c.getString(NUM_COL_NOM));
                joueur.setPrenom(c.getString(NUM_COL_PRENOM));
                joueur.setPseudo(c.getString(NUM_COL_PSEUDO));
                joueur.setNumero(c.getString(NUM_COL_NUMERO));
                listeJoueurs.add(joueur);
            } while (c.moveToNext());
        }
        c.close();
        return listeJoueurs;
    }

    private Joueur cursorToJoueur(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Joueur joueur = new Joueur();

        joueur.setId(c.getInt(NUM_COL_ID));
        joueur.setNom(c.getString(NUM_COL_NOM));
        joueur.setPrenom(c.getString(NUM_COL_PRENOM));
        joueur.setPseudo(c.getString(NUM_COL_PSEUDO));
        joueur.setNumero(c.getString(NUM_COL_NUMERO));

        c.close();

        return joueur;
    }
}
