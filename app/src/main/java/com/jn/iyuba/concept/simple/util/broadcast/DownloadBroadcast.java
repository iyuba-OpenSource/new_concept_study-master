package com.jn.iyuba.concept.simple.util.broadcast;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageInstaller;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DownloadBroadcast extends BroadcastReceiver {

    public static final String PACKAGE_INSTALLED_ACTION =
            "com.jn.iyuba.succinct.INSTALLER_SETTING";

    @SuppressLint("Range")
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {


            //获取下载id
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            Uri uri = downloadManager.getUriForDownloadedFile(downloadId);

            Intent installIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setData(uri);
            context.startActivity(installIntent);



/*
             PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
            packageInstaller.registerSessionCallback(new PackageInstaller.SessionCallback() {
                @Override
                public void onCreated(int sessionId) {

                }

                @Override
                public void onBadgingChanged(int sessionId) {

                }

                @Override
                public void onActiveChanged(int sessionId, boolean active) {

                }

                @Override
                public void onProgressChanged(int sessionId, float progress) {

                }

                @Override
                public void onFinished(int sessionId, boolean success) {

                }
            });
 PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
                    PackageInstaller.SessionParams.MODE_FULL_INSTALL);
            params.setAppPackageName("com.iyuba.newconcepttop");
            PackageInstaller.Session session = null;
            try {
                int sessionId = packageInstaller.createSession(params);
                session = packageInstaller.openSession(sessionId);
                OutputStream packageInSession = session.openWrite("package", 0, -1);

                InputStream is = context.getContentResolver().openInputStream(uri);
                byte[] buffer = new byte[4096];
                int n;
                while ((n = is.read(buffer)) >= 0) {
                    packageInSession.write(buffer, 0, n);
                }
                session.fsync(packageInSession);
                packageInSession.close();
                is.close();


                context.getApplicationContext().registerReceiver(new InstallStatusBroadcastReceiver(),new IntentFilter(PACKAGE_INSTALLED_ACTION));
                // 创建安装状态接收广播
                Intent broadcastIntent = new Intent(context.getApplicationContext(), InstallStatusBroadcastReceiver.class);
                broadcastIntent.setAction(PACKAGE_INSTALLED_ACTION);
                //此处也可以使用getService回调通知
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                IntentSender statusReceiver = pendingIntent.getIntentSender();
                // 提交会话，这将启动安装工作流
                session.commit(statusReceiver);

            } catch (IOException e) {
                if (session != null) {
                    session.abandon();
                }
                throw new RuntimeException(e);
            }*/
        }
    }


/*    static class InstallStatusBroadcastReceiver extends BroadcastReceiver {

        private static final String TAG = "InstallStatus";

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle extras = intent.getExtras();
            if (PACKAGE_INSTALLED_ACTION.equals(intent.getAction())) {
                int status = extras.getInt(PackageInstaller.EXTRA_STATUS);
                String message = extras.getString(PackageInstaller.EXTRA_STATUS_MESSAGE);

                switch (status) {
                    case PackageInstaller.STATUS_PENDING_USER_ACTION:
                        break;

                    case PackageInstaller.STATUS_SUCCESS:
                        //安装成功
                        Log.d(TAG, "Install succeeded!");
                        break;

                    case PackageInstaller.STATUS_FAILURE:
                    case PackageInstaller.STATUS_FAILURE_ABORTED:
                    case PackageInstaller.STATUS_FAILURE_BLOCKED:
                    case PackageInstaller.STATUS_FAILURE_CONFLICT:
                    case PackageInstaller.STATUS_FAILURE_INCOMPATIBLE:
                    case PackageInstaller.STATUS_FAILURE_INVALID:
                    case PackageInstaller.STATUS_FAILURE_STORAGE:
                        Log.e(TAG, "Install failed!" + status + ", " + message);
                        break;
                    default:
                        Log.e(TAG, "Unrecognized status received from installer: " + status);
                }
            }
        }
    }*/
}