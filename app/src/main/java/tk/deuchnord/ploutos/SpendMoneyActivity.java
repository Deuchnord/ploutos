package tk.deuchnord.ploutos;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jerome on 05/10/14.
 */
public class SpendMoneyActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spendmoney);

        ListView listPredefinedTransaction = (ListView) findViewById(R.id.listPredefinedTransactions);
        listPredefinedTransaction.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TransacDB db = new TransacDB(getApplicationContext());

                Cursor c = db.getReadableDatabase().query("predefinedTransactions", null, null, null, null, null, "nameTransaction", id + ", 1");

                if(!c.moveToNext()) {
                    Toast.makeText(getApplicationContext(), "Erreur interne.", Toast.LENGTH_LONG).show();
                    return;
                }

                double value = c.getDouble(1);

                TextView valueBox = (TextView) findViewById(R.id.amount);
                valueBox.setText(String.valueOf(value));
                saveData(null);

            }
        });

    }

    @Override
    public void onResume() {

        super.onResume();

        TransacDB dbb = new TransacDB(getApplicationContext());
        ListView listTransactions = (ListView) findViewById(R.id.listPredefinedTransactions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        Cursor c = dbb.getReadableDatabase().query("predefinedTransactions", null, null, null, null, null, "nameTransaction", null);

        while(c.moveToNext()) {

            String name = c.getString(0);
            double value = c.getDouble(1);

            adapter.add(name+" ("+value+" "+getString(R.string.symbolDevise)+")");

        }

        listTransactions.setAdapter(adapter);

    }

    public void saveData(View v) {

        TransacDB dbb = new TransacDB(getApplicationContext());
        TextView moneyAdded = (TextView) findViewById(R.id.amount);

        if(!moneyAdded.getText().toString().equals("")) {
            double value = Double.valueOf(moneyAdded.getText().toString());
            ContentValues cv = new ContentValues();
            cv.put("dateTransaction", System.currentTimeMillis());
            cv.put("amountTransaction", 0-value);

            dbb.getWritableDatabase().insert("transactions", null, cv);

            finish();
        }
        else
            Toast.makeText(getApplicationContext(), R.string.alert_moneyAdded_empty, Toast.LENGTH_LONG).show();

    }
}