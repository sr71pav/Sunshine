package com.example.android.sunshine.app;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.widget.ListView}.
 */
public class ForecastAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;
    private static final int VIEW_TYPE_COUNT = 2;
    private boolean mUseTodayLayout = true;

    public void setUseTodayLoayout(boolean useTodayLayout)
    {
        mUseTodayLayout = useTodayLayout;
    }
    @Override
    public int getItemViewType(int position) {
        return (position == 0 && mUseTodayLayout) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(Context context, double high, double low) {
        boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(context, high) + "/" + Utility.formatTemperature(context, low);
        return highLowStr;
    }

    /*
        This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
        string.
     */
    private String convertCursorRowToUXFormat(Context context, Cursor cursor) {
        String highAndLow = formatHighLows(context,
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        if (viewType == VIEW_TYPE_TODAY)
        {
            layoutId = R.layout.list_item_forecast_today;
        }
        else if (viewType == VIEW_TYPE_FUTURE_DAY)
        {
            layoutId = R.layout.list_item_forecast;
        }

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //cursor.moveToFirst();

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        int viewType = getItemViewType(cursor.getPosition());
        int weatherid = cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID);

        int weatherIconId = -1;
        if (viewType == VIEW_TYPE_TODAY)
        {
            weatherIconId = Utility.getArtResourceForWeatherCondition(weatherid);
        }
        else if (viewType == VIEW_TYPE_FUTURE_DAY)
        {
            weatherIconId = Utility.getIconResourceForWeatherCondition(weatherid);
        }

        long date = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        String strDate = Utility.getDayName(context, date);
        //viewHolder.iconView.setImageResource(weatherIconId);
        Glide.with(context)
                .load(Utility.getArtUrlForWeatherCondition(context, weatherid))
                .error(weatherIconId)
                .into(viewHolder.iconView);

        viewHolder.dateView.setText(strDate);
        viewHolder.dateView.setContentDescription(context.getString(R.string.a11y_date,strDate));

        String forecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        viewHolder.descriptionView.setText(forecast);
        viewHolder.descriptionView.setContentDescription(context.getString(R.string.a11y_forecast, forecast));

        //boolean isMetric = Utility.isMetric(context);
        double highTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        String strHighTemp = Utility.formatTemperature(context, highTemp);
        viewHolder.highTempView.setText(strHighTemp);
        viewHolder.highTempView.setContentDescription(context.getString(R.string.a11y_high,strHighTemp));

        double lowTemp = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        String strLowTemp = Utility.formatTemperature(context, lowTemp);
        viewHolder.lowTempView.setText(strLowTemp);
        viewHolder.lowTempView.setContentDescription(context.getString(R.string.a11y_low,strLowTemp));
    }

    private static class ViewHolder {
        public final ImageView iconView;
        public final TextView dateView;
        public final TextView descriptionView;
        public final TextView highTempView;
        public final TextView lowTempView;

        public ViewHolder(View view)
        {
            iconView = (ImageView) view.findViewById(R.id.list_item_icon);
            dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
            descriptionView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
            highTempView = (TextView) view.findViewById(R.id.list_item_high_textview);
            lowTempView = (TextView) view.findViewById(R.id.list_item_low_textview);
        }
    }
}