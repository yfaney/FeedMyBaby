package com.yfaney.feedmybaby;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.yfaney.feedmybaby.dummy.DummyContent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class FeedInfoFragment extends Fragment implements AbsListView.OnItemClickListener,
        AbsListView.OnItemLongClickListener, DialogInterface.OnClickListener, View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final int DATE_DIALOG_ID = 0;
    BabyInfoDBManager mDBManager;
    List<BabyInfo> mBabyList;
    ArrayList<TableItem> mFeedList;
    int mIndex = 0;
    String mSelectDate;
    int mSelectBaby;

    private int mYear;
    private int mMonth;
    private int mDay;


    // UI
    TextView textFeedDate;
    TextView textTotalQuantity;
    Button buttonAddFeedInfo;
    Button buttonChangeDate;

    private OnFragmentInteractionListener mListener;
    static FeedInfoFragment fragment;

    private Spinner mSpinner;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private TableAdapter mFeedAdapter;
    private ArrayAdapter<BabyInfo> mBabyAdapter;

    // UI Controller
    private Activity mActivity;


    public static FeedInfoFragment newInstance() {
        if(fragment == null) fragment = new FeedInfoFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // DB Initialize
        mDBManager = new BabyInfoDBManager(getActivity());
        // Select Date
        Date mDate = new Date();
        mYear = mDate.getYear() + 1900;
        mMonth = mDate.getMonth();
        mDay = mDate.getDate();
        mSelectDate = new SimpleDateFormat(BabyInfo.DATE_ONLY_FORMAT, Locale.ENGLISH).format(new Date());
        // Setting Baby List & Spinner Adapter
        mBabyList = mDBManager.selectBabyData();
        if(mBabyList.size() > 0){
            mBabyAdapter = new ArrayAdapter<BabyInfo>(getActivity(), android.R.layout.simple_spinner_item, mBabyList);
            mBabyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Setting Feed List & List Adapter
            mSelectBaby = mBabyList.get(0).getID();
            mFeedList = mDBManager.getFeedList(Integer.toString(mSelectBaby) , mSelectDate, getUoMfromPreference());
            if(mFeedList.size() > 0){
                mFeedAdapter = new TableAdapter(getActivity(), R.layout.row, mFeedList);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedinfo, container, false);

        // Set the spinner adapter
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setAdapter(mBabyAdapter);
        mSpinner.setOnItemSelectedListener(this);
        // Set the list adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));
        if(mFeedAdapter != null){
            ((AdapterView<ListAdapter>) mListView).setAdapter(mFeedAdapter);
        }else{
            setEmptyText(mActivity.getString(R.string.text_feed_list_empty));
        }
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        // Button Initialize
        buttonAddFeedInfo = (Button) view.findViewById(R.id.buttonAddFeedInfo);
        buttonChangeDate = (Button) view.findViewById(R.id.buttonChangeDate);
        buttonAddFeedInfo.setOnClickListener(this);
        buttonChangeDate.setOnClickListener(this);
        // Text Initialize
        textFeedDate = (TextView) view.findViewById(R.id.textFeedDate);
        textFeedDate.setText(mSelectDate);
        textTotalQuantity = (TextView) view.findViewById(R.id.textTotalQuantity);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        String lUoM = getUoMfromPreference();
        String lFormat = "";
        if(lUoM.equals(Rsc.UOM_OZ)){
            lFormat = "%.2f";
        }else{
            lFormat = "%.1f";
        }
        textTotalQuantity.setText(String.format(lFormat, mDBManager.getTotalFeedQuantity(Integer.toString(mSelectBaby), mSelectDate, lUoM)) + lUoM);
        refreshFeedView(mSelectBaby, mSelectDate);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            int selected = mFeedList.get(position).getFieldId();
            String param[] = {Integer.toString(mSelectBaby), mSelectDate};
            mListener.callEditActivityWithValues(NewFeedActivity.class, param, selected);
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            // mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        mIndex = mFeedList.get(position).getFieldId();
        AlertDialog.Builder dlg= new AlertDialog.Builder(mActivity);
        dlg.setTitle("Delete?")
                .setPositiveButton(R.string.btn_delete, this)
                .setNegativeButton(R.string.btn_cancel, this)
                .show();
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch(which) {
            case DialogInterface.BUTTON_POSITIVE:
                if(mIndex > 0){
                    BabyInfoDBManager dbManager = new BabyInfoDBManager(mActivity);
                    dbManager.removeFeedData(mIndex);
                    Toast.makeText(mActivity, "Successfully Removed.", Toast.LENGTH_SHORT).show();
                    refreshFeedView(mSelectBaby, mSelectDate);
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mIndex = 0;
                break;
        }
    }

    private void refreshView(){
        mBabyList = mDBManager.selectBabyData();
        mBabyAdapter = new ArrayAdapter<BabyInfo>(getActivity(), android.R.layout.simple_spinner_item, mBabyList);
        mBabyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mBabyAdapter);
    }

    private void refreshFeedView(int pBabyID, String pFeedDate){
        textFeedDate.setText(pFeedDate);
        String lUoM = getUoMfromPreference();
        mFeedList = mDBManager.getFeedList(Integer.toString(pBabyID), pFeedDate, lUoM);
        if(mFeedList.size() > 0){
            mFeedAdapter = new TableAdapter(getActivity(), R.layout.row, mFeedList);
        }
        else{
            if(mFeedAdapter != null) mFeedAdapter.clear();
            setEmptyText(mActivity.getString(R.string.text_feed_list_empty));
        }
        if(mFeedAdapter != null){
            ((AdapterView<ListAdapter>) mListView).setAdapter(mFeedAdapter);
        }
        String lFormat = "";
        if(lUoM.equals(Rsc.UOM_OZ)){
            lFormat = "%.2f";
        }else{
            lFormat = "%.1f";
        }
        textTotalQuantity.setText(String.format(lFormat
                ,mDBManager.getTotalFeedQuantity(Integer.toString(mSelectBaby), pFeedDate, lUoM)) + lUoM);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonChangeDate:
                new DatePickerDialog(getActivity(), mDateSetListener, mYear, mMonth, mDay).show();
                break;
            case R.id.buttonAddFeedInfo:
                String param[] = {Integer.toString(mSelectBaby), mSelectDate};
                mListener.callAddActivityWithValues(NewFeedActivity.class, param);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mSelectBaby = mBabyList.get(position).getID();
        refreshFeedView(mSelectBaby, mSelectDate);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    updateFeedDate();
                }
            };
    private void updateFeedDate(){
        Date lDate = new Date(mYear-1900, mMonth, mDay);
        mSelectDate = new SimpleDateFormat(BabyInfo.DATE_ONLY_FORMAT).format(lDate);
        refreshFeedView(mSelectBaby, mSelectDate);
    }

    private String getUoMfromPreference(){
        String l_Value = PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(Rsc.PREF_UNIT_OF_MEASURE, Integer.toString(FeedInfo.UNIT_OZ));
        if(l_Value.equals(Rsc.UNIT_PREF_OZ)){
            return Rsc.UOM_OZ;
        }else{
            return Rsc.UOM_ML;
        }
    }
}