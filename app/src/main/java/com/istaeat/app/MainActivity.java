package com.istaeat.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

public class MainActivity extends Activity {
    private static final String SITE_URL = "SITE_URL_PLACEHOLDER";
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.rgb(27, 79, 143));
        getWindow().setNavigationBarColor(Color.rgb(250, 248, 243));
        webView = new WebView(this);
        webView.setBackgroundColor(Color.rgb(250, 248, 243));
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(false);
        webView.getSettings().setSupportZoom(false);
        webView.addJavascriptInterface(new AndroidBridge(), "AndroidBridge");
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                String scheme = uri.getScheme() == null ? "" : uri.getScheme();
                String host = uri.getHost();
                if (host != null && SITE_URL.contains("://" + host)) return false;
                if ("file".equals(scheme) || "data".equals(scheme) || "about".equals(scheme)) return false;
                if ("http".equals(scheme) || "https".equals(scheme) || "mailto".equals(scheme)) {
                    try { startActivity(new Intent(Intent.ACTION_VIEW, uri)); }
                    catch (Exception ex) { Toast.makeText(MainActivity.this, "Impossible d’ouvrir ce lien.", Toast.LENGTH_SHORT).show(); }
                    return true;
                }
                return true;
            }
        });
        if (savedInstanceState != null) webView.restoreState(savedInstanceState);
        else webView.loadUrl(SITE_URL.startsWith("http") ? SITE_URL : "file:///android_asset/index.html");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (webView != null) webView.saveState(outState);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) webView.goBack();
        else super.onBackPressed();
    }

    public final class AndroidBridge {
        @JavascriptInterface
        public void saveTextFile(String name, String contents) {
            runOnUiThread(() -> {
                try {
                    File base = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                    if (base == null) base = getFilesDir();
                    if (!base.exists() && !base.mkdirs()) throw new Exception("Dossier indisponible");
                    String safeName = name == null ? "ista-eat.txt" : name.replaceAll("[^a-zA-Z0-9._-]", "_");
                    File file = new File(base, safeName);
                    try (FileOutputStream out = new FileOutputStream(file)) {
                        out.write((contents == null ? "" : contents).getBytes(StandardCharsets.UTF_8));
                    }
                    Toast.makeText(MainActivity.this, "Fichier enregistré : " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Toast.makeText(MainActivity.this, "Échec de l’enregistrement du fichier.", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
