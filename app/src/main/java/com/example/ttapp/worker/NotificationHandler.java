package com.example.ttapp.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ttapp.R;
import com.example.ttapp.ui.activity.DashboardActivity;

import java.util.Objects;

public class NotificationHandler extends Worker {

    private Context context;
    private static final String TAG=NotificationHandler.class.getSimpleName();

    public NotificationHandler(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context=context;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d(TAG,"Do work called");
        String receivedUserName=getInputData().getString(DashboardActivity.KEY_USERNAME);
        displayNotification(receivedUserName);
        return Worker.Result.success();
    }

    private void displayNotification(String username){
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_HIGH);
            channel.setLightColor(Color.RED);
            channel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            channel.enableVibration(true);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "default");
            builder.setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("UKHU JUICE WON")
                    .setContentText(username + " has won an ukhu juice");
            //NotificationManager notificationManager1 = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.notify(1, builder.build());
            Objects.requireNonNull(notificationManager).notify(1, builder.build());

    }
}
