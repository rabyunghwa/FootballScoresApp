package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;

import barqsoft.footballscores.util.LogUtil;
import barqsoft.footballscores.util.SvgDecoder;
import barqsoft.footballscores.util.SvgDrawableTranscoder;
import barqsoft.footballscores.util.SvgSoftwareLayerSetter;

/**
 * Created by yehya khaled on 2/26/2015.
 */
public class ScoresAdapter extends CursorAdapter
{

    public double detail_match_id = 0;

    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public ScoresAdapter(Context context, Cursor cursor, int flags)
    {
        super(context,cursor,flags);

        // use Glide library to load svg images
        // initialize Glide
        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View mItem = LayoutInflater.from(context).inflate(R.layout.scores_list_item, parent, false);
        ViewHolder mHolder = new ViewHolder(mItem);
        mItem.setTag(mHolder);
        //Log.v(FetchScoreTask.LOG_TAG,"new View inflated");
        return mItem;
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        final ViewHolder mHolder = (ViewHolder) view.getTag();
        mHolder.home_name.setText(cursor.getString(ScoresDBHelper.COL_HOME));
        mHolder.away_name.setText(cursor.getString(ScoresDBHelper.COL_AWAY));
        mHolder.date.setText(cursor.getString(ScoresDBHelper.COL_MATCHTIME));
        mHolder.score.setText(Utilities.getScores(cursor.getInt(ScoresDBHelper.COL_HOME_GOALS), cursor.getInt(ScoresDBHelper.COL_AWAY_GOALS)));
        mHolder.match_id = cursor.getDouble(ScoresDBHelper.COL_DETAIL_MATCH_ID);

        // Empty strings are an error. They are usually the sign of a programming mistake or malformed
        // response. You should use something like Guava's Strings.emptyToNull if you want to allow empty
        // strings. We expect either null indicating the absence of a URL or a well-formed URI to load

        String logoHomeUrl = mCursor.getString(ScoresDBHelper.COL_CREST_PATH_HOME);
        String logoAwayUrl = mCursor.getString(ScoresDBHelper.COL_CREST_PATH_AWAY);

        LogUtil.log_i("ScoresAdapter", "home logo: " + logoHomeUrl + ", away logo: " + logoAwayUrl);

        if (logoHomeUrl != null) {
            if (!logoHomeUrl.isEmpty()) {
                Uri uri = Uri.parse(logoHomeUrl);
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                // SVG cannot be serialized so it's not worth to cache it
                        .load(uri)
                        .into(mHolder.home_crest);
            }
        }
        if (logoAwayUrl != null) {
            if (!logoAwayUrl.isEmpty()) {
                Uri uri = Uri.parse(logoAwayUrl);
                requestBuilder
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                // SVG cannot be serialized so it's not worth to cache it
                        .load(uri)
                        .into(mHolder.away_crest);
            }
        }

        LayoutInflater vi = (LayoutInflater) context.getApplicationContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = vi.inflate(R.layout.detail_fragment, null);
        ViewGroup container = (ViewGroup) view.findViewById(R.id.details_fragment_container);
        if(mHolder.match_id == detail_match_id) {

            container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
            TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
            match_day.setText(Utilities.getMatchDay(cursor.getInt(ScoresDBHelper.COL_MATCHDAY),
                    cursor.getInt(ScoresDBHelper.COL_LEAGUE)));
            TextView league = (TextView) v.findViewById(R.id.league_textview);
            league.setText(Utilities.getLeague(cursor.getInt(ScoresDBHelper.COL_LEAGUE)));
            Button share_button = (Button) v.findViewById(R.id.share_button);
            share_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //add Share Action
                    context.startActivity(createShareForecastIntent(mHolder.home_name.getText()+" "
                    +mHolder.score.getText()+" "+mHolder.away_name.getText() + " "));
                }
            });
        } else {
            container.removeAllViews();
        }

    }
    public Intent createShareForecastIntent(String ShareText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String FOOTBALL_SCORES_HASHTAG = "#Football_Scores";
        shareIntent.putExtra(Intent.EXTRA_TEXT, ShareText + FOOTBALL_SCORES_HASHTAG);
        return shareIntent;
    }

}
