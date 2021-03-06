package com.subzzz.getoverhere;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.subzzz.getoverhere.Model.User;
import com.subzzz.getoverhere.Util.UserApi;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateUserInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserInfoFragment extends Fragment implements View.OnClickListener {


    private Button updatebtn, changepassbtn;
    private EditText fname, lname, phone, address, pass, confpass;
    private TextView email;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();




    public UpdateUserInfoFragment() {
        // Required empty public constructor
    }

    public static UpdateUserInfoFragment newInstance() {
        UpdateUserInfoFragment fragment = new UpdateUserInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


//    private void InputCurrDets() {
//        UserApi userapi = UserApi.getInstance();
//        User currentUser = userapi.getCurrentUser();
//        fname.setText(currentUser.getFirstName());
//        lname.setText(currentUser.getLastName());
//        email.setText(currentUser.getEmail());
//        phone.setText(currentUser.getPhoneNum());
//        address.setText(currentUser.getAddress());
//    }

    private void updatePass() {
        String strpass = pass.getText().toString().trim();
        String strconfpass = confpass.getText().toString().trim();
        if (TextUtils.equals(strpass,strconfpass) && !(TextUtils.isEmpty(strpass))){ //if passwords match and they are not empty, proceed
            FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();
            currUser.updatePassword(strpass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(),"Successfully changed password" ,Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            throw task.getException();
                        } catch(Exception e) {
                            pass.setError("Failed, try another password");
                            }
                    }
                }
            });
        }
        else if (!(TextUtils.equals(strpass,strconfpass))){
            pass.setError("Passwords doesn't match");
            confpass.setError("Passwords doesn't match");
            return;
        }
        else if (TextUtils.isEmpty(strpass))
        {
            pass.setError("Password cannot be empty");
            return;
        }
    }

    private void checkDetails() {

        String strfname = fname.getText().toString().trim();
        String strlname = lname.getText().toString().trim();
        String stremail = email.getText().toString().trim();
        String strphone = phone.getText().toString().trim();
        String straddress = address.getText().toString().trim();

        if (TextUtils.isEmpty(strfname))
        {
            fname.setError("First name cannot be empty");
            return;
        }
        else if (TextUtils.isEmpty(strlname)){
            lname.setError("Last name cannot be empty");
            return;
        }
        else if (TextUtils.isEmpty(strphone)){
            phone.setError("Phone cannot be empty");
        }
        else if (TextUtils.isEmpty(straddress)){
            address.setError("Address cannot be empty");
        }
        UserApi userapi = UserApi.getInstance();
        User currentUser = userapi.getCurrentUser();
        Map<String, Object> data = new HashMap<>();
        data.put("firstName",strfname);
        data.put("lastName",strlname);
        data.put("phoneNum",strphone);
        data.put("address",straddress);
        db.collection("Users").document(currentUser.getUserId()).set(data, SetOptions.merge());
        db.collection("Users").whereEqualTo("userId",currentUser.getUserId()).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            task.getResult().getDocuments().get(0).getReference().
                                    set(data, SetOptions.merge());
                            Toast.makeText(getActivity(), "Updated - log in again to see changes", Toast.LENGTH_SHORT).show();
                            FragmentManager fm = getParentFragmentManager();
                            Fragment fragment = new MapsFragment();
                            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

                        }
                        else {
                            Toast.makeText(getActivity(), "Something Went wrong updating user information", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_update_user_info, container, false);
        fname = (EditText) view.findViewById(R.id.EditFNameUpdate);
        lname = (EditText) view.findViewById(R.id.EditLNameUpdate);
        email = (TextView) view.findViewById(R.id.EditEmailUpdate);
        phone = (EditText) view.findViewById(R.id.EditPhoneUpdate);
        address = (EditText) view.findViewById(R.id.EditAddressUpdate);
        pass = (EditText) view.findViewById(R.id.EditPasswordUpdate);
        confpass = (EditText) view.findViewById(R.id.EditConfPassUpdate);
        updatebtn = (Button) view.findViewById(R.id.UpdateDetailsBtn);
        changepassbtn = (Button) view.findViewById(R.id.UpdatePassBtn);

        updatebtn.setOnClickListener(this);
        changepassbtn.setOnClickListener(this);

        pullPersonalInfo();
        return view;
    }

    private void pullPersonalInfo() {
        UserApi userapi = UserApi.getInstance();
        User currentUser = userapi.getCurrentUser();
        fname.setText(currentUser.getFirstName());
        lname.setText(currentUser.getLastName());
        phone.setText(currentUser.getPhoneNum());
        address.setText(currentUser.getAddress());

    }

    @Override
    public void onClick (View view){
        switch (view.getId()) {
            case R.id.UpdateDetailsBtn:
                checkDetails();
                break;
            case R.id.UpdatePassBtn:
                updatePass();
                break;
            default:
                break;
        }

    }

}