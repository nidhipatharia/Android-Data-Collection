package health.care.hhrwb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

import health.care.hhrwb.GPSService;
import health.care.hhrwb.ShowCamera;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.net.ParseException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Individual_Details_Part_4 extends Home {

	Bitmap bitmap;
	String picname;
	private Camera camera;
	private ShowCamera showcamera;
	private ImageView iv_photo_captured;
	private Button click, next;
	private RadioGroup rg_type_of_service_provider;
	private RadioButton radioTypeOfServiceProvider;
	int Type_Of_Service_Provider, Employment_Type;
	SharedPreferences pref;
	DataBaseHelperPreLoaded db;
	byte[] img = null;
	DataBaseHelper hhrwbDatabase;
	String Backup_Id;
	String fileName;
	// keep track of camera capture intent
	final int CAMERA_CAPTURE = 1;
	// captured picture uri
	private Uri picUri;
	// keep track of cropping intent
	final int PIC_CROP = 2;
	String Image_Path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_details_part_4);
		initializeControls();
																			// for
																			// private
																			// mode
		// backupId = UUID.randomUUID();
		// pref.edit().putString("Backup_Id", backupId.toString()).commit();
		Backup_Id = pref.getString("Backup_Id", "12345");

	}

	private void initializeControls() {
		iv_photo_captured = (ImageView) findViewById(R.id.imgPhoto);
		click = (Button) findViewById(R.id.btnClick);
		next = (Button) findViewById(R.id.btnNext);
		rg_type_of_service_provider = (RadioGroup) findViewById(R.id.rgTypeOfServiceProvider);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 -
		String Mode = pref.getString("Mode_Check", "Insert");
		if (Mode.equals("Update"))
			populateControls();
		click.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					// use standard intent to capture an image
					Intent captureIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					// we will handle the returned data in onActivityResult
					startActivityForResult(captureIntent, CAMERA_CAPTURE);
				} catch (ActivityNotFoundException anfe) {
					// display an error message
					String errorMessage = "Whoops - your device doesn't support capturing images!";
					Toast toast = Toast.makeText(getApplicationContext(),
							errorMessage, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent = getData();
				saveData();
				startActivity(intent);

			}
		});
	}

	/*
	 * private void insertToDatabase() { String Permanent_Id, Location_Id,
	 * First_Name, Middle_Name, Last_Name; String Father_First_Name,
	 * Father_Middle_Name, Father_Last_Name; String Mother_First_Name,
	 * Mother_Middle_Name, Mother_Last_Name; String Date_Of_Birth,
	 * Data_Entered_On; String Permanent_Address, Current_Address; int Gender,
	 * Marital_Status, Employment_Type, Type_Of_Service, Area_Type; String
	 * Mobile_Number, Alternate_Phone_Number, Email_Id, Pan, Voter_Id,
	 * Aadhar_Card, Data_Entered_By; String District_Code, Block_Code,
	 * Panchayat_Code, Municipality_Code, Municipal_Corporation_Code, sql; Blob
	 * Photo; double latitude, longitude;
	 * 
	 * hhrwbDatabase = new DataBaseHelper(getApplicationContext()); db = new
	 * DataBaseHelperPreLoaded(getApplicationContext());
	 * 
	 * First_Name = pref.getString("First_Name", "No Name"); Middle_Name =
	 * pref.getString("Middle_Name", "No Name"); Last_Name =
	 * pref.getString("Last_Name", "No Name");
	 * 
	 * Father_First_Name = pref.getString("Father_First_Name", "No Name");
	 * Father_Middle_Name = pref.getString("Father_Middle_Name", "No Name");
	 * Father_Last_Name = pref.getString("Father_Last_Name", "No Name");
	 * 
	 * Mother_First_Name = pref.getString("Mother_First_Name", "No Name");
	 * Mother_Middle_Name = pref.getString("Mother_Middle_Name", "No Name");
	 * Mother_Last_Name = pref.getString("Mother_Last_Name", "No Name");
	 * 
	 * Date_Of_Birth = pref.getString("Date_Of_Birth", "2014-01-01");
	 * Data_Entered_By = pref.getString("Data_Entered_By", "No Name");
	 * 
	 * Gender = pref.getInt("Gender", -1); Marital_Status =
	 * pref.getInt("Marital_Status", -1);
	 * 
	 * Permanent_Address = pref.getString("Permanent_Address", "No Address");
	 * Current_Address = pref.getString("Current_Address", "No Address");
	 * 
	 * Mobile_Number = pref.getString("Mobile_Number", "No mobile number");
	 * Alternate_Phone_Number = pref.getString("Alternate_Phone_Number",
	 * "No Alternate Number"); Email_Id = pref.getString("Email_Id",
	 * "No Email Id");
	 * 
	 * Pan = pref.getString("Pan_Card", "No Pan Card"); Voter_Id =
	 * pref.getString("Voter_Id", "No Voter id"); Aadhar_Card =
	 * pref.getString("Aadhar_Card", "No Aadhar Card");
	 * 
	 * Area_Type = pref.getInt("Area_Type", -1); District_Code =
	 * pref.getString("District_Code", "No Value"); Block_Code =
	 * pref.getString("Block_Code", "No Value"); Panchayat_Code =
	 * pref.getString("Panchayat_Code", "No Value"); Municipality_Code =
	 * pref.getString("Municipality_Code", "No Value");
	 * Municipal_Corporation_Code = pref.getString(
	 * "Municipal_Corporation_Code", "No Value");
	 * 
	 * Location_Id = db.getLocationId(Area_Type, District_Code, Block_Code,
	 * Panchayat_Code, Municipality_Code, Municipal_Corporation_Code);
	 * 
	 * Type_Of_Service = Type_Of_Service_Provider; Employment_Type =
	 * Employment_Type;
	 * 
	 * String address = ""; GPSService mGPSService = new
	 * GPSService(getApplicationContext()); mGPSService.getLocation();
	 * 
	 * if (mGPSService.isLocationAvailable == false) {
	 * Toast.makeText(getApplicationContext(),
	 * "Your location is not available, please try again.",
	 * Toast.LENGTH_SHORT).show(); return; } else { latitude =
	 * mGPSService.getLatitude(); longitude = mGPSService.getLongitude();
	 * address = mGPSService.getLocationAddress(); }
	 * 
	 * // make sure you close the gps after using it. Save user's battery power
	 * mGPSService.closeGPS();
	 * 
	 * ContentValues cvLocation = new ContentValues();
	 * cvLocation.put("Backup_Id", backupId.toString());
	 * cvLocation.put("Location_Id", Location_Id);
	 * cvLocation.put("Permanent_Address", Permanent_Address);
	 * cvLocation.put("Current_Address", Current_Address);
	 * cvLocation.put("Data_Entered_From_Latitude", latitude);
	 * cvLocation.put("Data_Entered_From_Longitude", longitude);
	 * cvLocation.put("Data_Entered_From_Address", address);
	 * 
	 * ContentValues cvIndividual_master = new ContentValues();
	 * 
	 * cvIndividual_master.put("Backup_Id", backupId.toString());
	 * cvIndividual_master.put("First_Name", First_Name);
	 * cvIndividual_master.put("Middle_Name", Middle_Name);
	 * cvIndividual_master.put("Last_Name", Last_Name);
	 * cvIndividual_master.put("Father_First_Name", Father_First_Name);
	 * cvIndividual_master.put("Father_Middle_Name", Father_Middle_Name);
	 * cvIndividual_master.put("Father_Last_Name", Father_Last_Name);
	 * cvIndividual_master.put("Mother_First_Name", Mother_First_Name);
	 * cvIndividual_master.put("Mother_Middle_Name", Mother_Middle_Name);
	 * cvIndividual_master.put("Mother_Last_Name", Mother_Last_Name);
	 * cvIndividual_master.put("Date_Of_Birth", Date_Of_Birth);
	 * cvIndividual_master.put("Gender", Gender);
	 * cvIndividual_master.put("Marital_Status", Marital_Status);
	 * cvIndividual_master.put("Mobile_Number", Mobile_Number);
	 * cvIndividual_master.put("Alternate_Phone_Number",
	 * Alternate_Phone_Number); cvIndividual_master.put("Email_Id", Email_Id);
	 * cvIndividual_master.put("Pan_Card", Pan);
	 * cvIndividual_master.put("Voter_Id", Voter_Id);
	 * cvIndividual_master.put("Aadhar_Card", Aadhar_Card);
	 * cvIndividual_master.put("Type_Of_Service", Type_Of_Service);
	 * cvIndividual_master.put("Employment_Type", Employment_Type);
	 * cvIndividual_master.put("Data_Entered_On", new
	 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
	 * cvIndividual_master.put("Data_Entered_By", Data_Entered_By);
	 * cvIndividual_master.put("photo", img);
	 * 
	 * try { if (hhrwbDatabase .insertValues("location_of_individual",
	 * cvLocation) != -1) { Toast.makeText(getApplicationContext(),
	 * "Successuly inserted to location of individual",
	 * Toast.LENGTH_SHORT).show(); int count =
	 * hhrwbDatabase.getCount("location_of_individual");
	 * Toast.makeText(getApplicationContext(), "Count = " + count,
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * } else Toast.makeText(getApplicationContext(), "Not Successuly inserted",
	 * Toast.LENGTH_SHORT).show(); } catch (Exception e) {
	 * Toast.makeText(getApplicationContext(), "Not Successuly inserted",
	 * Toast.LENGTH_SHORT).show(); e.printStackTrace(); } try { if
	 * (hhrwbDatabase.insertValues("individual_master", cvIndividual_master) !=
	 * -1) { Toast.makeText(getApplicationContext(),
	 * "Successuly inserted to individual master", Toast.LENGTH_SHORT).show();
	 * int count = hhrwbDatabase.getCount("individual_master");
	 * Toast.makeText(getApplicationContext(), "Count = " + count,
	 * Toast.LENGTH_SHORT).show();
	 * 
	 * } else Toast.makeText(getApplicationContext(),
	 * "Not Successuly inserted to individual master",
	 * Toast.LENGTH_SHORT).show(); } catch (Exception e) {
	 * Toast.makeText(getApplicationContext(),
	 * "Not Successuly inserted to individual master",
	 * Toast.LENGTH_SHORT).show(); e.printStackTrace(); }
	 * 
	 * }
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (data != null) {
				if (requestCode == CAMERA_CAPTURE) {
					// get the Uri for the captured image
					picUri = data.getData();
					Toast.makeText(getApplicationContext(),
							"Saved to: " + picUri, Toast.LENGTH_LONG).show();
					// carry out the crop operation
					performCrop();
				}
				// user is returning from cropping the image
				else if (requestCode == PIC_CROP) {
					// get the returned data
					Bundle extras = data.getExtras();
					if (extras != null) {
						// get the cropped bitmap
						Bitmap thePic = extras.getParcelable("data");

						// display the returned cropped image
						iv_photo_captured.setImageBitmap(thePic);
						try {
							// MediaStore.Images.Media.insertImage(getContentResolver(),
							// thePic, fileName , "Captured");
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {
							File myDirectory = new File(
									Environment.getExternalStorageDirectory(),
									"HHRWB");

							if (!myDirectory.exists()) {
								myDirectory.mkdirs();
							}

							Image_Path = myDirectory + File.separator
									+ Backup_Id + ".jpg";
							File file = new File(Image_Path);
							if (file.exists()) {
								file.delete();

							}
							FileOutputStream out = new FileOutputStream(Image_Path);
							thePic.compress(Bitmap.CompressFormat.JPEG, 90, out);

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	private void performCrop() {
		try {
			// call the standard crop action intent (the user device may not
			// support it)
			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 190);
			cropIntent.putExtra("outputY", 250);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		} catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	private Intent getData() {
		Intent intent = new Intent();

		int selectedTypeOfServiceProvider = rg_type_of_service_provider
				.getCheckedRadioButtonId();

		switch (selectedTypeOfServiceProvider) {
		case R.id.radioFHCPGov:
			// Type of Service Provider categories:
			// 1 - Formal
			// 2 - Informal
			// Employment type categories for type of service provider:
			// 1 - Government
			// 2 - Non Government
			// 0 - Informal Health Care Provider
			Type_Of_Service_Provider = 1;
			Employment_Type = 1;
			intent = new Intent(getApplicationContext(),
					Formal_Health_Care_Provider_Gov_Part_1.class);
			break;

		case R.id.radioFHCPNonGov:
			Type_Of_Service_Provider = 1;
			Employment_Type = 2;
			intent = new Intent(getApplicationContext(),
					Formal_Health_Care_Provider_Non_Gov_Part_1.class);
			break;

		case R.id.radioIFHCP:
			Type_Of_Service_Provider = 2;
			Employment_Type = 0;
			intent = new Intent(getApplicationContext(),
					Informal_Health_Care_Provider.class);
			break;
		default:
			break;
		}
		return intent;

	}

	private void populateControls() {
		Image_Path = pref.getString("Image_Path", null);
		Type_Of_Service_Provider = pref.getInt("Type_Of_Service_Provider", -1);
		Employment_Type = pref.getInt("Employment_Type_Individual", -1);
		Bitmap bmp = BitmapFactory.decodeFile(Image_Path);
		iv_photo_captured.setImageBitmap(bmp);
		if (Type_Of_Service_Provider == 1 && Employment_Type == 1) {
			rg_type_of_service_provider.check(R.id.radioFHCPGov);
		} else if (Type_Of_Service_Provider == 1 && Employment_Type == 2) {
			rg_type_of_service_provider.check(R.id.radioFHCPNonGov);
		} else if (Type_Of_Service_Provider == 2 & Employment_Type == 0) {
			rg_type_of_service_provider.check(R.id.radioIFHCP);
		}
	}

	private void saveData() {

		pref.edit().putString("Image_Path", Image_Path).commit();
		pref.edit()
				.putInt("Type_Of_Service_Provider", Type_Of_Service_Provider)
				.commit();
		pref.edit().putInt("Employment_Type_Individual", Employment_Type).commit();
		
	}
}