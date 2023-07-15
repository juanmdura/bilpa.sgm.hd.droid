package com.bilpa.android.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class TakePickPhotoSupportFragment extends SaveSupportFragment {

    public interface OnTakePickPhotoListener {
        public void onCapturePhoto(Bitmap bitmap);
        public void onPickPhoto(Bitmap bitmap);
    }

	public static final int ACTION_PICK_PHOTO = 134;
	public static final int ACTION_TAKE_PHOTO = 135;

	private static final String tag = TakePickPhotoSupportFragment.class.getSimpleName();

	private String mCurrentPhotoPath;

	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";

    private OnTakePickPhotoListener mOnTakePickPhotoListener;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (savedInstanceState != null && savedInstanceState.containsKey("mCurrentPhotoPath")) {
			mCurrentPhotoPath = savedInstanceState.getString("mCurrentPhotoPath");
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mCurrentPhotoPath != null) {
			outState.putString("mCurrentPhotoPath", mCurrentPhotoPath);
		}
	}

	@SuppressLint("InlinedApi")
	public void dispatchPickPhotoIntent(OnTakePickPhotoListener listener) {

        mOnTakePickPhotoListener = listener;

		Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
		photoPickerIntent.setType("image/*");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
		}
		startActivityForResult(photoPickerIntent, ACTION_PICK_PHOTO);
	}

	public void dispatchTakePictureIntent(OnTakePickPhotoListener listener) {

        mOnTakePickPhotoListener = listener;

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		File f = null;

		try {
			f = setUpPhotoFile();
			mCurrentPhotoPath = f.getAbsolutePath();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
		} catch (IOException e) {
			e.printStackTrace();
			f = null;
			mCurrentPhotoPath = null;
		}

		startActivityForResult(takePictureIntent, ACTION_TAKE_PHOTO);
	}

	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		mCurrentPhotoPath = f.getAbsolutePath();

		return f;
	}

	@SuppressLint("SimpleDateFormat")
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}

	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (!storageDir.mkdirs()) {
					if (!storageDir.exists()) {
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(tag, "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}

	private String getAlbumName() {
		return "Acarreo Album";
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch (requestCode) {

		case ACTION_TAKE_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				handleCameraPhoto();
			}
			break;
		
		case ACTION_PICK_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				handlePickPhoto(data);
			} 
			break;
		}
	}
	
	private void handlePickPhoto(Intent data) {
		Uri selectedImage = data.getData();
		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();

		Bitmap bitmap = decodeFile(new File(filePath));

		int rotate = getRotation(filePath);

		if (rotate != 0 && bitmap != null) {
			bitmap = rotate(bitmap, rotate);
		}

        if (mOnTakePickPhotoListener != null) {
            mOnTakePickPhotoListener.onPickPhoto(bitmap);
            mOnTakePickPhotoListener = null;
        }

		
	}
	
	private Bitmap decodeFile(File f){
	    try {
	        //Decode image size
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	        //The new size we want to scale to
	        final int REQUIRED_SIZE=700;

	        //Find the correct scale value. It should be the power of 2.
	        int scale=1;
	        while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
	            scale*=2;

	        //Decode with inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize=scale;
	        return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	    } catch (FileNotFoundException e) {}
	    return null;
	}
	
	private void handleCameraPhoto() {

		if (mCurrentPhotoPath != null) {
			setPic();
			mCurrentPhotoPath = null;
		}
	}

	private void setPic() {
		

		/* There isn't enough memory to open up more than a couple camera photos */
		/* So pre-scale the target bitmap into which the file is decoded */

		int targetW = 800;
		int targetH = 800;

		/* Get the size of the image */
		BitmapFactory.Options bmOptions = new BitmapFactory.Options();
		bmOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		int photoW = bmOptions.outWidth;
		int photoH = bmOptions.outHeight;
		
		/* Figure out which way needs to be reduced less */
		int scaleFactor = 1;
		if ((targetW > 0) || (targetH > 0)) {
			scaleFactor = Math.min(photoW/targetW, photoH/targetH);	
		}

		/* Set bitmap options to scale the image decode target */
		bmOptions.inJustDecodeBounds = false;
		bmOptions.inSampleSize = scaleFactor;
		bmOptions.inPurgeable = true;

		/* Decode the JPEG file into a Bitmap */
		Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
		
		int rotate = getRotation(mCurrentPhotoPath);
		
		if (rotate != 0 && bitmap != null) {
			bitmap = rotate(bitmap, rotate);
		}
		
		/* set callback */
        if (mOnTakePickPhotoListener != null) {
            mOnTakePickPhotoListener.onCapturePhoto(bitmap);
            mOnTakePickPhotoListener = null;
        }
	}
	
	public static Bitmap rotate(Bitmap bitmap, int degree) {
		try {
			int w = bitmap.getWidth();
			int h = bitmap.getHeight();
			Matrix mtx = new Matrix();
			mtx.postRotate(degree);
			return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
			
		} catch (Exception e) {
			Log.e(tag, e.getMessage(), e);
			return bitmap;
		}
	}

	private int getRotation(String filePath) {
		int rotate = 0;
		try {
			ExifInterface exif = new ExifInterface(filePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_270:
				rotate -= 90;
			case ExifInterface.ORIENTATION_ROTATE_180:
				rotate -= 180;
			case ExifInterface.ORIENTATION_ROTATE_90:
				rotate = 90;
			}
		} catch (Exception e) {
			Log.e(tag, e.getMessage(), e);
		}
		return rotate;
	}


    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    public final File getAlbumStorageDir(String albumName) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return new File(
                    Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                    ),
                    albumName
            );


        } else {
            return new File (
                    Environment.getExternalStorageDirectory()
                            + CAMERA_DIR
                            + albumName
            );
        }
    }
}
