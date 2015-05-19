package com.enseirb.pfa.bastats.tournoi.algorithme;

import android.content.Context;
import android.util.Log;

import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleEquipeDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;
import com.enseirb.pfa.bastats.data.model.Poule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rchabot on 25/02/15.
 */
public class Classement {
    private List<ClassementEquipe> mListeEquipe;
    private ArrayList<HashMap<String,String>> mClassementRow;

    public Classement(Poule poule, Context context){
        int pouleId = poule.getId();
        DBPouleEquipeDAO tablePouleEquipe = new DBPouleEquipeDAO(context);
        DBEquipeDAO tableEquipe = new DBEquipeDAO(context);
        List<Integer> equipeIds = new ArrayList<>(tablePouleEquipe.getTeamsList(pouleId));
        mListeEquipe = new ArrayList<>(equipeIds.size());
        for (int id : equipeIds){
            Equipe equipe = tableEquipe.getWithId(id);
            ClassementEquipe clt = new ClassementEquipe(poule, equipe, context);
            mListeEquipe.add(clt);
        }
        calculerClassement();
    }

    public void calculerClassement(){
        Collections.sort(mListeEquipe, ClassementEquipe.FruitNameComparator);
        for (int i=0; i<mListeEquipe.size();i++){
            mListeEquipe.get(i).setPosition(i+1);
        }
        Log.d("Classement", mListeEquipe.toString());
    }

    public ArrayList<HashMap<String, String>> getHashMapList(){
        mClassementRow = new ArrayList<>();
        for (int i=0; i < mListeEquipe.size(); i++){
            HashMap<String,String> map = new HashMap<>(mListeEquipe.get(i).toHaspMapFormat());
            mClassementRow.add(map);
        }
        return mClassementRow;
    }


}
