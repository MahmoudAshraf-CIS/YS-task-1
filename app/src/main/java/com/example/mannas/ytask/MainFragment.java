package com.example.mannas.ytask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.mannas.ytask.Content.Feed;
import com.example.mannas.ytask.Content.FeedsLoader;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

/**
 * Created by Mannas on 6/19/2017.
 */

public class MainFragment extends Fragment  implements LoaderManager.LoaderCallbacks<Object>,SharedPreferences.OnSharedPreferenceChangeListener{

    MainFeedsAdapter mainFeedsAdapter;
    RecyclerView main_recycler;
    AVLoadingIndicatorView loadingIndicator ;
    SwipeRefreshLayout swipeContainer;

    TextView error_msg;
    TextView offlinSign;

    boolean isTablet = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Feed> ls = new ArrayList<>();
        if(savedInstanceState!=null){
            ls = savedInstanceState.getParcelableArrayList("feedsLS");
        }else {
            reload();
        }
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

            mainFeedsAdapter = new MainFeedsAdapter(new ArrayList<Feed>(),
                    new MainFeedsAdapter.OnFeedClickListener() {
                    @Override
                    public void OnClick(Feed f) {
                        if(!isTablet){
                            Intent i = new Intent(getContext(),WebPageActivity.class);
                            //url
                            i.putExtra("feed",f);
                            getContext().startActivity(i);
                        }else {
                            //replace the fragment
                            WebPageFragment webPageFragment = new WebPageFragment();
                            Bundle b = new Bundle();
                            b.putParcelable("feed",f);
                            webPageFragment.setArguments(b);
                            getChildFragmentManager().beginTransaction()
                                    .add(R.id.right_pan,webPageFragment,webPageFragment.getClass().getName()).commit();
                    }
                }
             }
             );
        mainFeedsAdapter.changeDataSet(ls);


    }

    @Override
    public void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_main,container,false);

        error_msg = (TextView) view.findViewById(R.id.error_msg);
        loadingIndicator = (AVLoadingIndicatorView) view.findViewById(R.id.main_loding);

        isTablet = view.findViewById(R.id.right_pan)!= null;
        offlinSign = (TextView) view.findViewById(R.id.offline_sign);
        if(!isOffline()){
            offlinSign.setVisibility(View.GONE);
        }

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
                swipeContainer.setRefreshing(false);
            }
        });

        main_recycler = (RecyclerView) view.findViewById(R.id.main_feeds);
        main_recycler.setAdapter(mainFeedsAdapter);
        main_recycler.setLayoutManager(new LinearLayoutManager(getContext(),1,false));

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MaterialDialog.SingleButtonCallback onCancel =new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

            }
        };

        if(item.getItemId()== R.id.orderByAction){
            //String[] arr = getResources().getStringArray(R.array.orderByList);
            final String[] array = { "Home", "World", "National", "Politics", "Nyregion", "Business", "Opinion", "Technology", "Science"};

            final Integer sellectedIndex = PreferencesManager.getOrderBy(getContext());
            new MaterialDialog.Builder(getContext())
                    .title(R.string.orderBy)
                    .items(array)
                    .itemsCallbackSingleChoice(sellectedIndex , new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                            /**
                             * If you use alwaysCallSingleChoiceCallback(), which is discussed below,
                             * returning false here won't allow the newly selected radio button to actually be selected.
                             **/
                            dialog.setSelectedIndex(which);
                            PreferencesManager.putOrderBy(getContext(),which,text.toString());

                            dialog.dismiss();
                            return true;
                        }
                    })
                    .positiveText("Cancel")
                    .onPositive(onCancel)
                    .alwaysCallSingleChoiceCallback()
                    .show();
        }
        return true;
    }

    void reload() {
        if(loadingIndicator!=null ){
            error_msg.setVisibility(View.GONE);
            loadingIndicator.setVisibility(View.VISIBLE);
            main_recycler.setVisibility(View.GONE);
            if(isOffline())
                offlinSign.setVisibility(View.VISIBLE);
            else
                offlinSign.setVisibility(View.GONE);

        }
        getLoaderManager().restartLoader(FeedsLoader.id,null,this).forceLoad();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals( PreferencesManager.orderBy_key))
            reload();
    }

    /////////////////////// Loader callbacks

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if(id == FeedsLoader.id)
            return new FeedsLoader(getContext());
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        if(main_recycler!= null && loadingIndicator!=null){
            if(data!=null){
                loadingIndicator.setVisibility(View.GONE);

                mainFeedsAdapter.changeDataSet(((ArrayList<Feed>) data));
                main_recycler.setVisibility(View.VISIBLE);
                main_recycler.scrollToPosition(0);
            }else {
                main_recycler.setVisibility(View.GONE);
                error_msg.setVisibility(View.VISIBLE);
                loadingIndicator.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public ArrayList<Feed> getFeedsAdapterData(){
        return mainFeedsAdapter.mFeeds;
    }
    private Boolean isOffline(){
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info==null  || !info.isConnected();
    }
}
