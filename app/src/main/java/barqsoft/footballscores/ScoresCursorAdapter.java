package barqsoft.footballscores;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
 * Created by ByungHwa on 4/1/2016.
 */
public class ScoresCursorAdapter extends RecyclerView.Adapter<ScoresCursorAdapter.ViewHolder> implements View.OnClickListener {

    Cursor mCursor;
    Context mContext;
    public double detail_match_id = 0;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    ScoresCursorAdapter(Context context) {
        mContext = context;
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
    public ScoresCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scores_list_item, parent, false);
        v.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        v.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ViewHolder mHolder = new ViewHolder(v);
        v.setTag(mHolder);
        v.setOnClickListener(this);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(final ScoresCursorAdapter.ViewHolder mHolder, int position) {
        if (!mCursor.isClosed()) {
            mCursor.moveToPosition(position);

            mHolder.home_name.setText(mCursor.getString(ScoresDBHelper.COL_HOME));
            mHolder.away_name.setText(mCursor.getString(ScoresDBHelper.COL_AWAY));
            mHolder.date.setText(mCursor.getString(ScoresDBHelper.COL_MATCHTIME));
            mHolder.score.setText(Utilities.getScores(mCursor.getInt(ScoresDBHelper.COL_HOME_GOALS), mCursor.getInt(ScoresDBHelper.COL_AWAY_GOALS)));
            mHolder.match_id = mCursor.getDouble(ScoresDBHelper.COL_DETAIL_MATCH_ID);

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
                    //Glide.with(mContext).load(logoHomeUrl).placeholder(R.drawable.image_loading).error(R.drawable.image_error).into(mHolder.home_crest);
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
                    //Glide.with(mContext).load(logoAwayUrl).into(mHolder.away_crest);
                }
            }

            LayoutInflater vi = (LayoutInflater) mContext.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.detail_fragment, null);
            ViewGroup container = (ViewGroup) mHolder.frameLayout;
            if(mHolder.match_id == detail_match_id) {

                container.addView(v, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT));
                TextView match_day = (TextView) v.findViewById(R.id.matchday_textview);
                match_day.setText(Utilities.getMatchDay(mCursor.getInt(ScoresDBHelper.COL_MATCHDAY),
                        mCursor.getInt(ScoresDBHelper.COL_LEAGUE)));
                TextView league = (TextView) v.findViewById(R.id.league_textview);
                league.setText(Utilities.getLeague(mCursor.getInt(ScoresDBHelper.COL_LEAGUE)));
                Button share_button = (Button) v.findViewById(R.id.share_button);
                share_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //add Share Action
                        mContext.startActivity(createShareForecastIntent(mHolder.home_name.getText()+" "
                                +mHolder.score.getText()+" "+mHolder.away_name.getText() + " "));
                    }
                });
            } else {
                container.removeAllViews();
            }
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

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public void onClick(View v) {
        ScoresCursorAdapter.ViewHolder selected = (ScoresCursorAdapter.ViewHolder) v.getTag();
        detail_match_id = selected.match_id;
        MainActivity.selected_match_id = (int) selected.match_id;
        notifyDataSetChanged();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView home_name;
        public TextView away_name;
        public TextView score;
        public TextView date;
        public ImageView home_crest;
        public ImageView away_crest;
        public FrameLayout frameLayout;
        public double match_id;

        public ViewHolder(View view) {
            super(view);
            home_name = (TextView) view.findViewById(R.id.home_name);
            away_name = (TextView) view.findViewById(R.id.away_name);
            score     = (TextView) view.findViewById(R.id.score_textview);
            date      = (TextView) view.findViewById(R.id.date_textview);
            home_crest = (ImageView) view.findViewById(R.id.home_crest);
            away_crest = (ImageView) view.findViewById(R.id.away_crest);
            frameLayout = (FrameLayout) view.findViewById(R.id.details_fragment_container);
        }
    }

    public void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }
}
