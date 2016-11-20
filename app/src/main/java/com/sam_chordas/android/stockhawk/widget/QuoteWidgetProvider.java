package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.DetailsActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by Deepesh_Gupta1 on 11/14/2016.
 */

public class QuoteWidgetProvider extends AppWidgetProvider {
    private final String TAG = QuoteWidgetProvider.class.getSimpleName(); 
    
    @Override @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Log.d(TAG, "onUpdate: ");
        
        for ( int appWidgetId : appWidgetIds ){
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.quote_widget);

            Intent clickPendingIntent = new Intent(context, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, clickPendingIntent, 0);

            rv.setOnClickPendingIntent(R.id.widget, pendingIntent);
//            views.setRemoteAdapter(R.id.widget_list, new Intent(context, QuoteWidgetService.class));

            Intent intent = new Intent(context, QuoteWidgetService.class);
           /* intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));*/

            rv.setRemoteAdapter(R.id.widget_list, intent);
            rv.setEmptyView(R.id.widget_list, R.id.widget_empty);

            // Set up the collection
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }*/

            /*boolean useDetailsActivity = context.getResources()
                    .getBoolean(R.bool.use_detail_activity);

            Intent clickIntentTemplate = useDetailsActivity
                    ? new Intent(context, DetailsActivity.class)
                    : new Intent(context, MyStocksActivity.class);

            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setPendingIntentTemplate(R.id.widget_list, clickPendingIntentTemplate);
            views.setEmptyView(appWidgetId, R.id.widget_empty);*/

            appWidgetManager.updateAppWidget(appWidgetId, rv);
            //super.onUpdate(context, appWidgetManager, appWidgetIds);
        }
    }

    /*@Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIDs = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIDs, R.id.widget_list);
    }*/

    private void setRemoteAdapter(Context context, final RemoteViews views){
        views.setRemoteAdapter(R.id.widget_list, new Intent(context, QuoteWidgetService.class));
    }

    private void setRemoteAdapterV11(Context context, final RemoteViews views){
        views.setRemoteAdapter(0, R.id.widget_list, new Intent(context, QuoteWidgetService.class));
    }


}
