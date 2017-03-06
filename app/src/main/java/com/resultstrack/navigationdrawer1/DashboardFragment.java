package com.resultstrack.navigationdrawer1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.resultstrack.navigationdrawer1.commonUtilities.AsyncResponse;
import com.resultstrack.navigationdrawer1.model.OfflineData;


public class DashboardFragment extends Fragment implements AsyncResponse {

    TextView tvRegChild;
    TextView tvComMem;
    TextView tvMom;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View dView =  inflater.inflate(R.layout.fragment_dashboard, container, false);
        tvRegChild = (TextView) dView.findViewById(R.id.txtTotalChildren);
        tvComMem = (TextView) dView.findViewById(R.id.txtComMem);
        tvMom = (TextView) dView.findViewById(R.id.txtMom);
        OfflineData.delegate = this;

        tvRegChild.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
        tvComMem.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
        tvMom.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");

        final ImageView ivChildren = (ImageView) dView.findViewById(R.id.imageViewChildren);
        ivChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivChildren.setEnabled(false);
                OfflineData.SyncOfflineData();
                ivChildren.setEnabled(true);
            }
        });
        final ImageView ivComMem = (ImageView) dView.findViewById(R.id.imageViewComMem);
        ivComMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivComMem.setEnabled(false);
                OfflineData.SyncOfflineData();
                ivComMem.setEnabled(true);
            }
        });
        final ImageView ivMom = (ImageView) dView.findViewById(R.id.imageViewMom);
        ivMom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMom.setEnabled(false);
                OfflineData.SyncOfflineData();
                ivMom.setEnabled(true);
            }
        });
        final ImageView ivDownloadUpdates = (ImageView) dView.findViewById(R.id.imageViewDownloadUpdates);
        ivDownloadUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivDownloadUpdates.setEnabled(false);
                OfflineData.SyncOfflineData();
                ivDownloadUpdates.setEnabled(true);
            }
        });

        //Toast.makeText(getContext(), "Hello World", Toast.LENGTH_SHORT).show();
        final ImageButton syncButton = (ImageButton) dView.findViewById(R.id.imageButtonSyncNow);
        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncButton.setEnabled(false);
                OfflineData.SyncOfflineData();
                syncButton.setEnabled(true);
            }
        });

        return dView;

    }


    @Override
    public void processFinish(Object output) {
        String str = (String)output;
        switch (str) {
            case "REGCHILD":
                tvRegChild.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
                break;
            case "COMMEM":
                tvComMem.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
                break;
            case "MOM":
                tvMom.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
                break;
            case "ERROR":
                //Toast.makeText(getActivity().getApplicationContext(), "Sync Complete.", Toast.LENGTH_SHORT).show();
                break;
            default:
                tvRegChild.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
                tvComMem.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
                tvMom.setText(OfflineData.getOfflineChildRegistrationData().size() + " / 243");
        }
        Toast.makeText(getActivity().getApplicationContext(), "Sync Complete.", Toast.LENGTH_SHORT).show();
    }
}
