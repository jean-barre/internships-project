package com.enseirb.pfa.bastats.tournoi.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBClassementTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBEquipeDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiEquipeDAO;
import com.enseirb.pfa.bastats.data.model.ClassementTournoi;
import com.enseirb.pfa.bastats.data.model.Tournoi;
import com.enseirb.pfa.bastats.tournoi.AjouterClassement;
import com.enseirb.pfa.bastats.tournoi.KeyTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OverviewTournoi extends Fragment {
    private Button newEntry;
    private ListView mListView;
    private SimpleAdapter mAdapter;
    private ArrayList<HashMap<String, String>> mClassement;
    private List<ClassementTournoi> mCltDb;
    private TextView etat;

    public static final int REQUEST_CLT = 123;
    private static final String TOURNOI_ID = "tournoi_id";
    private int tournoiId;
    private int npValue;

    public OverviewTournoi() {
        // Required empty public constructor
    }

    public static OverviewTournoi newInstance(int tournoiId) {
        OverviewTournoi fragment = new OverviewTournoi();
        Bundle args = new Bundle();
        args.putInt(TOURNOI_ID, tournoiId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tournoiId = getArguments().getInt(TOURNOI_ID);
        }
        DBClassementTournoiDAO classementTournoiDAO = new DBClassementTournoiDAO(getActivity());
        mClassement = new ArrayList<>();
        if (classementTournoiDAO.getClassement(tournoiId) != null)
            mCltDb = new ArrayList<>(classementTournoiDAO.getClassement(tournoiId));
        else
            mCltDb = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_overview_tournoi, container, false);

        DBTournoiDAO tableTournoi = new DBTournoiDAO(getActivity());
        Tournoi tournoi = new Tournoi(tableTournoi.getWithId(tournoiId));
        TextView libelle = (TextView) view.findViewById(R.id.textViewLibelle);
        libelle.setText(tournoi.getLibelle());
        TextView lieu = (TextView) view.findViewById(R.id.textViewLieu);
        lieu.setText(tournoi.getLieu());
        TextView nbEquipe = (TextView) view.findViewById(R.id.textViewNbEquipes);
        nbEquipe.setText(Integer.toString(tournoi.getNbEquipeMax()));


        mListView = (ListView) view.findViewById(R.id.classement_tournoi);

        mAdapter = new SimpleAdapter(getActivity(), mClassement,R.layout.row_classement_tournoi,
                new String[]{"place","equipe","points","bouton"},
                new int[]{R.id.place, R.id.equipe, R.id.pts, R.id.btn_delete}) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                final int index = position;
                final Button button = (Button) view.findViewById(R.id.btn_delete);
                final TextView points = (TextView) view.findViewById(R.id.pts);
                points.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        modifyEntry("Nombre de points", points, index);
                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DBClassementTournoiDAO classementTournoiDAO = new DBClassementTournoiDAO(getActivity());
                        classementTournoiDAO.removeWithId(mCltDb.get(index).getId());
                        Log.d("VUE", "Suppresion du classement " + mCltDb.get(index));
                        mCltDb.remove(index);
                        mClassement.remove(index);
                        mAdapter.notifyDataSetChanged();
                        updateEtat();
                    }
                });



                return view;
            }
        };

        convertToHashMap();
        mListView.setAdapter(mAdapter);

        newEntry = (Button) view.findViewById(R.id.add_classement);

        newEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AjouterClassement.class);
                i.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                getActivity().startActivityForResult(i, REQUEST_CLT);
            }
        });

        etat = (TextView) view.findViewById(R.id.textViewEtat);
        updateEtat();

        return view;
    }

    private void modifyEntry(String title, TextView textView, final int index){
        final Dialog d = new Dialog(getActivity());
        final TextView tv = textView;
        d.setTitle(title);
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(100);
        np.setMinValue(0);
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                npValue= np.getValue();
                tv.setText(np.getValue()+" pts");
                Log.d("VUE", "Affectation");
                DBClassementTournoiDAO classement = new DBClassementTournoiDAO(getActivity());
                Log.d("VUE", mCltDb.get(index).toString());
                mCltDb.get(index).setPoints(npValue);
                classement.update(mCltDb.get(index).getId(), mCltDb.get(index));
                Log.d("VUE", mCltDb.get(index).toString());
                d.dismiss();
            }
        });
        negativeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void convertToHashMap(){
        DBEquipeDAO tableEquipe = new DBEquipeDAO(getActivity());
        mClassement.clear();
        for (ClassementTournoi clt : mCltDb) {

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("place", String.valueOf(clt.getPlace()));
            map.put("equipe", tableEquipe.getWithId(clt.getEquipeId()).getNom());
            map.put("points", clt.getPoints()+" pts");
            mClassement.add(map);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void update(){
        DBClassementTournoiDAO classementTournoiDAO = new DBClassementTournoiDAO(getActivity());
        if (classementTournoiDAO.getClassement(tournoiId) != null)
            mCltDb = new ArrayList<>(classementTournoiDAO.getClassement(tournoiId));
        else
            mCltDb = new ArrayList<>();
        convertToHashMap();
        updateEtat();
    }

    public void updateEtat(){
        DBTournoiDAO tableTournoi = new DBTournoiDAO(getActivity());
        Tournoi tournoi = new Tournoi(tableTournoi.getWithId(tournoiId));
        if (mClassement.size() == tournoi.getNbEquipeMax()){
            etat.setText("Terminé");
        } else
            etat.setText("En cours");
    }
}
