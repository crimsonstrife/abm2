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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
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
    private TermsViewAdapter adapter;

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
        termListModel = new ViewModelProvider(this, viewModelFactory).get(TermListModel.class);

        termListModel.getListOfAllTerms().observe(this, terms -> {
            if (termsList == null) {
                setListTerms(terms);
            }
        });

    }

    public void startDetailActivity(String termName, View view) {
        Intent intent = new Intent(this, TermDetailsActivity.class);
        intent.putExtra("term name", termName);

        //if the term name is not null or empty then we need to transition to the details activity with the existing information
        if(termName != null && !termName.isEmpty()) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this, new Pair<>(view.findViewById(R.id.itemTitle), "title"), new Pair<>(view.findViewById(R.id.itemDateRange), "dateRange"));
            startActivity(intent, options.toBundle());
        } else {
            //otherwise we need to transition to the activity with placeholder information
            startActivity(intent);
        }
    }

    public void setListTerms(List<Terms> termsList) {
        this.termsList = termsList;
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler.setLayoutManager(manager);
        adapter = new TermsViewAdapter();
        recycler.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(recycler.getContext(), manager.getOrientation());
        recycler.addItemDecoration(divider);
    }

    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
    }

    private class TermsViewAdapter extends RecyclerView.Adapter<TermsViewAdapter.TermsViewHolder> {
        @NonNull
        public TermsViewAdapter.TermsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.view_list_item, parent, false);
            return new TermsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TermsViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return termsList.size();
        }

        class TermsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView title;
            private TextView dateRange;
            private ViewGroup container;

            public TermsViewHolder(View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.itemTitle);
                dateRange = (TextView) itemView.findViewById(R.id.itemDateRange);
                container = (ViewGroup) itemView.findViewById(R.id.list_item);
                container.setOnClickListener(this);
            }

            public void onClick(View view) {
                Terms termList = termsList.get(getAdapterPosition());
                startDetailActivity(String.valueOf(termList.getTermName()), view);
            }
        }
    }
}