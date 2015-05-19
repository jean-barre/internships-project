package com.enseirb.pfa.bastats.match;


/**
 * Created by lionel on 26/01/15.
 */

public class ChronoModel {
    private long quarter;
    private long quarterLength;

    public ChronoModel(long quarter, long quarterLength) {
        this.quarter = quarter;
        this.quarterLength = quarterLength;
    }

    public long getQuarter() {return this.quarter;}

    public long getQuarterLength() {return this.quarterLength;}

}