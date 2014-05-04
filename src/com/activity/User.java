package com.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.activity.custom.CustomActivity;

/**
 * The Class Login is an Activity class that shows the login screen to users.
 * The current implementation simply start the MainActivity. You can write your
 * own logic for actual login and for login using Facebook and Twitter.
 */
public class User extends CustomActivity {

	private static final int CAMERA_REQUEST = 500;
	private ImageView imageView;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chatt.custom.CustomActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user);

		// setTouchNClick(R.id.btnLogin);
		// setTouchNClick(R.id.btnFb);
		// setTouchNClick(R.id.btnTw);
		// setTouchNClick(R.id.btnReg);
		setTouchNClick(R.id.btnRegSave);

		imageView = (ImageView) findViewById(R.id.userImage);
		Button bt = (Button) findViewById(R.id.btCam);

		bt.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				startActivityForResult(intent, CAMERA_REQUEST);

			}
		});
	}

	//@Override
	/*
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // TODO Auto-generated method stub
	    super.onActivityResult(requestCode, resultCode, data);

	    if (resultCode == RESULT_OK) {

	        switch (requestCode) {
	        case CAMERA_IMAGE_REQUEST:

	            Bitmap bitmap = null;
	            try {
	                GetImageThumbnail getImageThumbnail = new GetImageThumbnail();
	                bitmap = getImageThumbnail.getThumbnail(fileUri, this);
	            } catch (FileNotFoundException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            } catch (IOException e1) {
	                // TODO Auto-generated catch block
	                e1.printStackTrace();
	            }

	            // Setting image image icon on the imageview

	            ImageView imageView = (ImageView) this
	                    .findViewById(R.id.capturedImageview);

	            imageView.setImageBitmap(bitmap);

	            break;

	        default:
	            Toast.makeText(this, "Something went wrong...",
	                    Toast.LENGTH_SHORT).show();
	            break;
	        }

	    }
	}	
	*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			final Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case CAMERA_REQUEST:
			if (resultCode == RESULT_OK) {

				try {
					Uri selectedImage = imageReturnedIntent.getData();
					/*
					Cursor cursor = MediaStore.Images.Thumbnails
							.queryMiniThumbnails(getContentResolver(),
									selectedImage,
									MediaStore.Images.Thumbnails.MINI_KIND,
									null);
					if (cursor != null && cursor.getCount() > 0) {
						cursor.moveToFirst();// **EDIT**
						String uri = cursor
								.getString(cursor
										.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
					}
					*/

					////////
	                    InputStream imageStream = null;
	                    try {
	                        imageStream = getContentResolver().openInputStream(selectedImage);
	                    } catch (FileNotFoundException e) {
	                        // TODO Auto-generated catch block
	                        e.printStackTrace();
	                    }
	                    Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
					
					///
					// bmpSelectedImage = getThumbnail(selectedImage);
					//set_img_camera.setImageBitmap(bmpSelectedImage);
					//ImageView img = (ImageView)findViewById(R.id.);
					
					final int THUMBSIZE = 64;

					
					
					String[] filePathColumn = {MediaStore.Images.Media.DATA};
					/*
					 int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
					 String filePath = cursor.getString(columnIndex);
					 cursor.close();
					 Bitmap bMap = BitmapFactory.decodeFile(filePath);
					 */
					
					 
					 Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(filePathColumn[0]), 
			                    THUMBSIZE, THUMBSIZE);
					 
					 ImageView theImageView = (ImageView) this
			                    .findViewById(R.id.userImage);
					 
					 final int THUMBNAIL_SIZE = 140;
					 Bitmap imageBitmap = Bitmap.createScaledBitmap(yourSelectedImage, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
					 
			            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
					 
					 //theImageView.setImageBitmap(yourSelectedImage);
			            theImageView.setImageBitmap(imageBitmap);

				} catch (Exception e) {
					Log.d("image error", e.toString());
					System.out.println("Error");
				}

			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.chatt.custom.CustomActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() != R.id.btnReg) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}
}