package com.enseirb.pfa.bastats.data.DAO;

import com.enseirb.pfa.bastats.data.model.Equipe;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class DBEquipeDAO extends BaseDAO {

    public static final String TABLE_NAME = "EQUIPE";

    private static final String ID_FIELD_NAME           = "_ID";
    private static final String COULEUR_BASE_FIELD_NAME = "COULEUR_BASE";
    private static final String PHOTO_FIELD_NAME        = "PHOTO";
    private static final String NOM_FIELD_NAME          = "NOM";
    private static final String ACRONYME_FIELD_NAME     = "ACRONYME";

    private static final String ID_FIELD_TYPE           = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String COULEUR_BASE_FIELD_TYPE = "TEXT";
    private static final String PHOTO_FIELD_TYPE        = "TEXT";
    private static final String NOM_FIELD_TYPE          = "TEXT";
    private static final String ACRONYME_FIELD_TYPE     = "TEXT";

    private static final int NUM_COL_ID                 = 0;
    private static final int NUM_COL_COULEUR_BASE       = 1;
    private static final int NUM_COL_PHOTO              = 2;
    private static final int NUM_COL_NOM                = 3;
    private static final int NUM_COL_ACRONYME           = 4;

    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + COULEUR_BASE_FIELD_NAME + " " + COULEUR_BASE_FIELD_TYPE
            + ", " + PHOTO_FIELD_NAME + " " + PHOTO_FIELD_TYPE
            + ", " + NOM_FIELD_NAME + " " + NOM_FIELD_TYPE
            + ", " + ACRONYME_FIELD_NAME + " " + ACRONYME_FIELD_TYPE;


    public DBEquipeDAO(Context context) {
        super(context);
        this.mDb = this.open();
    }

    public long insert(Equipe equipe) {
        ContentValues values = new ContentValues();

        values.put(COULEUR_BASE_FIELD_NAME, equipe.getCouleur());
        values.put(PHOTO_FIELD_NAME, equipe.getPhoto());
        values.put(NOM_FIELD_NAME, equipe.getNom());
        values.put(ACRONYME_FIELD_NAME, equipe.getAcronyme());

        return mDb.insert(TABLE_NAME, null, values);
    }

    public int update(int id, Equipe equipe) {
        ContentValues values = new ContentValues();
        values.put(COULEUR_BASE_FIELD_NAME, equipe.getCouleur());
        values.put(PHOTO_FIELD_NAME, equipe.getPhoto());
        values.put(NOM_FIELD_NAME, equipe.getNom());
        values.put(ACRONYME_FIELD_NAME, equipe.getAcronyme());

        return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);

    }


    public int removeWithId(int id) {
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " + id, null);
    }


    public Equipe getWithId(int id) {
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME + "=" + id, null, null, null, null);
        return cursorToEquipe(c);
    }

    public int getEquipeId(String name){
        Cursor c = mDb.rawQuery("SELECT "+ID_FIELD_NAME+
                                " FROM "+TABLE_NAME+
                                " WHERE "+TABLE_NAME+"."+NOM_FIELD_NAME+"= ?", new String[] {name});
        int id = -1;
        if (c.moveToFirst())
            id = c.getInt(NUM_COL_ID);
        c.close();
        // throw Exception quand id = -1 : NotFoundException à traiter niveau interface
        // On demande une deuxième fois à l'utilisateur de rentrer un name
        return id;
    }



    public List<Equipe> getAll() {
        Cursor c = super.mDb.query(TABLE_NAME, null, null, null, null, null, null);
        return cursorToListEquipe(c);
    }

    public Equipe getLast(){
        Cursor c = mDb.query(TABLE_NAME,null, null, null, null, null, ID_FIELD_NAME+" DESC", null);
        c.moveToLast();
        return cursorToEquipe(c);
    }

    private List<Equipe> cursorToListEquipe(Cursor c) {
        if (c.getCount() == 0)
            return null;

        List<Equipe> listeEquipes = new ArrayList<Equipe>();
        listeEquipes.clear();

        if (c.moveToFirst()) {
            do {
                Equipe equipe = new Equipe();
                equipe.setId(c.getInt(NUM_COL_ID));
                equipe.setCouleur(c.getString(NUM_COL_COULEUR_BASE));
                equipe.setPhoto(c.getString(NUM_COL_PHOTO));
                equipe.setNom(c.getString(NUM_COL_NOM));
                equipe.setAcronyme(c.getString(NUM_COL_ACRONYME));
                listeEquipes.add(equipe);
            } while (c.moveToNext());
        }
        c.close();
        return listeEquipes;
    }


    private Equipe cursorToEquipe(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Equipe equipe = new Equipe();

        equipe.setId(c.getInt(NUM_COL_ID));
        equipe.setCouleur(c.getString(NUM_COL_COULEUR_BASE));
        equipe.setPhoto(c.getString(NUM_COL_PHOTO));
        equipe.setNom(c.getString(NUM_COL_NOM));
        equipe.setAcronyme(c.getString(NUM_COL_ACRONYME));

        c.close();

        return equipe;
    }
}



