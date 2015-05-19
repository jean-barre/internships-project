package com.enseirb.pfa.bastats.tournoi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.DAO.DBPhasePouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBPouleDAO;
import com.enseirb.pfa.bastats.data.DAO.DBTournoiDAO;
import com.enseirb.pfa.bastats.data.model.Phase;
import com.enseirb.pfa.bastats.data.model.Poule;
import com.enseirb.pfa.bastats.tournoi.fragment.CalendrierMatchPouleFragment;
import com.enseirb.pfa.bastats.tournoi.fragment.ClassementPouleFragment;

import java.util.ArrayList;
import java.util.List;


public class NavigationPouleActivity extends ActionBarActivity implements
    ClassementPouleFragment.OnFragmentInteractionListener{

    private Context mCtx;
    private static final String TAG = "NavigationPoule";
    private static final int MATCH_REQUEST = 1;
    private Button doRanking;
    private int currentPoule = 0;

    private int phasePouleId;
    private int phaseId;
    private int tournoiId;
    private int nbPoule;
    private int cltDebut=1;

    private ArrayList<ClassementPouleFragment> mListePouleFragment = new ArrayList<>();
    private ArrayList<CalendrierMatchPouleFragment> mCalendrierMatchPouleFragment = new ArrayList<>();
    private List<Poule> pouleList;
    //Utilisation d'une map pour tous les match à jouer ?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_poule);

        mCtx = this;
        getDataFromIntent();

        //ActionBar actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        Log.d("NavPoule", pouleList.toString());
        // On initialise toutes les poules et les calendrier respectifs
        for (int i = 0; i < pouleList.size(); i++){
            mListePouleFragment.add(ClassementPouleFragment.newInstance(pouleList.get(i)));
            mCalendrierMatchPouleFragment.add(CalendrierMatchPouleFragment.newInstance(pouleList.get(i)));
        }

        // On les ajoute à l'activité
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < pouleList.size(); i++){
            // Classement
            ft.add(R.id.poule_fragment_container, mListePouleFragment.get(i));
            ft.hide(mListePouleFragment.get(i));
            // Calendrier
            ft.add(R.id.calendrier_match_fragment_container, mCalendrierMatchPouleFragment.get(i));
            ft.hide(mCalendrierMatchPouleFragment.get(i));
        }
        ft.show(mListePouleFragment.get(currentPoule));
        ft.show(mCalendrierMatchPouleFragment.get(currentPoule));
        ft.commit();

        final DBTournoiDAO tableTournoi = new DBTournoiDAO(mCtx);

        doRanking = (Button) findViewById(R.id.doClassement);
        doRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClassementPouleFragment frag = mListePouleFragment.get(currentPoule);
                doClassement("Classement du vainqueur de la poule",1,
                        tableTournoi.getWithId(tournoiId).getNbEquipeMax()-frag.nbEquipesPoules()+1,
                        frag);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        setVisibleRankingButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_navigation_poule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNextPoule(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mListePouleFragment.get(currentPoule));
        ft.hide(mCalendrierMatchPouleFragment.get(currentPoule));

        if (currentPoule == nbPoule-1)
            currentPoule = 0;
        else
            currentPoule++;

        ft.show(mListePouleFragment.get(currentPoule));
        ft.show(mCalendrierMatchPouleFragment.get(currentPoule));
        ft.commit();
    }

    @Override
    public void onPreviousPoule(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(mListePouleFragment.get(currentPoule));
        ft.hide(mCalendrierMatchPouleFragment.get(currentPoule));

        if (currentPoule == 0)
            currentPoule = nbPoule-1;
        else
            currentPoule--;

        ft.show(mListePouleFragment.get(currentPoule));
        ft.show(mCalendrierMatchPouleFragment.get(currentPoule));
        ft.commit();
    }

    private void getDataFromIntent(){
        DBPhasePouleDAO tablePhasePoule = new DBPhasePouleDAO(mCtx);
        Intent intent = getIntent();
        phaseId = intent.getIntExtra(KeyTable.ARG_ID_PHASE,-1);
        tournoiId = intent.getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);
        int type = intent.getIntExtra(KeyTable.ARG_PHASE_TYPE,-1);
        if (type == Phase.TYPE_POULE){
            phasePouleId = tablePhasePoule.getFromPhaseId(phaseId).getId();
            nbPoule = tablePhasePoule.getFromPhaseId(phaseId).getNbPoule();
            DBPouleDAO tablePoule = new DBPouleDAO(mCtx);
            pouleList = new ArrayList<>(tablePoule.getPoules(phasePouleId));
            Log.d(TAG, "nbPoule="+nbPoule+" pouleList.size()="+pouleList.size());
            Log.d(TAG, "Liste des poules récupérées pour la phase:"+phaseId
                    +"\n"+pouleList.toString());
            nbPoule = pouleList.size(); // Permet de masquer les poules sans équipes

        } else if (type == Phase.TYPE_TABLEAU){
            // Impossible
        }


    }

    private void setVisibleRankingButton(){
        if (mCalendrierMatchPouleFragment.get(currentPoule).isFinish()){
            doRanking.setVisibility(Button.VISIBLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MATCH_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Log.d("POULE", "faire mise à jour");
                mCalendrierMatchPouleFragment.get(currentPoule).update();
                mListePouleFragment.get(currentPoule).remplirClassement();
                setVisibleRankingButton();
            }
        }
    }

    private void doClassement(String title, int min, int max, final ClassementPouleFragment frag){
        final Dialog d = new Dialog(mCtx);
        d.setTitle(title);
        d.setContentView(R.layout.dialog_number_picker);
        Button positiveButton = (Button) d.findViewById(R.id.positiveButton);
        Button negativeButton = (Button) d.findViewById(R.id.negativeButton);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker);
        np.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        np.setMaxValue(max);
        np.setMinValue(min);
        positiveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                cltDebut = np.getValue();
                frag.doRanking(tournoiId, cltDebut);
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

}
