package com.upt.cti.smartwallet;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import model.MonthlyExpenses;

public class MainActivity extends AppCompatActivity {

    private TextView tStatus;
    private EditText eSearch, eIncome, eExpenses;
    private DatabaseReference databaseReference;

    private String currentMonth;
    private ValueEventListener databaseListener = null;

    private final static String PREFS_SETTINGS = "prefs_settings";
    private SharedPreferences prefsUser, prefsApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tStatus = (TextView) findViewById(R.id.tStatus);
        eSearch = (EditText) findViewById(R.id.eSearch);
        eIncome = (EditText) findViewById(R.id.eIncome);
        eExpenses = (EditText) findViewById(R.id.eExpenses);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://smart-wallet-6b858-default-rtdb.us-central1.firebasedatabase.app");
        databaseReference = database.getReference();

        prefsUser = getSharedPreferences(PREFS_SETTINGS, Context.MODE_PRIVATE);
        prefsApp = getPreferences(Context.MODE_PRIVATE);

        String month = prefsUser.getString("KEY1", null);
        if(month!=null)
            eSearch.setText(month);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void clicked(View view) {
        switch (view.getId()) {
            case R.id.bSearch:
                if (!eSearch.getText().toString().isEmpty()) {
                    // save text to lower case (all our months are stored online in lower case)
                    currentMonth = eSearch.getText().toString().toLowerCase();

                    List<String> strings = Arrays.asList("january", "february", "march");
                    boolean anyMatch = strings.stream().anyMatch(s -> s.equals(currentMonth));
                    if (anyMatch) {
                        tStatus.setText("Searching ...");
                        SharedPreferences.Editor editor = prefsUser.edit();
                        editor.putString("KEY1",currentMonth);
                        editor.commit();
                        createNewDBListener();
                    } else {
                        tStatus.setText("Doesn't exits");
                    }
                } else {
                    Toast.makeText(this, "Search field may not be empty", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bUpdate:
                if (!eSearch.getText().toString().isEmpty()) {
                    // save text to lower case (all our months are stored online in lower case)
                    currentMonth = eSearch.getText().toString().toLowerCase();

                    List<String> strings = Arrays.asList("january", "february", "march");
                    boolean anyMatch = strings.stream().anyMatch(s -> s.equals(currentMonth));
                    if (anyMatch) {
                        tStatus.setText("Updating ...");
                        if(eExpenses.getText().toString().length()!=0 && eIncome.getText().toString().length()!=0 )
                            updateCalendar(Integer.parseInt(eExpenses.getText().toString()),Integer.parseInt(eIncome.getText().toString()));
                        else   tStatus.setText("Input error");
                    } else {
                        tStatus.setText("Doesn't exits");
                    }
                    break;
                }
        }
    }

    private void createNewDBListener() {
        // remove previous databaseListener
        if (databaseReference != null && currentMonth != null && databaseListener != null)
            databaseReference.child("calendar").child(currentMonth).removeEventListener(databaseListener);
        databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                MonthlyExpenses monthlyExpense = dataSnapshot.getValue(MonthlyExpenses.class);
                // explicit mapping of month name from entry key
                monthlyExpense.month = dataSnapshot.getKey();

                eIncome.setText(String.valueOf(monthlyExpense.getIncome()));
                eExpenses.setText(String.valueOf(monthlyExpense.getExpenses()));
                tStatus.setText("Found entry for " + currentMonth);
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        };

        // set new databaseListener
        databaseReference.child("calendar").child(currentMonth).addValueEventListener(databaseListener);
    }

    private void updateCalendar(float expenses , float income) {
        databaseReference.child("calendar").child(currentMonth).child("expenses").setValue(expenses);
        databaseReference.child("calendar").child(currentMonth).child("income").setValue(income);
        tStatus.setText("Done");

    }

}