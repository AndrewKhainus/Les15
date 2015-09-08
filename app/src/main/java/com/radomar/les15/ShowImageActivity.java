package com.radomar.les15;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.radomar.les15.adapters.ImageRecyclerAdapter;
import com.radomar.les15.tasks.DownloadImageTask;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Radomar on 27.08.2015.
 *
 * onCreate: If the file is not found, it will be downloaded from the Internet
 */
public class ShowImageActivity extends Activity {

    private ImageView mImage;
    private URL mUrl;
    private Bitmap mImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        mImage = (ImageView)findViewById(R.id.ivImage_ASI);

        Intent intent = getIntent();
        File imageFile = new File(getExternalCacheDir() , intent.getStringExtra(ImageRecyclerAdapter.KEY_FILENAME));
        if (imageFile.exists()) {
            mImageBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            mImage.setImageBitmap(mImageBitmap);
        } else {
            try {
                mUrl = new URL(intent.getStringExtra(ImageRecyclerAdapter.KEY_URL));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            new DownloadImageTask() {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    mImage.setImageBitmap(bitmap);
                }
            }.execute(mUrl);
        }
    }


}
