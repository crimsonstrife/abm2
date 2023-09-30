package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.COURSE_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.MENTOR_KEY;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Adapters.MentorAdapter;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.databinding.ActivityMentorEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditMentorsBinding;

import java.util.Objects;

public class MentorEditActivity extends AppCompatActivity {
    /**
     * Variables.
     */
    private boolean newMentor;
    private boolean edit;
    private int courseId = -1;
    int mentorId;
    private EditorModel viewModel;
    private ActivityMentorEditBinding activityBinding;
    private ContentEditMentorsBinding contentBinding;
    private EditText mentorName;
    private EditText mentorEmail;
    private EditText mentorPhone;
    private MentorAdapter adapter;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityMentorEditBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check the instance state to see if we are editing or creating a new mentor
        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        mentorName = contentBinding.editTextMentorTitle;
        mentorEmail = contentBinding.editTextMentorEmail;
        mentorPhone = contentBinding.editTextMentorPhone;

        //initialize the view model
        initializeViewModel();
    }

    private void initializeViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveMentors.observe(this, mentorEntity -> {
            if (mentorEntity != null && !edit) {
                mentorName.setText(mentorEntity.getMentorName());
                mentorEmail.setText(mentorEntity.getMentorEmail());
                mentorPhone.setText(mentorEntity.getMentorPhone());
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
                dialog.dismiss();
            });
            builder.setNegativeButton("Cancel", (dialog, id) -> {
                // User cancelled the dialog
                dialog.dismiss();
                finish();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (extras.containsKey(COURSE_KEY)){
            setTitle("New Mentor");
            courseId = extras.getInt(COURSE_KEY);
        } else {
            setTitle("Edit Mentor");
            mentorId = extras.getInt(MENTOR_KEY);
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
        try {
            String name = mentorName.getText().toString();
            String email = mentorEmail.getText().toString();
            String phone = mentorPhone.getText().toString();
            //save the mentor
            viewModel.saveMentor(name, phone, email, courseId);
            //notify the user that the mentor was saved
            Toast.makeText(this, "Mentor Saved", Toast.LENGTH_SHORT).show();
            //navigate back to the mentor list
            finish();
        } catch (Exception e) {
            Log.e("MentorEditActivity", Objects.requireNonNull(e.getMessage()));
            //notify the user that there was an error saving the mentor
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There was an error saving the mentor. \nPlease try again.");
            builder.setPositiveButton("OK", (dialog, id) -> {
                // User clicked OK button
                dialog.dismiss();
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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
            //if editing, cancel out of the activity
            if (!newMentor) {
                doDelete();
            } else {
                finish();
            }
        } else if (item.getItemId() == R.id.action_save) {
            saveAndReturn();
            return true;
        } else if (item.getItemId() == R.id.action_help) {
            //TODO: add help
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Do delete.
     */
    private void doDelete() {
        if (viewModel.liveMentors.getValue() != null) {
            String title = viewModel.liveMentors.getValue().getMentorName();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete " + title + "?");
            builder.setMessage("Are you sure you want to delete mentor '" + title + "'?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Yes", (dialog, id) -> {
                dialog.dismiss();
                viewModel.deleteMentor();
                finish();
            });
            builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    }