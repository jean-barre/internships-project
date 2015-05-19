package com.enseirb.pfa.bastats.tournoi.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.enseirb.pfa.bastats.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EquipePouleFragment extends Fragment {
    private ArrayList<HashMap<String,String>> mListeEquipe;
    private SimpleAdapter mAdapter;
    private ListView mListeView;

    private static final String ARG_NAME = "name";
    private char name = 'ù';

    private static final String ARG_NB_EQUIPES = "nbEquipes";
    private int nbEquipes = 0;

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
        public void onRemoveTeam(HashMap<String,String> team);
    }

    public void enleverPoule(HashMap<String,String> team){
        if (mListener != null) {
            mListener.onRemoveTeam(team);
        }
    }

    public void next(){
        if (mListener != null) {
            mListener.onNextPoule();
        }
    }

    public void previous(){
        if (mListener != null) {
            mListener.onPreviousPoule();
        }
    }

    public static EquipePouleFragment newInstance(char name) {
        EquipePouleFragment fragment = new EquipePouleFragment();
        Bundle args = new Bundle();
        args.putChar(ARG_NAME, name);
        args.putInt(ARG_NB_EQUIPES, 0);
        fragment.setArguments(args);
        return fragment;
    }

    public static EquipePouleFragment newInstance(int nbEquipes) {
        EquipePouleFragment fragment = new EquipePouleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NB_EQUIPES, nbEquipes);
        args.putChar(ARG_NAME, '0');
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getChar(ARG_NAME);
            nbEquipes = getArguments().getInt(ARG_NB_EQUIPES);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_equipe_poule, container, false);


        mListeView = (ListView) view.findViewById(R.id.equipes_poule);
        mListeEquipe = new ArrayList<HashMap<String, String>>();
        //mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mListeEquipe);
        mAdapter = new SimpleAdapter(getActivity(), mListeEquipe,R.layout.row_equipe_poule,
                new String[]{"nom","bouton"},
                new int[]{R.id.nom_equipe, R.id.supprimer_equipe}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                final int index = position;
                View v = super.getView(position, convertView, parent);
                Button buttonDelete = (Button) v.findViewById(R.id.supprimer_equipe);
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enleverPoule(mListeEquipe.get(index));
                        delete(index);
                    }
                });
                View view = super.getView(position, convertView, parent);
;
                return view;
            }
        };
        mListeView.setAdapter(mAdapter);

        /*
        mListeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                enleverPoule(mListeEquipe.get(position));
                delete(position);
            }
        });
        */
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



        nomPoule = (TextView) view.findViewById(R.id.nom_poule);
        if (nbEquipes == 0) {
            nomPoule.setText("POULE " + Character.toString(name));
        } else {
            nomPoule.setText("Phase tableau");
            nextButton.setVisibility(Button.INVISIBLE);
            prevButton.setVisibility(Button.INVISIBLE);
        }

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

    public void delete(int i){
        mListeEquipe.remove(i);
        mAdapter.notifyDataSetChanged();
    }

    public void add(HashMap<String,String> team){
        mListeEquipe.add(team);
        mAdapter.notifyDataSetChanged();
    }

    public List<HashMap<String,String>> getEquipesPoule(){
        return mListeEquipe;
    }

    public String getName(){
        return nomPoule.getText().toString();
    }

    public boolean isEmpty(){
        if (mListeEquipe.isEmpty())
            return true;
        else
            return false;
    }

    public boolean isOneElement(){
        if (mListeEquipe.size() == 1)
            return true;
        else
            return false;
    }

    public int getSize(){
        return mListeEquipe.size();
    }
}
