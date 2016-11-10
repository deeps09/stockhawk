package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {

    private LineChartView mLineChartView;
    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_graph);

        mTv = (TextView) findViewById(R.id.tv);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", -1);

        String prices = "";
        String symbol = "";

        Cursor cursor = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, new String[]{QuoteColumns.SYMBOL},
                QuoteColumns._ID + "= ?", new String[]{String.valueOf(id)}, null);

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            symbol = cursor.getString(cursor.getColumnIndex(QuoteColumns.SYMBOL));

            Cursor data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, new String[]{QuoteColumns.BIDPRICE},
                    QuoteColumns.SYMBOL + "= ?", new String[]{symbol}, null);

            if (data.getCount() > 0) {
                data.moveToFirst();
                int i = 0;
                while (i < data.getCount()) {
                    prices = prices + ", " + data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE));
                    if (!data.isLast())
                        data.moveToNext();
                    i++;
                }
            }
        }



        mTv.setText(prices);

        mLineChartView = (LineChartView) findViewById(R.id.linechart);

        String[] labels = new String[3];
        LineSet dataset = new LineSet(new String[]{"A", "B", "C"}, new float[]{0.0f, 5.0f, 10.0f});

        mLineChartView.addData(dataset);
        mLineChartView.show();

    }
}
