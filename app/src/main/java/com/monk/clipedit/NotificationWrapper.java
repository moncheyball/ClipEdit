package com.monk.clipedit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

/**
 * Notification 関連クラス
 */
class NotificationWrapper {
    private static final String TAG = "NotificationWrapper";

    private Context mContext;
    private String channelId = "clipedit";

    NotificationWrapper(Context context) {
        this.mContext = context;
    }

    /**
     * Notification を表示する
     */
    void setNotification() {
        Log.d(TAG, "setNotification()");

        // Notificationタップ時の起動イベント
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setClassName(this.mContext.getApplicationContext().getPackageName(), this.mContext.getClass().getName())
                .setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        // Notification生成
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android 0 以上：チャンネル登録
            String channelName = "ClipEdit";
            int importance = NotificationManager.IMPORTANCE_NONE;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            if (null != notificationManager) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        int notifyId = 1;
        Notification notification = new NotificationCompat.Builder(mContext, channelId)
                .setContentIntent(pendingIntent)
                .setContentTitle(mContext.getResources().getString(R.string.edit_clipboard))
                .setSmallIcon(R.drawable.ic_clip)
                .setShowWhen(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .build();
        NotificationManagerCompat.from(mContext).notify(notifyId, notification);
    }

}
