package com.subzzz.getoverhere;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UpdateRatesDialog extends AppCompatDialogFragment {
    private EditText rate;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.update_rates_dialog,null);
        builder.setView(view)
                .setTitle("Current Rate Per Km:")
                .setNegativeButton("Back", (dialogInterface, i) -> {
                })
                .setPositiveButton("Update", (dialogInterface, i) -> {
                    db.collection("Drive Rates").document("Rates")
                            .update("currentRate", String.valueOf(rate.getText()));
                    Toast.makeText(getActivity(), "New Rate is " + rate.getText().toString().trim() + " $", Toast.LENGTH_SHORT).show();
                });
        rate = view.findViewById(R.id.current_rate_update_text);
        db.collection("Drive Rates").document("Rates").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        String currentRate = (String) task.getResult().get("currentRate");
                        rate.setText(currentRate);
                    }
                });
        return builder.create();
    }
}
