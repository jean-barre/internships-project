package com.enseirb.pfa.bastats.data.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.enseirb.pfa.bastats.data.model.PouleMatch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dohko on 13/02/15.
 */
public class DBPouleMatchDAO extends  BaseDAO{

    public static final String TABLE_NAME = "POULE_MATCH";

    public static final String POULE_ID_FIELD_NAME = "POULE_ID";
    public static final String MATCH_ID_FIELD_NAME = "MATCH_ID";

    private static final String POULE_ID_FIELD_TYPE = "INTEGER";
    private static final String MATCH_ID_FIELD_TYPE = "INTEGER";

    private static final int NUM_COL_POULE_ID = 0;
    private static final int NUM_COL_MATCH_ID = 1;

    public static final String CREATE_TABLE_STATEMENT = POULE_ID_FIELD_NAME + " " + POULE_ID_FIELD_TYPE
            + ", " + MATCH_ID_FIELD_NAME + " " + MATCH_ID_FIELD_TYPE
            + ", " + "PRIMARY KEY(" + POULE_ID_FIELD_NAME + ", " + MATCH_ID_FIELD_NAME + ")"
            + ", " + "FOREIGN KEY (" + POULE_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBPouleDAO.TABLE_NAME + "(ID)"
            + ", " + "FOREIGN KEY (" + MATCH_ID_FIELD_NAME + ") "
            + "REFERENCES " + DBMatchDAO.TABLE_NAME + "(ID)";



    public DBPouleMatchDAO(Context context) {
        super(context);
        super.mDb = this.open();
    }

    public long insert(int pouleId, int matchId){
        ContentValues values = new ContentValues();

        values.put(POULE_ID_FIELD_NAME, pouleId);
        values.put(MATCH_ID_FIELD_NAME, matchId);
        return super.mDb.insert(TABLE_NAME, null, values);
    }

    public int removeWithId(int pouleId,int matchId){
        return super.mDb.delete(TABLE_NAME, POULE_ID_FIELD_NAME + " = " + pouleId+" AND "+MATCH_ID_FIELD_NAME+ " = " + matchId, null);
    }


    public PouleMatch getWithId(int pouleId,int matchId){
        Cursor c = super.mDb.query(TABLE_NAME, null,POULE_ID_FIELD_NAME + " = " + pouleId+" AND "+MATCH_ID_FIELD_NAME+ " = " + matchId, null, null, null, null);
        return cursorToPouleMatch(c);
    }


    public List<Integer> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListPouleMatch(c);
    }

    public List<Integer> getMatchIdsFromPoule(int pouleId){
        Cursor c = super.mDb.rawQuery("SELECT "+MATCH_ID_FIELD_NAME+
                " FROM "+TABLE_NAME+
                " WHERE "+TABLE_NAME+"."+POULE_ID_FIELD_NAME+"="+pouleId, null);
        return cursorToListPouleMatch(c);
    }

    public List<Integer> cursorToListPouleMatch(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Integer> matchList = new ArrayList<>();
        matchList.clear();

        if (c.moveToFirst()) {
            do {
                matchList.add(c.getInt(0)); // 0 car on sélectionne seulement une colonne
            } while (c.moveToNext());
        }
        c.close();
        return matchList;
    }

    /*
    private List<PouleMatch> cursorToListPouleMatch(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<PouleMatch> listePouleMatchs = new ArrayList<PouleMatch>();
        listePouleMatchs.clear();

        if (c.moveToFirst()) {
            do {
                PouleMatch pouleMatch = new PouleMatch();
                pouleMatch.setPouleId(c.getInt(NUM_COL_POULE_ID));
                pouleMatch.setMatchId(c.getInt(NUM_COL_MATCH_ID));
                listePouleMatchs.add(pouleMatch);
            } while (c.moveToNext());
        }
        c.close();
        return listePouleMatchs;
    }
    */
    private PouleMatch cursorToPouleMatch(Cursor c){
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        PouleMatch pouleMatch = new PouleMatch();
        pouleMatch.setPouleId(c.getInt(NUM_COL_POULE_ID));
        pouleMatch.setMatchId(c.getInt(NUM_COL_MATCH_ID));

        c.close();

        return pouleMatch;
    }




}
