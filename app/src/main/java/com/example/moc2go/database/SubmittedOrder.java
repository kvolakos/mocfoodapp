package com.example.moc2go.database;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SubmittedOrder {
    private Boolean fulfilled;
    private Date submitted;

    public SubmittedOrder() {

    }

    public SubmittedOrder(Boolean fulfilled, Date submitted) {
        this.fulfilled = fulfilled;
        this.submitted = submitted;
    }

    public Boolean getFulfilled() {
        return fulfilled;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public String getSubmittedStr() {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(submitted);
    }
}
