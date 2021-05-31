package org.techtown.evtalk;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.techtown.evtalk.ui.message.ChatActivity;

import java.util.List;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "파이어베이스 메시지";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String username1 = "";
        String username2 = "";
        String roomNumber = "";

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        //Log.i(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            //Log.i(TAG, "Message data payload: " + remoteMessage.getData());
            username1 = remoteMessage.getData().get("username1");
            username2 = remoteMessage.getData().get("username2");
            roomNumber = remoteMessage.getData().get("roomNumber");
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            //Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), username1, username2, roomNumber);
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void handleNow() {
        Log.i(TAG, "Short lived task is done.");
    }

    private void scheduleJob() {
    }

    private void sendNotification(String messageTitle, String messageBody, String username1, String username2, String roomNumber) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = manager.getRunningTasks(1);
        ComponentName componentName= info.get(0).topActivity;
        String topActivityName = componentName.getShortClassName().substring(1);
        int idx = 0;    //현재 액티비티가 ChatActivity.class 일 경우 1, 아닐 경우 0

        Intent intent;
        PendingIntent pendingIntent = null;
        if(roomNumber.equals(""))
            intent = new Intent(this, LoginActivity.class);
        else {
            if(!topActivityName.equals("ui.message.ChatActivity")) {
                intent = new Intent(this, ChatActivity.class);
                intent.putExtra("username1", username1);
                intent.putExtra("username2", username2);
                intent.putExtra("roomNumber", roomNumber);

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                idx = 0;
            }
            else
                idx = 1;
        }
        String channelId = "channelid";
        String GROUP_KEY_EVTALK = "com.android.example.EVTALK";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo_96_white)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setGroup(GROUP_KEY_EVTALK);

        if(idx == 0)
            notificationBuilder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelName = "evtalk";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }
}
