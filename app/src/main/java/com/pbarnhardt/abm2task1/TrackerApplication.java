package com.pbarnhardt.abm2task1;

import android.app.Application;

import com.pbarnhardt.abm2task1.Views.TermDetailsActivity;
import com.pbarnhardt.abm2task1.Views.TermsListActivity;

public class TrackerApplication extends Application {

    private TrackerApplicationComponent trackerApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public TrackerApplicationComponent getTrackerApplicationComponent() {
        return trackerApplicationComponent;
    }

    public interface TrackerApplicationComponent {
        Application application();

        void inject(TermDetailsActivity termDetailsActivity);
        void inject(TermsListActivity termsListActivity);
    }
}
