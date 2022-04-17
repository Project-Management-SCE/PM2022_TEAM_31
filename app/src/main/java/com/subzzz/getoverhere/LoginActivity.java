package com.subzzz.getoverhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.subzzz.getoverhere.Model.Admin;
import com.subzzz.getoverhere.Model.Driver;
import com.subzzz.getoverhere.Model.Passenger;
import com.subzzz.getoverhere.Util.UserApi;

import org.w3c.dom.Document;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailText;
    private EditText passText;
    private Button loginBtn;
    private Button createAccBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailText = findViewById(R.id.emailT);
        passText = findViewById(R.id.PasswordT);
        loginBtn = findViewById(R.id.submitB);
        createAccBtn = findViewById(R.id.registerB);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {

        };


        createAccBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

    private void LoginAction(View v) {
        String emailAddr = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        if (!TextUtils.isEmpty(emailAddr) && !TextUtils.isEmpty((pass))) {
            firebaseAuth.signInWithEmailAndPassword(emailAddr, pass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();
                            collectionReference.whereEqualTo("userId", currentUserId).get().addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    DocumentSnapshot doc = task1.getResult().getDocuments().get(0);
                                    String UType = (String) doc.get("utype");
                                    UserApi.getInstance().setUType(UType);
                                    switch (Objects.requireNonNull(UType)) {
                                        case "Admin":
                                            Admin admin = doc.toObject(Admin.class);
                                            UserApi.getInstance().setCurrentUser(admin);
                                            break;
                                        case "Driver":
                                            Driver driver = doc.toObject(Driver.class);
                                            UserApi.getInstance().setCurrentUser(driver);
                                            break;
                                        case "Passenger":
                                            Passenger passenger = doc.toObject(Passenger.class);
                                            UserApi.getInstance().setCurrentUser(passenger);
                                            break;
                                        default:
                                            Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_LONG)
                                                    .show();
                                            return;
                                    }
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                }
                            });


                        } else {
                            Log.w("login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Email/Password Incorrect",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (TextUtils.isEmpty(emailAddr)) {
            Toast.makeText(LoginActivity.this, "Please Enter your Email", Toast.LENGTH_LONG)
                    .show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(LoginActivity.this, "Please Enter your Password", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void createAccActivity(View v) {
        startActivity(new Intent(LoginActivity.this, CreateAccActivity.class));
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(@NonNull View view) {
        switch (view.getId()) {
            case R.id.submitB:
                LoginAction(view);
                break;
            case R.id.registerB:
                createAccActivity(view);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}