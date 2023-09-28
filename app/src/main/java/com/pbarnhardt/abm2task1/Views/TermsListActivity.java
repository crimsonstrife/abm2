package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Adapters.TermAdapter;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.TermModel;
import com.pbarnhardt.abm2task1.R;

import java.util.ArrayList;
import java.util.List;

public class TermsListActivity extends AppCompatActivity {
    private List<Terms> termsList = new ArrayList<>();
    private TermAdapter termAdapter;

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initializeRecyclerView();
        initializeViewModel();

        // On click listener for add term button
        final FloatingActionButton addTermButton = findViewById(R.id.floatingAddTermButton);
        addTermButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Term");
            builder.setMessage("Are you sure you want to add a term?");
            builder.setIcon(R.drawable.ic_action_add);
            builder.setPositiveButton("Yes", (dialog, which) -> {
                Intent intent = new Intent(TermsListActivity.this, TermEditActivity.class);
                startActivity(intent);
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // Do nothing
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void initializeViewModel() {
        RecyclerView recyclerView = findViewById(R.id.termListRecyclerView);
        final Observer<List<Terms>> observer = terms -> {
            termsList.clear();
            termsList.addAll(terms);
            if (termAdapter == null) {
                termAdapter = new TermAdapter(termsList, this, RecyclerAdapter.MAIN);
                recyclerView.setAdapter(termAdapter);
            } else {
                termAdapter.notifyDataSetChanged();
            }
        };
        TermModel termModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(TermModel.class);
        termModel.terms.observe(this, observer);
    }

    private void initializeRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.termListRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }
}