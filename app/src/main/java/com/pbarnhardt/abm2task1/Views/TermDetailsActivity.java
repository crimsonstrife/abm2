package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Models.TermDetailModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.TrackerApplication;

import java.util.List;

public class TermDetailsActivity extends BaseDetailActivity {

    private List<Courses> coursesList;
    private TextView name;
    private Button startDate;
    private Button endDate;
    private TextView dateRange;
    private RecyclerView coursesInTerm;
    private TextView courses;
    private Terms originalTerm;
    private LayoutInflater inflater;

    ViewModelProvider.Factory viewModelFactory;
    private TermDetailModel termDetailModel;

    public TermDetailsActivity newInstance(String termName) {
        TermDetailsActivity termDetailsActivity = new TermDetailsActivity();
        Bundle args = new Bundle();
        args.putString("Term Name", termName);
        return this;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

    }

    @Override
    void screenPopulation(){
        ((TrackerApplication) getApplication()).getTrackerApplicationComponent().inject((TermDetailsActivity) this);

        inflater = getLayoutInflater();
        setContentView(R.layout.activity_term_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.termDetailsToolbar);
        toolbar.setTitle("Term Details");
        setSupportActionBar(toolbar);

        termDetailModel = new ViewModelProvider(this, viewModelFactory).get(TermDetailModel.class);

        Intent intent = getIntent();
        String termName = intent.getStringExtra("Term Name");

    }
}