package com.pbarnhardt.abm2task1.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pbarnhardt.abm2task1.Adapters.MentorAdapter;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.MentorModel;
import com.pbarnhardt.abm2task1.R;

import java.util.ArrayList;
import java.util.List;

public class MentorsListActivity extends AppCompatActivity implements MentorAdapter.MentorSelectedListener {
    /**
     * Bind the recycler view.
     */
    RecyclerView recyclerView = findViewById(R.id.mentorListRecyclerView);

    /**
     * Variables
     */
    private MentorAdapter mentorAdapter;
    private MentorModel mentorModel;
    private List<Mentors> mentorsList = new ArrayList<>();

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeRecyclerView();
        initializeViewModel();
    }

    private void initializeViewModel() {
        final Observer<List<Mentors>> observer = mentors -> {
            mentorsList.clear();
            mentorsList.addAll(mentors);
            if (mentorAdapter == null) {
                mentorAdapter = new MentorAdapter(mentorsList, MentorsListActivity.this, RecyclerAdapter.MAIN, this);
                recyclerView.setAdapter(mentorAdapter);
            } else {
                mentorAdapter.notifyDataSetChanged();
            }
        };
        mentorModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MentorModel.class);
        mentorModel.mentors.observe(this, observer);
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onMentorSelected(int position, Mentors mentor) {

    }

    public void addMentorFloatingButtonClicked(View view) {
        Intent intent = new Intent(this, MentorEditActivity.class);
        startActivity(intent);
    }
}