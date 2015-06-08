package com.mgw.member.updateApp;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mgw.member.R;
import com.mgw.member.uitls.Utils;

/**
 * 
 * 〈一句话功能简述〉<br>
 * 〈功能详细描述〉 下载通知定制
 * 
 * @author 14052012
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class EppNotificationControl {

    NotificationManager mNotificationManager;
    NotificationCompat.Builder mBuilder;
    String urlPath;

    int progress;
    final int NOTIFYCATIONID = 1001;
    Context context;

    public EppNotificationControl(String urlPath, Context context) {
        this.urlPath = urlPath;
        this.context = context;
        initNotifycation();
        Log.i("jone", urlPath);
    }

    /**
     * 
     * 功能描述: <br>
     * 〈功能详细描述〉 初始化一个builder Author: 14052012 zyn Date: 2014年11月6日 下午7:13:14
     * 
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private void initNotifycation() {
        mNotificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setWhen(System.currentTimeMillis()).setSmallIcon(
                R.drawable.icon);
    }

    /**
     * 
     * 功能描述: <br>
     * 〈功能详细描述〉 首次展示通知栏 Author: 14052012 zyn Date: 2014年11月6日 下午7:13:39
     * 
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    public void showProgressNotify() {
        mBuilder.setContentTitle("等待下载").setContentText("进度:") .setTicker("开始下载");// 通知首次出现在通知栏，带上升动画效果的
        Notification mNotification = mBuilder.build();
        // mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
        // 确定进度的
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条 设置为true就是不确定的那种进度条效果
        mNotificationManager.notify(NOTIFYCATIONID, mNotification);
    }

    /** 设置下载进度 */
    public void updateNotification(int progress) {
        Notification mNotification = mBuilder.build();
        mNotification.flags = Notification.FLAG_ONGOING_EVENT;//
        mBuilder.setProgress(100, progress, false); // 这个方法是显示进度条
        mBuilder.setContentText("下载中...").setContentTitle("");
        if (progress >= 100) {
            mBuilder.setContentText("").setContentTitle("完成");
            // installApk();
            mNotificationManager.cancel(NOTIFYCATIONID);// 删除一个特定的通知ID对应的通知
            mNotificationManager.cancelAll();
            
            //删除mgw下的文件
            
            Utils.installApk(urlPath, context, mNotificationManager,
                    NOTIFYCATIONID);
        }
        mNotificationManager.notify(NOTIFYCATIONID, mNotification);
    }

}
