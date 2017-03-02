package com.resultstrack.navigationdrawer1;


import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class SurveyFragment extends Fragment implements AsyncResponse{

    private String currentURL;
    private ChildReg oChildReg=null;

    public SurveyFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View sView = inflater.inflate(R.layout.fragment_survey, container, false);
        try {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ResultsTrack - Child Survey");
            //get registration object
            //ChildReg oChildReg = (ChildReg) savedInstanceState.getParcelable("CHILD");
            Bundle bundle = this.getArguments();
            if(bundle!=null) {
                oChildReg = (ChildReg) bundle.getParcelable("CHILD");
                oChildReg.delegate = this;
            }

            WebView wv = (WebView) sView.findViewById(R.id.wvSurvey);
            wv.getSettings().setJavaScriptEnabled(true);
            wv.addJavascriptInterface(new Java2JSAgent(getContext(), this), "android");
            wv.getSettings().setAllowFileAccess(true);
            wv.getSettings().setAllowContentAccess(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                wv.getSettings().setAllowFileAccessFromFileURLs(true);
                wv.getSettings().setAllowUniversalAccessFromFileURLs(true);
            }
            //wv.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            //wv.getSettings().setDomStorageEnabled(true);
            wv.setWebViewClient(new WebViewClient());
            //wv.loadUrl(currentURL);
            wv.loadUrl("file:///android_asset/www/ques.html");
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return sView;
    }

    public void SaveSurveyResponse(JSONArray response){
        try {
            List<SurveyResponse> lstResponses = new ArrayList<SurveyResponse>(); //oChildReg.getSurveyResponses();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jObject = response.getJSONObject(i);
                SurveyResponse oResponse = new SurveyResponse();
                oResponse.setId(UUID.randomUUID().toString());
                oResponse.setSurveyId(jObject.getString("survey_id"));
                oResponse.setOrganizationId(RTGlobal.getOrganizationId());
                oResponse.setQues_id(jObject.getString("ques_id"));
                oResponse.setQues_typ(jObject.getInt("ques_typ"));
                oResponse.setResponse(jObject.getString("response"));
                oResponse.setResponseDate(DateFormat.getDateTimeInstance().format(new Date()));
                lstResponses.add(oResponse);
            }
            //add survey responses
            oChildReg.setSurveyResponses(lstResponses);
            //save child registration
            oChildReg.save();

            /*RegisterChildFragment childFragment = new RegisterChildFragment();
            getFragmentManager().beginTransaction()
                    .replace(((ViewGroup) getView().getParent()).getId(), childFragment)
                    .addToBackStack(null)
                    .commit();

            //remove current fragment
            getFragmentManager().beginTransaction().remove(this).commit();*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(Object output) {

        //remove current fragment
        getFragmentManager().beginTransaction().remove(this).commit();
        getFragmentManager().popBackStack();

        RegisterChildFragment childFragment = new RegisterChildFragment();
        getFragmentManager().beginTransaction()
                .add(((ViewGroup) getView().getParent()).getId(), childFragment)
                .addToBackStack(null)
                .commit();
    }
}

