package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.Poule;

import java.util.ArrayList;
import java.util.List;

public class DBPouleDAO extends BaseDAO{

    public static final String TABLE_NAME                         = "POULE";

    private static final String ID_FIELD_NAME           		  = "_ID";
    private static final String LIBELLE_FIELD_NAME           	  = "LIBELLE";
    private static final String PHASE_POULE_ID_FIELD_NAME         = "PHASE_POULE_ID";
    private static final String ETAT_FIELD_NAME           		  = "ETAT";
    private static final String POINTS_VICTOIRE_FIELD_NAME        = "POINTS_VICTOIRE";
    private static final String POINTS_DEFAITE_FIELD_NAME         = "POINTS_DEFAITE";
    private static final String POINTS_NUL_FIELD_NAME             = "POINTS_NUL";
    private static final String GOAL_AVERAGE_ECART_MAX_FIELD_NAME = "GOAL_AVERAGE_ECART_MAX";

    private static final String ID_FIELD_TYPE          			  = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String LIBELLE_FIELD_TYPE          	  = "TEXT NOT NULL";
    private static final String PHASE_POULE_ID_FIELD_TYPE         = "INTEGER NOT NULL";
    private static final String ETAT_FIELD_TYPE          		  = "INTEGER NOT NULL";
    private static final String POINTS_VICTOIRE_FIELD_TYPE        = "INTEGER NOT NULL";
    private static final String POINTS_DEFAITE_FIELD_TYPE         = "INTEGER NOT NULL";
    private static final String POINTS_NUL_FIELD_TYPE          	  = "INTEGER NOT NULL";
    private static final String GOAL_AVERAGE_ECART_MAX_FIELD_TYPE = "INTEGER";

    private static final int NUM_COL_ID          		  		  = 0;
    private static final int NUM_COL_LIBELLE           			  = 1;
    private static final int NUM_COL_PHASE_POULE_ID      		  = 2;
    private static final int NUM_COL_ETAT      			  		  = 3;
    private static final int NUM_COL_POINTS_VICTOIRE       		  = 4;
    private static final int NUM_COL_POINTS_DEFAITE   			  = 5;
    private static final int NUM_COL_POINTS_NUL   	  			  = 6;
    private static final int NUM_COL_GOAL_AVERAGE_ECART_MAX   	  = 7;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + LIBELLE_FIELD_NAME         		 + " " + LIBELLE_FIELD_TYPE 
	+ ", " + PHASE_POULE_ID_FIELD_NAME      	 + " " + PHASE_POULE_ID_FIELD_TYPE
    + ", " + ETAT_FIELD_NAME      				 + " " + ETAT_FIELD_TYPE
    + ", " + POINTS_VICTOIRE_FIELD_NAME      	 + " " + POINTS_VICTOIRE_FIELD_TYPE
    + ", " + POINTS_DEFAITE_FIELD_NAME   		 + " " + POINTS_DEFAITE_FIELD_TYPE
    + ", " + POINTS_NUL_FIELD_NAME   			 + " " + POINTS_NUL_FIELD_TYPE
    + ", " + GOAL_AVERAGE_ECART_MAX_FIELD_NAME   + " " + GOAL_AVERAGE_ECART_MAX_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + PHASE_POULE_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBPhasePouleDAO.TABLE_NAME+"(ID)";


    public DBPouleDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(Poule poule){
	    ContentValues values = new ContentValues();

	    values.put(LIBELLE_FIELD_NAME,                poule.getLibelle());
	    values.put(PHASE_POULE_ID_FIELD_NAME,         poule.getPhasePouleId());
        values.put(ETAT_FIELD_NAME,          		  poule.getEtat());
        values.put(POINTS_VICTOIRE_FIELD_NAME,        poule.getPointsVictoire());
        values.put(POINTS_DEFAITE_FIELD_NAME,         poule.getPointsDefaite());
        values.put(POINTS_NUL_FIELD_NAME,       	  poule.getPointsNul());
        values.put(GOAL_AVERAGE_ECART_MAX_FIELD_NAME, poule.getGoalAverageEcartMax());

	    return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, Poule poule){
	    ContentValues values = new ContentValues();

	    values.put(LIBELLE_FIELD_NAME,                poule.getLibelle());
	    values.put(PHASE_POULE_ID_FIELD_NAME,         poule.getPhasePouleId());
        values.put(ETAT_FIELD_NAME,          		  poule.getEtat());
        values.put(POINTS_VICTOIRE_FIELD_NAME,        poule.getPointsVictoire());
        values.put(POINTS_DEFAITE_FIELD_NAME,         poule.getPointsDefaite());
        values.put(POINTS_NUL_FIELD_NAME,       	  poule.getPointsNul());
        values.put(GOAL_AVERAGE_ECART_MAX_FIELD_NAME, poule.getGoalAverageEcartMax());

        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }
 
    public int removeWithId(int id){
	    return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public Poule getWithId(int id){
	    Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
	    return cursorToPoule(c);
    }

    public List<Poule> getPoules(int phasePouleId){
        Cursor c = super.mDb.query(TABLE_NAME, null, PHASE_POULE_ID_FIELD_NAME +"=" + phasePouleId,
                null, null, null, null);
        return cursorToListPoule(c);
    }

    public Poule getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToPoule(c);
    }



    private Poule cursorToPoule(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Poule poule = new Poule();
        poule.setId(c.getInt(NUM_COL_ID));
        poule.setPhasePouleId(c.getInt(NUM_COL_PHASE_POULE_ID));
        poule.setEtat(c.getInt(NUM_COL_ETAT));
        poule.setLibelle(c.getString(NUM_COL_LIBELLE));
        poule.setPointsVictoire(c.getInt(NUM_COL_POINTS_VICTOIRE));
        poule.setPointsNul(c.getInt(NUM_COL_POINTS_NUL));
        poule.setPointsDefaite(c.getInt(NUM_COL_POINTS_DEFAITE));
        poule.setGoalAverageEcartMax(c.getInt(NUM_COL_GOAL_AVERAGE_ECART_MAX));

        c.close();

        return poule;
    }

    private List<Poule> cursorToListPoule(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Poule> listePoules = new ArrayList<Poule>();
        listePoules.clear();

        if (c.moveToFirst()) {
            do {
                Poule poule = new Poule();
                poule.setId(c.getInt(NUM_COL_ID));
                poule.setPhasePouleId(c.getInt(NUM_COL_PHASE_POULE_ID));
                poule.setEtat(c.getInt(NUM_COL_ETAT));
                poule.setLibelle(c.getString(NUM_COL_LIBELLE));
                poule.setPointsVictoire(c.getInt(NUM_COL_POINTS_VICTOIRE));
                poule.setPointsNul(c.getInt(NUM_COL_POINTS_NUL));
                poule.setPointsDefaite(c.getInt(NUM_COL_POINTS_DEFAITE));
                poule.setGoalAverageEcartMax(c.getInt(NUM_COL_GOAL_AVERAGE_ECART_MAX));
                listePoules.add(poule);
            } while (c.moveToNext());
        }
        c.close();
        return listePoules;
    }
}
