package com.yfaney.feedmybaby;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Younghwan on 7/18/2014.
 */
public class BabyInfo {
    public final static String DATE_ONLY_FORMAT = "MM/dd/yyyy";
    private int mID;
    private String mBabyName;
    private Date mBirthDate;

    public BabyInfo(int mID, String mBabyName, Date mBirthDate) {
        this.mID = mID;
        this.mBabyName = mBabyName;
        this.mBirthDate = mBirthDate;
    }

    public BabyInfo(int mID, String mBabyName, String mBirthDate) {
        this.mID = mID;
        this.mBabyName = mBabyName;
        try {
            this.mBirthDate = (new SimpleDateFormat(DATE_ONLY_FORMAT)).parse(mBirthDate);
        } catch (ParseException e) {
            e.printStackTrace();
            this.mBirthDate = new Date();
        }
    }


    public int getID(){
        return mID;
    }
    public String getBabyName() {
        return mBabyName;
    }

    public Date getBirthDate() {
        return mBirthDate;
    }
    public String getBirthDateStr(){
        return new SimpleDateFormat(DATE_ONLY_FORMAT).format(mBirthDate);
    }

    public void setBabyName(String mBabyName) {
        this.mBabyName = mBabyName;
    }

    public void setBirthDate(Date mBirthDate) {
        this.mBirthDate = mBirthDate;
    }
    public void setBirthDate(String mBirthDate){
        try {
            this.mBirthDate = (new SimpleDateFormat(DATE_ONLY_FORMAT)).parse(mBirthDate);
        } catch (ParseException e) {
            e.printStackTrace();
            this.mBirthDate = new Date();
        }

    }
    @Override
    public String toString(){
        return this.mBabyName;
    }

}
