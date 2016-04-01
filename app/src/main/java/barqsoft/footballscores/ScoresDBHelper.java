package barqsoft.footballscores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import barqsoft.footballscores.DatabaseContract.scores_table;

/**
 * Created by yehya khaled on 2/25/2015.
 */
public class ScoresDBHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "Scores.db";
    private static final int DATABASE_VERSION = 3;

    public static final int COL_ID = 0;
    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_LEAGUE = 5;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_DETAIL_MATCH_ID = 8;
    public static final int COL_MATCHDAY = 9;
    public static final int COL_CREST_PATH_HOME = 10;
    public static final int COL_CREST_PATH_AWAY = 11;


    public ScoresDBHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String CreateScoresTable = "CREATE TABLE " + DatabaseContract.SCORES_TABLE + " ("
                + scores_table._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + scores_table.DATE_COL + " TEXT,"
                + scores_table.TIME_COL + " INTEGER,"
                + scores_table.HOME_COL + " TEXT,"
                + scores_table.AWAY_COL + " TEXT,"
                + scores_table.LEAGUE_COL + " INTEGER,"
                + scores_table.HOME_GOALS_COL + " TEXT,"
                + scores_table.AWAY_GOALS_COL + " TEXT,"
                + scores_table.MATCH_ID + " INTEGER,"
                + scores_table.MATCH_DAY + " INTEGER,"
                + scores_table.CREST_PATH_HOME + " TEXT,"
                + scores_table.CREST_PATH_AWAY + " TEXT,"
                + " UNIQUE ("+scores_table.MATCH_ID+") ON CONFLICT REPLACE"
                + ");";
        db.execSQL(CreateScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Remove old values when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.SCORES_TABLE);
    }
}
