package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilities;
import barqsoft.footballscores.util.LogUtil;

/**
 * Created by ByungHwa on 7/9/2015.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballScoreDetailWidgetRemoteViewsService extends RemoteViewsService {

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
    private RemoteViews views;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            // this "data" contains 5 days of football score data
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();
                Uri footballForDateUri = DatabaseContract.scores_table.buildScoreWithoutSpecificDate();
                data = getContentResolver().query(footballForDateUri,
                        FOOTBALL_COLUMNS,
                        null,
                        null,
                        DatabaseContract.scores_table.DATE_COL + " ASC");
                LogUtil.log_i("info", "Detail Widget Cursor is empty: " + !data.moveToFirst());
                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                views = new RemoteViews(getPackageName(),
                        R.layout.widget_detail_list_item);

                // fill views with real data

                // data.moveToPosition(position) declared above has already moved data to position. thus here "data"
                // only contains one day of data
                // Extract the weather data from the Cursor
                String date = data.getString(INDEX_DATE);
                String time = data.getString(INDEX_TIME);
                String home = data.getString(INDEX_HOME);
                String away = data.getString(INDEX_AWAY);
                int homeGoals = data.getInt(INDEX_HOME_GOALS);
                int awayGoals = data.getInt(INDEX_AWAY_GOALS);
                double matchID = data.getDouble(INDEX_MATCH_ID);
                int matchDay = data.getInt(INDEX_MATCH_DAY);
                int leagueId = data.getInt(INDEX_LEAGUE);
                String league = Utilities.getLeague(leagueId);
                //int homeCrestResId = Utilities.getTeamCrestByTeamName(home);
                //int awayCrestResId = Utilities.getTeamCrestByTeamName(away);
//                String homeCrestPath = data.getString(INDEX_CREST_PATH_HOME);
//                if (homeCrestPath != null) {
//                    if (homeCrestPath.endsWith("svg")) {
//                        DownloadImageTask task = new DownloadImageTask();
//                        task.execute(homeCrestPath);
//                    } else {
//                        Uri uri = Uri.parse(homeCrestPath);
//                        views.setImageViewUri(R.id.home_crest_detail, uri);
//                    }
//                }
//
//                String awayCrestPath = data.getString(INDEX_CREST_PATH_AWAY);
                String matchDayString = Utilities.getMatchDay(matchDay,
                        leagueId);
                String score = Utilities.getScores(homeGoals, awayGoals);

                //Date date = new Date(System.currentTimeMillis()+((position-2))*86400000);
                //SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                //String finalDateString = mformat.format(date);

                // Add the data to the RemoteViews
                //views.setImageViewResource(R.id.home_crest_detail, homeCrestResId);
                //views.setImageViewResource(R.id.away_crest_detail, awayCrestResId);

//                Bitmap theBitmap = null;
//                try {
//                    theBitmap = Glide.
//                            with(getApplicationContext()).
//                            load(data.getString()).
//                            asBitmap().
//                            into(100, 100). // Width and height
//                            get();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//
//                views.setImageViewBitmap(R.id.home_crest_widget, theBitmap);

                views.setTextViewText(R.id.widget_date_textview_detail, time);
                //views.setTextViewText(R.id.match_description, getDayName(MyApplication.applicationContext, System.currentTimeMillis()+((position-2))*86400000));
                views.setTextViewText(R.id.match_description, date);
                views.setTextViewText(R.id.home_name_detail, home);
                views.setTextViewText(R.id.away_name_detail, away);
                views.setTextViewText(R.id.league_textview_detail, league);
                views.setTextViewText(R.id.matchday_textview_detail, matchDayString);
                views.setTextViewText(R.id.widget_score_textview_detail, score);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_detail_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }

}
