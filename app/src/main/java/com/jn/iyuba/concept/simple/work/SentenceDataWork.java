package com.jn.iyuba.concept.simple.work;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.ForegroundInfo;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.jn.iyuba.succinct.R;
import com.jn.iyuba.concept.simple.util.DataUtil;

public class SentenceDataWork extends Worker {

    public SentenceDataWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        DataUtil.addSentenceData(getApplicationContext().getAssets(), "sentence.json");
        return Result.success();
    }

    @NonNull
    @Override
    public ForegroundInfo getForegroundInfo() {


        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel("1002", "加载数据", NotificationManager.IMPORTANCE_HIGH);
            notificationManagerCompat.createNotificationChannel(notificationChannel);

            notification = new Notification.Builder(getApplicationContext(), "1002")
                    .setContentTitle("加载数据")
                    .setContentText("正在初始化句子列表")
                    .setSmallIcon(R.mipmap.logo)
                    .setAutoCancel(true)
                    .build();
        } else {

            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("加载数据")
                    .setContentText("正在初始化句子列表")
                    .setSmallIcon(R.mipmap.logo)
                    .setAutoCancel(true)
                    .build();
        }


        return new ForegroundInfo(2, notification);
    }
}
