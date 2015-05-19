package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.Phase;
import com.enseirb.pfa.bastats.data.model.PhasePoule;

import java.util.ArrayList;
import java.util.List;

public class DBPhasePouleDAO extends BaseDAO{

    public static final String TABLE_NAME                   	= "PHASE_POULE";

    private static final String ID_FIELD_NAME           		= "_ID";
    private static final String PHASE_ID_FIELD_NAME     		= "PHASE_ID";
    private static final String NB_POULE_FIELD_NAME         	= "NB_POULE";
    private static final String NB_PERIODE_MATCH_FIELD_NAME 	= "NB_PERIODE_MATCH";
    private static final String DUREE_PERIODE_MATCH_FIELD_NAME 	= "DUREE_PERIODE_MATCH";

    private static final String ID_FIELD_TYPE          			= "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String PHASE_ID_FIELD_TYPE          	= "INTEGER NOT NULL";
    private static final String NB_POULE_FIELD_TYPE          	= "INTEGER NOT NULL";
    private static final String NB_PERIODE_MATCH_FIELD_TYPE     = "INTEGER NOT NULL";
    private static final String DUREE_PERIODE_MATCH_FIELD_TYPE  = "TEXT    NOT NULL";

    private static final int NUM_COL_ID          				= 0;
    private static final int NUM_COL_PHASE         				= 1;
    private static final int NUM_COL_NB_POULE      				= 2;
    private static final int NUM_COL_NB_PERIODE_MATCH      		= 3;
    private static final int NUM_COL_DUREE_PERIODE_MATCH      	= 4;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + PHASE_ID_FIELD_NAME         		+ " " + PHASE_ID_FIELD_TYPE 
	+ ", " + NB_POULE_FIELD_NAME      			+ " " + NB_POULE_FIELD_TYPE
    + ", " + NB_PERIODE_MATCH_FIELD_NAME      	+ " " + NB_PERIODE_MATCH_FIELD_TYPE
    + ", " + DUREE_PERIODE_MATCH_FIELD_NAME     + " " + DUREE_PERIODE_MATCH_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + PHASE_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBPhaseDAO.TABLE_NAME+"(ID)";



    public DBPhasePouleDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(PhasePoule phase_poule){
	    ContentValues values = new ContentValues();

	    values.put(PHASE_ID_FIELD_NAME,             phase_poule.getPhaseId());
	    values.put(NB_POULE_FIELD_NAME,          	phase_poule.getNbPoule());
        values.put(NB_PERIODE_MATCH_FIELD_NAME,     phase_poule.getNbPeriodeMatch());
        values.put(DUREE_PERIODE_MATCH_FIELD_NAME,  phase_poule.getDureePeriodeMatch());

	    return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, PhasePoule phase_poule){
	    ContentValues values = new ContentValues();
	    values.put(PHASE_ID_FIELD_NAME,             phase_poule.getPhaseId());
	    values.put(NB_POULE_FIELD_NAME,          	phase_poule.getNbPoule());
        values.put(NB_PERIODE_MATCH_FIELD_NAME,     phase_poule.getNbPeriodeMatch());
        values.put(DUREE_PERIODE_MATCH_FIELD_NAME,  phase_poule.getDureePeriodeMatch());


        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }
 
    public int removeWithId(int id){
	    return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public PhasePoule getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToPhasePoule(c);
    }

    public PhasePoule getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"="+ id, null, null, null, null);
        return cursorToPhasePoule(c);
    }

    public PhasePoule getFromPhaseId(int phaseId){
        Cursor c = super.mDb.query(TABLE_NAME, null,PHASE_ID_FIELD_NAME +"="+ phaseId, null, null, null, null);
        return cursorToPhasePoule(c);
    }

    private PhasePoule cursorToPhasePoule(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        PhasePoule phasePoule = new PhasePoule();
        phasePoule.setId(c.getInt(NUM_COL_ID));
        phasePoule.setPhaseId(c.getInt(NUM_COL_PHASE));
        phasePoule.setDureePeriodeMatch(c.getString(NUM_COL_DUREE_PERIODE_MATCH));
        phasePoule.setNbPeriodeMatch(c.getInt(NUM_COL_NB_PERIODE_MATCH));
        phasePoule.setNbPoule(c.getInt(NUM_COL_NB_POULE));
        c.close();

        return phasePoule;
    }
/*
    public PhasePoule getWithId(int id){
	    Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
	    return cursorToPhasePoule(c);
    }

    public List<PhasePoule> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListPhasePoule(c);
    }
*/
}