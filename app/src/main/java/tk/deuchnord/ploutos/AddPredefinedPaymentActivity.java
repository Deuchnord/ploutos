package tk.deuchnord.ploutos;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by jerome on 08/11/14.
 */
public class AddPredefinedPaymentActivity extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpredefinedpayment);

        Button saveBtn = (Button) findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TransacDB db = new TransacDB(getApplicationContext());
                TextView title = (TextView) findViewById(R.id.namePayment),
                        value = (TextView) findViewById(R.id.valuePayment);

                if(title.getText().toString().isEmpty() || value.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires !", Toast.LENGTH_LONG).show();
                else if(title.getText().length() >= 100)
                    Toast.makeText(getApplicationContext(), "Le nom du paiement prédéfini est trop long !", Toast.LENGTH_LONG).show();
                else {

                    double doubledValue = Double.valueOf(value.getText().toString());

                    if(doubledValue < 0)
                        doubledValue = 0 - doubledValue;

                    ContentValues cv = new ContentValues();
                    cv.put("nameTransaction", title.getText().toString());
                    cv.put("amountTransaction", doubledValue);

                    db.getWritableDatabase().insert("predefinedTransactions", null, cv);

                    finish();

                }

            }
        });

    }

}
