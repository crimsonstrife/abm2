package com.pbarnhardt.abm2task1;

import android.app.Application;

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
    }
}
