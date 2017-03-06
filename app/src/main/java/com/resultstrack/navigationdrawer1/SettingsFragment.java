package com.resultstrack.navigationdrawer1;


import android.media.Rating;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Switch;

import com.resultstrack.navigationdrawer1.commonUtilities.RTGlobal;
import com.resultstrack.navigationdrawer1.model.LocalSettings;
import com.resultstrack.navigationdrawer1.model.OfflineData;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private Switch OffLineSwitch;
    private Switch AutoSyncSwitch;
    private RatingBar SatisfactionRating;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        OffLineSwitch = (Switch) rootView.findViewById(R.id.swtOffline);
        AutoSyncSwitch = (Switch) rootView.findViewById(R.id.swtAutoSync);
        SatisfactionRating = (RatingBar) rootView.findViewById(R.id.ratingBar);

        SatisfactionRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                LocalSettings settings=new LocalSettings("UserRating","RatingBar", String.valueOf(rating));
                settings.save();
            }
        });

        OffLineSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                RTGlobal.setOffLineFlg(isChecked);
                //Update Database - Local Settings
                LocalSettings settings=new LocalSettings("DataOffline","Switch", String.valueOf(isChecked));
                settings.save();
            }
        });

        AutoSyncSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                RTGlobal.setAutoSyncFlg(isChecked);
                //Update Database - Local Settings
                LocalSettings settings=new LocalSettings("AutoSync","Switch", String.valueOf(isChecked));
                settings.save();
            }
        });

        GetSavedLocalSettings();

        return rootView;
    }

    private void GetSavedLocalSettings() {

        try
        {
            List<LocalSettings> lstLocalSettings = new LocalSettings().getLocalSettings();
            for (LocalSettings setting:lstLocalSettings) {
                switch (setting.getProperty()){
                    case "DataOffline":
                        OffLineSwitch.setChecked(Boolean.valueOf(setting.getValue()));
                        break;
                    case "AutoSync":
                        AutoSyncSwitch.setChecked(Boolean.valueOf(setting.getValue()));
                        break;
                    case "Rating":
                        SatisfactionRating.setRating(Float.valueOf(setting.getValue()));
                        break;
                }
            }
        }catch (Exception e){

        }
    }

}
