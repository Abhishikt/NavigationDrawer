package com.resultstrack.navigationdrawer1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class DashboardFragment extends Fragment {

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View dView =  inflater.inflate(R.layout.fragment_dashboard, container, false);

        Toast.makeText(getContext(), "Hello World", Toast.LENGTH_SHORT).show();

        return dView;

    }


}
