package com.jn.iyuba.concept.simple.util;

import android.app.Application;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.jn.iyuba.concept.simple.MyApplication;

public class DownloadApk {

    public static void download(Context context, String name, String url) {

        Uri downloadUri = Uri.parse(url);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setAllowedOverRoaming(true);
        //处理点击通知，提示无法打开的问题application/vnd.android.package-archive
        request.setMimeType("application/vnd.android.package-archive");//表示apk类型
        //通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle(name);
        request.setDescription("正在下载....");
        request.setVisibleInDownloadsUi(true);
        //可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name + ".apk");
        long downloadId = downloadManager.enqueue(request);
    }
}
