package com.subzzz.getoverhere;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.subzzz.getoverhere.Model.Passenger;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class CreateAccActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText firstName;
    private EditText lastName;
    private EditText emailText;
    private EditText phoneNum;
    private EditText Idnum;
    private EditText myAddrs;
    private Spinner genderEntry;
    private DatePicker birthDay;
    private EditText passText;
    private EditText confirmPassText;
    private Button createAccBtn;
    private GifImageView loadingImage;


    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        loadComponents();

    }

    private void loadComponents(){
        firstName = findViewById(R.id.FisrtNameI);
        lastName = findViewById(R.id.LastNameI);
        emailText = findViewById(R.id.EmailI);
        phoneNum = findViewById(R.id.phoneI);
        Idnum = findViewById(R.id.IDI);
        myAddrs = findViewById(R.id.ADRI);
        genderEntry = findViewById(R.id.gender);
        birthDay = findViewById(R.id.datePicker);
        passText = findViewById(R.id.PS1I);
        confirmPassText = findViewById(R.id.PS2I);
        loadingImage = findViewById(R.id.loadingImage);
        createAccBtn = findViewById(R.id.submitBtn);
        createAccBtn.setOnClickListener(this);
    }

    private void createNewAccount(View v){
        String fname = firstName.getText().toString().trim();
        String lname = lastName.getText().toString().trim();
        String emailAdd = emailText.getText().toString().trim();
        String phNum = phoneNum.getText().toString().trim();
        String idnumber = Idnum.getText().toString().trim();
        String addrs = myAddrs.getText().toString().trim();
        String gender = genderEntry.getSelectedItem().toString().trim();
        String password = passText.getText().toString().trim();
        String passwordConfirm = confirmPassText.getText().toString().trim();
        Calendar cal = Calendar.getInstance();
        cal.set(birthDay.getYear(),birthDay.getMonth(),birthDay.getDayOfMonth());
        Timestamp bDay = new Timestamp(cal.getTime());

        if(checkIfEmptyString(fname,"First Name") || checkIfEmptyString(lname,"Last Name")
                || checkIfEmptyString(emailAdd,"Email") || checkIfEmptyString(phNum,"Phone Number")
                || checkIfEmptyString(idnumber,"Id Number") || checkIfEmptyString(addrs,"Address")
                || checkIfEmptyString(password,"Password") || checkIfEmptyString(passwordConfirm,"Confirm Password")){
            return;
        }

        if(!isPassSame(password,passwordConfirm)){
            Toast.makeText(CreateAccActivity.this,"Your Password doesn't match",Toast.LENGTH_LONG)
                    .show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(emailAdd,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                currentUser = firebaseAuth.getCurrentUser();
                assert currentUser != null;
                final String currentUserId = currentUser.getUid();

                Passenger newPassenger = new Passenger(currentUserId,fname,lname,emailAdd,phNum,idnumber,addrs,gender,bDay);
                collectionReference.document().set(newPassenger).
                        addOnSuccessListener(unused -> {
                            Toast.makeText(CreateAccActivity.this,"Thank you for Signing up",Toast.LENGTH_LONG)
                                    .show();
                            startActivity(new Intent(CreateAccActivity.this,
                                    LoginActivity.class));
                        });
            }else{
                loadingImage.setVisibility(View.GONE);
                findViewById(R.id.scroll_view_register).setVisibility(View.VISIBLE);
                Toast.makeText(CreateAccActivity.this,"Could Not SignUp",Toast.LENGTH_LONG)
                        .show();
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
                    findViewById(R.id.scroll_view_register).setVisibility(View.GONE);
                    loadingImage.setVisibility(View.VISIBLE);
                    createNewAccount(view);
                break;
            default:
                break;
        }
    }


}