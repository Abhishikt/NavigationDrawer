package com.resultstrack.navigationdrawer1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.resultstrack.navigationdrawer1.model.OfflineData;


public class DashboardFragment extends Fragment {


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View dView =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        TextView tvRegChild = (TextView) dView.findViewById(R.id.txtTotalChildren);
        TextView tvComMem = (TextView) dView.findViewById(R.id.txtComMem);
        TextView tvMom = (TextView) dView.findViewById(R.id.txtMom);

        tvRegChild.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243" );

        Toast.makeText(getContext(), "Hello World", Toast.LENGTH_SHORT).show();

        return dView;

    }


}
