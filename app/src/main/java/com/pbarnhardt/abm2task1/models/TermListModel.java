package com.pbarnhardt.abm2task1.Models;

import android.view.LayoutInflater;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.pbarnhardt.abm2task1.Database.CourseRepository;
import com.pbarnhardt.abm2task1.Entity.Terms;
import com.pbarnhardt.abm2task1.R;

import java.util.List;

public class TermListModel extends ViewModel {

    private CourseRepository courseRepository;
    private List<Terms> listOfAllTerms;
    private LayoutInflater inflater;
    private RecyclerView recycler;

    private ViewModelProvider.Factory viewModelFactory;
    private TermListModel termListModel;

    int getContentViewId() {
        return R.layout.activity_term_list;
    }

    int getNavigationMenuItemId() {
        return R.id.termsNavAction;
    }

    public TermListModel(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public LiveData<List<Terms>> getListOfAllTerms() {
        return this.courseRepository.;
    }
}
