package com.monk.clipedit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

/**
 * Notification 関連クラス
 */
class NotificationWrapper {
    private static final String TAG = "NotificationWrapper";

    private Context mContext;

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
        int smallIcon = R.drawable.ic_stat_transparent;
        int priority = Notification.PRIORITY_MIN;
        Notification notification = new NotificationCompat.Builder(this.mContext)
                .setContentIntent(pendingIntent)
                .setShowWhen(false)
                .setOngoing(true)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setSmallIcon(smallIcon)
                .setPriority(priority)
                .build();
        NotificationManager manager = (NotificationManager) this.mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (null != manager) {
            manager.notify(0, notification);
        }
    }
}
