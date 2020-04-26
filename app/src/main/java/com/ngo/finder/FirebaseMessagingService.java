package com.ngo.finder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
   String TAG="FCM";
   SharedPreferences prefrences;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }


        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        sendNotification(remoteMessage.getData().get("body"));
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        prefrences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody) {
        JSONObject jsonObject;
        String msg=null,from=null,requester_token=null,status=null,title=null,latitude=null,longitude=null;
        try{ jsonObject=new JSONObject(messageBody);
//        from=jsonObject.getString("from");
            msg=jsonObject.getString("msg");
            title=jsonObject.getString("title");
            from=jsonObject.getString("from");
            try {
                boolean check=false;
                JSONArray jsonArray=new JSONArray(prefrences.getString("default_teachers",""));
                int count=0;
                while (count<jsonArray.length()){
                    if(jsonArray.getJSONObject(count).getString("fuserid").equalsIgnoreCase(from)){
                        check=true;
                        break;
                    }
                    count++;
                }
                if(check){
                    Log.i(TAG, "sendNotification: requester tokeinid"+requester_token);
                    Intent intent = new Intent(this, NotificationActivity.class);
                    intent.putExtra("msg",msg);
                    intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

                    notification(title,msg,pendingIntent,2);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }catch (Exception e){e.printStackTrace();}



    }
    public void notification(String title, String msg, PendingIntent pendingIntent,int notifyid) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("com.faculty.system", "Faculty Finder", importance);
            channel.setDescription("Notification for inlift");
            channel.setShowBadge(true);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this


            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);


            Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)
                    .setChannelId("com.faculty.system")
                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(notifyid, notificationBuilder.build());

        } else {
            Notification.Builder notificationBuilder = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setAutoCancel(true)

                    .setSound(defaultSoundUri)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(notifyid, notificationBuilder.build());
        }
    }
}
