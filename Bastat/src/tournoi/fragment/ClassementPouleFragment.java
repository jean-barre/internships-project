package com.enseirb.pfa.bastats.tournoi.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBClassementTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBMatchDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleEquipeDAO;
import com.enseirb.pfa.bastats.data.model.ClassementTournoi;
import com.enseirb.pfa.bastats.data.model.Match;
import com.enseirb.pfa.bastats.data.model.Poule;
import com.enseirb.pfa.bastats.tournoi.KeyTable;
import com.enseirb.pfa.bastats.tournoi.algorithme.Classement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassementPouleFragment extends Fragment {
    private ListView mClassementView;
    private ArrayList<HashMap<String, String>> mClassementRow;
    private SimpleAdapter mAdapter;

    private int[] colors = new int[]{Color.parseColor("#F0F0F0"), Color.parseColor("#d7d7d7")};

    private static final String ARG_NAME = "name";
    private String name = "";

    private static final String ARG_POULE_ID = "poule_id";
    private int pouleId;

    private TextView nomPoule;
    private ImageButton nextButton;
    private ImageButton prevButton;

    private OnFragmentInteractionListener mListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onNextPoule();

        public void onPreviousPoule();
    }

    public void next() {
        if (mListener != null) {
            mListener.onNextPoule();
        }
    }

    public void previous() {
        if (mListener != null) {
            mListener.onPreviousPoule();
        }
    }

    public static ClassementPouleFragment newInstance(Poule poule) {
        ClassementPouleFragment fragment = new ClassementPouleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, poule.getLibelle());
        args.putInt(ARG_POULE_ID, poule.getId());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            pouleId = getArguments().getInt(ARG_POULE_ID);
            //Log.d("CltPouleFrg", "pouleId: "+pouleId);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classement_poule, container, false);

        nomPoule = (TextView) view.findViewById(R.id.nom_poule);
        nomPoule.setText(name);

        mClassementView = (ListView) view.findViewById(R.id.clasement_poule);
        mClassementRow = new ArrayList<HashMap<String, String>>();
        mAdapter = new SimpleAdapter(getActivity(), mClassementRow, R.layout.row_classement_poule,
                new String[]{KeyTable.CLT, KeyTable.EQUIPE, KeyTable.POINTS, KeyTable.JOUER,
                        KeyTable.NB_VICTOIRES, KeyTable.NB_NULS, KeyTable.NB_DEFAITES,
                        KeyTable.POUR, KeyTable.CONTRE, KeyTable.DIFF},
                new int[]{R.id.place, R.id.equipe, R.id.pts, R.id.jouer, R.id.victoire, R.id.nul,
                        R.id.defaite, R.id.pour, R.id.contre, R.id.difference}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                int colorPos = position % colors.length;
                view.setBackgroundColor(colors[colorPos]);
                return view;
            }
        };

        //test();
        remplirClassement();

        mClassementView.setAdapter(mAdapter);

        nextButton = (ImageButton) view.findViewById(R.id.next_poule);
        prevButton = (ImageButton) view.findViewById(R.id.previous_poule);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previous();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void remplirClassement() {
        DBPouleDAO tablePoule = new DBPouleDAO(getActivity());
        Poule poule = new Poule(tablePoule.getWithId(pouleId));
        Log.d("TAG", poule.toString());
        Classement clt = new Classement(poule, getActivity());
        //mClassementRow = new ArrayList<>(clt.getHashMapList());
        mClassementRow.clear();
        mClassementRow.addAll(clt.getHashMapList());
        mAdapter.notifyDataSetChanged();
    }

    public void doRanking(int tournoiId, int cltDebut){
        DBClassementTournoiDAO classementTournoiDAO = new DBClassementTournoiDAO(getActivity());
        for (HashMap<String, String> map : mClassementRow){
            int positionPoule = Integer.parseInt(map.get(KeyTable.CLT));
            int equipeId = Integer.parseInt(map.get(KeyTable.ID_EQUIPE_A));
            Log.d("POULE", "insertion "+equipeId+" tournoi "+tournoiId+" position"+positionPoule+cltDebut);
            classementTournoiDAO.insert(new ClassementTournoi(equipeId, tournoiId, cltDebut+positionPoule-1,0));
        }
        Intent i = new Intent();
        getActivity().setResult(getActivity().RESULT_OK,i);
        getActivity().finish();
    }

    public int nbEquipesPoules(){
        return mClassementRow.size();
    }
}

