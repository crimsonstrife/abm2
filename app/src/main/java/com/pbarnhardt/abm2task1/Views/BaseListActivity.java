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
            //originally planned to use a switch statement here, but this caused an IDE error regarding the use of Resource IDs and them being non-final
            //might've been more performant, but in this particular case I don't think it matters, and I avoid the error/warnings.
            if(itemId == R.id.homeNavAction){
                startActivity(new Intent(this, MainActivity.class));
            } else if(itemId == R.id.termsNavAction){
                startActivity(new Intent(this, TermsListActivity.class));
            } else if(itemId == R.id.coursesNavAction){
                startActivity(new Intent(this, CoursesListActivity.class));
            } else if(itemId == R.id.assessmentsNavAction){
                startActivity(new Intent(this, AssessmentsListActivity.class));
            } else {
                finish();
            }
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
        ItemTouchHelper.SimpleCallback simpleTouchCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //swipe helper
            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO: implement swipe features
            }

            //drag helper
            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView recyclerView, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                return false; //return false so that the drag feature is disabled
            }
        };
        return simpleTouchCallback;
    }
}
