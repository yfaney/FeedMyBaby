package com.yfaney.feedmybaby;

import android.support.v4.app.Fragment;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 * Created by Younghwan on 7/19/2014.
 */
public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onFragmentInteraction(BabyInfo id);
    public void onFragmentChange(Fragment mFragment);
    public void onFragmentAdd(Fragment mFragment);
    public void callAddActivity(Class activity);
    public void callAddActivityWithValues(Class activity, String[] values);
    public void callEditActivity(Class activity, int index);
    public void callEditActivityWithValues(Class activity, String[] values, int index);
}
