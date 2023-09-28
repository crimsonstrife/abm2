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

public class MentorDetailsActivity extends AppCompatActivity {
    /**
     * Assign views by id
     */
    TextView mentorNameView = findViewById(R.id.editText_mentor_title);
    TextView mentorEmailView = findViewById(R.id.editText_mentor_email);
    TextView mentorPhoneView = findViewById(R.id.editText_mentor_phone);

    /**
     * Variables
     */
    private int mentorId;
    private Toolbar toolbar;

    /**
     * On create method
     * @param savedInstanceState saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_details);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initiateViewModel();

        //on click listener for the floating button
        final FloatingActionButton editMentorBtn = findViewById(R.id.floatingEditMentorButton);
        editMentorBtn.setOnClickListener(new View.OnClickListener() {
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
        EditorModel viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
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