package com.freelancer.tvprograme2;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.freelancer.tvprograme2.dto.TvProgramme;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;


public class MainActivity extends Activity implements ProgrammeRestController.OnDownloadDataCallback, EndlessScrollListener.OnScrollToLastItemListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.tv_programmes_recyclerview)
    RecyclerView mTvProgrammeRecyclerView;

    public static OkHttpClient okHttpClient;
    public static Retrofit retrofit;

    private EndlessScrollListener mEndlessScrollListener;

    //the current page starts at zero.
    private int currentPage = 0;
    //the number of items per page
    private static final int items_per_page = 10;
    //there are no items loaded yet so we start at zero.
    private int currentItemsCount = 0;
    //create the adapter.
    private TvProgrammeAdapter mAdapter = new TvProgrammeAdapter();

    //show textview when download failed
    @Bind(R.id.error_download_textview)
    protected TextView mDownloadFailedTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up everything that we need
        ButterKnife.bind(this);
        setUpRetrofit();
        setUpRecyclerView();

        //we are now ready to load the items.
        loadPage(currentPage);
    }

    private void setUpRetrofit() {

        File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
        File cacheDirectory = null;
        try {
            cacheDirectory = File.createTempFile("prefix", "extension", outputDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(cacheDirectory, cacheSize);

        okHttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new CachingControlInterceptor(getApplicationContext()))
                .cache(cache)
                .build();

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setUpRecyclerView() {
        mTvProgrammeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mEndlessScrollListener = new EndlessScrollListener(mTvProgrammeRecyclerView, this);
        mTvProgrammeRecyclerView.addOnScrollListener(mEndlessScrollListener);
        mTvProgrammeRecyclerView.setAdapter(mAdapter);
    }

    /**
     * Load the page in the recyclerview.
     *
     * @param page
     */
    private void loadPage(int page) {
        int start = page * items_per_page;
        //if the start is greater than the item count, this means that we are on the last page already.
        if(start > currentItemsCount){
            Toast.makeText(getApplicationContext(), getString(R.string.error_last_page_reached), Toast.LENGTH_SHORT).show();
        }else{
            ProgrammeRestController.getTvProgramme(getApplicationContext(), start, this);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onScrollToLastItem() {
        //the user has scrolled to the last item of the recyclerview. load the next page.
        currentPage++;
        loadPage(currentPage);
    }

    @Override
    public void onDownloadFailure() {
        mDownloadFailedTextView.setVisibility(View.VISIBLE);
        mTvProgrammeRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onDownloadSuccess(TvProgramme programme) {
        mDownloadFailedTextView.setVisibility(View.GONE);
        mTvProgrammeRecyclerView.setVisibility(View.VISIBLE);

        //null check first
        if(programme == null){
            return;
        }

        currentItemsCount = programme.getCount();

        // if we are going to load the first page of items, set the programme
        // if we are going to show the next page, append the programme items.
        if (currentPage == 0) {
            mAdapter.setTvProgrammes(programme.getResults());
        } else {
            mAdapter.appendTvProgrammes(programme.getResults());
        }
    }

    @OnClick(R.id.error_download_textview)
    public void onDownloadErrorTextViewClicked(){
        loadPage(currentPage);
    }
}
