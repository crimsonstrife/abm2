package com.pbarnhardt.abm2task1.Adapters;

import static com.pbarnhardt.abm2task1.Utils.Constants.MENTOR_KEY;

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
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.Enums.RecyclerAdapter;
import com.pbarnhardt.abm2task1.R;
import com.pbarnhardt.abm2task1.Views.MentorDetailsActivity;
import com.pbarnhardt.abm2task1.Views.MentorEditActivity;

import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.ViewHolder> {
    private final Context theContext;
    private final RecyclerAdapter recyclerContext;
    private final List<Mentors> listedMentors;
    private MentorSelectedListener mentorSelectedListener;

    public MentorAdapter(List<Mentors> listedMentors, Context theContext, RecyclerAdapter recyclerContext, MentorSelectedListener mentorSelectedListener) {
        this.listedMentors = listedMentors;
        this.theContext = theContext;
        this.recyclerContext = recyclerContext;
        this.mentorSelectedListener = mentorSelectedListener;
    }

    public interface MentorSelectedListener {
        void onMentorSelected(int position, Mentors mentor);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //bind views here
        TextView mentorNameView = itemView.findViewById(R.id.mentor_name);
        TextView mentorEmailView = itemView.findViewById(R.id.mentor_email);
        //bind button
        FloatingActionButton mentorFloatingActionButton = itemView.findViewById(R.id.mentor_floatingEditButton);
        ImageButton mentorDetailsButton = itemView.findViewById(R.id.mentorDetails);

        MentorSelectedListener mentorSelectedListener;

        public ViewHolder(@NonNull View itemView, MentorSelectedListener mentorSelectedListener) {
            super(itemView);
            this.mentorSelectedListener = mentorSelectedListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mentorSelectedListener.onMentorSelected(getAdapterPosition(), listedMentors.get(getAdapterPosition()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_list_mentors, parent, false);
        return new ViewHolder(view, mentorSelectedListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Mentors mentor = listedMentors.get(position);
        holder.mentorNameView.setText(mentor.getMentorName());
        holder.mentorEmailView.setText(mentor.getMentorEmail());

        switch (recyclerContext) {
            case MAIN:
                holder.mentorFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(theContext, R.drawable.ic_action_edit));
                holder.mentorDetailsButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, MentorDetailsActivity.class);
                    intent.putExtra(MENTOR_KEY, mentor.getMentorId());
                    theContext.startActivity(intent);
                });
                holder.mentorFloatingActionButton.setOnClickListener(v -> {
                    Intent intent = new Intent(theContext, MentorEditActivity.class);
                    intent.putExtra(MENTOR_KEY, mentor.getMentorId());
                    theContext.startActivity(intent);
                });
                break;
            case CHILD:
                holder.mentorFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(theContext, R.drawable.ic_action_delete));
                holder.mentorDetailsButton.setOnClickListener(v -> {
                    if(mentorSelectedListener != null) {
                        mentorSelectedListener.onMentorSelected(position, mentor);
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listedMentors.size();
    }
}
