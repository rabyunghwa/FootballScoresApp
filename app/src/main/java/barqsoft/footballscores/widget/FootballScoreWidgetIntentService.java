package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.util.LogUtil;

/**
 * Created by ByungHwa on 7/8/2015.
 */
public class FootballScoreWidgetIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */

    private static final String TAG = "FootballScoreWidgetIntentService";

    private static final String[] FOOTBALL_COLUMNS = {
            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.TIME_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.MATCH_DAY,
            DatabaseContract.scores_table.LEAGUE_COL,
            DatabaseContract.scores_table.CREST_PATH_HOME,
            DatabaseContract.scores_table.CREST_PATH_AWAY
    };

    // these indices must match the projection
    private static final int INDEX_DATE = 0;
    private static final int INDEX_TIME = 1;
    private static final int INDEX_HOME = 2;
    private static final int INDEX_AWAY = 3;
    private static final int INDEX_HOME_GOALS = 4;
    private static final int INDEX_AWAY_GOALS = 5;
    private static final int INDEX_MATCH_ID = 6;
    private static final int INDEX_MATCH_DAY = 7;
    private static final int INDEX_LEAGUE = 8;
    private static final int INDEX_CREST_PATH_HOME = 9;
    private static final int INDEX_CREST_PATH_AWAY = 10;

    private String[] fragmentdate = new String[1];
    private RemoteViews views;


    public FootballScoreWidgetIntentService() {
        super("FootballScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve all of the Today widget ids: these are the widgets we need to update
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballScoreWidgetProvider.class));

        // Get football's data from the ContentProvider
        Date date = new Date(System.currentTimeMillis()+2*86400000);
        SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
        fragmentdate[0] = mformat.format(date);
        //Uri footballForDateUri = DatabaseContract.scores_table.buildScoreWithDate();
        Uri footballForDateUri = DatabaseContract.scores_table.buildScoreWithoutSpecificDate();
        Cursor data = getContentResolver().query(footballForDateUri, FOOTBALL_COLUMNS, FOOTBALL_COLUMNS[0],
                fragmentdate, DatabaseContract.scores_table.DATE_COL + " ASC");
        LogUtil.log_i(TAG, "Football Widget Intent Service cursor is null: " + (data == null));
        LogUtil.log_i(TAG, "Football Widget Intent Service cursor is empty: " + (!data.moveToFirst()));
        if (!data.moveToFirst()) {
            data.close();
            return;
        }

        data.moveToFirst();

        // Extract the weather data from the Cursor
        String time = data.getString(INDEX_TIME);
        String home = data.getString(INDEX_HOME);
        String away = data.getString(INDEX_AWAY);
        int homeGoals = data.getInt(INDEX_HOME_GOALS);
        int awayGoals = data.getInt(INDEX_AWAY_GOALS);
        double matchID = data.getDouble(INDEX_MATCH_ID);
        int matchDay = data.getInt(INDEX_MATCH_DAY);
        int leagueId = data.getInt(INDEX_LEAGUE);

        //String crestPathHome = data.getString(INDEX_CREST_PATH_HOME);
        //String crestPathAway = data.getString(INDEX_CREST_PATH_AWAY);

        String league = Utilities.getLeague(leagueId);
        //int homeCrestResId = Utilities.getTeamCrestByTeamName(home);
        //int awayCrestResId = Utilities.getTeamCrestByTeamName(away);
        String matchDayString = Utilities.getMatchDay(matchDay,
                leagueId);
        String score = Utilities.getScores(homeGoals, awayGoals);

        LogUtil.log_i(TAG, "Football Score Widget Intent Service time: " + time);
        LogUtil.log_i(TAG, "Football Score Widget Intent Service score: " + score);
        LogUtil.log_i(TAG, "Football Score Widget Intent Service home name: " + home);
        LogUtil.log_i(TAG, "Football Score Widget Intent Service away name: " + away);
        LogUtil.log_i(TAG, "Football Score Widget Intent Service match id: " + matchID);
        LogUtil.log_i(TAG, "Football Score Widget Intent Service match day: " + matchDayString);
        LogUtil.log_i(TAG, "Football Score Widget Intent Service league: " + league);
        //LogUtil.log_i(TAG, "Football Score Widget Intent Service home crest resource id: " + homeCrestResId);
        //LogUtil.log_i(TAG, "Football Score Widget Intent Service away crest resource id: " + awayCrestResId);

        data.close();

        // Perform this loop procedure for each football widget
        for (int appWidgetId : appWidgetIds) {
            // update different
            int layoutId = R.layout.football_widget;

            views = new RemoteViews(getPackageName(), layoutId);

            // Content Descriptions for RemoteViews were only added in ICS MR1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, league);
            }
             //Add the data to the RemoteViews
//            Message message_home = new Message();
//            message_home.what = 0;
//            message_home.obj = crestPathHome;
//            mHandler.sendMessage(message_home);

            views.setTextViewText(R.id.widget_date_textview, time);
            views.setTextViewText(R.id.home_name, home);
            views.setTextViewText(R.id.away_name, away);
            views.setTextViewText(R.id.league_textview, league);
            views.setTextViewText(R.id.matchday_textview, matchDayString);
            views.setTextViewText(R.id.widget_score_textview, score);

            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.match_description, description);
    }

}
