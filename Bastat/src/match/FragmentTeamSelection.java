package com.enseirb.pfa.bastats.match;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.content.DialogInterface;

import com.enseirb.pfa.bastats.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dohko on 17/02/15.
 */
public class FragmentTeamSelection extends Fragment {
    private Context Ctx;
    private ListView listB;
    private ArrayList<HashMap<String, String>> personnesB = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter adapterB;
    private Button ajoutB;
    private EditText joueurB;
    private EditText numB;
    private EditText equipeB;
    private final static int dialogB = 2;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_team_selection, container, false);
        listB = (ListView) v.findViewById(R.id.listB);
        ajoutB = (Button) v.findViewById(R.id.ajoutB);
        joueurB = (EditText) v.findViewById(R.id.nomJoueurB);
        numB = (EditText) v.findViewById(R.id.numeroJoueurB);
        equipeB = (EditText) v.findViewById(R.id.equipeB);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //equipeB.setText("hello");
        //equipeB.setHint("hello");
        Ctx = getActivity();
        ajoutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String> map=new HashMap<String,String>();
                map.put("num", numB.getText().toString());
                map.put("nom", joueurB.getText().toString());
                personnesB.add(map);
                adapterB=new SimpleAdapter(Ctx,personnesB,R.layout.player,new String[]{"num","nom"},new int[]{R.id.num,R.id.joueurB});
                listB.setAdapter(adapterB);
                joueurB.setText("");
                numB.setText("");

            }
        });
        adapterB = new SimpleAdapter(Ctx, personnesB, R.layout.player, new String[]{"num", "nom"}, new int[]{R.id.joueurB, R.id.num});
        listB.setAdapter(adapterB);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }


    class AjoutJoueurB implements View.OnClickListener {
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(View v) {
           getActivity().showDialog(dialogB);

        }
    }

    public Dialog onCreateDialog(int id) {
        AlertDialog dialog = null;

        switch (id) {
            case dialogB:
                AlertDialog.Builder builder = new AlertDialog.Builder(Ctx);
                builder.setMessage("Etes-vous sur d'ajouter " + numB.getText() + " " + joueurB.getText().toString() + "? ");
                builder.setCancelable(true);
                builder.setPositiveButton("Oui", new AjoutB());
                builder.setNegativeButton("Non", new NonAjoutB());
                dialog = builder.create();
                break;


        }
        return dialog;
    }

    public void onPrepareDialog(int id,Dialog dialog){
        switch(id){
            case dialogB:
                ((AlertDialog) dialog).setMessage("Etes-vous sur d'ajouter "+numB.getText()+" "+joueurB.getText().toString()+ "? ");
                break;

        }
    }
    class AjoutB implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("num", numB.getText().toString());
            map.put("nom", joueurB.getText().toString());
            personnesB.add(map);
            adapterB=new SimpleAdapter(Ctx,personnesB,R.layout.player,new String[]{"num","nom"},new int[]{R.id.num,R.id.joueurB});
            listB.setAdapter(adapterB);
            joueurB.setText("");
            numB.setText("");
        }
    }

    class NonAjoutB implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            joueurB.setText("");
            numB.setText("");
        }
    }

     public void setEquipeB(String txt){
        equipeB.setHint(txt);
    }

    public void setJoueurB(String txt){
        joueurB.setHint(txt);
    }

    public String getEquipe(){
        return equipeB.getText().toString().trim();
    }

    public ArrayList<HashMap<String, String>> getPersonnes(){
        return personnesB;

    }

}


