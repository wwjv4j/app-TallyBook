package com.example.tallybook;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FreshService extends Service {
    public FreshService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}