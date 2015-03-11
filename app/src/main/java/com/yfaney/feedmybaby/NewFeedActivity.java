package com.yfaney.feedmybaby;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yfaney.feedmybaby.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewFeedActivity extends ActionBarActivity {

    boolean editMode = false;
    int editIndex = 0;

    int mSelectBaby;
    String mSelectDate;
    String mFeedTime;

    // UI
    TextView mTextFeedDate;
    TextView mTextUnit;
    SeekBar mSeekBar;
    TimePicker mTimePicker;
    EditText mEditQuantity;
    EditText mEditEtc;
    Button mButtonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feed);
        // UI (Text, Edit)
        mTextFeedDate = (TextView) findViewById(R.id.textFeedDate);
        mEditQuantity = (EditText) findViewById(R.id.editQuantity);
        mTextUnit = (TextView) findViewById(R.id.textUnit);
        mEditEtc = (EditText) findViewById(R.id.editEtc);
        mButtonAdd = (Button) findViewById(R.id.buttonFeedAdd);
        // UI - SeekBar, TimePicker
        mSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        mTimePicker = (TimePicker) findViewById(R.id.timePicker);
        // Events
        mEditQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lProgress = 0;
                String lUoM = Rsc.getUoMfromPreference(getBaseContext());
                if(lUoM.equals(Rsc.UOM_OZ)){
                    lProgress = (int) (Float.parseFloat(s.toString()) * 10);
                }else{
                    lProgress = (int) (Float.parseFloat(s.toString()) / 2);
                }
                if(lProgress >= 0 && lProgress <= 100){
                    mSeekBar.setProgress(lProgress);
                }
                else{
                    Toast.makeText(getBaseContext(), "Out of value", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float lQuantity;
                String lUoM = Rsc.getUoMfromPreference(getBaseContext());
                if(lUoM.equals(Rsc.UOM_OZ)){
                    lQuantity = ((float)progress / 10f);
                }
                else{
                    lQuantity = (float)progress * 2;
                }
                mEditQuantity.setText(Float.toString(lQuantity));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Initialize Values
        Intent intent = getIntent();
        int i = intent.getIntExtra("PARAM_LENGTH", 0);
        if(i >= 2){
            mSelectBaby = Integer.parseInt(intent.getStringExtra("PARAM_0"));
            mSelectDate = intent.getStringExtra("PARAM_1");
            mTextFeedDate.setText(mSelectDate);
        }else{
            Date lCurrent = new Date();
            mSelectDate = new SimpleDateFormat(FeedInfo.DATE_FORMAT).format(lCurrent);
            mTextFeedDate.setText(mSelectDate);
        }
        editMode = intent.getBooleanExtra("EditMode", false);
        if(editMode) {
            editIndex = intent.getIntExtra("EditIndex", 0);
            BabyInfoDBManager dbManager = new BabyInfoDBManager(this);
            FeedInfo mFeedInfo = dbManager.selectFeedData(editIndex);
            mTimePicker.setCurrentHour(mFeedInfo.getFeedDate().getHours());
            mTimePicker.setCurrentMinute(mFeedInfo.getFeedDate().getMinutes());
            mFeedTime = new SimpleDateFormat(FeedInfo.TIME_FORMAT).format(mFeedInfo.getFeedDate());
            String lUoM = Rsc.getUoMfromPreference(this);
            mEditQuantity.setText(Float.toString(mFeedInfo.getQuantity(lUoM)));
            mEditEtc.setText(mFeedInfo.getEtc());
            /*int progress = 0;
            if(lUoM.equals(Rsc.UOM_OZ)){
                progress = (int)(mFeedInfo.getQuantity(lUoM) * 10);
            }
            else{
                progress = (int)(mFeedInfo.getQuantity(lUoM) / 2);
            }
            if(progress > 100) progress = 100;
            mSeekBar.setProgress(progress);*/
            setTitle(getString(R.string.title_edit_feed_info));
            mEditEtc.setText(mFeedInfo.getEtc());
            mButtonAdd.setText(getString(R.string.btn_save));
        }
        else{
            Date lCurrent = new Date();
            mFeedTime = new SimpleDateFormat(FeedInfo.TIME_FORMAT).format(lCurrent);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        String lUoM = Rsc.getUoMfromPreference(this);
        if(lUoM.equals(Rsc.UOM_OZ)){
            mTextUnit.setText(Rsc.UOM_OZ);
        }else{
            mTextUnit.setText(Rsc.UOM_ML);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddClicked(View v){
        String lUoM = Rsc.getUoMfromPreference(this);
        float lQuantity = Float.parseFloat(mEditQuantity.getText().toString());
        String lEtc = mEditEtc.getText().toString();
        BabyInfoDBManager dbManager = new BabyInfoDBManager(this);
        if(mSelectBaby > 0 && mSelectDate != null){
            FeedInfo info = new FeedInfo(0, mSelectBaby, mSelectDate, mFeedTime, lQuantity, lUoM, lEtc);
            if(editMode) {
                dbManager.updateFeedData(info, editIndex);
                Toast.makeText(this, getString(R.string.toast_save_success), Toast.LENGTH_SHORT).show();
            }else{
                if(dbManager.insertFeedData(info) >0 ){
                Toast.makeText(this, getString(R.string.toast_save_success), Toast.LENGTH_SHORT).show();
                }
            }
        }
        finish();
    }

    public void onCancelClicked(View v){
        finish();
    }

}
