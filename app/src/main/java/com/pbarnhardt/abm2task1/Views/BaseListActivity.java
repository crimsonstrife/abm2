package com.pbarnhardt.abm2task1.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.pbarnhardt.abm2task1.R;

public abstract class BaseListActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    protected BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        populateScreen();
        navigationView = (BottomNavigationView) findViewById(R.id.navigation);
        if(navigationView != null) {
            navigationView.setOnItemSelectedListener(this);
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    public void onPause(){
        super.onPause();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        navigationView.postDelayed(() -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.homeNavAction:
                    startActivity(new Intent(this, MainActivity.class));
                    break;
                case R.id.termsNavAction:
                    startActivity(new Intent(this, TermsListActivity.class));
                    break;
                case R.id.coursesNavAction:
                    startActivity(new Intent(this, CoursesListActivity.class));
                    break;
                case R.id.assessmentsNavAction:
                    startActivity(new Intent(this, AssessmentsListActivity.class));
                    break;
            }
            finish();
        }, 300);
        return true;
    }

    private void updateNavigationBarState(){
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        MenuItem item = navigationView.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    abstract int getContentViewId();

    abstract int getNavigationMenuItemId();

    abstract void populateScreen();

    public ItemTouchHelper.Callback createHelperCallback() {
        return null;
    }
}
