package tk.deuchnord.ploutos;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static java.lang.StrictMath.abs;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listTransactions);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setMessage(getString(R.string.deleteTransaction_messageContent))
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_addMoney:
        	    addMoney();
        	    return true;
            case R.id.action_addSpend:
                spendMoney();
                return true;
            case R.id.action_managePredefinedPayments:
                managePredefinedPayments();
                return true;
            case R.id.action_preferences:
                Intent intentPreferences = new Intent(this, AppPreferencesActivity.class);
                startActivity(intentPreferences);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void managePredefinedPayments() {
        Intent intent = new Intent(this, PredefinedPaymentsManagerActivity.class);
        startActivity(intent);
    }

    private void addMoney() {
        Intent intentActivity = new Intent(this, AddMoneyActivity.class);
        startActivity(intentActivity);
    }

    private void spendMoney() {
        Intent intent = new Intent(this, SpendMoneyActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {

        super.onResume();

        TransacDB dbb = new TransacDB(getApplicationContext());
        ListView listTransactions = (ListView) findViewById(R.id.listTransactions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        List<Transaction> transactions = dbb.getLastTransactions();

        for(Transaction t : transactions) {

            String operation = "";

            if(t.getValue() < 0)
                operation = getString(R.string.operation_payment);
            else
                operation = getString(R.string.operation_recharge);

            adapter.add(operation+" "+abs(t.getValue())+" "+getString(R.string.symbolDevise)+"\n"+t.getDate().getDate()+"/"+(t.getDate().getMonth()+1)+"/"+(t.getDate().getYear()+1900));

        }

        listTransactions.setAdapter(adapter);

        TextView tv = (TextView) findViewById(R.id.remainingMoney);
            tv.setText(getString(R.string.remaining)+" "+dbb.getAmount()+" "+getString(R.string.symbolDevise));

        // Launching the notifications service
        Intent notifServiceIntent = new Intent(this, NotificationsService.class);
        startService(notifServiceIntent);

    }


    private void deleteLine(int pos) {

        // Step 1: get the line n°pos
        TransacDB dbb = new TransacDB(getApplicationContext());

        dbb.deleteTransaction(pos);

        Toast.makeText(getApplicationContext(), getString(R.string.lineDeleted), Toast.LENGTH_LONG).show();

        onResume();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
