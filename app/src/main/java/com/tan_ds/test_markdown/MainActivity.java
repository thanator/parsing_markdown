package com.tan_ds.test_markdown;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.Toast;
import org.markdown4j.Markdown4jProcessor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText myEditText;
    private String myUrl;
    private WebView myWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = (WebView) findViewById(R.id.myWebView);
        myEditText = (EditText) findViewById(R.id.myEditText);
    }

    public void Shout(View view) {
        if (!myEditText.getText().toString().isEmpty()){
                myUrl = myEditText.getText().toString();
            } else{
                myUrl = "";
                Toast toast = Toast.makeText(getApplicationContext(), "This shit is empty", Toast.LENGTH_SHORT);
                toast.show();
            }
            getSupportLoaderManager().restartLoader(999, null, new downloadCallBack());
        }

        private class downloadCallBack implements LoaderManager.LoaderCallbacks <String>{

            @Override
            public Loader<String> onCreateLoader(int id, Bundle args) {
                return new DocReader(MainActivity.this, myUrl); 
            }

            @Override
            public void onLoadFinished(Loader<String> loader, String data) {
                try {
                    String testMarkdown = new Markdown4jProcessor().process(data);
                    myWebView.loadData(testMarkdown, "text/html; charset=utf-8", "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLoaderReset(Loader<String> loader) {
            }
        }
}
