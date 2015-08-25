package tk.deuchnord.ploutos;

import java.util.Date;

import static tk.deuchnord.ploutos.MainActivity.round;

/**
 * Created by jerome on 02/01/15.
 */
public class Transaction {

    protected double value;
    protected Date date;

    /**
     * Creates a new instance of Transaction
     * @param date the date of the transaction
     * @param value the amount of the transaction
     */
    public Transaction(Date date, double value) {

        this.date = date;
        this.value = value;

    }

    /**
     * Creates a new instance of Transaction
     * @param date the date of the transaction as a timestamp
     * @param value the amount of the transaction
     */
    public Transaction(long date, double value) {

        this(new Date(date), value);

    }

    public Date getDate() {

        return date;

    }

    public double getValue() {

        return round(value, 2);

    }

}
