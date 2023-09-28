package com.pbarnhardt.abm2task1.Popups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Adapters.MentorPopperAdapter;
import com.pbarnhardt.abm2task1.Entity.Mentors;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class DropdownMentors extends PopupWindow {
    /**
     * Variables
     */
    private Context theContext;
    private List<Mentors> mentorsList;
    private MentorPopperAdapter popperAdapter;

    /**
     * Constructor
     * @param theContext Context
     * @param mentorsList List of mentors
     */
    public DropdownMentors(Context theContext, List<Mentors> mentorsList) {
        super(theContext);
        this.theContext = theContext;
        this.mentorsList = mentorsList;
        setupView();
    }

    private void setupView() {
        View view = LayoutInflater.from(theContext).inflate(R.layout.menu_popup, null);
        RecyclerView popupRecyclerView = view.findViewById(R.id.popupRecyclerView);
        popupRecyclerView.setHasFixedSize(true);
        popupRecyclerView.setLayoutManager(new LinearLayoutManager(theContext, LinearLayoutManager.VERTICAL, false));
        popupRecyclerView.addItemDecoration(new DividerItemDecoration(theContext, LinearLayoutManager.VERTICAL));
        popperAdapter = new MentorPopperAdapter(mentorsList);
        popupRecyclerView.setAdapter(popperAdapter);
        setContentView(view);
    }

    public void setSelectedMentorListener(MentorPopperAdapter.MentorListener setMentorListener) {
        popperAdapter.setMentorListener(setMentorListener);
    }
}
