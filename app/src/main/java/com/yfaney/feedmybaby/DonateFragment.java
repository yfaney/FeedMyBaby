package com.yfaney.feedmybaby;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 */
public class DonateFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    //private WebView mWebDonate;
    static DonateFragment fragment;

    public static DonateFragment newInstance(){
        if(fragment == null) fragment = new DonateFragment();
        return fragment;
    }

    public DonateFragment() {
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
            View view = inflater.inflate(R.layout.fragment_donate, container, false);
            //mWebDonate = (WebView) view.findViewById(R.id.textDonate);
            //mWebDonate.setWebViewClient(new WebViewClient());
            //mWebDonate.loadUrl(lWebForm);
            //mTextDonate = (TextView) view.findViewById(R.id.textDonate);
            //mTextDonate.setText(Html.fromHtml("<a href=\"http://www.google.com/\">google</a>"));
        return view;
    }

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
}
