package com.khf.xkcd.xkcdmobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private String baseUrl;
    private String currentUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseUrl = "http://xkcd.org";
        currentUrl = "/";

        findViewById(R.id.parent).setOnTouchListener(new SwipeComicHandler(getApplicationContext(), this));
        new NewComicLoader(this).execute(baseUrl, currentUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            currentUrl = findViewById(R.id.comicImage).getContentDescription().toString();
            sendIntent.putExtra(Intent.EXTRA_TEXT, baseUrl + currentUrl);
            startActivity(Intent.createChooser(sendIntent, "Share comic through"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void prevComic(View v) {
        Button tmp = (Button) v;
        if(tmp.getContentDescription() != null) {
            String prevUrl = tmp.getContentDescription().toString();
            new NewComicLoader(this).execute(baseUrl, prevUrl);
        }
    }

    public void nextComic(View v) {
        Button tmp = (Button) v;
        if(tmp.getContentDescription() != null) {
            String nextUrl = tmp.getContentDescription().toString();
            new NewComicLoader(this).execute(baseUrl, nextUrl);
        }
    }

    public void goFullscreen(View v) {
        Intent fullscreenIntent = new Intent(this, FullscreenActivity.class);
        currentUrl = findViewById(R.id.comicTitle).getContentDescription().toString();
        String title = ((TextView) findViewById(R.id.comicTitle)).getText().toString();
	    String alt = findViewById(R.id.progressBar).getContentDescription ().toString ();
        Toast.makeText(this, title, Toast.LENGTH_LONG).show();
        fullscreenIntent.putExtra("imageURL", currentUrl);
        fullscreenIntent.putExtra("imageTitle", title);
	    fullscreenIntent.putExtra ("imageAlt", alt);

        startActivity(fullscreenIntent);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }


    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) { return false; }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) { return false; }

    @Override
    public void onLongPress(MotionEvent e) {  }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        Toast.makeText(this, "Fling", Toast.LENGTH_SHORT).show();
        float xDiff = e1.getX() - e2.getX();
        float yDiff = e1.getY() - e2.getY();

        if(Math.abs(xDiff) > Math.abs(yDiff) && Math.abs(xDiff) > 100)
            if (xDiff < 0) prevComic(findViewById(R.id.prevButton));
            else nextComic(findViewById(R.id.nextButton));

        return true;
    }
}

class NewComicLoader extends AsyncTask<String, Integer, XKCDComic> {

    private Activity mActivity;

    NewComicLoader(Activity activity) {
        mActivity = activity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(5);
    }

    @Override
    protected void onPostExecute(XKCDComic xkcdComic) {
        super.onPostExecute (xkcdComic);

        ProgressBar mProgressBar = (ProgressBar) mActivity.findViewById(R.id.progressBar);
        mProgressBar.setVisibility (View.INVISIBLE);

        TextView textView = (TextView) mActivity.findViewById(R.id.comicTitle);
        ImageView imageView = (ImageView) mActivity.findViewById(R.id.comicImage);

        if(xkcdComic != null) {
            textView.setText(xkcdComic.getTitle());
            textView.setTextColor (Color.WHITE);
            textView.setContentDescription (xkcdComic.getImageURL ());

            imageView.setImageBitmap (xkcdComic.getImage ());
            imageView.setContentDescription (xkcdComic.getCurrentURL ());

            mActivity.findViewById(R.id.prevButton).setContentDescription (xkcdComic.getPrevURL ());
            mActivity.findViewById(R.id.nextButton).setContentDescription (xkcdComic.getNextURL ());

            mProgressBar.setContentDescription (xkcdComic.getAltText ());
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        ProgressBar mProgressBar = (ProgressBar) mActivity.findViewById(R.id.progressBar);
        mProgressBar.setProgress(values[0]);
    }

    @Override
    protected XKCDComic doInBackground(String... params){
        Document doc;
        String imgURL;
        XKCDComic xkcdComic = new XKCDComic();
        try {
            doc = Jsoup.connect(params[0] + params[1]).get();

            Element mainDiv = doc.getElementById("comic");
            Element mainImg = mainDiv.getElementsByTag("img").first();
            imgURL = mainImg.absUrl("src");
            xkcdComic.setTitle(mainImg.attr("alt"));
            xkcdComic.setAltText (mainImg.attr("title"));
            publishProgress(1);

            URL url = new URL(imgURL);
            xkcdComic.setImageURL(imgURL);
            Bitmap comicImg = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            xkcdComic.setImage(comicImg);
            publishProgress(2);

            Element navDiv = doc.getElementsByClass("comicNav").first();
            String prevUrl = navDiv.child(1).getElementsByTag("a").first().attr("href");
            xkcdComic.setPrevURL(prevUrl);
            publishProgress(3);
            String nextUrl = navDiv.child(3).getElementsByTag("a").first().attr("href");
            xkcdComic.setNextURL(nextUrl);
            publishProgress(4);

            xkcdComic.setCurrentURL(params[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return xkcdComic;
    }
}