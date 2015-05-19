package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.Formation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dohko on 12/02/15.
 */
public class DBFormationDAO extends BaseDAO {

    public static final String TABLE_NAME         = "FORMATION";

    public static final String ID_FIELD_NAME                    = "_ID";
    public static final String EQUIPE_ID_FIELD_NAME             = "EQUIPE_ID";
    public static final String FORMATION_PAR_DEFAUT_FIELD_NAME  = "FORMATION_PAR_DEFAUT";


    private static final String ID_FIELD_TYPE                    = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String EQUIPE_ID_FIELD_TYPE             = "INTEGER";
    private static final String FORMATION_PAR_DEFAUT_FIELD_TYPE  = "INTEGER DEFAULT 0";


    private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_EQUIPE_ID = 1;
    private static final int NUL_COL_DEFAUT = 2;


    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + EQUIPE_ID_FIELD_NAME     + " " + EQUIPE_ID_FIELD_TYPE
            + ", " + FORMATION_PAR_DEFAUT_FIELD_NAME + " "+FORMATION_PAR_DEFAUT_FIELD_TYPE
            + ", " + "FOREIGN KEY (" + EQUIPE_ID_FIELD_NAME+") "
            + "REFERENCES "+ DBEquipeDAO.TABLE_NAME+"(ID)";



    public DBFormationDAO(Context context){
        super(context);
        super.mDb = this.open();
    }

    public long insert(int equipeId){
        ContentValues values = new ContentValues();

        values.put(EQUIPE_ID_FIELD_NAME, equipeId);
        values.put(FORMATION_PAR_DEFAUT_FIELD_NAME, Formation.FORMATION_MATCH);

        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public long insert(int equipeId, int valeurParDefaut){
        ContentValues values = new ContentValues();

        values.put(EQUIPE_ID_FIELD_NAME, equipeId);
        values.put(FORMATION_PAR_DEFAUT_FIELD_NAME, valeurParDefaut);

        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int removeWithId(int id){
        return super.mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " +id, null);
    }

    public Formation getWithId(int id){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME +"=" + id, null, null, null, null);
        return cursorToFormation(c);
    }

    public Formation getDefaultFormation(int equipeId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                        EQUIPE_ID_FIELD_NAME +"=" + equipeId
                        +" AND "+FORMATION_PAR_DEFAUT_FIELD_NAME+"="+Formation.FORMATION_PAR_DEFAUT,
                null, null, null, null);
        if (c.getCount() == 0){
            return null;
        }
        return cursorToFormation(c);
    }

    public Formation getLastFormationUse(int equipeId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                EQUIPE_ID_FIELD_NAME +"=" + equipeId
                        +" AND "+FORMATION_PAR_DEFAUT_FIELD_NAME+"="+Formation.LAST,
                null, null, null, null);
        if (c.getCount() == 0){
            return null;
        }
        return cursorToFormation(c);
    }

    public List<Formation> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null , null, null, null, null, null);
        return cursorToListFormation(c);
    }

    public boolean belongTeam(int formationId, int equipeId){
        Cursor c = super.mDb.query(TABLE_NAME, null,
                EQUIPE_ID_FIELD_NAME +"=" + equipeId
                +" AND "+ID_FIELD_NAME+"="+ formationId,
                null, null, null, null);
        return (c.getCount() != 0); // Return true si on a une formation de l'equipe
    }

    private List<Formation> cursorToListFormation(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Formation> listeFormations = new ArrayList<Formation>();
        listeFormations.clear();

        if (c.moveToFirst()) {
            do {
                Formation formation = new Formation();
                formation.setId(c.getInt(NUM_COL_ID));
                formation.setEquipeId(c.getInt(NUM_COL_EQUIPE_ID));

                listeFormations.add(formation);
            } while (c.moveToNext());
        }
        c.close();
        return listeFormations;
    }

    private Formation cursorToFormation(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Formation formation = new Formation();
        formation.setId(c.getInt(NUM_COL_ID));
        formation.setEquipeId(c.getInt(NUM_COL_EQUIPE_ID));
        c.close();

        return formation;
    }



    public Formation getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToFormation(c);
    }






}
