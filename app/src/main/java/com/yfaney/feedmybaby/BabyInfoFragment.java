package com.yfaney.feedmybaby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.yfaney.feedmybaby.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 */
public class BabyInfoFragment extends Fragment implements AbsListView.OnItemClickListener,
        AbsListView.OnItemLongClickListener, DialogInterface.OnClickListener {

    BabyInfoDBManager mDBManager;
    ArrayList<TableItem> mList;
    int mIndex = 0;
    static BabyInfoFragment fragment;
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private TableAdapter mAdapter;

    // UI Controller

    private Activity mActivity;
    private Button mButtonAdd;
    private Button mButtonCancel;




    // TODO: Rename and change types of parameters
    public static BabyInfoFragment newInstance() {
        if(fragment == null) fragment = new BabyInfoFragment();
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BabyInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDBManager = new BabyInfoDBManager(this.getActivity());
        mList = mDBManager.getBabyList();
        mAdapter = new TableAdapter(getActivity(), R.layout.row, mList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_babyinfo, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        mListView.setEmptyView(view.findViewById(android.R.id.empty));

        mButtonAdd = (Button)view.findViewById(R.id.buttonAdd);
        mButtonCancel = (Button)view.findViewById(R.id.buttonCancel);
        if(mList.size() == 0) setEmptyText(mActivity.getString(R.string.text_baby_list_empty));

        return view;
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
    public void onResume(){
        super.onResume();
        Log.d("BabyInfoFragment", "onResume()");
        refreshView();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Notify the active callbacks interface (the activity, if the
// fragment is attached to one) that an item has been selected.
//mListener.onFragmentInteraction(mBabyList.get(position));
        if (null != mListener) {
            if (parent != null) {
                mListener.callEditActivity(NewBabyActivity.class, mList.get(position).getFieldId());
            }
        }
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        mIndex = mList.get(position).getFieldId();
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
                    dbManager.removeBabyData(mIndex);
                    Toast.makeText(mActivity, "Successfully Removed.", Toast.LENGTH_SHORT).show();
                    refreshView();
                }
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                mIndex = 0;
                break;
        }
    }

    private void refreshView(){
        mList = mDBManager.getBabyList();
        mAdapter = new TableAdapter(getActivity(),R.layout.row, mList);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
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
}
