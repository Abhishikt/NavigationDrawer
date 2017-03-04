package com.resultstrack.navigationdrawer1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.resultstrack.navigationdrawer1.commonUtilities.AsyncResponse;
import com.resultstrack.navigationdrawer1.commonUtilities.GPSTracker;
import com.resultstrack.navigationdrawer1.model.CommitteeMember;

import java.io.ByteArrayOutputStream;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCMember extends Fragment implements AsyncResponse {


    /*private OnFragmentInteractionListener mListener;*/
    Button btnTakePhoto;
    ImageView ivMemberImage;
    EditText etMemberFName;
    EditText etMemberLName;
    EditText etMemberEmail;
    EditText etMemberPhoneNo;
    EditText etMemberAddress;
    Spinner spinnerDesignation;
    Spinner spinnerTitle;
    Button btnRegister;
    private Location gLocation;
    private String memberImageBase64;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1888;
    byte[] byteArray;
    String fileName = null;
    CommitteeMember registration;


    public RegisterCMember() {
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
        //return inflater.inflate(R.layout.fragment_register_cmember, container, false);
        final View rootView = inflater.inflate(R.layout.fragment_register_cmember, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("ResultsTrack - Add Member");

        btnTakePhoto = (Button)rootView.findViewById(R.id.btnMemberImage);
        ivMemberImage = (ImageView) rootView.findViewById(R.id.imageView2);
        etMemberFName = (EditText) rootView.findViewById(R.id.txtMemberFName);
        etMemberLName = (EditText) rootView.findViewById(R.id.txtMemberLName);
        etMemberEmail = (EditText) rootView.findViewById(R.id.txtMemberEmail);
        etMemberPhoneNo = (EditText) rootView.findViewById(R.id.txtMemberPhoneNo);
        etMemberAddress  = (EditText) rootView.findViewById(R.id.txtMemberAddress);
        spinnerDesignation  = (Spinner) rootView.findViewById(R.id.spinnerDesignation);
        spinnerTitle  = (Spinner) rootView.findViewById(R.id.spinnerTitle);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);

        GPSTracker tracker = new GPSTracker(this.getContext());

        if(tracker.canGetLocation){
            gLocation = tracker.getLocation();
            Toast.makeText(getContext(), "Lat:" + tracker.getLatitude() +" - Lng: " +tracker.getLongitude() , Toast.LENGTH_SHORT).show();
        }

        //TakePhoto
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        //SaveChild
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveMemberRegistration();
            }
        });

        return rootView;
    }

    public void SaveMemberRegistration()
    {
        try {
            //save Child registration data
            registration = new CommitteeMember();
            registration.delegate = this;
            registration.setFstnm(etMemberFName.getText().toString());
            registration.setLstnm(etMemberLName.getText().toString());
            registration.setDesignation(spinnerDesignation.getSelectedItem().toString());
            registration.setTitle(spinnerTitle.getSelectedItem().toString());
            registration.setEmail(etMemberEmail.getText().toString());
            registration.setPhone(etMemberPhoneNo.getText().toString());
            registration.setAddress(etMemberAddress.getText().toString());
            registration.setImage(memberImageBase64);
            if(gLocation!=null) {
                registration.setLocation("[" + String.valueOf(gLocation.getLongitude()) + "," + String.valueOf(gLocation.getLatitude()) + "]");
            }else{registration.setLocation("[null,null]");}
            registration.setState("");
            registration.setDistrict("");
            registration.setBlock("");
            registration.setVillageName("");
            registration.setPincode("");
            registration.setAnganwadiCode("");

            //Save Member Registration
            registration.save();

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
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

                memberImageBase64 = Base64.encodeToString(byteArray,Base64.DEFAULT);

                //convert byte array to Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                ivMemberImage.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public void processFinish(Object output) {
        String msg = (String)output;
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

        //remove current fragment
        getFragmentManager().beginTransaction().remove(this).commit();
        getFragmentManager().popBackStack();

        RegisterCMember memberFragment = new RegisterCMember();
        getFragmentManager().beginTransaction()
                .add(((ViewGroup) getView().getParent()).getId(), memberFragment)
                .addToBackStack(null)
                .commit();
    }
}
