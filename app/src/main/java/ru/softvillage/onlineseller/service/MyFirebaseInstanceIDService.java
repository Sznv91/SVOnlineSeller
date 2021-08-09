package ru.softvillage.onlineseller.service;

import static ru.softvillage.onlineseller.AppSeller.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String LOCAL_TAG = "_" + MyFirebaseInstanceIDService.class.getSimpleName();
    public static final String ACTION_AUTH_CONFIRMED = "AUTH_CONFIRMED";
    public static final String ACTION_UPDATE_USER_OR_ORG_LIST = "UPDATE_USERS_ORG";

    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(AppSeller.TAG + "_MyFirebaseInstanceIDService", "NEW_TOKEN: " + s);
        AuthPresenter.getInstance().setFireBaseToken(s);
        //todo отправка обновленного токена на бекенд

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
            if (remoteMessage.getData().get("action") != null) {
                switch (remoteMessage.getData().get("action")) {
                    case ACTION_AUTH_CONFIRMED:
                        Log.d(TAG + LOCAL_TAG, "onMessageReceived: swith AUTH_CONFIRMED");
                        AuthPresenter.getInstance().setFirstStageAuth(true);
                        break;
                    case ACTION_UPDATE_USER_OR_ORG_LIST:
                        Log.d(TAG + LOCAL_TAG, "onMessageReceived: swith ACTION_UPDATE_USER_OR_ORG_LIST");
                        AppPresenter.getInstance().setNeedLoadUserFromNetwork(true);
                        break;
                }
            }
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
