package com.enseirb.pfa.bastats.data.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.enseirb.pfa.bastats.data.MaBaseSQLite;

public abstract class BaseDAO {
    protected final static int VERSION_BDD = 1;
    protected final static String NOM_BDD = "database.db";

    protected SQLiteDatabase mDb = null;
    protected MaBaseSQLite mDbHelper = null;
    protected Context mCtx;

    public BaseDAO(Context pContext) {
        this.mDbHelper = new MaBaseSQLite(pContext, NOM_BDD, null, VERSION_BDD);
        this.mCtx = pContext;
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mDbHelper.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
