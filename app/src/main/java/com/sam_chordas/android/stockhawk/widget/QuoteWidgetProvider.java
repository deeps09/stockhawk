package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.rest.Utils;
import com.sam_chordas.android.stockhawk.service.StockTaskService;
import com.sam_chordas.android.stockhawk.ui.DetailsActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Deepesh_Gupta1 on 11/14/2016.
 */

public class QuoteWidgetProvider extends AppWidgetProvider {
    private final String TAG = QuoteWidgetProvider.class.getSimpleName();

    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "onUpdate: ");

        for (int appWidgetId : appWidgetIds) {
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.quote_widget);

            Intent clickPendingIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickPendingIntent, 0);

            rv.setOnClickPendingIntent(R.id.widget, pendingIntent);

            Intent intent = new Intent(context, QuoteWidgetService.class);

            rv.setRemoteAdapter(R.id.widget_list, intent);
            rv.setEmptyView(R.id.widget_list, R.id.widget_empty);

            Intent intentTemplate = new Intent(context, DetailsActivity.class);
            PendingIntent clickTemplatePendingIntent = PendingIntent.getActivity(context, 0, intentTemplate, PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.widget_list, clickTemplatePendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, rv);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(Utils.DATA_UPDATED_ACTION)) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.widget_list);
        }
    }
}
