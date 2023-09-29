package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.TermAdapter;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.TermModel;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.databinding.ActivityTermListBinding;
import com.pbarnhardt.abm2task1.databinding.ContentListTermsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TermsListActivity extends AppCompatActivity {
    private final List<Terms> termsList = new ArrayList<>();
    private TermAdapter termAdapter;
    private ActivityTermListBinding activityBinding;
    private ContentListTermsBinding contentBinding;
    private RecyclerView recyclerView;

    /**
     * On create.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBinding = ActivityTermListBinding.inflate(getLayoutInflater());
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
        recyclerView = contentBinding.termListRecyclerView;
        initializeRecyclerView(recyclerView);

        // On click listener for add term button
        activityBinding.floatingAddTermButton.setOnClickListener(v -> {
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
        final Observer<List<Terms>> observer = terms -> {
            termsList.clear();
            termsList.addAll(terms);
            if (termAdapter == null) {
                termAdapter = new TermAdapter(termsList, this, RecyclerAdapter.MAIN);
                recyclerView.setAdapter(termAdapter);
            } else {
                termAdapter.notifyItemRangeChanged(0, termsList.size());
            }
        };
        TermModel termModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(TermModel.class);
        termModel.terms.observe(this, observer);
    }

    private void initializeRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
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