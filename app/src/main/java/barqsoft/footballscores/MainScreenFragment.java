package barqsoft.footballscores;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import barqsoft.footballscores.util.LogUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //public ScoresAdapter mAdapter;
    public ScoresCursorAdapter mAdapter;
    public static final int SCORES_LOADER = 0;
    private TextView tvNoData;
    private String[] fragmentdate = new String[1];
    private int last_selected_item = -1;
    //private ListView score_list;
    private RecyclerView score_list;

    public MainScreenFragment() {
    }

    public void setFragmentDate(String date)
    {
        fragmentdate[0] = date;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        tvNoData = (TextView) rootView.findViewById(R.id.tv_no_data);
        //score_list = (ListView) rootView.findViewById(R.id.scores_list);
        score_list = (RecyclerView) rootView.findViewById(R.id.scores_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        score_list.setLayoutManager(layoutManager);
        //mAdapter = new ScoresAdapter(getActivity(),null,0);
        mAdapter = new ScoresCursorAdapter(getActivity());
        score_list.setAdapter(mAdapter);
        getLoaderManager().initLoader(SCORES_LOADER,null,this);
        mAdapter.detail_match_id = MainActivity.selected_match_id;
//        score_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
//            {
//                ScoresCursorAdapter.ViewHolder selected = (ScoresCursorAdapter.ViewHolder) view.getTag();
//                mAdapter.detail_match_id = selected.match_id;
//                MainActivity.selected_match_id = (int) selected.match_id;
//                mAdapter.notifyDataSetChanged();
//            }
//        });
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        LogUtil.log_i("info", "MainScreenFragment onCreateLoader dateString: " + fragmentdate[0]);
        return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(),
            null, null, fragmentdate, null);
        //return new CursorLoader(getActivity(), DatabaseContract.CONTENT_URI, null, DatabaseContract.scores_table.DATE_COL, fragmentdate, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        //Log.v(FetchScoreTask.LOG_TAG,"loader finished");

        LogUtil.log_i("info", "cursor count: " + fragmentdate[0] + " " + cursor.getCount());
        LogUtil.log_i("info", "cursor id: " + cursor.toString());

        int i = 0;
        if (!cursor.moveToFirst()) {
            tvNoData.setVisibility(View.VISIBLE);
            score_list.setVisibility(View.GONE);
            return;
        }
        while (!cursor.isAfterLast()) {
            i++;
            cursor.moveToNext();
        }
        tvNoData.setVisibility(View.GONE);
        score_list.setVisibility(View.VISIBLE);
        //Log.v(FetchScoreTask.LOG_TAG,"Loader query:         " + String.valueOf(i));
        LogUtil.log_i("MainScreenFragment", "cursor is null? " + (cursor == null));
        mAdapter.swapCursor(cursor);
        //mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader)
    {
        mAdapter.swapCursor(null);
    }


}
