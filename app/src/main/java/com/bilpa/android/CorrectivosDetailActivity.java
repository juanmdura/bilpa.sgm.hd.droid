package com.bilpa.android;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bilpa.android.fragments.CorrectivoDetailFragment;
import com.bilpa.android.model.correctivos.Correctivo;
import com.bilpa.android.utils.Consts;
import com.crashlytics.android.Crashlytics;
import com.mautibla.bitmaps.ImageCache;
import com.mautibla.bitmaps.ImageFetcher;

public class CorrectivosDetailActivity extends AppCompatActivity {

    private static final String IMAGE_CACHE_DIR = "imageslarge";


    public static final String TAG_CORRECTIVOS_DETAIL = "TAG_CORRECTIVO_DETAIL";

    private Correctivo mCorrectivo;
    public Toolbar toolbar;
    private ImageFetcher mImageFetcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correctivo_detail);

        mCorrectivo = (Correctivo) getIntent().getSerializableExtra("correctivo");

        Crashlytics.setString(Consts.CrashKeys.estacionNombre, mCorrectivo.estacion);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.actionbar_logo);
        setAppBarTitle(mCorrectivo.estacion);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        ViewGroup vContainer = (ViewGroup) findViewById(R.id.vContainer2);
        vContainer.removeAllViews();


        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.vContainer2, CorrectivoDetailFragment.newInstance(mCorrectivo), TAG_CORRECTIVOS_DETAIL);
            ft.addToBackStack(TAG_CORRECTIVOS_DETAIL);
            ft.commit();
        }


        ImageView vActionBarPromoImage = (ImageView) findViewById(R.id.vActionBarPromoImage);

        // Icono del sello

        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        final int height = displayMetrics.heightPixels;
        final int width = displayMetrics.widthPixels;
        final int longest = (height > width ? height : width) / 2;

        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
        mImageFetcher = new ImageFetcher(this, longest);
        mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
        mImageFetcher.setImageFadeIn(false);
        mImageFetcher.loadImage(mCorrectivo.fotoEstacion, vActionBarPromoImage);

    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAppBarTitle(int title) {
        getSupportActionBar().setTitle(title);
    }

    private void setAppBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();

        Crashlytics.setString(Consts.CrashKeys.estacionNombre, null);

    }

}
