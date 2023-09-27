package com.pbarnhardt.abm2task1.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class MentorPopperAdapter extends RecyclerView.Adapter<MentorPopperAdapter.ViewHolder>{
    private List<Mentors> mentorsList;
    private MentorListener mentorListener;

    public interface MentorListener {
        void onMentorSelected(int position, Mentors mentor);
    }

    /**
     * Constructor
     * @param mentorsList
     */
    public MentorPopperAdapter(List<Mentors> mentorsList) {
        super();
        this.mentorsList = mentorsList;
    }

    /**
     * Listener
     * @param mentorListener
     */
    public void setMentorListener(MentorListener mentorListener) {
        this.mentorListener = mentorListener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        ImageView mentorItemAddIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            mentorItemAddIcon = itemView.findViewById(R.id.mentorItemAddIcon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mentor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Mentors mentor = mentorsList.get(position);
        holder.itemTitle.setText(mentor.getMentorName());
        holder.mentorItemAddIcon.setOnClickListener(view -> {
            if(mentorListener != null) {
                mentorListener.onMentorSelected(position, mentor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mentorsList.size();
    }
}
