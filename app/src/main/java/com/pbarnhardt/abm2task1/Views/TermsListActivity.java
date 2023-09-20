package com.pbarnhardt.abm2task1.Views;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Models.TermListModel;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class TermsListActivity extends BaseListActivity {

    private List<Terms> termsList;
    private LayoutInflater inflater;
    private RecyclerView recycler;

    ViewModelProvider.Factory viewModelFactory;
    TermListModel termListModel;

    @Override
    int getContentViewId() {
        return R.layout.activity_term_list;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.termsNavAction;
    }

    @Override
    void populateScreen() {
        FloatingActionButton fab = findViewById(R.id.floatingAddTermButton);
        fab.setOnClickListener(view -> startDetailActivity(null, view));

        recycler = (RecyclerView) findViewById(R.id.termListRecyclerView);
        inflater = getLayoutInflater();
        termListModel = ViewModelProviders.of(this, viewModelFactory).get(TermListModel.class);

        termListModel.getListOfAllTerms().observe(this, new Observer<List<Terms>>(){
            @Override
            public void onChanged(@Nullable List<Terms> terms) {
                if (termsList == null) {
                    setListTerms(terms);
                }
            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
    }
}