package com.subzzz.getoverhere;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ApproveNewDriversFragment extends Fragment implements RecyclerViewAdapter.OnApplicantClickListener {
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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_approve_new_drivers, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        applicantList = new ArrayList<>();
        createList();
        recyclerView.setHasFixedSize(true);


        // Inflate the layout for this fragment
        return view;
    }



    //to fix skip adapter layout
    private void createList() {
        db.collection("Driver-Applicants").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            applicantList.add(document.toObject(DriverApplicant.class));
                        }
                        recyclerViewAdapter = new RecyclerViewAdapter(applicantList, this);
                        recyclerView.setAdapter(recyclerViewAdapter);
                        recyclerViewAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void OnApplicantClick(int position) {
        DriverApplicant applicant = applicantList.get(position);

        FragmentManager fm = getParentFragmentManager();
        Fragment fragment = DisplayAplicantFragment.newInstance(applicant.getUid());


        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }
}