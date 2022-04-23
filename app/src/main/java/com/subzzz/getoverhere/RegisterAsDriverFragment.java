package com.subzzz.getoverhere;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.subzzz.getoverhere.Model.DriverApplicant;
import com.subzzz.getoverhere.Model.User;
import com.subzzz.getoverhere.Util.UserApi;

import java.util.Calendar;
import java.util.Objects;

import okhttp3.internal.Util;


public class RegisterAsDriverFragment extends Fragment implements View.OnClickListener {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText licenceNum;
    private DatePicker licenceIssueDate;
    private DatePicker licenceExprDate;
    private Spinner licenceType;
    private Button uploadLicenceImgBtn;
    private Button uploadIdImgBtn;
    private Button submitApplicationBtn;
    private Uri licenceImgUri;
    private Uri IdImgUri;

    private ActivityResultLauncher<Intent> licenceActivityResultLuncher;
    private ActivityResultLauncher<Intent> idActivityResultLuncher;

    public RegisterAsDriverFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static RegisterAsDriverFragment newInstance() {
        return new RegisterAsDriverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register2, container, false);
        loadComponents(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void loadComponents(View view) {
        licenceNum = view.findViewById(R.id.DLNI);
        licenceIssueDate = view.findViewById(R.id.licenceIssueDate);
        licenceExprDate = view.findViewById(R.id.licenceExprDate);
        licenceType = view.findViewById(R.id.LT);
        uploadLicenceImgBtn = view.findViewById(R.id.uploadLicenceBtn);
        uploadIdImgBtn = view.findViewById(R.id.uploadIdBtn);
        submitApplicationBtn = view.findViewById(R.id.submitDriverApplicationBtn);

        uploadLicenceImgBtn.setOnClickListener(this);
        uploadIdImgBtn.setOnClickListener(this);
        submitApplicationBtn.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerActionLunchers();

    }

    private void registerActionLunchers() {
        licenceActivityResultLuncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        licenceImgUri = data.getData();
                    }
                }
        );

        idActivityResultLuncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        IdImgUri = data.getData();
                    }
                }
        );
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uploadLicenceBtn:
                addLicenceImg();
                break;
            case R.id.uploadIdBtn:
                addIdImg();
                break;
            case R.id.submitDriverApplicationBtn:
                applyAsDriver();
                break;
            default:
                break;
        }
    }

    private void backToHomePage() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MapsFragment())
                .commit();
    }

    private void addIdImg() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        Intent.createChooser(galleryIntent, "Choose a Licence Image");
        licenceActivityResultLuncher.launch(galleryIntent);
    }

    private void addLicenceImg() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        Intent.createChooser(galleryIntent, "Choose an Id Image");
        idActivityResultLuncher.launch(galleryIntent);
    }

    private void applyAsDriver() {
        if (licenceImgUri == null) {
            Toast.makeText(getActivity(), "Please add Image of your Licence", Toast.LENGTH_SHORT).show();
            return;
        }
        if (IdImgUri == null) {
            Toast.makeText(getActivity(), "Please add Image of your Id", Toast.LENGTH_LONG).show();
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(licenceIssueDate.getYear(), licenceIssueDate.getMonth(), licenceIssueDate.getDayOfMonth());
        Timestamp issueTime = new Timestamp(cal.getTime());

        cal.set(licenceExprDate.getYear(), licenceExprDate.getMonth(), licenceExprDate.getDayOfMonth());
        Timestamp exprTime = new Timestamp(cal.getTime());

        User currentUser = UserApi.getInstance().getCurrentUser();
        DriverApplicant newDriver = new DriverApplicant();
        newDriver.setUid(currentUser.getUserId());
        newDriver.setIdNum(currentUser.getIdNum());
        newDriver.setfName(currentUser.getFirstName());
        newDriver.setlName(currentUser.getLastName());
        newDriver.setEmail(currentUser.getEmail());
        newDriver.setLicenceNum(licenceNum.getText().toString().trim());
        newDriver.setLicenceType(licenceType.getSelectedItem().toString().trim());
        newDriver.setLicenceIssueDate(issueTime);
        newDriver.setLicenceExprDate(exprTime);

        StorageReference licenceRef = storage.getReference().child("Driver Applicants/" + UserApi.getInstance().getCurrentUser().getUserId())
                .child("Licence");

        licenceRef.putFile(licenceImgUri)
                .addOnSuccessListener(taskSnapshot -> licenceRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            newDriver.setLicenceImgUrl(uri.toString());
                            StorageReference idRef = storage.getReference().child("Driver Applicants/" + UserApi.getInstance().getCurrentUser().getUserId())
                                    .child("Id");
                            idRef.putFile(IdImgUri).addOnSuccessListener(taskSnapshot2 -> idRef.getDownloadUrl()
                                    .addOnSuccessListener(uri2 -> {
                                        newDriver.setIdImgUrl(uri2.toString());

                                        db.collection("Driver-Applicants").add(newDriver)
                                                .addOnCompleteListener(task -> {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getActivity(), "Thank you for applying.", Toast.LENGTH_SHORT).show();
                                                        backToHomePage();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Something went wrong with applying please retry at a later date.", Toast.LENGTH_SHORT).show();
                                                        storage.getReference().child("Driver Applicants/" + UserApi.getInstance().getCurrentUser().getUserId()).delete();
                                                    }
                                                });
                                    }));
                        }));


    }

}