package com.example.myapplication.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.ReportData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdmin extends AppCompatActivity {


    private HistoryAdapter adapter;
    private List<ReportData> reportList;
    DatabaseReference databaseReference;
    RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_admin);



        rvHistory = findViewById(R.id.rvHistory);
        reportList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("reports");

// Attach a layout manager to the RecyclerView
        rvHistory.setLayoutManager(new GridLayoutManager(this, 1));

// Create the adapter
        adapter = new HistoryAdapter(reportList, this);

// Set the adapter to the RecyclerView
        rvHistory.setAdapter(adapter);

// Query the database for all reports
        Query query = FirebaseDatabase.getInstance().getReference("reports");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)

            {
                // Clear the report list
                reportList.clear();

                // Loop through all the reports and add them to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ReportData report = snapshot.getValue(ReportData.class);
                    if (report != null) {
                        reportList.add(report);
                    }
                }

                // Update the adapter with the new data
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Display an error message if something goes wrong
                Toast.makeText(HistoryAdmin.this, "Failed to load reports: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }



}