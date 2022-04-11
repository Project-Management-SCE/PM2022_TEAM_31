package com.subzzz.getoverhere;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.subzzz.getoverhere.Adapter.RecyclerViewAdapter;
import com.subzzz.getoverhere.Model.DriverApplicant;
import com.subzzz.getoverhere.Model.Passenger;

import java.util.List;


public class ApproveNewDriversFragment extends Fragment implements RecyclerViewAdapter.OnApplicantClickListener {
    private AppCompatActivity appCompatActivity;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private List<DriverApplicant> applicantList;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public ApproveNewDriversFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ApproveNewDriversFragment newInstance() {
        ApproveNewDriversFragment fragment = new ApproveNewDriversFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appCompatActivity = new AppCompatActivity(R.layout.fragment_approve_new_drivers);
        recyclerView = appCompatActivity.findViewById(R.id.recyler_view);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(appCompatActivity));


        db.collection("Driver-Applicants").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot document: task.getResult()){
                            applicantList.add(document.toObject(DriverApplicant.class));
                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_approve_new_drivers, container, false);
    }

    @Override
    public void OnApplicantClick(int position) {

    }
}