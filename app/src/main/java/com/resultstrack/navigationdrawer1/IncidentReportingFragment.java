package com.resultstrack.navigationdrawer1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.SuppressLint;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.resultstrack.navigationdrawer1.commonUtilities.AsyncResponse;
import com.resultstrack.navigationdrawer1.model.ChildReg;
import com.resultstrack.navigationdrawer1.model.SearchResultsAdapter;


public class IncidentReportingFragment extends Fragment implements AdapterView.OnItemClickListener, AsyncResponse {

    View myFragmentView;
    SearchView search;
    ImageButton buttonBarcode;
    ImageButton buttonAudio;
    Typeface type;
    ListView searchResults;
    String found = "N";
    ChildReg childReg;
    ProgressDialog pd;

    //This arraylist will have data as pulled from server. This will keep cumulating.
    ArrayList<ChildReg> childResults = new ArrayList<ChildReg>();
    //Based on the search string, only filtered products will be moved here from productResults
    ArrayList<ChildReg> filteredChildResults = new ArrayList<ChildReg>();

    /*public IncidentReportingFragment() {
        // Required empty public constructor
    }*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //get the context of the HomeScreen Activity
        //final MainActivity activity = (MainActivity) getActivity();

        childReg = new ChildReg();
        childReg.delegate = this;
        //define a typeface for formatting text fields and listview.

        //type = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),"fonts/glyphicons-halflings-regular.ttf");
        myFragmentView = inflater.inflate(R.layout.fragment_incident_reporting, container, false);
        //View rootView = inflater.inflate(R.layout.fragment_incident_reporting, container, false);

        search=(SearchView) myFragmentView.findViewById(R.id.searchView);
        search.setQueryHint("Start typing to search...");

        searchResults = (ListView) myFragmentView.findViewById(R.id.childList);

        //this part of the code is to handle the situation when user enters any
        // search criteria, how should the application behave?
        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                Toast.makeText(getActivity().getApplicationContext(), String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 3)
                {
                    searchResults.setVisibility(myFragmentView.VISIBLE);
                    //myAsyncTask m= (myAsyncTask) new myAsyncTask().execute(newText);
                    /*pd= new ProgressDialog(getActivity());
                    pd.setCancelable(false);
                    pd.setMessage("Searching...");
                    pd.getWindow().setGravity(Gravity.CENTER);
                    pd.show();*/
                    childReg.getChildren(newText);
                }
                else
                {
                    searchResults.setVisibility(myFragmentView.INVISIBLE);
                }
                return false;
            }
        });

        return myFragmentView;
    }

    //this filters products from productResults and copies to filteredProductResults based on search text

    public void filterProductArray(String newText)
    {

        String pName;

        filteredChildResults.clear();
        for (int i = 0; i < childResults.size(); i++)
        {
            pName = childResults.get(i).getName().toLowerCase();
            if ( pName.contains(newText.toLowerCase()) || childResults.get(i).getParentName().contains(newText))
            {
                filteredChildResults.add(childResults.get(i));
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity().getApplicationContext(), "Hello Data....", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void processFinish(Object output) {
        childResults = (ArrayList<ChildReg>) output;
        if(childResults.size()>0) {
            searchResults.setAdapter(new SearchResultsAdapter(getActivity().getApplicationContext(), childResults));
        }else{
            Toast.makeText(getActivity().getApplicationContext(), "No Data....", Toast.LENGTH_SHORT).show();
        }
        //pd.dismiss();
    }
}


