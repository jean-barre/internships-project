package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;

import com.enseirb.pfa.bastats.data.model.Departage;

/**
 * Created by dohko on 13/02/15.
 */
public class DBDepartageDAO  extends BaseDAO   {

    public static final String TABLE_NAME = "DEPARTAGE";

    private static final String POULE_ID_FIELD_NAME = "POULE_ID";
    private static final String EQUIPE_ID_FIELD_NAME = "EQUIPE_ID";
    private static final String POINTS_FIELD_NAME = "POINTS";

    private static final String POULE_ID_FIELD_TYPE = "INTEGER";
    private static final String EQUIPE_ID_FIELD_TYPE = "INTEGER";
    private static final String POINTS_FIELD_TYPE = "INTEGER";

    private static final int NUM_COL_POULE_ID = 0;
    private static final int NUM_COL_EQUIPE_ID = 1;
    private static final int NUM_COL_POINTS = 2;


    public static final String CREATE_TABLE_STATEMENT = POULE_ID_FIELD_NAME + " " + POULE_ID_FIELD_TYPE
            + ", " + EQUIPE_ID_FIELD_NAME + " " + EQUIPE_ID_FIELD_TYPE
            + ", " + POINTS_FIELD_NAME + " " + POINTS_FIELD_TYPE
            + ", " + "PRIMARY KEY(" + POULE_ID_FIELD_NAME + ", " + EQUIPE_ID_FIELD_NAME + ")"
            + ", " + "FOREIGN KEY (" + POULE_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBPouleDAO.TABLE_NAME + "(ID)"
            + ", " + "FOREIGN KEY (" + EQUIPE_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBEquipeDAO.TABLE_NAME + "(ID)";



    public DBDepartageDAO(Context context) {
        super(context);
        super.mDb = this.open();
    }


    public long insert(Departage departage){
        ContentValues values = new ContentValues();

        values.put(POULE_ID_FIELD_NAME,departage.getPouleId());
        values.put(EQUIPE_ID_FIELD_NAME,departage.getEquipeId());
        values.put(POINTS_FIELD_NAME,departage.getPoints());
        return super.mDb.insert(TABLE_NAME, null, values);
    }


    public int update(int equipeId,int pouleId,int points){
        ContentValues values = new ContentValues();
        values.put(POINTS_FIELD_NAME, points);


        return super.mDb.update(TABLE_NAME, values, POULE_ID_FIELD_NAME + " = " + pouleId+" AND "+EQUIPE_ID_FIELD_NAME+ " = " + equipeId, null);
    }

    public int removeWithId(int equipeId,int pouleId){
        return super.mDb.delete(TABLE_NAME, POULE_ID_FIELD_NAME + " = " + pouleId+" AND "+EQUIPE_ID_FIELD_NAME+ " = " + equipeId, null);
    }




}
