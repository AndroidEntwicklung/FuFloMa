package com.example.fufloma;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class SellFormActivity extends Activity {

	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int SELECT_IMAGE_ACTIVITY_REQUEST_CODE = 200;
	private Uri fileUri;
	private DataStorage dataStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dataStorage = (DataStorage) getApplication();
		setContentView(R.layout.activity_sell_form);

		setupActionBar();

		// save button
		Button saveButton = (Button) findViewById(R.id.saveSellForm);
		saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (checkData()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SellFormActivity.this);
					builder.setMessage(R.string.insertAlert).setNegativeButton(
							"Abbrechen",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									dialog.cancel();
								}
							}).setPositiveButton("Eintragen", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int id) {
									saveData();
								}
							});

					builder.create().show();
				}
			}
		});	
		
		// get pic / take pic
		Button getPicButton = (Button) findViewById(R.id.getPicButton);
		getPicButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, SELECT_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});

		Button takePicButton = (Button) findViewById(R.id.takePicButton);
		takePicButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// create Intent to take a picture and return control to the
				// calling application
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

				// start the image capture Intent
				startActivityForResult(intent,
						CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
		
		// selling place
		EditText locEt = (EditText) findViewById(R.id.locationEdit);
		
		SharedPreferences sharedPref = this.getSharedPreferences(
				getString(R.string.preference_file_key), Context.MODE_PRIVATE);
		String locName = (String) sharedPref.getString("locName", "Stuttgart");
		locEt.setText(locName);
		
		// state
		Spinner spinner = (Spinner) findViewById(R.id.stateSpinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.state_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);	
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
		         ImageView imageView = (ImageView) findViewById(R.id.sellImgView);
		         imageView.setImageBitmap(BitmapFactory.decodeFile(fileUri.getPath()));
	        }
	    }
	    
	    if (requestCode == SELECT_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
	         Uri selectedImage = data.getData();
	         String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	         Cursor cursor = getContentResolver().query(selectedImage,
	                 filePathColumn, null, null, null);
	         cursor.moveToFirst();
	 
	         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	         String picturePath = cursor.getString(columnIndex);
	         cursor.close();
	                      
	         ImageView imageView = (ImageView) findViewById(R.id.sellImgView);
	         imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
	         
	         fileUri = Uri.fromFile(new File(picturePath));
	    }
	}

	/** Create a file Uri for saving an image or video */
	private Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		// File mediaStorageDir = new
		// File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
		// "FuFloMa");
		
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"FuFloMa");

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("FuFloMa", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.GERMANY).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else {
			return null;
		}

		return mediaFile;
	}

	public static String getMimeType(String url)
	{
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    return type;
	}
	
	private boolean checkData()
	{
		// image loaded?
		if (fileUri == null)
		{
			Toast.makeText(this, "Sie benötigen noch ein Foto!",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		// location?
		EditText locEt = (EditText) findViewById(R.id.locationEdit);
		String locName = locEt.getText().toString();
		
		List<Address> addresses = null;
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		
		try {
			addresses = geoCoder.getFromLocationName(locName, 2);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (addresses == null || addresses.size() == 0)
		{
			Toast.makeText(this, "Standort nicht gefunden, bitte prüfen!",
					Toast.LENGTH_SHORT).show();
			return false;			
		}

		if (addresses.size() >= 2)
		{
			Toast.makeText(this, "Standort nicht eindeutig, bitte exakter angeben (Postleitzahl)!",
					Toast.LENGTH_SHORT).show();
			return false;			
		}
		
		// state?
		Spinner spinner = (Spinner) findViewById(R.id.stateSpinner);
		String stateItem = spinner.getSelectedItem().toString();
		
		if (stateItem.isEmpty())
		{
			Toast.makeText(this, "Bitte den Zustand des Artikels angeben!",
					Toast.LENGTH_SHORT).show();
			return false;			
		}
		
		// price?
		EditText priceEt = (EditText) findViewById(R.id.priceEdit);
		String priceItem = priceEt.getText().toString();
		
		if (!isNumeric(priceItem))
		{
			Toast.makeText(this, "Bitte geben Sie eine gültige Zahl beim Preis ein!",
					Toast.LENGTH_SHORT).show();
			return false;			
		}

		// desciption?
		EditText descEt = (EditText) findViewById(R.id.descEdit);
		String descItem = descEt.getText().toString();
		
		if (descItem.isEmpty() || descItem.length() < 50) {
			Toast.makeText(this, "Bitte geben Sie eine Beschreibung mit mindestens 50 Zeichen ein!",
					Toast.LENGTH_SHORT).show();
			return false;				
		}
		
		return true;
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	private void saveData()
	{
		// encode image
		Bitmap bm = BitmapFactory.decodeFile(fileUri.getPath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
		byte[] byteArrayImage = baos.toByteArray(); 
		
		String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
		
		// build JSON
		JSONObject object_imgdata = new JSONObject();
		JSONObject object_file = new JSONObject();
		JSONObject object = new JSONObject();
		
		  try {
			  File tmpFile = new File(fileUri.getPath());
			  String fileName = tmpFile.getName();
			  String fileType = getMimeType(fileUri.getPath());
			  
			  object_imgdata.put("content_type", fileType);
			  object_imgdata.put("data", encodedImage.replace("\n", ""));
			  
			  object_file.put(fileName, object_imgdata);
			  
			  object.put("_attachments", object_file);
			  object.put("description", "Testobjekt");
		  } catch (JSONException e) {
		    e.printStackTrace();
		  }
		  
		  sendDataToDB(object);
		  finish();
	}
	
	private void sendDataToDB(JSONObject object) {
		
		dataStorage.newDocument(object);
		/*final String URL = "/volley/resource/12";
		// Post params to be sent to the server
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", "AbCdEfGh123456");
	
		JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
		       new Response.Listener<JSONObject>() {
		           @Override
		           public void onResponse(JSONObject response) {
		               try {
		                   VolleyLog.v("Response:%n %s", response.toString(4));
		               } catch (JSONException e) {
		                   e.printStackTrace();
		               }
		           }
		       }, new Response.ErrorListener() {
		           @Override
		           public void onErrorResponse(VolleyError error) {
		               VolleyLog.e("Error: ", error.getMessage());
		           }
		       });
	
		// add the request object to the queue to be executed
		ApplicationController.getInstance().addToRequestQueue(req);*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sell, menu);
		return true;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_user:
			// openSettings();
			return true;
		case R.id.action_help:
			helpDialog();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void helpDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.helpSellDialog).setPositiveButton(
				R.string.intrAlertOK, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		builder.create().show();
	}
}
