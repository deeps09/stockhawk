package com.sam_chordas.android.stockhawk.widget;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Binder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;

/**
 * Created by Deepesh_Gupta1 on 11/14/2016.
 */

public class QuoteWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private final String TAG = QuoteWidgetService.class.getSimpleName();
            private Cursor cursorData = null;


            @Override
            public void onCreate() {
            }

            @Override
            public void onDataSetChanged() {
                if (cursorData != null)
                    cursorData.close();

                long token = Binder.clearCallingIdentity();
                cursorData = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI, null,
                        QuoteColumns.ISCURRENT + " = ?", new String[]{"1"}, null);

                Binder.restoreCallingIdentity(token);
            }

            @Override
            public void onDestroy() {
                if (cursorData != null) {
                    cursorData.close();
                    cursorData = null;
                }
            }

            @Override
            public int getCount() {
                return cursorData == null ? 0 : cursorData.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        cursorData == null || !cursorData.moveToPosition(position)) {
                    return null;
                }

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_list_item_quote);

                String symbol = cursorData.getString(cursorData.getColumnIndex(QuoteColumns.SYMBOL));
                String bidPrice = cursorData.getString(cursorData.getColumnIndex(QuoteColumns.BIDPRICE));
                String change = cursorData.getString(cursorData.getColumnIndex(QuoteColumns.CHANGE));

                views.setTextViewText(R.id.widget_list_item_stock_symbol, symbol);
                views.setTextViewText(R.id.widget_list_item_bid_price, bidPrice);
                views.setTextViewText(R.id.widget_list_item_change, change);

                //Setting change views background on the basis of price change

                String negOrPos = change.substring(0, 1);

                if (negOrPos.equals("+"))
                    views.setInt(R.id.widget_list_item_change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                else if (negOrPos.equals("-"))
                    views.setInt(R.id.widget_list_item_change, "setBackgroundResource", R.drawable.percent_change_pill_red);


                Intent fillInIntent = new Intent();
                fillInIntent.putExtra("symbol", symbol);
                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }


            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item_quote);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (cursorData.moveToPosition(position)) {
                    return cursorData.getLong(cursorData.getColumnIndex(QuoteColumns._ID));
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
