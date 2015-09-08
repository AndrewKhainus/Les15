package com.radomar.les15.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.radomar.les15.R;
import com.radomar.les15.ShowImageActivity;
import com.radomar.les15.other.Scaler;
import com.radomar.les15.tasks.DownloadImageTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Radomar on 26.08.2015.
 */
public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.CustomViewHolder> {

    public static final String KEY_FILENAME = "fileName";
    public static final String KEY_URL = "url";

    private Activity mActivity;
    private ArrayList<String> mData;
    private Scaler mScaler;

    public ImageRecyclerAdapter(Activity activity, ArrayList<String> mData) {
        this.mActivity = activity;
        this.mData = mData;
        mScaler = Scaler.getInstance(activity);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mActivity.getApplicationContext()).inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.onBind();
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;

        public CustomViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView)itemView.findViewById(R.id.ivImage_LI);
            imageView.getLayoutParams().height = mScaler.getLayoutSize();
            imageView.getLayoutParams().width = mScaler.getLayoutSize();
        }

        /**
          read from file if it cached. Or download it by URL if file not exist and save to sdCard.
         */
        public void onBind() {
            String sUrl = null;
            URL url = null;
            try {
                sUrl = mData.get(getAdapterPosition());
                url = new URL(sUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            final String fileName = sUrl.substring(sUrl.lastIndexOf('/') + 1);
            final File imageFile = new File(mActivity.getExternalCacheDir(),"thumb " + fileName);

            if (imageFile.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
            } else {
                new DownloadImageTask() {
                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        saveImage(mScaler.resize(bitmap), fileName);
                        saveImage(mScaler.generateThumbnail(bitmap), "thumb " + fileName);
                    }
                }.execute(url);
            }
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(mActivity, ShowImageActivity.class);
            String url = mData.get(getAdapterPosition());
            String fileName = url.substring(url.lastIndexOf('/') + 1);
            myIntent.putExtra(KEY_FILENAME, fileName);
            myIntent.putExtra(KEY_URL, url);
            mActivity.startActivity(myIntent);
        }

        /**
         save image to sdCard (ExternalCacheDir)
         */
        private void saveImage(Bitmap bmImg, String fileName) {
            if (getFreeSpaceMb() < 30) {
                return;
            }
            File ImageFile;
            try{
                ImageFile = new File(mActivity.getExternalCacheDir(), fileName);

                FileOutputStream out = new FileOutputStream(ImageFile);
                bmImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();

            } catch (IOException e) {
            }
        }

        /**
         * this method return free space on SDcard in MByte
         */
        private long getFreeSpaceMb() {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
            return bytesAvailable / 1048576;
        }

    }
}
