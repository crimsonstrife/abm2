package com.pbarnhardt.abm2task1.Views;

import static com.pbarnhardt.abm2task1.Utils.Constants.EDIT_KEY;
import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.pbarnhardt.abm2task1.Entity.Courses;
import com.pbarnhardt.abm2task1.Models.EditorModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.databinding.ActivityTermEditBinding;
import com.pbarnhardt.abm2task1.databinding.ContentEditTermsBinding;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TermEditActivity extends AppCompatActivity {
    /**
     * Variables.
     */
    private boolean newTerm;
    private boolean edit;
    int termId;
    private EditorModel viewModel;
    private final List<Courses> courseList = new ArrayList<>();
    private ActivityTermEditBinding activityBinding;
    private ContentEditTermsBinding contentBinding;
    private EditText termTitle;
    private Button termStartDate;
    private Button termEndDate;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityTermEditBinding.inflate(getLayoutInflater());
        View view = activityBinding.getRoot();
        setContentView(view);
        Toolbar toolbar = activityBinding.toolbar;
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_action_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //check the instance state to see if we are editing or creating a new term
        if (savedInstanceState != null) {
            edit = savedInstanceState.getBoolean(EDIT_KEY);
        }

        //initialize the binding
        contentBinding = activityBinding.contentInclude;
        termTitle = contentBinding.termDetailTitle;
        termStartDate = contentBinding.termDetailEditStartDate;
        termEndDate = contentBinding.termDetailEditEndDate;

        //initialize the view model
        initiateViewModel();

        //set the date buttons
        termStartDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (startView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
            };
            new DatePickerDialog(TermEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            //update the button text with the selected date
            termStartDate.setText(Formatting.dateFormat.format(calendar.getTime()));
            termStartDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
        });
        termEndDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = (endView, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
            };
            new DatePickerDialog(TermEditActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            //update the button text with the selected date
            termEndDate.setText(Formatting.dateFormat.format(calendar.getTime()));
            termEndDate.setHint(Formatting.dateFormat.format(calendar.getTime()));
        });
    }

    private void initiateViewModel() {
        viewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(EditorModel.class);
        viewModel.liveTerms.observe(this, terms -> {
            if (terms != null && !edit) {
                termTitle.setText(terms.getTermName());
                termStartDate.setText(Formatting.dateFormat.format(terms.getTermStartDate()));
                termStartDate.setHint(Formatting.dateFormat.format(terms.getTermStartDate()));
                termEndDate.setText(Formatting.dateFormat.format(terms.getTermEndDate()));
                termEndDate.setHint(Formatting.dateFormat.format(terms.getTermEndDate()));
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.new_term_title));
            newTerm = true;
        } else {
            setTitle(getString(R.string.edit_term_title));
            termId = extras.getInt(TERM_KEY);
            viewModel.loadTerm(termId);
        }

        final Observer<List<Courses>> observer = courses -> {
            courseList.clear();
            courseList.addAll(courses);
        };

        viewModel.getCoursesByTermId(termId).observe(this, observer);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDIT_KEY, true);
        super.onSaveInstanceState(outState);
    }

    /**
     * On back pressed.
     */
    @Override
    public void onBackPressed() {
        saveAndReturn();
    }

    /**
     * Save and return.
     * This method will save the term and return to the previous activity
     */
    public void saveAndReturn() {
        final EditText termTitle = findViewById(R.id.termDetailTitle);
        final Button termStartDate = findViewById(R.id.termDetailEditStartDate);
        final Button termEndDate = findViewById(R.id.termDetailEditEndDate);
        try {
            Date startDate = Formatting.dateFormat.parse(termStartDate.getText().toString());
            Date endDate = Formatting.dateFormat.parse(termEndDate.getText().toString());
            viewModel.saveTerm(termTitle.getText().toString(), startDate, endDate);
            //notify the user that the term was saved
            Toast.makeText(this, "Term Saved", Toast.LENGTH_SHORT).show();
            //navigate back to the term list
            Intent intent = new Intent(this, TermsListActivity.class);
            startActivity(intent);
            finish();
        } catch (ParseException e) {
            Log.v("Exception: ", Objects.requireNonNull(e.getLocalizedMessage()));
            //notify the user that the term could not be saved
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Error saving term");
            builder.setMessage("There was an error saving the term.  Please check your entries and try again.");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("OK", (dialog, id) -> dialog.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        finish();
    }

    /**
     * Options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(!newTerm) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            saveAndReturn();
            return true;
        } else if(item.getItemId() == R.id.action_delete) {
            //if editing, cancel out of the activity
            if(!newTerm) {
                doDelete();
            } else {
                finish();
            }
        } else if(item.getItemId() == R.id.action_save) {
            saveAndReturn();
            return true;
        } else if(item.getItemId() == R.id.action_help) {
            //TODO: add help
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Do delete of the term
     */
    private void doDelete() {
        if(viewModel.liveTerms.getValue() != null) {
            String title = viewModel.liveTerms.getValue().getTermName();
            if(courseList != null && courseList.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Problem deleting " + title);
                builder.setMessage("You cannot delete a term that has courses assigned to it. \nYou must first delete the courses assigned to this term.  \nThis deletion will be cancelled.");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss();
                    finish();
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Delete " + title + "?");
                builder.setMessage("Are you sure you want to delete term '" + title + "'?");
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("Delete", (dialog, id) -> {
                    dialog.dismiss();
                    viewModel.deleteTerm();
                    finish();
                });
                builder.setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
}
