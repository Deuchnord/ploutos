package tk.deuchnord.ploutos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static tk.deuchnord.ploutos.MainActivity.round;

public class TransacDB extends SQLiteOpenHelper {

    final static String name = "gestcarteru";
    final static int version = 2;

    // Create tables
    private final static String SQL_TABLES_CREATION_TRANSACTIONS =
            "CREATE TABLE transactions(" +
                    "dateTransaction TIMESTAMP," +
                    "amountTransaction DOUBLE" +
                    ")";
    private final static String SQL_TABLES_CREATION_PREDEFINEDTRANSACTION =
            "CREATE TABLE predefinedTransactions(" +
                    "nameTransaction VARCHAR(100)," +
                    "amountTransaction DOUBLE" +
                    ")";

    public TransacDB(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_TABLES_CREATION_TRANSACTIONS);
        db.execSQL(SQL_TABLES_CREATION_PREDEFINEDTRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }

    /**
     * @return the <code>n</code> last transactions
     */
    public List<Transaction> getLastTransactions() {

        List<Transaction> listTransactions = new ArrayList<Transaction>();

        Cursor c = getReadableDatabase().query("transactions", null, null, null, null, null, "dateTransaction DESC", null);

        while(c.moveToNext()) {

            long timestamp = c.getLong(0);
            double value = c.getDouble(1);

            listTransactions.add(new Transaction(timestamp, value));

        }

        return listTransactions;

    }

    /**
     * @return the total amount
     */
    public double getAmount() {

        String[] s = {"SUM(amountTransaction)"};
        Cursor c = getReadableDatabase().query("transactions", s, null, null, null, null, null, null);
        if(c.moveToNext()){

            return round(c.getDouble(0), 2);

        }

        else
            return 0;

    }

    /**
     * Deletes the transaction at position <code>pos</code>.
     * @param pos the position of the transaction to delete in the database (as given by <code>getLastTransactions()</code> method).
     */
    public void deleteTransaction(int pos) {

        Cursor c = getReadableDatabase().query("transactions", null, null, null, null, null, "dateTransaction DESC", null);
        c.moveToPosition(pos);
        long timestamp = c.getLong(0);
        double value = c.getDouble(1);

        // Step 2: remove the line
        getWritableDatabase().delete("transactions", "dateTransaction=? AND amountTransaction=?", new String[]{String.valueOf(timestamp), String.valueOf(value)});

    }

}
