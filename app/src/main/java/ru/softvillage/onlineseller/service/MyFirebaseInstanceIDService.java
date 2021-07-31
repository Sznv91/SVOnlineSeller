package ru.softvillage.onlineseller.service;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.softvillage.onlineseller.AppSeller;

import static ru.softvillage.onlineseller.AppSeller.TAG;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(AppSeller.TAG + "_MyFirebaseInstanceIDService", "NEW_TOKEN: " + s);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        /*sendRegistrationToServer(refreshedToken);*/
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG + "_MyFirebaseMessagingService", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG + "_MyFirebaseMessagingService", "Message data payload: " + remoteMessage.getData());

            /* if (*//* Check if data needs to be processed by long running job *//* true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }*/

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG + "_MyFirebaseMessagingService", "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}