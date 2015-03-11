package com.yfaney.feedmybaby;

/**
 * Created by Younghwan on 7/19/2014.
 */

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This uses Class uses for displaying data in Listview
 * @author Administrator
 *
 */
public class TableAdapter extends ArrayAdapter<TableItem> {
    private ArrayList<TableItem> items;

    public TableAdapter(Context context, int textViewResourceId, ArrayList<TableItem> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.row, null);
        }
        TableItem subList = items.get(position);
        if (subList != null) {
            TextView tt = (TextView) v.findViewById(R.id.toptext);
            TextView bt = (TextView) v.findViewById(R.id.bottomtext);
            if (tt != null){
                tt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
                tt.setTextColor(0xCD7979FF);
                tt.setText(subList.getFieldName());
            }
            if(bt != null){
                bt.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                bt.setText(subList.getFieldTitle());
            }
        }
        return v;
    }
}