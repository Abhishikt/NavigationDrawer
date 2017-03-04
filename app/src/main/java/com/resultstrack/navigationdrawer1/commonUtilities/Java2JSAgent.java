package com.resultstrack.navigationdrawer1.commonUtilities;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.resultstrack.navigationdrawer1.SurveyFragment;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by abhishikt on 2/1/2017.
 */

public class Java2JSAgent {


    private Context mContext;
    private SurveyFragment surveyFragment;

    public Java2JSAgent(Context ctx, SurveyFragment fragment){
        this.mContext = ctx;
        this.surveyFragment=fragment;
    }

    @JavascriptInterface
    public String GetSurveyQuestions(){

        return "Hello from android";
    }

    @JavascriptInterface
    public void SubmitSurveyResponse(String surveyResponse) {

        Toast.makeText(this.mContext, "Response received:" + surveyResponse.toString(), Toast.LENGTH_LONG).show();
        try {
            JSONArray responseArray = new JSONArray(surveyResponse.toString());
            surveyFragment.SaveSurveyResponse(responseArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @JavascriptInterface
    public void showMessage(String msg){
        Toast.makeText(this.mContext, msg, Toast.LENGTH_SHORT).show();
    }
}
