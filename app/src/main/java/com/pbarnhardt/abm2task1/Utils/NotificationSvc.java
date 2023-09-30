package com.pbarnhardt.abm2task1.Utils;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class NotificationSvc extends IntentService {

    /**
     * Creates an IntentService.  Invoked by subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public NotificationSvc(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    /**
     * constructor
     */
    public NotificationSvc() {
        super("NotificationSvc");
    }

    //seems like this was an old way to do this, so didn't use.
}
