package barqsoft.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MyApplication;
import barqsoft.footballscores.R;
import barqsoft.footballscores.util.LogUtil;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class MyFetchService extends IntentService {
    public static final String LOG_TAG = "MyFetchService";

    //String BASE_URL;
    final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days

    boolean isHome;

    public MyFetchService() {
        super("MyFetchService");
    }

    // define constant for Widget data update
    public static final String ACTION_DATA_UPDATED = "barqsoft.footballscores.ACTION_DATA_UPDATED";

    @Override
    protected void onHandleIntent(Intent intent) {
        getData("n2");
        getData("p2");
        return;
    }

    private String getLogoPathByTeamName(String timeFrame, String league, String name) {
        String BASE_URL_LOGO = "http://api.football-data.org/alpha/soccerseasons/" + league + "/teams/"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days

        Uri fetch_build = Uri.parse(BASE_URL_LOGO).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();

        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod("GET");
            m_connection.addRequestProperty("X-Auth-Token", "5a4ce2e9edd843cf96354de0d8597bce");
            m_connection.connect();

            // Read the input stream into a String
            InputStream inputStream = m_connection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            JSON_data = buffer.toString();

            //json data: [{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/145935"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/395"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/17"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/46"}},"date":"2015-08-28T16:30:00Z","status":"TIMED","matchday":5,"homeTeamName":"SC Freiburg","awayTeamName":"SV Sandhausen","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/145937"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/395"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/44"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/13"}},"date":"2015-08-28T16:30:00Z","status":"TIMED","matchday":5,"homeTeamName":"1. FC Heidenheim 1846","awayTeamName":"1. FC Kaiserslautern","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/145941"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/395"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/28"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/721"}},"date":"2015-08-28T16:30:00Z","status":"TIMED","matchday":5,"homeTeamName":"1. FC Union Berlin","awayTeamName":"Red Bull Leipzig","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/148969"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/403"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/204"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/1054"}},"date":"2015-08-28T17:00:00Z","status":"TIMED","matchday":6,"homeTeamName":"Fortuna Köln","awayTeamName":"Magdeburg","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146688"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/510"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/544"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"Ajaccio AC","awayTeamName":"RC Tours","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146689"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/519"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/556"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"AJ Auxerre","awayTeamName":"Nîmes Olympique","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146690"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/1042"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/517"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"Bourg-P&eacute;ronnas","awayTeamName":"Sochaux FC","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146692"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/528"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/573"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"Dijon FCO","awayTeamName":"US Créteil","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146697"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/3
            LogUtil.log_i(LOG_TAG, "json data: " + JSON_data);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception here" + e.getMessage());
        } finally {
            if (m_connection != null) {
                m_connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream");
                }
            }
        }
        try {
            if (JSON_data != null) {
                return processLogoPathJSONdata(name, JSON_data);
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    private String processLogoPathJSONdata(String teamName, String json_data) {
        String crestUrl = null;
        try {
            JSONArray matches = new JSONObject(json_data).getJSONArray("teams");

            for (int i = 0; i < matches.length(); i++) {
                LogUtil.log_i("info", "number of matches: " + matches.length());
                JSONObject match_data = matches.getJSONObject(i);
                if (teamName.equals(match_data.getString("name"))) {
                    crestUrl = match_data.getString("crestUrl");
                }
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        LogUtil.log_i(LOG_TAG, "crest url: " + crestUrl);
        return crestUrl;
    }

    private void getData(String timeFrame) {
        //Creating fetch URL
        final String BASE_URL = "http://api.football-data.org/alpha/fixtures"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        //Log.v(LOG_TAG, fetch_build.toString()); //log spam
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod("GET");
            m_connection.addRequestProperty("X-Auth-Token", "5a4ce2e9edd843cf96354de0d8597bce");
            m_connection.connect();

            // Read the input stream into a String
            InputStream inputStream = m_connection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            JSON_data = buffer.toString();

            //json data: [{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/145935"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/395"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/17"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/46"}},"date":"2015-08-28T16:30:00Z","status":"TIMED","matchday":5,"homeTeamName":"SC Freiburg","awayTeamName":"SV Sandhausen","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/145937"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/395"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/44"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/13"}},"date":"2015-08-28T16:30:00Z","status":"TIMED","matchday":5,"homeTeamName":"1. FC Heidenheim 1846","awayTeamName":"1. FC Kaiserslautern","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/145941"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/395"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/28"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/721"}},"date":"2015-08-28T16:30:00Z","status":"TIMED","matchday":5,"homeTeamName":"1. FC Union Berlin","awayTeamName":"Red Bull Leipzig","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/148969"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/403"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/204"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/1054"}},"date":"2015-08-28T17:00:00Z","status":"TIMED","matchday":6,"homeTeamName":"Fortuna Köln","awayTeamName":"Magdeburg","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146688"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/510"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/544"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"Ajaccio AC","awayTeamName":"RC Tours","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146689"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/519"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/556"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"AJ Auxerre","awayTeamName":"Nîmes Olympique","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146690"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/1042"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/517"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"Bourg-P&eacute;ronnas","awayTeamName":"Sochaux FC","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146692"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/397"},"homeTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/528"},"awayTeam":{"href":"http:\/\/api.football-data.org\/alpha\/teams\/573"}},"date":"2015-08-28T18:00:00Z","status":"TIMED","matchday":5,"homeTeamName":"Dijon FCO","awayTeamName":"US Créteil","result":{"goalsHomeTeam":-1,"goalsAwayTeam":-1}},{"_links":{"self":{"href":"http:\/\/api.football-data.org\/alpha\/fixtures\/146697"},"soccerseason":{"href":"http:\/\/api.football-data.org\/alpha\/soccerseasons\/3
            LogUtil.log_i(LOG_TAG, "json data: " + JSON_data);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception here" + e.getMessage());
        } finally {
            if (m_connection != null) {
                m_connection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error Closing Stream");
                }
            }
        }
        try {
            if (JSON_data != null) {
                //This bit is to check if the data contains any matches. If not, we call processJson on the dummy data
                JSONArray matches = new JSONObject(JSON_data).getJSONArray("fixtures");
                if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONdata(getString(R.string.dummy_data), getApplicationContext(), false);
                    LogUtil.log_i("info", "Dummy data is being used");
                    return;
                }

                processJSONdata(JSON_data, getApplicationContext(), true);
            } else {
                //Could not Connect
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void processJSONdata(String JSONdata, Context mContext, boolean isReal) {
        //JSON data

        final String BUNDESLIGA1 = "394";
        final String BUNDESLIGA2 = "395";
        final String LIGUE1 = "396";
        final String LIGUE2 = "397";
        final String PREMIER_LEAGUE = "398";
        final String PRIMERA_DIVISION = "399";
        final String SEGUNDA_DIVISION = "400";
        final String SERIE_A = "401";
        final String PRIMEIRA_LIGA = "402";
        final String BUNDESLIGA3 = "403";
        final String EREDIVISIE = "404";
        final String CHAMPIONS_LEAGUE = "405";

        final String SEASON_LINK = "http://api.football-data.org/alpha/soccerseasons/";
        final String MATCH_LINK = "http://api.football-data.org/alpha/fixtures/";
        final String FIXTURES = "fixtures";
        final String LINKS = "_links";
        final String SOCCER_SEASON = "soccerseason";
        final String SELF = "self";
        final String MATCH_DATE = "date";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String RESULT = "result";
        final String HOME_GOALS = "goalsHomeTeam";
        final String AWAY_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";

        //Match data
        String League;
        String mDate;
        String mTime;
        String Home;
        String Away;
        String Home_goals;
        String Away_goals;
        String match_id;
        String match_day;

        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES);

            //LogUtil.log_i("info", "json data: " + matches.toString());

            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector<ContentValues>(matches.length());
            for (int i = 0; i < matches.length(); i++) {
                LogUtil.log_i("info", "number of matches: " + matches.length());
                JSONObject match_data = matches.getJSONObject(i);
                League = match_data.getJSONObject(LINKS).getJSONObject(SOCCER_SEASON).
                        getString("href");
                League = League.replace(SEASON_LINK, "");

                if (League.equals(PREMIER_LEAGUE) ||
                        League.equals(SERIE_A) ||
                        League.equals(BUNDESLIGA1) ||
                        League.equals(BUNDESLIGA2) ||
                        League.equals(PRIMERA_DIVISION) ||
                        League.equals(SEGUNDA_DIVISION) ||
                        League.equals(PRIMEIRA_LIGA) ||
                        League.equals(LIGUE1) ||
                        League.equals(LIGUE2) ||
                        League.equals(BUNDESLIGA3) ||
                        League.equals(EREDIVISIE) ||
                        League.equals(CHAMPIONS_LEAGUE)) {
                    match_id = match_data.getJSONObject(LINKS).getJSONObject(SELF).
                            getString("href");
                    match_id = match_id.replace(MATCH_LINK, "");
                    if (!isReal) {
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        match_id = match_id + Integer.toString(i);
                    }

                    mDate = match_data.getString(MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                    mDate = mDate.substring(0, mDate.indexOf("T"));
                    SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parseddate = match_date.parse(mDate + mTime);
                        SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        new_date.setTimeZone(TimeZone.getDefault());
                        mDate = new_date.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0, mDate.indexOf(":"));

                        if (!isReal) {
                            LogUtil.log_i("info", "no date data fetched, using default");
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis() + ((i - 2) * 86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                            mDate = mformat.format(fragmentdate);
                        }
                    } catch (Exception e) {
                        Log.d(LOG_TAG, "error here!");
                        Log.e(LOG_TAG, e.getMessage());
                    }
                    Home = match_data.getString(HOME_TEAM);
                    Away = match_data.getString(AWAY_TEAM);

                    String pathLogoHome = getLogoPathByTeamName("n2", League, Home);
                    LogUtil.log_i(LOG_TAG, "home: " + Home + ", away: " + Away);
                    String pathLogoAway = getLogoPathByTeamName("n2", League, Away);
                    //LogUtil.log_i(LOG_TAG, "away: " + Away + ", away: " + pathLogoAway);

                    Home_goals = match_data.getJSONObject(RESULT).getString(HOME_GOALS);
                    Log.i("info", "home goals: " + Home_goals);
                    Away_goals = match_data.getJSONObject(RESULT).getString(AWAY_GOALS);
                    Log.i("info", "away goals: " + Away_goals);
                    match_day = match_data.getString(MATCH_DAY);
                    ContentValues match_values = new ContentValues();
                    match_values.put(DatabaseContract.scores_table.MATCH_ID, match_id);
                    match_values.put(DatabaseContract.scores_table.DATE_COL, mDate);
                    LogUtil.log_i("info", "my fetch service inserting date string: " + mDate);
                    match_values.put(DatabaseContract.scores_table.TIME_COL, mTime);
                    match_values.put(DatabaseContract.scores_table.HOME_COL, Home);
                    match_values.put(DatabaseContract.scores_table.AWAY_COL, Away);
                    match_values.put(DatabaseContract.scores_table.HOME_GOALS_COL, Home_goals);
                    match_values.put(DatabaseContract.scores_table.AWAY_GOALS_COL, Away_goals);
                    match_values.put(DatabaseContract.scores_table.LEAGUE_COL, League);
                    match_values.put(DatabaseContract.scores_table.MATCH_DAY, match_day);
                    match_values.put(DatabaseContract.scores_table.CREST_PATH_HOME, pathLogoHome);
                    match_values.put(DatabaseContract.scores_table.CREST_PATH_AWAY, pathLogoAway);

                    values.add(match_values);
                }
            }

            int inserted_data = 0;
            if (values.size() > 0) {
                ContentValues[] insert_data = new ContentValues[values.size()];
                values.toArray(insert_data);
                inserted_data = mContext.getContentResolver().bulkInsert(
                        DatabaseContract.CONTENT_URI, insert_data);

                updateWidgets();
                LogUtil.log_i("info", "Widget Updating...");
            }


            Log.v(LOG_TAG, "Succesfully Inserted : " + String.valueOf(inserted_data));
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

    }

    private void updateWidgets() {
        Context context = MyApplication.applicationContext;
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}

