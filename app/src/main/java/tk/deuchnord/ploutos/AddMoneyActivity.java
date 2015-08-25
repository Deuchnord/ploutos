package tk.deuchnord.ploutos;

import android.app.NotificationManager;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AddMoneyActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addmoney);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(001);

	}

    public void saveData(View v) {

        TransacDB dbb = new TransacDB(getApplicationContext());
        TextView  moneyAdded = (TextView) findViewById(R.id.amount);

        if(!moneyAdded.getText().toString().equals("")) {
            double value = Double.valueOf(moneyAdded.getText().toString());
            ContentValues cv = new ContentValues();
            cv.put("dateTransaction", System.currentTimeMillis());
            cv.put("amountTransaction", value);

            dbb.getWritableDatabase().insert("transactions", null, cv);

            finish();
        }
        else
            Toast.makeText(getApplicationContext(), R.string.alert_moneyAdded_empty, Toast.LENGTH_LONG).show();

    }
}
