package com.yfaney.feedmybaby;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Younghwan on 7/20/2014.
 */
public class Rsc {
    public final static int UNIT_OZ = 1;
    public final static int UNIT_ML = 2;
    public final static String UNIT_PREF_OZ = "1";
    public final static String UNIT_PREF_ML = "2";
    public final static String DATE_TIME_FORMAT = "MM/dd/yyyy h:mm:ss a z";
    public final static String DATE_FORMAT = "MM/dd/yyyy";
    public final static String TIME_FORMAT = "h:mm:ss a z";
    public final static String UOM_OZ = "oz";
    public final static String UOM_ML = "ml";
    public final static String PREF_UNIT_OF_MEASURE = "unit_of_quantity_list";

    public static String getUoMfromPreference(Context context){
        String l_Value = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Rsc.PREF_UNIT_OF_MEASURE, Integer.toString(FeedInfo.UNIT_OZ));
        if(l_Value.equals(Rsc.UNIT_PREF_OZ)){
            return Rsc.UOM_OZ;
        }else{
            return Rsc.UOM_ML;
        }
    }

}
