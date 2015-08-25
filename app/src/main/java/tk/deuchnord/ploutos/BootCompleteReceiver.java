package tk.deuchnord.ploutos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jerome on 02/01/15.
 */
public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

            Intent serviceIntent = new Intent(context, NotificationsService.class);
            context.startService(serviceIntent);

        }

    }
}
