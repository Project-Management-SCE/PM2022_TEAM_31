package com.subzzz.getoverhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailText;
    private EditText passText;
    private Button loginBtn;
    private Button createAccBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //ToDO add find by id for all componnents




        createAccBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);


    }

    private void LoginAction(View v){
        String emailAddr = emailText.getText().toString().trim();
        String pass = passText.getText().toString().trim();

        if(!TextUtils.isEmpty(emailAddr) && !TextUtils.isEmpty((pass))){
            firebaseAuth.signInWithEmailAndPassword(emailAddr,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            assert user != null;
                            final String currentUserId = user.getUid();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }else if(TextUtils.isEmpty(emailAddr)){
            Toast.makeText(LoginActivity.this,"Please Enter your Email",Toast.LENGTH_LONG)
                    .show();
        }
        else if(TextUtils.isEmpty(pass)){
            Toast.makeText(LoginActivity.this,"Please Enter your Password",Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void createAccActivity(View v){
        startActivity(new Intent(LoginActivity.this,CreateAccActivity.class));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.://add

                break;
            case R.id.://add

                break;

            default:
                break;
        }
    }
}