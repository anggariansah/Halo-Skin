package com.solvedev.haloskin.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.solvedev.haloskin.R;
import com.solvedev.haloskin.activity.BantuanActivity;
import com.solvedev.haloskin.activity.BuyProdukActivity;
import com.solvedev.haloskin.activity.ChatKonsultasiActiviy;
import com.solvedev.haloskin.activity.ListDokterActivity;

import java.util.Map;

import static android.content.ContentValues.TAG;

public class NotificationService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        Common.tokenNotif = token;
        Log.d("TOKENFCM", "Refresh Token :" + token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String click_action = "";

        showNotification(remoteMessage);

    }

//    private void showNotification(RemoteMessage notification, String click_action) {
//
//        if (notification.getData().size() > 0) {
//            Log.d("NOTIFTOKEN", "Message data payload: " + notification.getData());
//
//
//            String id_room = "K-00597";
//            String nama = notification.getData().get("nama");
//
//            Log.d("NAMA_NOTIF", nama);
//
////        Intent intent = new Intent(click_action);
////        intent.setAction(Intent.ACTION_MAIN);
////        intent.addCategory(Intent.CATEGORY_LAUNCHER);
////        intent.putExtra("nama", nama);
////        intent.putExtra("room_id", id_room);
////        intent.putExtra("jenis", "chat");
//            //   PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//            Notification.Builder mBuilder =
//                    new Notification.Builder(this)
//                            .setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentTitle(notification.getNotification().getTitle())
//                            .setContentText("JANCOK");
//
//            Intent resultIntent = new Intent(this, BantuanActivity.class);
//            resultIntent.setAction(Intent.ACTION_MAIN);
//            resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//            resultIntent.putExtra("nama", id_room);
//            resultIntent.putExtra("room_id", "K-00597");
//            resultIntent.putExtra("jenis", "chat");
//            resultIntent.putExtra("notif_token", "riwayat");
//
//            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
//                    resultIntent, 0);
//
//            mBuilder.setContentIntent(pendingIntent);
//            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            mNotificationManager.notify(1, mBuilder.build());
//
//
////        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
////                .setSmallIcon(R.mipmap.ic_launcher)
////                .setContentTitle(notification.getNotification().getTitle())
////                .setContentText(notification.getNotification().getBody())
////                .setAutoCancel(true)
////                .setContentIntent(pendingIntent);
////
////
////        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
////        notificationManager.notify(1, builder.build());
//
//        }else{
//            Log.d("NOTIFTOKEN", "Tidak ada Data");
//        }
//
//    }

    private void showNotification(RemoteMessage notification) {

        Map<String, String> data = notification.getData();

        String id_room = data.get("id_room").toString();
        String nama = data.get("nama").toString();

        String click_action = notification.getNotification().getClickAction();


        Log.d("NAMA_NOTIF", nama);

        Intent intent;

        if(click_action.equals("help")){
            intent = new Intent(this, BantuanActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (click_action.equals("dokter")){
            intent = new Intent(this, ListDokterActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else{
            intent = new Intent(this, BuyProdukActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
//
//        Intent intent = new Intent(this, ChatKonsultasiActiviy.class);
//        intent.setAction(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.putExtra("nama", nama);
//        intent.putExtra("room_id", id_room);
//        intent.putExtra("jenis", "chat");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notification.getNotification().getTitle())
                .setContentText(notification.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
