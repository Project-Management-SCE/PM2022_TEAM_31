package com.subzzz.getoverhere.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.subzzz.getoverhere.Model.Driver;
import com.subzzz.getoverhere.Model.DriverApplicant;
import com.subzzz.getoverhere.Model.Passenger;
import com.subzzz.getoverhere.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<DriverApplicant> applicantList;
    private OnApplicantClickListener applicantClickListener;


    public RecyclerViewAdapter(List<DriverApplicant> applicantList, OnApplicantClickListener applicantClickListener) {
        this.applicantList = applicantList;
        this.applicantClickListener = applicantClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.applicant_row,parent,false);
        return new ViewHolder(view, applicantClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameText.setText(String.format("%s %s", applicantList.get(position).getfName()
                , applicantList.get(position).getlName()));
        holder.idText.setText(applicantList.get(position).getIdNum());
        holder.emailText.setText(applicantList.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return applicantList.size();
    }

    public interface OnApplicantClickListener{
        void OnApplicantClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private final TextView nameText;
        private final TextView idText;
        private final TextView emailText;
        OnApplicantClickListener onApplicantClickListener;

        public ViewHolder(@NonNull View itemView, OnApplicantClickListener applicantClickListener) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText_row);
            idText = itemView.findViewById(R.id.idText_row);
            emailText = itemView.findViewById(R.id.emailText_row);
            this.onApplicantClickListener = applicantClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onApplicantClickListener.OnApplicantClick(getAdapterPosition());
        }
    }
}
