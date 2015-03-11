package com.yfaney.feedmybaby;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.CustomLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewStyle;
import com.jjoe64.graphview.LineGraphView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class GraphFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int[] GRAPH_COLOR =
            { 0xFFFF1121, 0xFF00FF21, 0xFF1824FF, 0xFFFFFF15, 0xFFFF18FF, 0xFF13FFFF,
            0xFFFF0000, 0xFF00FF00, 0xFF0000FF, 0xFFFFFF00, 0xFFFF00FF, 0xFF00FFFF,
            0xFF484848, 0xFF000000};


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private BabyInfoDBManager mDbManager;

    private Date mBirthDate;

    static GraphFragment fragment;

    GraphView mGraphView;
    RelativeLayout mLayout;
    TextView mTextAverageValue;

    public static GraphFragment newInstance() {
        if(fragment == null) fragment = new GraphFragment();
        return fragment;
    }

    public GraphFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        mDbManager = new BabyInfoDBManager(getActivity());
        mLayout = (RelativeLayout) view.findViewById(R.id.graphLayout);
        mTextAverageValue = (TextView)view.findViewById(R.id.textAverageValue);
        return view;
    }
    public void onResume(){
        super.onResume();

        if(mGraphView != null) mLayout.removeView(mGraphView);

        ArrayList<BabyInfo> lBabyList = mDbManager.selectBabyData();
        ArrayList<GraphViewSeries> graphViewList = new ArrayList<GraphViewSeries>();
        String lUoM = Rsc.getUoMfromPreference(getActivity());
        StringBuffer lTextAvg = new StringBuffer();
        String lFormat = "";
        if(lUoM.equals(Rsc.UOM_OZ)){
            lFormat = "%.3f";
        }else{
            lFormat = "%.2f";
        }
        for (int i=0; i < lBabyList.size() ; i++){
            BabyInfo lBInfo = lBabyList.get(i);
            if(mBirthDate != null){
                if(mBirthDate.after(lBInfo.getBirthDate())){
                    mBirthDate = lBInfo.getBirthDate();
                }
            }else{
                mBirthDate = lBInfo.getBirthDate();
            }
            lTextAvg.append(lBInfo.getBabyName())
                    .append("- ")
                    .append(String.format(lFormat, mDbManager.getAverageFeedquantity(lBInfo.getID(), lUoM)))
                    .append(lUoM)
                    .append("; ");
            //ArrayList<FeedInfo> lFeedList = mDbManager.selectFeedData(lBInfo.getBabyName());
            ArrayList<String[]> lSumFeed = mDbManager.getTotalFeedSetQuantity(Integer.toString(lBInfo.getID()), lUoM);
            GraphView.GraphViewData[] graphData = new GraphView.GraphViewData[lSumFeed.size()];
            GraphViewSeries.GraphViewSeriesStyle graphStyle = new GraphViewSeries.GraphViewSeriesStyle(GRAPH_COLOR[i%GRAPH_COLOR.length],2);
            int j = 0;
            for(String[] set : lSumFeed){
                try {
                    Date lFeedDate = new SimpleDateFormat(Rsc.DATE_FORMAT).parse(set[0]);
                    int lDayFrom = Days.daysBetween(new DateTime(mBirthDate), new DateTime(lFeedDate)).getDays();
                    graphData[j] = new GraphView.GraphViewData(lDayFrom, Float.parseFloat(set[1]));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                j++;
            }
            GraphViewSeries graphSeries = new GraphViewSeries(lBInfo.getBabyName(), graphStyle, graphData);
            graphViewList.add(graphSeries);
        }
        mGraphView = new LineGraphView(
                getActivity() // context
                , getString(R.string.text_graph_feeding) // heading
        );
        GraphViewStyle style = new GraphViewStyle(0xFF000055,0xFF000055,0xFF000000);
        style.setLegendWidth(200);
        mGraphView.setGraphViewStyle(style);
        for (GraphViewSeries lSeries : graphViewList){
            mGraphView.addSeries(lSeries);
        }
        mGraphView.setCustomLabelFormatter(new CustomLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                String lUoM = Rsc.getUoMfromPreference(getActivity());
                if (isValueX) {
                    return getGraphLabelX(value, mBirthDate);
                }else{
                    return getGraphLabelY(value, lUoM);
                }
            }
        });
        //graphView.setViewPort(0, 28);
        mGraphView.setScalable(true);
        mGraphView.setShowLegend(true);
        //graphView.setLegendWidth(150);
        mLayout.addView(mGraphView);
        mTextAverageValue.setText(lTextAvg.toString());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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

    public static String getGraphLabelX(double value, Date pBirthDate){
        int lDaysFromBirth = (int)value;
        DateTime lBirthDate = new DateTime(pBirthDate);
        Date toDate = lBirthDate.plusDays(lDaysFromBirth).toDate();
        return new SimpleDateFormat(Rsc.DATE_FORMAT, Locale.ENGLISH).format(toDate);
    }
    public static String getGraphLabelY(double value, String pUoM)
    {
        return String.format("%.3g", value) + pUoM;
        /*if(pUoM.equals(Rsc.UOM_OZ)){

            if (value == 0) {
                return "0" + Rsc.UOM_OZ;
            } else if (value <= 1) {
                return "1.0" + Rsc.UOM_OZ;
            } else if (value <= 2) {
                return "2.0" + Rsc.UOM_OZ;
            } else if (value <= 3) {
                return "3.0" + Rsc.UOM_OZ;
            } else if (value <= 4) {
                return "4.0" + Rsc.UOM_OZ;
            } else if (value <= 5) {
                return "5.0" + Rsc.UOM_OZ;
            } else if (value <= 6) {
                return "6.0" + Rsc.UOM_OZ;
            } else if (value <= 7) {
                return "7.0" + Rsc.UOM_OZ;
            } else if (value <= 8) {
                return "8.0" + Rsc.UOM_OZ;
            } else if (value <= 9) {
                return "9.0" + Rsc.UOM_OZ;
            } else {
                return "10.0" + Rsc.UOM_OZ;
            }
        }
        else{
            if (value == 0) {
                return "0" + Rsc.UOM_ML;
            } else if (value <= 50) {
                return "50" + Rsc.UOM_ML;
            } else if (value <= 100) {
                return "100" + Rsc.UOM_ML;
            } else if (value <= 150) {
                return "150" + Rsc.UOM_ML;
            } else if (value <= 200) {
                return "200" + Rsc.UOM_ML;
            } else if (value <= 250) {
                return "250" + Rsc.UOM_ML;
            } else if (value <= 300) {
                return "300" + Rsc.UOM_ML;
            } else if (value <= 350) {
                return "350" + Rsc.UOM_ML;
            } else if (value <= 400) {
                return "400" + Rsc.UOM_ML;
            } else if (value <= 450) {
                return "450" + Rsc.UOM_ML;
            } else {
                return "500" + Rsc.UOM_ML;
            }
        }*/
    }
}
