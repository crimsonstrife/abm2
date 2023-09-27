package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.TermAdapter;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.Models.TermModel;
import com.pbarnhardt.abm2task1.R;

import java.util.ArrayList;
import java.util.List;

public class TermsListActivity extends AppCompatActivity {
    private List<Terms> termsList = new ArrayList<>();
    RecyclerView recyclerView = findViewById(R.id.termListRecyclerView);
    private TermModel termModel;
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
    }

    private void initializeViewModel() {
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
        termModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()).create(TermModel.class);
        termModel.terms.observe(this, observer);
    }

    private void initializeRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Add button click.
     */
    public void addTermFloatingButtonClicked(View view) {
        Intent intent = new Intent(this, TermEditActivity.class);
        startActivity(intent);
    }
}