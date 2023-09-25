package com.pbarnhardt.abm2task1.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.R;

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.term_list_itemview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.ViewHolder holder, int position) {
        final Terms term = listedTerms.get(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //bind views here
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
