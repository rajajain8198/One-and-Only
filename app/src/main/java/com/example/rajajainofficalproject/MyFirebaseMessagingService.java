package com.example.rajajainofficalproject;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.rajajainofficalproject.Activity.DashBoardActivity;
import com.example.rajajainofficalproject.Activity.UserDetailsActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String  title = "Title", message = "Message", fromReceivingID = "100";
    String token = null;
    private static final String TAG = "MyFirebaseMsgService";
    public static final String ACTION_NOTIFICATION = "action_notification";
    public static final String EXTRA_NOTIFICATION_MESSAGE = "extra_message";
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        token = s;
        Log.d(TAG, "Refreshed token: " + token);

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("TAG", "Message data payload: " + remoteMessage.getData());

            title = remoteMessage.getData().get("title").toString();
            message = remoteMessage.getData().get("message").toString();
            fromReceivingID = remoteMessage.getData().get("receiver_id").toString();
        }else{
            title = remoteMessage.getNotification().getTitle();
            message = remoteMessage.getNotification().getBody();
        }
        Log.d(TAG, "Data From Key Value Pair : " + " Title :  " + title + ", Message : " + message +
                ", fromReceivingID : " + fromReceivingID);
        sendNotification(title, message);


    }

    private void sendNotification(String titleText, String body_message) {

        Intent intent = new Intent(this, UserDetailsActivity.class);
//        if(!catchID.equalsIgnoreCase("0")){
//            intent.putExtra("catch_Id", catchID);
//        }
//        if(!compID.equalsIgnoreCase("0")){
//            intent.putExtra("comp_Id", compID);
//        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.com_facebook_button_icon)
                        .setContentTitle(titleText)
                        .setContentText(body_message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "IFish Comp Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onMessageSent(@NonNull String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }


    private void updateMainActivity(String message) {

        Intent intent = new Intent(ACTION_NOTIFICATION);
        //put whatever data you want to send, if any
        intent.putExtra(EXTRA_NOTIFICATION_MESSAGE, message);
        //send broadcast
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//        PrefsUser prefsUser = new PrefsUser(this);
//        prefsUser.setNotification_status("true");
//        int badgeCount = prefsUser.getBadge_count();
//        ShortcutBadger.applyCount(this, badgeCount); //for 1.1.4+

//        SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString("notiStatus", "true");
//        editor.commit();
    }
}
