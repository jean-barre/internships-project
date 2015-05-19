package com.enseirb.pfa.bastats.tournoi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.model.Equipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class SelectionEquipePouleFragment extends Fragment {
    private ArrayList<HashMap<String,String>> mListeEquipe;
    private SimpleAdapter mAdapter;
    private ListView mListeView;

    private int tournoiId = -1;
    private static final String ARG_TOURNOI_ID = "tournoiId";

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
        public void onAddTeam(HashMap<String,String> team);
    }

    public void ajouterPoule(HashMap<String,String> team){
        if (mListener != null) {
            mListener.onAddTeam(team);
        }
    }

    public static SelectionEquipePouleFragment newInstance(int tournoiId) {
        SelectionEquipePouleFragment fragment = new SelectionEquipePouleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TOURNOI_ID, tournoiId);
        fragment.setArguments(args);
        return fragment;
    }

    public SelectionEquipePouleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournoiId = getArguments().getInt(ARG_TOURNOI_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_selection_equipe_poule, container, false);
        // Inflate the layout for this fragment
        mListeView = (ListView) view.findViewById(R.id.equipes_tournoi);
        mListeEquipe = new ArrayList<HashMap<String,String>>();

        getTeams(tournoiId);


        //mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mListeEquipe);
        mAdapter = new SimpleAdapter(getActivity(), mListeEquipe,R.layout.row_selection_equipe_poule,
                new String[]{"nom","bouton"},
                new int[]{R.id.nom_equipe, R.id.supprimer_equipe}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                final int index = position;
                View view = super.getView(position, convertView, parent);
                Button buttonAdd = (Button) view.findViewById(R.id.ajouter_equipe);
                buttonAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ajouterPoule(mListeEquipe.get(index));
                        delete(index);
                    }
                });
                return view;
            }
        };
        mListeView.setAdapter(mAdapter);

        /*
        mListeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ajouterPoule(mListeEquipe.get(position));
                delete(position);
            }
        });
        */
        return view;
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
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void delete(int i){
        mListeEquipe.remove(i);
        mAdapter.notifyDataSetChanged();
    }

    public void add(HashMap<String,String> team){
        mListeEquipe.add(team);
        mAdapter.notifyDataSetChanged();
    }

    private void getTeams(int tournoiId){
        if (tournoiId != -1) {
            DBTournoiEquipeDAO tableTournoiEquipe = new DBTournoiEquipeDAO(getActivity());
            DBEquipeDAO tableEquipe = new DBEquipeDAO(getActivity());
            List<Integer> teamIdList = new ArrayList<>();
            teamIdList.addAll(tableTournoiEquipe.getTeamsList(tournoiId));

            for (Integer tmp : teamIdList) {
                int equipeId = tmp.intValue();
                Equipe equipe = tableEquipe.getWithId(equipeId);
                HashMap<String, String> map = new HashMap<>();
                map.put("nom", equipe.getNom());
                map.put("id", String.valueOf(equipe.getId()));
                mListeEquipe.add(map);
            }
        } else {
            Log.d("TAG", "tournoiId non initilisé dans l'activité");
            //throw exception
        }
    }

    public void setTournoiId(int id){
        this.tournoiId = id;
    }

}
