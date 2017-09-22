package com.tan_ds.test_markdown;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.markdown4j.Markdown4jProcessor;
import java.io.IOException;
import java.util.List;

import ru.noties.markwon.Markwon;
import ru.noties.markwon.SpannableConfiguration;
import ru.noties.markwon.il.AsyncDrawableLoader;
import ru.noties.markwon.renderer.SpannableRenderer;
import ru.noties.markwon.spans.AsyncDrawable;

public class MainActivity extends FragmentActivity {

    private final static int NUM_FRAGMENTS = 2;

    private EditText myEditText;
    private String myUrl;
    private WebView myWebView;
    private TextView myTextView;
    private ViewFlipper myFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myWebView = findViewById(R.id.myWebView);
        myEditText =  findViewById(R.id.myEditText);
        myTextView = findViewById(R.id.myTextView);
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


                final Parser parser = Markwon.createParser();



                final SpannableConfiguration configuration = SpannableConfiguration.builder(getApplicationContext())
                        .asyncDrawableLoader(new AsyncDrawable.Loader(){

                            @Override
                            public void load(@NonNull String destination, @NonNull final AsyncDrawable drawable) {


                               final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        drawable.setResult(new BitmapDrawable(getResources(), bitmap));
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };

                                Picasso.with(getApplicationContext())
                                        .load(destination)
                                        .resize(50, 50)
                                        .centerCrop()
                                        .into(target);
                            }

                            @Override
                            public void cancel(@NonNull String destination) {

                            }
                        })
                        .build();


              //  final SpannableConfiguration configuration = SpannableConfiguration.create(getApplicationContext());
                final SpannableRenderer renderer = new SpannableRenderer();
                final Node node = parser.parse(data);
                final CharSequence text = renderer.render(configuration, node);

                myTextView.setMovementMethod(LinkMovementMethod.getInstance());


                Markwon.unscheduleDrawables(myTextView);
                Markwon.unscheduleTableRows(myTextView);

                myTextView.setText(text);

                Markwon.scheduleDrawables(myTextView);
                Markwon.scheduleTableRows(myTextView);


            }

            @Override
            public void onLoaderReset(Loader<String> loader) {
            }
        }
}
