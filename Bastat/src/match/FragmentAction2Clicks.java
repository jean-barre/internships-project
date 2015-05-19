package com.enseirb.pfa.bastats.match;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.model.TempsDeJeu;

/**
 * Created by dohko on 22/02/15.
 */
public class FragmentAction2Clicks extends Fragment {

    public interface Action2licks{
        public void tir3Click();
        public void tir3rClick();
        public void lancerFrancRClick();
        public void lancerFrancClick();
        public void assistClick();



    }


    private Button tir_3;
    private Button tir_3r;
    private Button lancerFranc;
    private Button lancerFrancR;
    private Button assist;
    private Context Ctx;
    private Action2licks clickCallback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_actions_2clicks,container,false);
        tir_3 = (Button) v.findViewById(R.id.tir_3);
        tir_3r = (Button) v.findViewById(R.id.tir_3r);
        lancerFranc = (Button) v.findViewById(R.id.lancer_franc);
        lancerFrancR = (Button) v.findViewById(R.id.lancer_franc_r);
        assist = (Button) v.findViewById(R.id.assist);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Ctx=getActivity();
        initButtonAction();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        clickCallback=(Action2licks) activity;
    }

    public void initButtonAction(){
        tir_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clickCallback.tir3Click();

            }
        });

        tir_3r.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clickCallback.tir3rClick();
            }
        });

        lancerFrancR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clickCallback.lancerFrancRClick();
            }
        });

        lancerFranc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clickCallback.lancerFrancClick();
            }
        });

        assist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                clickCallback.assistClick();
            }

        });

    }



}


