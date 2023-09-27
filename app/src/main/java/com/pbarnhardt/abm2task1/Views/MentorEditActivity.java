package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;

import java.util.Objects;

public class MentorEditActivity extends AppCompatActivity {
    /**
     * Bind views.
     */
    EditText mentorNameView = findViewById(R.id.editText_mentor_title);
    EditText mentorEmailView = findViewById(R.id.editText_mentor_email);
    EditText mentorPhoneView = findViewById(R.id.editText_mentor_phone);

    /**
     * Variables.
     */
    private boolean newMentor;
    private boolean edit;
    private int courseId = -1;
    private EditorModel viewModel;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }
        initializeViewModel();
    }

    private void initializeViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveMentors.observe(this, mentorEntity -> {
            if (mentorEntity != null && !edit) {
                mentorNameView.setText(mentorEntity.getMentorName());
                mentorEmailView.setText(mentorEntity.getMentorEmail());
                mentorPhoneView.setText(mentorEntity.getMentorPhone());
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New Mentor");
            //notify the user that a new mentor is being created without a course id, they should add a mentor from within a course, or assign the course manually
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You are creating a new mentor without a course. \nYou can add a mentor from within a course, \nor continue assign the course manually.");
            builder.setPositiveButton("Continue", (dialog, id) -> {
                // User clicked OK button
                setTitle("New Mentor");
                newMentor = true;
            });
            builder.setNegativeButton("Cancel", (dialog, id) -> {
                // User cancelled the dialog
                finish();
            });
        } else if (extras.containsKey(COURSE_KEY)){
            setTitle("New Mentor");
            courseId = extras.getInt(COURSE_KEY);
        } else {
            setTitle("Edit Mentor");
            int mentorId = extras.getInt(COURSE_KEY);
            viewModel.loadMentor(mentorId);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDIT_KEY, true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    private void saveAndReturn() {
        String mentorName = mentorNameView.getText().toString().trim();
        String mentorEmail = mentorEmailView.getText().toString().trim();
        String mentorPhone = mentorPhoneView.getText().toString().trim();
        //validate the input
        if (mentorName.isEmpty()) {
            mentorNameView.setError("Please enter a name");
            return;
        }
        if (mentorEmail.isEmpty()) {
            mentorEmailView.setError("Please enter an email");
            return;
        }
        if (mentorPhone.isEmpty()) {
            mentorPhoneView.setError("Please enter a phone number");
            return;
        }
        //check that the email is valid
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mentorEmail).matches()) {
            mentorEmailView.setError("Please enter a valid email address");
            return;
        }
        //check that the phone number is valid
        if (!android.util.Patterns.PHONE.matcher(mentorPhone).matches()) {
            mentorPhoneView.setError("Please enter a valid phone number");
            return;
        }
        //if no errors, save the mentor
        viewModel.saveMentor(mentorName, mentorEmail, mentorPhone, courseId);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!newMentor) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_delete) {
            viewModel.deleteMentor();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}