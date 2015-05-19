package com.enseirb.pfa.bastats.tournoi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.model.Phase;
import com.enseirb.pfa.bastats.tournoi.fragment.NavigationDrawerFragment;
import com.enseirb.pfa.bastats.tournoi.fragment.OverviewTournoi;

public class NavigationDrawerTournoi extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int REQUEST_AJOUT_CLASSEMENT = 88;
    private static final int REQUEST_NOUVELLE_PHASE = 99;


    private int tournoiId;
    private Context mCtx;

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private OverviewTournoi mOverviewTournoi;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_tournoi);

        mCtx = this;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
               getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        tournoiId = getIntent().getIntExtra(KeyTable.ARG_ID_TOURNOI,-1);
        Log.d("Navigation Drawer", "Identifiant du tournoi " + tournoiId);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),
                tournoiId);

        mOverviewTournoi = OverviewTournoi.newInstance(tournoiId);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container, mOverviewTournoi)
                .commit();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        if (position == 0) {
            // Do nothing title
        }
        else if (position == 1){
            // TODO Call Overview fragment
            // update the main content by replacing fragments

        }
        else if (position == 2){
            // TODO Call Statistiques fragment
        }
        else if (position == 3) {
            Intent intent = new Intent(this, ChoixTypePhaseActivity.class);
            intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
            // On enregistre l'ordre de temporel de la phase que l'on va créer
            //intent.putExtra(KeyTable.ARG_NUM_PHASE, mNavigationDrawerFragment.getNumeroPhase());
            startActivityForResult(intent, REQUEST_NOUVELLE_PHASE);
        }
        else if (position == 4){
            // Do nothing Title
        } else {
            // Cas où l'on a la liste des phases
            if (mNavigationDrawerFragment.getType(position) == Phase.TYPE_TABLEAU){
                Intent intent = new Intent(this, NavigationTableau.class);
                intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                intent.putExtra(KeyTable.ARG_ID_PHASE, mNavigationDrawerFragment.getPhaseId(position));
                intent.putExtra(KeyTable.ARG_PHASE_TYPE, mNavigationDrawerFragment.getType(position));
                startActivityForResult(intent,REQUEST_AJOUT_CLASSEMENT);
            } else if (mNavigationDrawerFragment.getType(position) == Phase.TYPE_POULE) {
                Intent intent = new Intent(this, NavigationPouleActivity.class);
                intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                intent.putExtra(KeyTable.ARG_ID_PHASE, mNavigationDrawerFragment.getPhaseId(position));
                intent.putExtra(KeyTable.ARG_PHASE_TYPE, mNavigationDrawerFragment.getType(position));
                // On rempli intent en fonction de la position (liste qui stocke les phases dans le fragment?)
                startActivityForResult(intent,REQUEST_AJOUT_CLASSEMENT);
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.navigation_drawer_tournoi, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OverviewTournoi.REQUEST_CLT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mOverviewTournoi.update();
                Log.d("RES", "onResult");
            }
        }
        if (requestCode == REQUEST_AJOUT_CLASSEMENT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                mOverviewTournoi.update();
                Log.d("RES", "depuis NavigationTableau");
            }
        }
        if (requestCode == REQUEST_NOUVELLE_PHASE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(mCtx, NavigationDrawerTournoi.class);
                intent.putExtra(KeyTable.ARG_ID_TOURNOI, tournoiId);
                startActivity(intent);
                finish();
            }
        }
    }
}
