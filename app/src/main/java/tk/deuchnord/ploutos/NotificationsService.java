package tk.deuchnord.ploutos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.Preference;
import android.preference.PreferenceManager;

/**
 * Created by jerome on 02/01/15.
 */
public class NotificationsService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean shouldNofity = AppPreferencesActivity.areNotificationsActivated(this);
        double alertValue = Double.valueOf(AppPreferencesActivity.getAlertValue(this));
        TransacDB db = new TransacDB(getApplicationContext());
        double currentAmount = db.getAmount();

        if(shouldNofity && currentAmount <= alertValue)
            notifyMoney(currentAmount);
        else
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(001);
        }

        return START_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void notifyMoney(double currentAmount) {

        Notification.Builder notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setSmallIcon(R.drawable.ic_stat_ploutos);
        notificationBuilder.setContentTitle(getString(R.string.app_name));
        notificationBuilder.setContentText(getString(R.string.notif_remaining)+" "+currentAmount+" "+getString(R.string.symbolDevise));

        Intent intentActivityAddMoney = new Intent(this, AddMoneyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentActivityAddMoney, PendingIntent.FLAG_CANCEL_CURRENT);
        notificationBuilder.setContentIntent(pendingIntent);

        int notifId = 001;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notifId, notificationBuilder.build());

    }

}
