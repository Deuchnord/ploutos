package tk.deuchnord.ploutos;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by jerome on 02/01/15.
 */
public class AppPreferencesActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public void onResume() {

        super.onResume();

        getSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        updateDisplay();

    }

    public void updateDisplay() {

        // Enabling/Disabling alertValue following activateNotification
        Preference preference = findPreference("alertValue");
        preference.setEnabled(areNotificationsActivated(this));

        // Updating alertValue description
        preference = findPreference("alertValue");
        preference.setSummary("Lorsque le solde atteint "+getAlertValue(this)+" €.");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateDisplay();
    }


    public static SharedPreferences getSharedPreferences(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static String getAlertValue(Context context) {

        String value = getSharedPreferences(context).getString("alertValue", "10");
        if(value.equals(""))
            value = "10";

        return value;

    }

    public static boolean areNotificationsActivated(Context context) {

        return getSharedPreferences(context).getBoolean("activateNotifications", false);

    }

}