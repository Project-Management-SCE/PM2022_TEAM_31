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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.subzzz.getoverhere.Model.Passenger;

import java.util.Map;

public class CreateAccActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText emailText;
    private EditText passText;
    private EditText confirmPassText;
    private EditText firstName;
    private EditText lastName;
    private EditText userName;
    private EditText Idnum;
    private Button createAccBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //todo add all components

        createAccBtn = findViewById(R.id.submitBtn);



        firebaseAuth = FirebaseAuth.getInstance();
        createAccBtn.setOnClickListener(this);
    }
    private void createNewAccount(View v){
        String emailAddr = emailText.getText().toString().trim();
        String password = passText.getText().toString().trim();
        String passwordConfirm = confirmPassText.getText().toString().trim();
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String idnumber = Idnum.getText().toString().trim();
        String username = userName.getText().toString().trim();


        if(checkIfEmptyString(emailAddr,"Email") && checkIfEmptyString(password,"Password")
            && checkIfEmptyString(passwordConfirm,"Confirm Password") && checkIfEmptyString(fname,"First Name")
            && checkIfEmptyString(lname,"Last Name") && checkIfEmptyString(idnumber,"Id Number")
            && checkIfEmptyString(username,"Username")){
            return;
        }

        if(!isPassSame(password,passwordConfirm)){
            Toast.makeText(CreateAccActivity.this,"Your Password doesn't match",Toast.LENGTH_LONG)
                    .show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailAddr,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                    currentUser = firebaseAuth.getCurrentUser();
                    assert currentUser != null;
                    final String currentUserId = currentUser.getUid();

                Passenger newPassenger = new Passenger(currentUserId,emailAddr,fname,lname,username,idnumber);
                collectionReference.document(idnumber).set(newPassenger).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                startActivity(new Intent(CreateAccActivity.this,
                                        MainActivity.class));
                            }
                        });
            }
        });
    }

    private boolean isPassSame(String pass,String confirmPass){
        return pass.equals(confirmPass);
    }

    private boolean checkIfEmptyString(String str, String type){
        if(TextUtils.isEmpty(str)){
            Toast.makeText(CreateAccActivity.this,"Please Enter your " + type,Toast.LENGTH_LONG)
                    .show();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submitBtn:
                   // createNewAccount(view);
                break;
            default:
                break;
        }
    }

}