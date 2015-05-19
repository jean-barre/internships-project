package com.enseirb.pfa.bastats.tournoi.fragment;

import com.enseirb.pfa.bastats.R;
import com.enseirb.pfa.bastats.data.model.Phase;

/**
 * Created by rchabot on 24/02/15.
 */
public class DrawerItem {

    private String ItemName;
    private int imgResID;
    private String title;
    private int phaseId;
    private int type;

    public DrawerItem(String title) {
        this.title = title;
    }

    public DrawerItem(String item, boolean isTitle){
        if (!isTitle){
            this.ItemName = item;
            this.title = null;
        }
        else this.title = item;
    }

    public DrawerItem(Phase phase){
        if (phase.getType() == Phase.TYPE_POULE){
            setImgResID(R.drawable.ic_calendar);
        }
        if (phase.getType() == Phase.TYPE_TABLEAU){
            setImgResID(R.drawable.ic_phase_tableau);
        }
        this.ItemName = phase.toString();
        this.phaseId = phase.getId();
        this.type = phase.getType();
    }

    public DrawerItem(String itemName, int imgResID) {
        super();
        ItemName = itemName;
        this.imgResID = imgResID;
    }


    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public int getType() {
        return type;
    }
}
