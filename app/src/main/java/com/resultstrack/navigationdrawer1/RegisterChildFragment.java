package com.resultstrack.navigationdrawer1;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.resultstrack.navigationdrawer1.commonUtilities.GPSTracker;
import com.resultstrack.navigationdrawer1.commonUtilities.RTGlobal;
import com.resultstrack.navigationdrawer1.model.ChildReg;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.UUID;

/*import static com.resultstrack.navigationdrawer1.R.style.AppTheme;*/


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterChildFragment extends Fragment {

    //implements ChildSurveyResponseSetListener

    /*private static final int REQUEST_IMAGE_CAPTURE = 1888;
    Button btnCamera;
    ImageView imageView;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    private OnFragmentInteractionListener mListener;*/
    Button btnTakePhoto;
    ImageView ivChildPhoto;
    EditText etChildName;
    EditText etParentName;
    static EditText etDOB;
    RadioGroup rdoGenderGrp;
    RadioButton rdoGenderMale;
    RadioButton rdoGenderFemale;
    Button btnRegister;
    private Location gLocation;
    //private JSONArray childSurveyResponse = null;
    private String childImageBase64;
    private GPSTracker tracker;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    byte[] byteArray;
    String fileName = null;

    public RegisterChildFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        /*return inflater.inflate(R.layout.fragment_register_child, container, false);*/
        final View rootView = inflater.inflate(R.layout.fragment_register_child,container,false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ResultsTrack - Register Child");

        btnTakePhoto = (Button)rootView.findViewById(R.id.btnChildImage);
        ivChildPhoto = (ImageView) rootView.findViewById(R.id.imageView2);
        etChildName = (EditText) rootView.findViewById(R.id.txtChildName);
        etParentName = (EditText) rootView.findViewById(R.id.txtParentName);
        etDOB = (EditText) rootView.findViewById(R.id.txtDateOfBirth);
        rdoGenderGrp  = (RadioGroup) rootView.findViewById(R.id.rdoGenderGroup);
        rdoGenderMale  = (RadioButton) rootView.findViewById(R.id.rdoGenderM);
        rdoGenderFemale  = (RadioButton) rootView.findViewById(R.id.rdoGenderF);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);

        //parent activity object
        RTGlobal.childSurveyResponse =null;

        tracker = new GPSTracker(this.getContext());

        if(tracker.canGetLocation){
            gLocation = tracker.getLocation();
            Toast.makeText(getContext(), "Lat:" + tracker.getLatitude() +" - Lng: " +tracker.getLongitude() , Toast.LENGTH_SHORT).show();
        }
        //DatePicker
        etDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new SelectDateFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        //TakePhoto
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();*/
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        //SaveChild
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //save Child registration data
                    ChildReg registration = new ChildReg();
                    registration.setName(etChildName.getText().toString());
                    registration.setParentName(etParentName.getText().toString());
                    registration.setDateOfBirth(etDOB.getText().toString());
                    int rdoId = rdoGenderGrp.getCheckedRadioButtonId();
                    RadioButton rdobtn = (RadioButton) rootView.findViewById(rdoId);
                    Toast.makeText(getContext(), rdobtn.getText(), Toast.LENGTH_LONG).show();
                    if (rdobtn.getText() == "Male")
                        registration.setGender(1);
                    else
                        registration.setGender(0);

                    registration.setImage(childImageBase64);
                    gLocation = tracker.getLocation();
                    if(gLocation!=null) {
                        registration.setLocation("[" + String.valueOf(gLocation.getLongitude()) + "," + String.valueOf(gLocation.getLatitude()) + "]");
                    }else{registration.setLocation("[85.308000,23.350216]");}
                    registration.setState(RTGlobal.getState());
                    registration.setDistrict(RTGlobal.getDistrict());
                    registration.setBlock(RTGlobal.getBlock());
                    registration.setVillageName(RTGlobal.getVillage());
                    registration.setPincode("");
                    registration.setAnganwadiCode(RTGlobal.getAnganwadiCode());

                    //Call Child Survey
                    getSurveyFragment(registration);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }

    private void getSurveyFragment(ChildReg oChildReg) {

        try {
            //remove current fragment
            getFragmentManager().beginTransaction().remove(this).commit();
            getFragmentManager().popBackStack();

            Bundle data = new Bundle();
            data.putParcelable("CHILD", oChildReg);
            SurveyFragment surveyFragment = new SurveyFragment();
            surveyFragment.setArguments(data);
            //surveyFragment
            this.getFragmentManager().beginTransaction()
                    .add(((ViewGroup) getView().getParent()).getId(), surveyFragment)
                    .addToBackStack(null) //"SurveyFragment"
                    .commit();
        }
        catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK){

                fileName = UUID.randomUUID().toString() + ".jpeg";

                Bitmap bmp = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byteArray = stream.toByteArray();

                childImageBase64 = Base64.encodeToString(byteArray,Base64.DEFAULT);

                //convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivChildPhoto.setImageBitmap(bitmap);

                /*//upload image thru AsyncTask
                new FileUpload(byteArray, fileName, new AsyncResponse(){
                    @Override
                    public void processFinish(Object output) {
                        if(((String)output).equals("OK")){
                            Toast.makeText(getContext(), "Image Uploaded Successfully!!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(), "Image Uploading Failed!!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();*/
            }
        }
    }

    public static class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), R.style.datepicker, this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            etDOB.setText(month+"/"+day+"/"+year);
        }
    }

}

