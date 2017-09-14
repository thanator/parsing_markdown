package com.tan_ds.test_markdown;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.markdown4j.Markdown4jProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private EditText myEditText;
    private String myTextHTML, MyUrl;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = (WebView) findViewById(R.id.myWebView);
        myEditText = (EditText) findViewById(R.id.myEditText);
    }


    public void Shout(View view) {

        Context tContext = getApplicationContext();

        if (!myEditText.getText().toString().isEmpty()){
            MyUrl = myEditText.getText().toString();
        } else{
            MyUrl = "";
            Toast toast = Toast.makeText(getApplicationContext(), "This shit is empty", Toast.LENGTH_SHORT);
            toast.show();
        }

        getSupportLoaderManager().initLoader(999, null, new downloadCallBack());

        }


        private class downloadCallBack implements LoaderManager.LoaderCallbacks <String>{

            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new docReader(MainActivity.this, MyUrl);  // POKA KOSTIL    https://vk.com/doc48817927_450398490
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {

                try {
                    String test_Markdown = new Markdown4jProcessor().process(data);
                    myWebView.loadData(test_Markdown, "text/html; charset=utf-8", "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLoaderReset(Loader<String> loader) {

            }
        }


}
