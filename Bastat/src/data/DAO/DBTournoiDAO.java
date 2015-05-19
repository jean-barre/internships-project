package com.enseirb.pfa.bastats.data.DAO;

import com.enseirb.pfa.bastats.data.model.Tournoi;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class DBTournoiDAO extends BaseDAO {

    public static final String TABLE_NAME = "TOURNOI";

    private static final String ID_FIELD_NAME            = "_ID";
    private static final String LIBELLE_FIELD_NAME       = "LIBELLE";
    private static final String LIEU_FIELD_NAME          = "LIEU";
    private static final String NB_EQUIPE_MAX_FIELD_NAME = "NB_EQUIPE_MAX";

    private static final String ID_FIELD_TYPE            = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String LIBELLE_FIELD_TYPE       = "TEXT";
    private static final String LIEU_FIELD_TYPE          = "TEXT";
    private static final String NB_EQUIPE_MAX_FIELD_TYPE = "INTEGER";

    private static final int NUM_COL_ID            = 0;
    private static final int NUM_COL_LIBELLE       = 1;
    private static final int NUM_COL_LIEU          = 2;
    private static final int NUM_COL_NB_EQUIPE_MAX = 3;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
	+ ", " + LIBELLE_FIELD_NAME       + " " + LIBELLE_FIELD_TYPE
    + ", " + LIEU_FIELD_NAME          + " " + LIEU_FIELD_TYPE
    + ", " + NB_EQUIPE_MAX_FIELD_NAME + " " + NB_EQUIPE_MAX_FIELD_TYPE;


    public DBTournoiDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }

    public long insert(Tournoi tournoi) {
        ContentValues values = new ContentValues();

        values.put(LIBELLE_FIELD_NAME,       tournoi.getLibelle());
        values.put(LIEU_FIELD_NAME,          tournoi.getLieu());
        values.put(NB_EQUIPE_MAX_FIELD_NAME, tournoi.getNbEquipeMax());
       
        return mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, Tournoi tournoi) {
        ContentValues values = new ContentValues();

        values.put(LIBELLE_FIELD_NAME,        tournoi.getLibelle());
        values.put(LIEU_FIELD_NAME,           tournoi.getLieu());
        values.put(NB_EQUIPE_MAX_FIELD_NAME,  tournoi.getNbEquipeMax());
       
        return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
    }


    public int removeWithId(int id) {
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " + id, null);
    }


    public Tournoi getWithId(int id) {
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME + "=" + id, null, null, null, null);
        return cursorToTournoi(c);
    }

    public List<Tournoi> getAll() {
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListTournoi(c);
    }


    private List<Tournoi> cursorToListTournoi(Cursor c) {
        if (c.getCount() == 0)
            return null;

        List<Tournoi> listeTournois = new ArrayList<Tournoi>();
        listeTournois.clear();

        if (c.moveToFirst()) {
            do {
                Tournoi tournoi = new Tournoi();
                tournoi.setId(c.getInt(NUM_COL_ID));
                tournoi.setLibelle(c.getString(NUM_COL_LIBELLE));
                tournoi.setLieu(c.getString(NUM_COL_LIEU));
                tournoi.setNbEquipeMax(c.getInt(NUM_COL_NB_EQUIPE_MAX));
              
                listeTournois.add(tournoi);
            } while (c.moveToNext());
        }
        c.close();
        return listeTournois;
    }

    public Tournoi getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToTournoi(c);
    }

    private Tournoi cursorToTournoi(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Tournoi tournoi = new Tournoi();

        tournoi.setId(c.getInt(NUM_COL_ID));
        tournoi.setLibelle(c.getString(NUM_COL_LIBELLE));
        tournoi.setLieu(c.getString(NUM_COL_LIEU));
        tournoi.setNbEquipeMax(c.getInt(NUM_COL_NB_EQUIPE_MAX));
     
        c.close();

        return tournoi;
    }
}



