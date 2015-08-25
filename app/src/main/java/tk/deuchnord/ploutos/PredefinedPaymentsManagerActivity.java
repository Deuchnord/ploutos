package tk.deuchnord.ploutos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by jerome on 05/10/14.
 */
public class PredefinedPaymentsManagerActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefinedpaymentsmanager);

        ListView listView = (ListView) findViewById(R.id.listPredefinedTransactions);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PredefinedPaymentsManagerActivity.this);

                builder.setMessage(getString(R.string.deletePredefinedTransaction_messageContent))
                        .setPositiveButton(getString(R.string.deleteTransaction_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteLine(position);
                            }
                        })
                        .setNegativeButton(getString(R.string.deleteTransaction_no), null);
                builder.show();

                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.predefinedpaymentsmanager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_addPredefinedPayments)
            addItem();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {

        super.onResume();

        TransacDB db = new TransacDB(getApplicationContext());
        ListView listTransactions = (ListView) findViewById(R.id.listPredefinedTransactions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        Cursor c = db.getReadableDatabase().query("predefinedTransactions", null, null, null, null, null, "nameTransaction", null);
        while(c.moveToNext()) {

            String name = c.getString(0);
            double value = c.getDouble(1);

            adapter.add(name+" ("+value+" "+getString(R.string.symbolDevise)+")");

        }

        listTransactions.setAdapter(adapter);

    }

    private void addItem() {

        Intent intent = new Intent(this, AddPredefinedPaymentActivity.class);
        startActivity(intent);

    }

    private void deleteLine(int pos) {

        // Step 1: get the line n°pos
        TransacDB dbb = new TransacDB(getApplicationContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        Cursor c = dbb.getReadableDatabase().query("predefinedTransactions", null, null, null, null, null, "nameTransaction", null);
        c.moveToPosition(pos);
        String name = c.getString(0);
        double value = c.getDouble(1);

        // Step 2: remove the line
        dbb.getWritableDatabase().delete("predefinedTransactions", "nameTransaction=? AND amountTransaction=?", new String[]{String.valueOf(name), String.valueOf(value)});

        onResume();
    }

}