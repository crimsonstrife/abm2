package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.MentorAdapter;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.MentorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.databinding.ActivityMentorListBinding;
import com.pbarnhardt.abm2task1.databinding.ContentListMentorsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MentorsListActivity extends AppCompatActivity implements MentorAdapter.MentorSelectedListener {
    /**
     * Variables
     */
    private MentorAdapter mentorAdapter;
    private final List<Mentors> mentorsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ActivityMentorListBinding activityBinding;
    private ContentListMentorsBinding contentBinding;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMentorListBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);
        //set toolbar color
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initializeViewModel();

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        recyclerView = contentBinding.mentorListRecyclerView;
        initializeRecyclerView(recyclerView);

        //on click listener for the floating action button
        activityBinding.floatingAddMentorButton.setOnClickListener(v -> {
            Intent intent = new Intent(MentorsListActivity.this, MentorEditActivity.class);
            startActivity(intent);
        });
    }

    private void initializeViewModel() {
        final Observer<List<Mentors>> observer = mentors -> {
            mentorsList.clear();
            mentorsList.addAll(mentors);
            if (mentorAdapter == null) {
                mentorAdapter = new MentorAdapter(mentorsList, MentorsListActivity.this, RecyclerAdapter.MAIN, this);
                recyclerView.setAdapter(mentorAdapter);
            } else {
                mentorAdapter.notifyItemRangeChanged(0, mentorsList.size());
            }
        };
        MentorModel mentorModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(MentorModel.class);
        mentorModel.mentors.observe(this, observer);
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onMentorSelected(int position, Mentors mentor) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }
}