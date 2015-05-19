package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.PhaseTableau;

public class DBPhaseTableauDAO extends BaseDAO{

    public static final String TABLE_NAME                   = "PHASE_TABLEAU";

    private static final String ID_FIELD_NAME           	= "_ID";
    private static final String PHASE_ID_FIELD_NAME         = "PHASE_ID";
    private static final String ETAT_FIELD_NAME           	= "ETAT";

    private static final String ID_FIELD_TYPE          		= "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String PHASE_ID_FIELD_TYPE         = "INTEGER NOT NULL";
    private static final String ETAT_FIELD_TYPE          	= "INTEGER NOT NULL";

    private static final int NUM_COL_ID          			= 0;
    private static final int NUM_COL_PHASE_ID         		= 1;
    private static final int NUM_COL_ETAT      				= 2;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + PHASE_ID_FIELD_NAME         + " " + PHASE_ID_FIELD_TYPE 
	+ ", " + ETAT_FIELD_NAME      		 + " " + ETAT_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + PHASE_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBPhaseDAO.TABLE_NAME+"(ID)";


	public DBPhaseTableauDAO(Context context){
        super(context);
        this.mDb = this.open();
    }

    public long insert(PhaseTableau phaseTableau){
	    ContentValues values = new ContentValues();

	    values.put(PHASE_ID_FIELD_NAME,             phaseTableau.getPhaseId());
	    values.put(ETAT_FIELD_NAME,          phaseTableau.getEtat());

	    return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, PhaseTableau phaseTableau){
	    ContentValues values = new ContentValues();
	    values.put(PHASE_ID_FIELD_NAME,             phaseTableau.getPhaseId());
	    values.put(ETAT_FIELD_NAME,          phaseTableau.getEtat());


        return super.mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }
 
    public int removeWithId(int id){
	    return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public PhaseTableau getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"="+ id, null, null, null, null);
        return cursorToPhaseTableau(c);
    }

    public PhaseTableau getFromPhaseId(int phaseId){
        Cursor c = super.mDb.query(TABLE_NAME, null,PHASE_ID_FIELD_NAME +"="+ phaseId, null, null, null, null);
        return cursorToPhaseTableau(c);
    }

    public PhaseTableau getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToPhaseTableau(c);
    }

    private PhaseTableau cursorToPhaseTableau(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        PhaseTableau phaseTableau = new PhaseTableau();
        phaseTableau.setId(c.getInt(NUM_COL_ID));
        phaseTableau.setPhaseId(c.getInt(NUM_COL_PHASE_ID));
        phaseTableau.setEtat(c.getInt(NUM_COL_ETAT));
        c.close();

        return phaseTableau;
    }
/*
    public PhaseTableau getWithId(int id){
	    Cursor c = super.mDb.query(TABLE_NAME, null,ID_FIELD_NAME +"=" + id, null, null, null, null);
	    return cursorToPhaseTableau(c);
    }

    public List<PhaseTableau> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListPhaseTableau(c);
    }
*/
}