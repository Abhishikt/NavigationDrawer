package com.resultstrack.navigationdrawer1.model;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.resultstrack.navigationdrawer1.R;

import java.util.ArrayList;

/**
 * Created by abhishikt sk on 5/18/2017.
 */

public class SearchResultsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private ArrayList<ChildReg> childDetails=new ArrayList<ChildReg>();
    int count;
    Typeface type;
    Context context;

    //constructor method
    public SearchResultsAdapter(Context context, ArrayList<ChildReg> child_details) {

        layoutInflater = LayoutInflater.from(context);

        this.childDetails=child_details;
        this.count= child_details.size();
        this.context = context;
        //type = Typeface.createFromAsset(context.getAssets(),"fonts/glyphicons-halflings-regular.ttf");
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int index) {
        return childDetails.get(index);
    }

    @Override
    public long getItemId(int index) {
        return index;
    }

    @Override
    public View getView(int index, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChildReg tempChild = childDetails.get(index);

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.childlistitem, null);
            holder = new ViewHolder();
            holder.child_name = (TextView) convertView.findViewById(R.id.child_name);
            holder.child_dob = (TextView) convertView.findViewById(R.id.child_dob);
            holder.child_gender = (TextView) convertView.findViewById(R.id.child_gender);
            holder.child_parent = (TextView) convertView.findViewById(R.id.child_parent);
            holder.child_vulnerability = (TextView) convertView.findViewById(R.id.child_vulnerability);
            holder.reportIncident = (Button) convertView.findViewById(R.id.report_incident);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.child_name.setText(tempChild.getName());
        //holder.child_name.setTypeface(type);

        //holder.child_dob.setTypeface(type);
        if(tempChild.getGender()>0)
            holder.child_gender.setText("Male");
        else
            holder.child_gender.setText("Female");
        //holder.child_gender.setTypeface(type);

        //holder.child_parent.setTypeface(type);

        holder.child_vulnerability.setText(tempChild.getLocation());
        //holder.child_vulnerability.setTypeface(type);

        return convertView;
    }

    static class ViewHolder
    {
        TextView child_name;
        TextView child_dob;
        TextView child_gender;
        TextView child_parent;
        TextView child_vulnerability;
        TextView childId;
        TextView product_savingsvalue;
        TextView qty;
        TextView product_value;
        Button reportIncident;

    }
}
