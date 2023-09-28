package com.pbarnhardt.abm2task1.Adapters;

import static com.pbarnhardt.abm2task1.Utils.Constants.TERM_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Utils.Formatting;
import com.pbarnhardt.abm2task1.Views.TermDetailsActivity;
import com.pbarnhardt.abm2task1.Views.TermEditActivity;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.ViewHolder> {
    private final List<Terms> listedTerms;
    private final Context theContext;
    private final RecyclerAdapter recyclerContext;

    public TermAdapter(List<Terms> listedTerms, Context theContext, RecyclerAdapter recyclerContext) {
        this.listedTerms = listedTerms;
        this.theContext = theContext;
        this.recyclerContext = recyclerContext;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //bind views here
        TextView termTitleView = itemView.findViewById(R.id.term_title);
        TextView termDatesView = itemView.findViewById(R.id.term_dates);
        //bind button
        FloatingActionButton termFloatingActionButton = itemView.findViewById(R.id.term_floatingEditButton);
        ImageButton termDetailsButton = itemView.findViewById(R.id.termDetails);
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_list_terms, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder holder, int position) {
        final Terms term = listedTerms.get(position);
        holder.termTitleView.setText(term.getTermName());
        //need to add dates - these are separate fields in the database so need to join
        String dateRange = Formatting.dateFormat.format(term.getTermStartDate()) + " - " + Formatting.dateFormat.format(term.getTermEndDate());
        holder.termDatesView.setText(dateRange);

        switch(recyclerContext) {
            case MAIN:
                holder.termFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(theContext, R.drawable.ic_action_edit));
                holder.termDetailsButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, TermDetailsActivity.class);
                    intent.putExtra(TERM_KEY, term.getTermId());
                    theContext.startActivity(intent);
                });
                holder.termFloatingActionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, TermEditActivity.class);
                    intent.putExtra(TERM_KEY, term.getTermId());
                    theContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.termFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(theContext, R.drawable.ic_action_delete));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listedTerms.size();
    }
}
