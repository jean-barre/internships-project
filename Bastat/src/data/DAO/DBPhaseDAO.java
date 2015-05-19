package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.Phase;

import java.util.ArrayList;
import java.util.List;

public class DBPhaseDAO extends BaseDAO{

    public static final String TABLE_NAME                 = "PHASE";

    private static final String ID_FIELD_NAME          	  = "_ID";
    private static final String TOURNOI_ID_FIELD_NAME     = "TOURNOI_ID";
    private static final String LIBELLE_FIELD_NAME        = "LIBELLE";
    private static final String DATE_FIELD_NAME           = "DATE";
    private static final String ORDRE_TEMPOREL_FIELD_NAME = "ORDRE_TEMPOREL";
    private static final String TYPE_FIELD_NAME           = "TYPE";
    //private static final String SPECIFIQUE_ID_FIELD_NAME  = "SPECIFIQUE_ID";

    private static final String ID_FIELD_TYPE          	  = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String TOURNOI_ID_FIELD_TYPE  	  = "INTEGER NOT NULL";
    private static final String LIBELLE_FIELD_TYPE     	  = "TEXT    NOT NULL";
    private static final String DATE_FIELD_TYPE        	  = "TEXT";
    private static final String ORDRE_TEMPOREL_FIELD_TYPE = "INTEGER NOT NULL";
    private static final String TYPE_FIELD_TYPE           = "INTEGER NOT NULL";
    //private static final String SPECIFIQUE_ID_FIELD_TYPE  = "INTEGER NOT NULL";

    private static final int NUM_COL_ID          		  = 0;
    private static final int NUM_COL_TOURNOI_ID           = 1;
    private static final int NUM_COL_LIBELLE      		  = 2;
    private static final int NUM_COL_DATE      			  = 3;
    private static final int NUM_COL_ORDRE_TEMPOREL       = 4;
    private static final int NUM_COL_TYPE   			  = 5;
    //private static final int NUM_COL_SPECIFIQUE_ID   	  = 6;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + TOURNOI_ID_FIELD_NAME         	+ " " + TOURNOI_ID_FIELD_TYPE 
	+ ", " + LIBELLE_FIELD_NAME      		+ " " + LIBELLE_FIELD_TYPE
    + ", " + DATE_FIELD_NAME      			+ " " + DATE_FIELD_TYPE
    + ", " + ORDRE_TEMPOREL_FIELD_NAME      + " " + ORDRE_TEMPOREL_FIELD_TYPE
    + ", " + TYPE_FIELD_NAME   				+ " " + TYPE_FIELD_TYPE
    + ", " + "FOREIGN KEY (" + TOURNOI_ID_FIELD_NAME+") "
    + "REFERENCES "+ DBTournoiDAO.TABLE_NAME+"(ID)";
    //+ ", " + SPECIFIQUE_ID_FIELD_NAME   	+ " " + SPECIFIQUE_ID_FIELD_TYPE;


    public DBPhaseDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(Phase phase){
	    ContentValues values = new ContentValues();

	    values.put(TOURNOI_ID_FIELD_NAME,       phase.getTournoiId());
	    values.put(LIBELLE_FIELD_NAME,          phase.getLibelle());
        values.put(DATE_FIELD_NAME,          	phase.getDate());
        values.put(ORDRE_TEMPOREL_FIELD_NAME,   phase.getOrdreTemporel());
        values.put(TYPE_FIELD_NAME,       		phase.getType());
        //values.put(SPECIFIQUE_ID_FIELD_NAME,    phase.getSpecifiqueId());

	    return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, Phase phase){
	    ContentValues values = new ContentValues();
	    values.put(TOURNOI_ID_FIELD_NAME,       phase.getTournoiId());
	    values.put(LIBELLE_FIELD_NAME,          phase.getLibelle());
        values.put(DATE_FIELD_NAME,          	phase.getDate());
        values.put(ORDRE_TEMPOREL_FIELD_NAME,   phase.getOrdreTemporel());
        values.put(TYPE_FIELD_NAME,       		phase.getType());
        //values.put(SPECIFIQUE_ID_FIELD_NAME,    phase.getSpecifiqueId());

        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }
 
    public int removeWithId(int id){
	    return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }
    
    public List<Phase> getAll() {
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListPhase(c);
    }

    public Phase getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"="+ id, null, null, null, null);
        return cursorToPhase(c);
    }

    public List<Phase> getTournamentPhases(int tournoiId){
        Cursor c = super.mDb.query(TABLE_NAME, null, TOURNOI_ID_FIELD_NAME+"="+tournoiId, null, null, null, null);
        if (c.getCount() == 0)
            return null;
        else return cursorToListPhase(c);
    }

    private List<Phase> cursorToListPhase(Cursor c) {
        if (c.getCount() == 0)
            return null;

        List<Phase> listePhases = new ArrayList<Phase>();
        listePhases.clear();

        if (c.moveToFirst()) {
            do {
                Phase phase = new Phase();
                phase.setId(c.getInt(NUM_COL_ID));
                phase.setTournoiId(c.getInt(NUM_COL_TOURNOI_ID));
                phase.setLibelle(c.getString(NUM_COL_LIBELLE));
                phase.setType(c.getInt(NUM_COL_TYPE));
                phase.setOrdreTemporel(c.getInt(NUM_COL_ORDRE_TEMPOREL));
                phase.setDate(c.getString(NUM_COL_DATE));

                listePhases.add(phase);
            } while (c.moveToNext());
        }
        c.close();
        return listePhases;
    }

    public Phase getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToPhase(c);
    }

    private Phase cursorToPhase(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Phase phase = new Phase();
        phase.setId(c.getInt(NUM_COL_ID));
        phase.setTournoiId(c.getInt(NUM_COL_TOURNOI_ID));
        phase.setLibelle(c.getString(NUM_COL_LIBELLE));
        phase.setType(c.getInt(NUM_COL_TYPE));
        phase.setOrdreTemporel(c.getInt(NUM_COL_ORDRE_TEMPOREL));
        phase.setDate(c.getString(NUM_COL_DATE));

        c.close();

        return phase;
    }

}
