package com.sam_chordas.android.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private LineChartView mLineChartView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Utils.getFormattedDate();

        setContentView(R.layout.activity_line_graph);

        Intent intent = getIntent();
        String symbol = intent.getStringExtra("symbol");

        Cursor data = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, new String[]{QuoteColumns.BIDPRICE, QuoteColumns.CREATED},
                QuoteColumns.SYMBOL + "= ?", new String[]{symbol}, null);

        Log.d("DetailsActivity", DatabaseUtils.dumpCursorToString(data));

        List<Float> price = new ArrayList<>();
        List<String> label = new ArrayList<>();


        if (data.getCount() > 0) {
            data.moveToFirst();
            int i = 0;
            while (i < data.getCount()) {
                String bidPrice = data.getString(data.getColumnIndex(QuoteColumns.BIDPRICE));
                String created = data.getString(data.getColumnIndex(QuoteColumns.CREATED));

                if (!TextUtils.isEmpty(bidPrice)) {
                    price.add(Float.valueOf(bidPrice));
                    label.add(created);
                }
                if (!data.isLast())
                    data.moveToNext();
                i++;
            }
        }

        float[] bid_price = new float[price.size()];
        String[] labels = new String[price.size()];

        int i = 0;
        for (float p : price) {
            bid_price[i] = p;
            labels[i] = label.get(i);
            i++;
        }

        //mTv.setText(prices);
        float max = Collections.max(price);
        float min = Collections.min(price);
        int step = (int) (max-min);



        Log.d("DetailsActivity","Step = " + String.valueOf(step) + " Min = " + String.valueOf( (int)min) + " Max = " + String.valueOf((int)max));
        mLineChartView = (LineChartView) findViewById(R.id.linechart);

        LineSet dataSet = new LineSet(labels, bid_price);
        dataSet.setSmooth(true);
        dataSet.setFill(getResources().getColor(R.color.chart_blue));

        mLineChartView.addData(dataSet);
        mLineChartView.setAxisColor(getResources().getColor(R.color.chart_axis_color));
        mLineChartView.setAxisThickness(2);
        mLineChartView.setLabelsColor(getResources().getColor(R.color.chart_label_color));
        mLineChartView.setAxisBorderValues((int) min-10, (int) max);
        mLineChartView.setBackgroundColor(getResources().getColor(R.color.chart_bg_color));
        mLineChartView.show();

    }
}
