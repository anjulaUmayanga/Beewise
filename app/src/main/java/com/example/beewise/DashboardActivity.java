package com.example.beewise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    private TextView phView;
    private TextView tdsView;
    private TextView ecView;
    private TextView weightView;
    private TextView outputView;

    private ProgressBar phBar;
    private ProgressBar tdsBar;
    private ProgressBar ecBar;
    private ProgressBar weightBar;

    // Min and max values for the comparison
    private final double EC_MIN = 1970.02771;
    private final double EC_MAX = 2022.06128;
    private final double PH_MIN = 2.82539;
    private final double PH_MAX = 2.84254;
    private final double TDS_MIN = 179.65845;
    private final double TDS_MAX = 183.3024;
    private final double WEIGHT_MIN = 224.92998;
    private final double WEIGHT_MAX = 225.27422;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        phView = findViewById(R.id.phval);
        tdsView = findViewById(R.id.tdsval);
        ecView = findViewById(R.id.tempval); // Assuming tempval is EC value
        weightView = findViewById(R.id.weightval);
        outputView = findViewById(R.id.Output); // Output to indicate honey status

        phBar = findViewById(R.id.ph);
        tdsBar = findViewById(R.id.tdsbar);
        ecBar = findViewById(R.id.ecbar);
        weightBar = findViewById(R.id.weightbar);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataSnapshot> sensorReadingsList = new ArrayList<>();

                // Add all sensor readings to a list
                for (DataSnapshot data : snapshot.getChildren()) {
                    String key = data.getKey();

                    // Capture only the sensor readings named like "SensorReading1", "SensorReading2", etc.
                    if (key != null && key.startsWith("SensorReading")) {
                        sensorReadingsList.add(data);
                    }
                }

                // Sort the list based on the numeric value in the key
                Collections.sort(sensorReadingsList, new Comparator<DataSnapshot>() {
                    @Override
                    public int compare(DataSnapshot o1, DataSnapshot o2) {
                        int num1 = extractNumber(o1.getKey());
                        int num2 = extractNumber(o2.getKey());
                        return Integer.compare(num1, num2);
                    }
                });

                // Get the last entry from the sorted list (which will be the latest sensor reading)
                if (!sensorReadingsList.isEmpty()) {
                    DataSnapshot lastReading = sensorReadingsList.get(sensorReadingsList.size() - 1);

                    // Retrieve the sensor values
                    Double ph = lastReading.child("PH").getValue(Double.class);
                    Double tds = lastReading.child("TDS").getValue(Double.class);
                    Double ec = lastReading.child("EC").getValue(Double.class);
                    Double weight = lastReading.child("Weight").getValue(Double.class);

                    // Display the values in the TextViews
                    if (ph != null) phView.setText(String.valueOf(ph));
                    if (tds != null) tdsView.setText(String.valueOf(tds));
                    if (ec != null) ecView.setText(String.valueOf(ec));
                    if (weight != null) weightView.setText(String.valueOf(weight));

                    // Update progress bars (assuming out of 100)
                    if (ph != null) phBar.setProgress(clampProgress((ph - PH_MIN) / (PH_MAX - PH_MIN) * 100));
                    if (tds != null) tdsBar.setProgress(clampProgress((tds - TDS_MIN) / (TDS_MAX - TDS_MIN) * 100));
                    if (ec != null) ecBar.setProgress(clampProgress((ec - EC_MIN) / (EC_MAX - EC_MIN) * 100));
                    if (weight != null) weightBar.setProgress(clampProgress((weight - WEIGHT_MIN) / (WEIGHT_MAX - WEIGHT_MIN) * 100));

                    // Check if values are within the acceptable ranges
                    if (isWithinRange(ph, PH_MIN, PH_MAX) &&
                            isWithinRange(tds, TDS_MIN, TDS_MAX) &&
                            isWithinRange(ec, EC_MIN, EC_MAX) &&
                            isWithinRange(weight, WEIGHT_MIN, WEIGHT_MAX)) {
                        outputView.setText("Pure Honey");
                    } else {
                        outputView.setText("Adulterated Honey");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                Toast.makeText(DashboardActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to check if a value is within the range
    private boolean isWithinRange(Double value, double min, double max) {
        if (value == null) return false;
        return value >= min && value <= max;
    }

    // Helper method to extract the number from the SensorReading key
    private int extractNumber(String key) {
        // Remove the "SensorReading" part and convert the rest to an integer
        return Integer.parseInt(key.replace("SensorReading", ""));
    }

    // Helper method to clamp progress between 0 and 100
    private int clampProgress(double progress) {
        return Math.max(0, Math.min((int) progress, 100));
    }
}
