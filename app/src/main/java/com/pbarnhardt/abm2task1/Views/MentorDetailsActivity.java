package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.MENTOR_KEY;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.databinding.ActivityMentorDetailsBinding;
import com.pbarnhardt.abm2task1.databinding.ContentDetailsMentorsBinding;

public class MentorDetailsActivity extends AppCompatActivity {
    /**
     * Variables
     */
    private int mentorId;
    private Toolbar toolbar;
    private EditorModel viewModel;
    private TextView mentorNameView;
    private TextView mentorEmailView;
    private TextView mentorPhoneView;
    private ActivityMentorDetailsBinding activityBinding;
    private ContentDetailsMentorsBinding contentBinding;

    /**
     * On create method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMentorDetailsBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        mentorNameView = contentBinding.editTextMentorTitle;
        mentorEmailView = contentBinding.editTextMentorEmail;
        mentorPhoneView = contentBinding.editTextMentorPhone;

        initiateViewModel();

        //on click listener for the floating button
        activityBinding.floatingEditMentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MentorDetailsActivity.this, MentorEditActivity.class);
                intent.putExtra(MENTOR_KEY, mentorId);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveMentors.observe(this, mentors -> {
            mentorNameView.setText(mentors.getMentorName());
            mentorEmailView.setText(mentors.getMentorEmail());
            mentorPhoneView.setText(mentors.getMentorPhone());
            toolbar.setTitle(mentors.getMentorName());
        });
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mentorId = extras.getInt(MENTOR_KEY);
            viewModel.loadMentor(mentorId);
        } else {
            finish();
        }
    }
}