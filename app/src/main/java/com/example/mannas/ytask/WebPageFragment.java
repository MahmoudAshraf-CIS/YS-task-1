package com.example.mannas.ytask;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.mannas.ytask.Content.Feed;

/**
 * Created by Mannas on 6/20/2017.
 */

public class WebPageFragment extends Fragment {
    Feed f ;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            f = getArguments().getParcelable("feed");
        }else{
            f = new Feed();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_page_fragment,container,false);
        WebView myWebView = (WebView) view.findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient());

        myWebView.getSettings().setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );

        myWebView.loadUrl(f.getUrl());
        View share = view.findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = f.getUrl();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share Using ?" ) );
            }
        });

        return view;
    }

    private Boolean isOffline(){
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return info==null  || !info.isConnected();
    }
}
