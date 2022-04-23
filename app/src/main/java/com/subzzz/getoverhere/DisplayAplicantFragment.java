package com.subzzz.getoverhere;

import android.annotation.SuppressLint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.subzzz.getoverhere.Model.DriverApplicant;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DisplayAplicantFragment extends Fragment implements View.OnClickListener {
    private DriverApplicant applicant;
    private TextView fullName;
    private TextView idNum;
    private TextView email;
    private TextView licenceNum;
    private TextView licenceIssueDate;
    private TextView licenceExprDate;
    private EditText comments;
    private TextView licenceType;

    private Button approve;
    private Button reject;

    private ImageView licenceImageView;
    private ImageView idImageView;
    private ImageView licenceZoom;
    private ImageView idZoom;
    private ImageView zoomedImage;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();


    public DisplayAplicantFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DisplayAplicantFragment newInstance(String userId) {
        DisplayAplicantFragment fragment = new DisplayAplicantFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadApplicantData() {
        fullName.setText(String.format("%s %s", applicant.getfName(), applicant.getlName()));
        idNum.setText(String.format("%s", applicant.getIdNum()));
        email.setText(String.format("%s", applicant.getEmail()));
        licenceNum.setText(String.format("%s", applicant.getLicenceNum()));
        licenceIssueDate.setText(String.format("%s", applicant.getLicenceIssueDate().toDate().toString()));
        licenceExprDate.setText(String.format("%s", applicant.getLicenceExprDate().toDate().toString()));
        licenceType.setText(String.format("%s", applicant.getLicenceType()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_aplicant, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullName = view.findViewById(R.id.fullNameText);
        idNum = view.findViewById(R.id.idNumText);
        email = view.findViewById(R.id.emailText);
        comments = view.findViewById(R.id.CommentsText);
        licenceNum = view.findViewById(R.id.licenceNumText);
        licenceIssueDate = view.findViewById(R.id.licenceIssueDateText);
        licenceExprDate = view.findViewById(R.id.licenceExprDateText);
        licenceType = view.findViewById(R.id.licenceTypeText);


        licenceImageView = view.findViewById(R.id.licenceImageView);
        idImageView = view.findViewById(R.id.idImageView);
        licenceZoom = view.findViewById(R.id.imageZoomLicence);
        idZoom = view.findViewById(R.id.imageZoomId);
        zoomedImage = view.findViewById(R.id.expanded_image);

        approve = view.findViewById(R.id.approveBtn);
        reject = view.findViewById(R.id.rejectBtn);

        approve.setOnClickListener(this);
        reject.setOnClickListener(this);
        licenceZoom.setOnClickListener(this);
        idZoom.setOnClickListener(this);
        zoomedImage.setOnClickListener(this);

        Bundle args = this.getArguments();
        if (args != null) {
            String uid = args.getString("userId");
            db.collection("Driver-Applicants").whereEqualTo("uid", uid)
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    applicant = task.getResult().getDocuments().get(0).toObject(DriverApplicant.class);
                    loadImages();
                    loadApplicantData();
                } else {
                    Toast.makeText(getActivity(), "Something Went wrong loading applicant data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void loadImages() {
        Picasso.get().load(applicant.getLicenceImgUrl()).fit().into(licenceImageView);
        Picasso.get().load(applicant.getIdImgUrl()).fit().into(idImageView);

    }

    private void rejectApplicant() {
        StorageReference applicationRef = storage.getReference().child("Driver Applicants").child(applicant.getUid());
        applicationRef.child("Licence").delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                applicationRef.child("Id").delete().addOnSuccessListener(unused -> {
                    deleteApplication();
                    backToHomePage();
                });
                Toast.makeText(getActivity(), "Rejection was sent to user", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptApplicant() throws IOException {
        StorageReference sourceRefLicence = storage.getReference().child("Driver Applicants")
                .child(applicant.getUid()).child("Licence");
        StorageReference sourceRefId = storage.getReference().child("Driver Applicants")
                .child(applicant.getUid()).child("Id");
        StorageReference destinyRefLicence = storage.getReference().child("Drivers")
                .child(applicant.getUid()).child("Licence");
        StorageReference destinyRefId = storage.getReference().child("Drivers")
                .child(applicant.getUid()).child("Id");
        final String[] licenceImgUrl = new String[1];
        final String[] IdImgUrl = new String[1];

        File licenceFile = File.createTempFile("images","*");
        File IdFile = File.createTempFile("images","*");
        sourceRefLicence.getFile(licenceFile).addOnSuccessListener(taskSnapshot -> {
            UploadTask uploadTaskLicence = destinyRefLicence.putFile(Uri.fromFile(licenceFile));
            uploadTaskLicence
                    .addOnSuccessListener(taskSnapshot2 -> destinyRefLicence.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                licenceImgUrl[0] = uri.toString();
                                sourceRefId.getFile(IdFile).addOnSuccessListener(taskSnapshot3 -> {
                                    UploadTask uploadTaskLicence2 = destinyRefId.putFile(Uri.fromFile(IdFile));
                                    uploadTaskLicence2
                                            .addOnSuccessListener(taskSnapshot4 -> destinyRefId.getDownloadUrl()
                                                    .addOnSuccessListener(uri2 -> {
                                                        IdImgUrl[0] = uri2.toString();
                                                        sourceRefLicence.delete();
                                                        sourceRefId.delete();
                                                        addNewDriverData(licenceImgUrl[0],IdImgUrl[0]);
                                                    }));
                                });
                            }));
        });
    }

    private void addNewDriverData(String s, String s1) {
        Map<String, Object> data = new HashMap<>();
        data.put("utype", "Driver");
        data.put("licenceNum", applicant.getLicenceNum());
        data.put("licenceType", applicant.getLicenceType());
        data.put("licenceIssueDate", applicant.getLicenceIssueDate());
        data.put("licenceExprDate", applicant.getLicenceExprDate());
        data.put("licenceImgUrl",s );
        data.put("idImgUrl", s1);
        data.put("rank", (float) 0);
        db.collection("Users")
                .whereEqualTo("userId", applicant.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                        doc.getReference().set(data, SetOptions.merge());
                        deleteApplication();
                        Toast.makeText(getActivity(), "Welcome as a new driver", Toast.LENGTH_SHORT).show();
                        backToHomePage();
                    } else {
                        Toast.makeText(getActivity(), "Could Not update Application", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteApplication() {
        db.collection("Driver-Applicants")
                .whereEqualTo("uid", applicant.getUid()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        task.getResult().getDocuments().get(0).getReference().delete();
                    } else {
                        Log.w("driver applicants", "the document was not deleted");
                    }
                });
    }

    private void backToHomePage() {
        FragmentManager fm = getParentFragmentManager();
        Fragment fragment = new MapsFragment();
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void zoomInImage(String imgUri) {
        LinearLayout mainLayout = (LinearLayout) requireActivity().findViewById(R.id.mainLayout);
        Picasso.get().load(imgUri).fit().into(zoomedImage);
        zoomedImage.setVisibility(View.VISIBLE);
        mainLayout.setVisibility(View.GONE);
    }

    private void zoomOutImage() {
        LinearLayout mainLayout = (LinearLayout) requireActivity().findViewById(R.id.mainLayout);
        zoomedImage.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.expanded_image:
                zoomOutImage();
                break;
            case R.id.imageZoomLicence:
                zoomInImage(applicant.getLicenceImgUrl());
                break;
            case R.id.imageZoomId:
                zoomInImage(applicant.getIdImgUrl());
                break;
            case R.id.approveBtn:
                try {
                    acceptApplicant();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rejectBtn:
                rejectApplicant();
                break;
            default:
                break;
        }
    }


}