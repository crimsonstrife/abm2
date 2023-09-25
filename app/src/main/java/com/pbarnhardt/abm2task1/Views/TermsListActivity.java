package com.pbarnhardt.abm2task1.Views;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Models.TermListModel;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class TermsListActivity extends AppCompatActivity {

    private List<Terms> termsList;

    /**
     * Binding for the recycler view
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);

        RecyclerView recyclerView = findViewById(R.id.termListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        final TermListAdapter adapter = new TermListAdapter();
        recyclerView.setAdapter(adapter);

        TermListModel model = new ViewModelProvider(this).get(TermListModel.class);
        model.getAllTerms().observe(this, terms -> {
            termsList = terms;
            adapter.setTerms(terms);
        });

        FloatingActionButton fab = findViewById(R.id.terms_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(TermsListActivity.this, TermEditActivity.class);
            startActivity(intent);
        });
    }
}