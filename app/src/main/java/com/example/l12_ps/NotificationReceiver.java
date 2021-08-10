package com.example.l12_ps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

public class NotificationReceiver extends BroadcastReceiver {
    int requestCode = 123;
    int notificationID = 888;

    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getExtras().getString("name");
        String description = intent.getExtras().getString("description");
        int id = intent.getExtras().getInt("id");
        System.out.println("Test12" + name + description + id);
        Log.d("Test12", name + description + id);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/" + R.raw.test123);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            NotificationChannel channel = new NotificationChannel("default", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);

            channel.setDescription("This is for default notification");
            channel.enableLights(true);
            channel.setSound(sound, attributes);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }

        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("name", name);
        i.putExtra("description", description);
        i.putExtra("id", id);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, requestCode, i, PendingIntent.FLAG_CANCEL_CURRENT);

        //Bitmap pic = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher_background);


        //ReplyActivity
        Intent intentreply = new Intent(context, MainActivity.class);
        PendingIntent pendingIntentReply = PendingIntent.getActivity(context, 0, intentreply, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteInput ri = new RemoteInput.Builder("status").setLabel("Status report").setChoices(new String [] {"Speak Now", "Draw Emoji", "Complete"}).build();
        NotificationCompat.Action action1 = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Reply", pendingIntentReply).addRemoteInput(ri).build();



        //build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        builder.setContentTitle(name);
        builder.setContentText(description);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
//        builder.setLargeIcon(pic);
        builder.setContentIntent(pendingIntent);
//        builder.setStyle(new NotificationCompat.BigPictureStyle()
//                .bigPicture(pic)
//                .bigLargeIcon(null));
        builder.setAutoCancel(true);
        builder.setSound(sound);
        builder.setVibrate(new long[] { 1000, 1000});
        builder.setLights(Color.BLUE, 3000, 3000);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "This is an action",pendingIntent).build();
        NotificationCompat.WearableExtender extender = new NotificationCompat.WearableExtender();
        extender.addAction(action);
        extender.addAction(action1);

        builder.extend(extender);

        Notification n = builder.build();
        notificationManager.notify(123, n);

    }
}
