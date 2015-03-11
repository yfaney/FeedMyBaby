package com.yfaney.feedmybaby;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Younghwan on 7/18/2014.
 */
public class FeedInfo {
    public final static int UNIT_OZ = 1;
    public final static int UNIT_ML = 2;
    public final static double CFACTOR_OZ_TO_ML = 29.5735296;
    public final static String DATE_TIME_FORMAT = "MM/dd/yyyy h:mm:ss a z";
    public final static String DATE_FORMAT = "MM/dd/yyyy";
    public final static String TIME_FORMAT = "h:mm:ss a z";
    public final static String UOM_OZ = "oz";
    public final static String UOM_ML = "ml";

    int mID;
    int mBabyID;
    Date mFeedDate;
    float mQuantity_oz;
    float mQuantity_ml;
    String mPrefUoM;
    String mEtc;

    public FeedInfo(int mID, int mBabyID, String mFeedDate, String mFeedTime,
                    float mQuantity, String mUoM, String mEtc) {
        this.mID = mID;
        this.mBabyID= mBabyID;
        try {
            this.mFeedDate = new SimpleDateFormat(DATE_TIME_FORMAT)
                    .parse(mFeedDate + " " + mFeedTime);
        } catch (ParseException e) {
            this.mFeedDate = new Date();
        }
        if(mUoM.equals(UOM_OZ)){
            this.mQuantity_oz = mQuantity;
            this.mQuantity_ml = (float)(mQuantity * CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_OZ;
        }
        else{
            this.mQuantity_ml = mQuantity;
            this.mQuantity_oz = (float)(mQuantity / CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_ML;
        }
        this.mEtc = mEtc;
    }
    public FeedInfo(int mID, int mBabyID, Date mFeedDate, float mQuantity, int unit, String mEtc) {
        this.mID = mID;
        this.mBabyID= mBabyID;
        this.mFeedDate = mFeedDate;
        if(unit == UNIT_ML){
            this.mQuantity_ml = mQuantity;
            this.mQuantity_oz = (float)(mQuantity / CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_ML;
        }
        else{
            this.mQuantity_oz = mQuantity;
            this.mQuantity_ml = (float)(mQuantity * CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_OZ;
        }
        this.mEtc = mEtc;
    }
    public FeedInfo(int mID, int mBabyID, String mFeedDate, String mFeedTime,
                    float mQuantity, int unit, String mEtc) {
        this.mID = mID;
        this.mBabyID= mBabyID;
        try {
            this.mFeedDate = new SimpleDateFormat(DATE_TIME_FORMAT)
                    .parse(mFeedDate + " " + mFeedTime);
        } catch (ParseException e) {
            this.mFeedDate = new Date();
        }
        if(unit == UNIT_ML){
            this.mQuantity_ml = mQuantity;
            this.mQuantity_oz = (float)(mQuantity / CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_ML;
        }
        else{
            this.mQuantity_oz = mQuantity;
            this.mQuantity_ml = (float)(mQuantity * CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_OZ;
        }
        this.mEtc = mEtc;
    }

    public int getID(){
        return mID;
    }
    public int getBabyID() {
        return mBabyID;
    }

    public Date getFeedDate() {
        return mFeedDate;
    }

    public String getFeedDateStr(){
        return new SimpleDateFormat(DATE_FORMAT).format(mFeedDate);
    }
    public String getFeedTimeStr(){
        return new SimpleDateFormat(TIME_FORMAT).format(mFeedDate);
    }

    public float getQuantity(int unit) {
        if(unit == UNIT_ML){
            return this.mQuantity_ml;
        }
        else{
            return this.mQuantity_oz;
        }
    }

    public float getQuantity(String pUoM) {
        if(pUoM.equals(UOM_OZ)){
            return this.mQuantity_oz;
        }
        else{
            return this.mQuantity_ml;
        }
    }


    public String getEtc() {
        return mEtc;
    }

    public void setBabyID(int mBabyID) {
        this.mBabyID = mBabyID;
    }

    public void setFeedDate(Date mFeedDate) {
        this.mFeedDate = mFeedDate;
    }
    public void setFeedDateTime(String mFeedDate, String mFeedTime){
        try {
            this.mFeedDate = new SimpleDateFormat(DATE_TIME_FORMAT)
                    .parse(mFeedDate + " " + mFeedTime);
        } catch (ParseException e) {
            this.mFeedDate = new Date();
        }
    }

    public void setQuantity(float mQuantity, int unit) {
        if(unit == UNIT_ML){
            this.mQuantity_ml = mQuantity;
            this.mQuantity_oz = (float)(mQuantity / CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_ML;
        }
        else{
            this.mQuantity_oz = mQuantity;
            this.mQuantity_ml = (float)(mQuantity * CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_OZ;
        }
    }

    public void setQuantity(float mQuantity, String pUoM) {
        if(pUoM.equals(UOM_OZ)){
            this.mQuantity_oz = mQuantity;
            this.mQuantity_ml = (float)(mQuantity * CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_OZ;
        }
        else{
            this.mQuantity_ml = mQuantity;
            this.mQuantity_oz = (float)(mQuantity / CFACTOR_OZ_TO_ML);
            this.mPrefUoM = UOM_ML;
        }
    }
    public String getPreferredUoM(){
        return this.mPrefUoM;
    }

    public void setEtc(String mEtc) {
        this.mEtc = mEtc;
    }

    @Override
    public String toString(){
       return Float.toString(this.getQuantity(mPrefUoM)) + " " + mPrefUoM;
    }
}