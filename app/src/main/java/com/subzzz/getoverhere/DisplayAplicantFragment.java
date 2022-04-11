package com.subzzz.getoverhere;

import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.subzzz.getoverhere.Model.DriverApplicant;

import org.w3c.dom.Text;

public class DisplayAplicantFragment extends Fragment implements View.OnClickListener {
     private DriverApplicant applicant;
     private AppCompatActivity appCompatActivity;
     private TextView fullName;
     private TextView idNum;
     private TextView email;
     private EditText comments;

     private Button approve;
     private Button reject;

     private ImageView licenceImageView;
     private ImageView idImageView;
     private ImageView licenceZoom;
     private ImageView idZoom;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();





    public DisplayAplicantFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisplayAplicantFragment newInstance() {
        DisplayAplicantFragment fragment = new DisplayAplicantFragment();
        Bundle bundle = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if(bundle != null){
            applicant = bundle.getParcelable("applicant");
        }

        appCompatActivity = new AppCompatActivity(R.layout.fragment_display_aplicant);
        fullName = appCompatActivity.findViewById(R.id.fullNameText);
        idNum = appCompatActivity.findViewById(R.id.idNumText);
        email = appCompatActivity.findViewById(R.id.emailText);
        comments = appCompatActivity.findViewById(R.id.CommentsText);

        licenceImageView = appCompatActivity.findViewById(R.id.licenceImageView);
        idImageView = appCompatActivity.findViewById(R.id.idImageView);
        licenceZoom = appCompatActivity.findViewById(R.id.imageZoomLicence);
        idZoom = appCompatActivity.findViewById(R.id.imageZoomId);

        approve = appCompatActivity.findViewById(R.id.approveBtn);
        reject = appCompatActivity.findViewById(R.id.rejectBtn);

        approve.setOnClickListener(this);
        reject.setOnClickListener(this);
        licenceZoom.setOnClickListener(this);
        idZoom.setOnClickListener(this);

        loadImages();
        loadApplicantData();


    }

    private void loadApplicantData() {
        fullName.setText(String.format("Full Name: %s %s", applicant.getfName(), applicant.getlName()));
        idNum.setText(String.format("Id: %s", applicant.getIdNum()));
        email.setText(String.format("Email: %s", applicant.getEmail()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_aplicant, container, false);
    }

    public void loadImages(){
        Picasso.get().load(applicant.getLicenceFilePath()).fit().into(licenceImageView);
        Picasso.get().load(applicant.getIdFilePath()).fit().into(idImageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageZoomLicence:
                //Spread Licence Image to full screen ToDo
                break;
            case R.id.imageZoomId:
                //Spread id Image to full screen Todo
                break;
            case R.id.approveBtn:
                //approve application and move back to map
                break;
            case R.id.rejectBtn:
                //reject application and move back to map
                break;
            default:
                break;
        }
    }
}