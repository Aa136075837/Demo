package com.example.bo.nixon.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.bo.nixon.R;
import com.example.bo.nixon.utils.ConstantURL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NixonUrlActivity extends AppCompatActivity {

    @BindView (R.id.act_nixon_url_web)
    WebView mActNixonUrlWeb;
    @BindView (R.id.act_url_progress)
    ProgressBar mActUrlProgress;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_nixon_url);
        ButterKnife.bind (this);
        init();
        mActNixonUrlWeb.loadUrl (ConstantURL.NIXON_COM_URL);
    }

    private void init () {
        mActNixonUrlWeb.setWebViewClient (new WebViewClient () {
            //覆写shouldOverrideUrlLoading实现内部显示网页
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                view.loadUrl (url);
                return true;
            }
        });
        WebSettings seting = mActNixonUrlWeb.getSettings ();
        seting.setJavaScriptEnabled (true);//设置webview支持javascript脚本
        mActNixonUrlWeb.setWebChromeClient (new WebChromeClient () {
            @Override
            public void onProgressChanged (WebView view, int newProgress) {
                if (newProgress == 100) {
                    mActUrlProgress.setVisibility (View.GONE);//加载完网页进度条消失
                } else {
                    mActUrlProgress.setVisibility (View.VISIBLE);//开始加载网页时显示进度条
                    mActUrlProgress.setProgress (newProgress);//设置进度值
                }

            }
        });
    }
}
