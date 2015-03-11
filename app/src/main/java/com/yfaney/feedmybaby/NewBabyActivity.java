package com.yfaney.feedmybaby;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.yfaney.feedmybaby.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NewBabyActivity extends ActionBarActivity {

    // UI Control
    EditText editBabyName;
    DatePicker pickerBirthDate;

    boolean editMode = false;
    int editIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_baby);
        editBabyName = (EditText) findViewById(R.id.editBabyName);
        pickerBirthDate = (DatePicker) findViewById(R.id.datePickerBirth);
        editMode = getIntent().getBooleanExtra("EditMode", false);
        if(editMode){
            editIndex = getIntent().getIntExtra("EditIndex", 0);
            BabyInfoDBManager dbManager = new BabyInfoDBManager(this);
            BabyInfo baby = dbManager.selectBabyData(editIndex);
            Date bDate = baby.getBirthDate();
            editBabyName.setText(baby.getBabyName());
            int year = bDate.getYear() + 1900;
            int month = bDate.getMonth();
            int date = bDate.getDate();
            pickerBirthDate.updateDate(year,month,date);
            setTitle(getString(R.string.title_edit_baby));
            ((Button)findViewById(R.id.buttonAdd)).setText(getString(R.string.btn_save));
        }
        else{
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_baby, menu);
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
        BabyInfoDBManager dbManager = new BabyInfoDBManager(this);
        String lBabyName = editBabyName.getText().toString();
        String month = Integer.toString(pickerBirthDate.getMonth() + 1);
        String date = Integer.toString(pickerBirthDate.getDayOfMonth());
        String year = Integer.toString(pickerBirthDate.getYear());
        String lBirthDate = month + "/" + date + "/" +  year;
        BabyInfo info = new BabyInfo(0, lBabyName, lBirthDate);
        if(editMode){
            dbManager.updateBabyData(info, editIndex);
            Toast.makeText(this, getString(R.string.toast_save_success), Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            if(dbManager.insertBabyData(info) > 0){
                Toast.makeText(this, getString(R.string.toast_save_success), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        //finish();
        //BabyInfo baby = new BabyInfo()
        //dbManager.insertBabyData()
    }
    public void onCancelClicked(View v){
        finish();
    }
}
