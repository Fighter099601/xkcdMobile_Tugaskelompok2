package com.khf.xkcd.xkcdmobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.URL;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullscreenActivity extends AppCompatActivity {

    private String viewTitle;
    private String imageURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        imageURL = getIntent().getStringExtra("imageURL");
        viewTitle = getIntent().getStringExtra("imageTitle");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(viewTitle);
            actionBar.setIcon(R.drawable.cueball);
        }

        new FullscreenLoader(this).execute(imageURL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void downloadComic(View v) {
        ComicDownloader comicDownloader = new ComicDownloader();
        if(comicDownloader.isReadyForDownload(this)) {
            if(!comicDownloader.isFilePresent("/xkcdComics/" , viewTitle + ".png"))
                comicDownloader.DownloadFile(getApplicationContext(), imageURL, "xkcdComics", viewTitle, ".png");
            else
                Toast.makeText(this, "Comic already downloaded...", Toast.LENGTH_SHORT).show();
        }
    }
}

class FullscreenLoader extends AsyncTask<String, Integer, Bitmap> {

    private PhotoViewAttacher photoViewAttacher;
    private ProgressBar progressBar;
    private Activity mActivity;
    FullscreenLoader(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = (ProgressBar) mActivity.findViewById(R.id.imageLoader);
        progressBar.setMax(3);
        progressBar.setProgress(0);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        progressBar.setVisibility(View.INVISIBLE);

        ImageView imageView = (ImageView) mActivity.findViewById(R.id.fullscreen_content);
        imageView.setImageBitmap(bitmap);

        if(photoViewAttacher != null) photoViewAttacher.update();
        else photoViewAttacher = new PhotoViewAttacher(imageView, true);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        String url = params[0];
        publishProgress(1);
        Bitmap bmp = null;
        try {
            URL displayURL = new URL(url);
            bmp = BitmapFactory.decodeStream(displayURL.openConnection().getInputStream());
            publishProgress(2);
        }catch(Exception e) {
            e.printStackTrace();
        }

        return (bmp != null ? bmp : null);
    }
}