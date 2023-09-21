package com.pbarnhardt.abm2task1.Views;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.pbarnhardt.abm2task1.Fragments.DatePicker;
import com.pbarnhardt.abm2task1.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class BaseDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    // Variables
    public Button lastActive;
    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    //Overrides
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        screenPopulation();
    }
    @Override
    public void onStart() {
        super.onStart();
        screenPopulation();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_tools, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Methods
    void screenPopulation() {

    }
    void returnDate(final Calendar cal) {
        if(lastActive != null) {
            lastActive.setText(dateFormat.format(cal.getTime()));
        }
    }
    public void datePick(View view) {
        DatePicker datePicker = new DatePicker();
        datePicker.show(getSupportFragmentManager(), "Date");
    }
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        returnDate(new GregorianCalendar(year,month,dayOfMonth));
    }

    public ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //swipe helper
            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO: implement swipe features
            }

            //drag helper
            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                return false; //return false so that the drag feature is disabled
            }
        };
        return simpleTouchCallback;
    }

}
