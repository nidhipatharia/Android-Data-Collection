package health.care.hhrwb;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;

import health.care.hhrwb.GPSService;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class Confirm_Details_IFHCP extends Home {

	ImageView iv_photo_captured;
	Button btnModify, btnInsertToDatabase;
	SharedPreferences pref;

	private TextView tv_first_name, tv_middle_name, tv_last_name,
			tv_father_first_name, tv_father_middle_name, tv_father_last_name,
			tv_mother_first_name, tv_mother_middle_name, tv_mother_last_name,
			tv_date_of_bith, tv_gender, tv_marital_Status, tv_location,
			tv_permanent_address, tv_current_address, tv_mobile,
			tv_alternate_phone_number, tv_email_id, tv_identification_number,
			tv_qualification, tv_practicing_since, tv_practicing_location,
			tv_prescribing_method, tv_mode_of_service, tv_vehicle;

	byte[] img = null;
	String Data_Entered_By, Backup_Id, Image_Path, First_Name, Middle_Name,
			Last_Name;
	String Father_First_Name, Father_Middle_Name, Father_Last_Name,
			Mother_First_Name, Mother_Middle_Name, Mother_Last_Name;
	String Date_Of_Birth, Location, Permanent_Address, Current_Address,
			Mobile_Number, Alternate_Phone_Number, Email_Id,
			Identification_Number;
	String Practicing_Since, Practicing_Location;
	String Qualification, Qualification_Location;
	String Prescribing_Method, Mode_Of_Service, Selected_Vehicle = "",
			Vehicle_Other;

	String District, Block, Panchayat, Municipality, Municipal_Corporation;
	String District_Code, Block_Code, Panchayat_Code, Municipality_Code, Municipal_Corporation_Code;
	String Gender, Marital_Status, Pan, Voter_Id, Aadhar_Card;

	int Area_Type, Gender_Code, Marital_Status_Code, Type_Of_Service_Provider,
			Employment_Type_Individual;
	int Practicing_Location_Code, Vehicle;
	int Qualification_Location_Code, Prescribing_Mehtod_Code,
			Mode_Of_Service_Code;
	double latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_details_ifhcp);
		initializeControls();

		btnModify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pref.edit().putString("Mode_Check", "Update").commit();
				Intent objIndent = new Intent(getApplicationContext(),
						Individual_Details_Part_1.class);

				startActivity(objIndent);
			}

		});

		btnInsertToDatabase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				int insertionFlag = insertToDatabase();
				if(insertionFlag == 4)
				{	
					AlertDialog.Builder builder = new AlertDialog.Builder(Confirm_Details_IFHCP.this);
					builder.setMessage("Data Successfully inserted to the database!")
					       .setCancelable(false)
					       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   Intent objIndent = new Intent(getApplicationContext(),
											Home.class);
									startActivity(objIndent);
					           }
					       });
					AlertDialog alert = builder.create();
					alert.show();
				}
			}

		});

	}

	public void initializeControls() {

		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		iv_photo_captured = (ImageView) findViewById(R.id.imgPhoto);
		tv_first_name = (TextView) findViewById(R.id.tvFirstName);
		tv_middle_name = (TextView) findViewById(R.id.tvMiddleName);
		tv_last_name = (TextView) findViewById(R.id.tvLastName);
		tv_father_first_name = (TextView) findViewById(R.id.tvFatherFirstName);
		tv_father_middle_name = (TextView) findViewById(R.id.tvFatherMiddleName);
		tv_father_last_name = (TextView) findViewById(R.id.tvFatherLastName);
		tv_mother_first_name = (TextView) findViewById(R.id.tvMotherFirstName);
		tv_mother_middle_name = (TextView) findViewById(R.id.tvMotherMiddleName);
		tv_mother_last_name = (TextView) findViewById(R.id.tvMotherLastName);
		tv_date_of_bith = (TextView) findViewById(R.id.tvDateOfBirthProvided);
		tv_gender = (TextView) findViewById(R.id.tvGenderSelected);
		tv_marital_Status = (TextView) findViewById(R.id.tvMaritalStatusSelected);
		tv_location = (TextView) findViewById(R.id.tvLocationSelected);
		tv_permanent_address = (TextView) findViewById(R.id.tvPermanentAddressProvided);
		tv_current_address = (TextView) findViewById(R.id.tvCurrentAddressProvided);
		tv_mobile = (TextView) findViewById(R.id.tvMobileNumberProvided);
		tv_alternate_phone_number = (TextView) findViewById(R.id.tvAlternateNumberProvided);
		tv_email_id = (TextView) findViewById(R.id.tvEmailIdProvided);
		tv_identification_number = (TextView) findViewById(R.id.tvIdentificationNumbersProvided);
		tv_qualification = (TextView) findViewById(R.id.tvQualificationSelected);
		tv_practicing_since = (TextView) findViewById(R.id.tvPracticingSinceProvided);
		tv_practicing_location = (TextView) findViewById(R.id.tvPracticingLocationProvided);
		tv_prescribing_method = (TextView) findViewById(R.id.tvPrescribingMethodSelected);
		tv_mode_of_service = (TextView) findViewById(R.id.tvModeOfServiceSelected);
		tv_vehicle = (TextView) findViewById(R.id.tvVehicleSelected);
		btnModify = (Button) findViewById(R.id.btnModify);
		btnInsertToDatabase = (Button) findViewById(R.id.btnInsertToDatabase);
		retriveFields();
	}

	private void retriveFields() {

		pref.edit().putString("Mode_Check", "Update").commit();
		Data_Entered_By = pref.getString("Data_Entered_By", "");
		Backup_Id = pref.getString("Backup_Id", "No Value");
		Image_Path = pref.getString("Image_Path", null);
		First_Name = pref.getString("First_Name", "");
		Middle_Name = pref.getString("Middle_Name", "");
		Last_Name = pref.getString("Last_Name", "");
		Father_First_Name = pref.getString("Father_First_Name", "");
		Father_Middle_Name = pref.getString("Father_Middle_Name", "");
		Father_Last_Name = pref.getString("Father_Last_Name", "");
		Mother_First_Name = pref.getString("Mother_First_Name", "");
		Mother_Middle_Name = pref.getString("Mother_Middle_Name", "");
		Mother_Last_Name = pref.getString("Mother_Last_Name", "");
		Date_Of_Birth = pref.getString("Date_Of_Birth", "");

		Gender_Code = pref.getInt("Gender", -1);

		if (Gender_Code == 1)
			Gender = this.getResources().getString(R.string.male);
		else if (Gender_Code == 2)
			Gender = this.getResources().getString(R.string.female);
		else
			Gender = this.getResources().getString(R.string.others);

		Area_Type = pref.getInt("Area_Type", -1);
		
		District = pref.getString("District", "");
		District_Code = pref.getString("District_Code", "");
		
		Block = pref.getString("Block", "");
		Block_Code = pref.getString("Block_Code", "");
		
		Panchayat = pref.getString("Panchayat", "");
		Panchayat_Code = pref.getString("Panchayat_Code", "");
		
		Municipality = pref.getString("Municipality", "");
		Municipality_Code = pref.getString("Municipality_Code", "");
		
		Municipal_Corporation = pref.getString("Municipal_Corporation", "");
		Municipal_Corporation_Code = pref.getString("Municipal_Corporation_Code", "");

		if (Area_Type == 1)
			Location = "District: " + District + " Block: " + Block
					+ " Panchayat: " + Panchayat;
		else if (Area_Type == 2)
			Location = "District: " + District + " Municipality:"
					+ Municipality;
		else if (Area_Type == 3)
			Location = "District: " + District + " Municipal Corporation: "
					+ Municipal_Corporation;

		Permanent_Address = pref.getString("Permanent_Address", "");
		Current_Address = pref.getString("Current_Address", "");

		Marital_Status_Code = pref.getInt("Marital_Status", -1);

		if (Marital_Status_Code == 1)
			Marital_Status = this.getResources().getString(R.string.single);
		else if (Marital_Status_Code == 2)
			Marital_Status = this.getResources().getString(R.string.married);
		else
			Marital_Status = this.getResources().getString(R.string.divorcee);

		Mobile_Number = pref.getString("Mobile_Number", "");
		Alternate_Phone_Number = pref.getString("Alternate_Phone_Number", "");
		Email_Id = pref.getString("Email_Id", "");
		Pan = pref.getString("Pan_Card", "");
		Voter_Id = pref.getString("Voter_Id", "");
		Aadhar_Card = pref.getString("Aadhar_Card", "");
		Identification_Number = "PAN Card: " + Pan + " Voter_Id: " + Voter_Id
				+ " Aadhar Card: " + Aadhar_Card;

		Type_Of_Service_Provider = 2;
		Employment_Type_Individual = 0;

		Practicing_Since = pref.getString("Practicing_Since", "");

		Practicing_Location_Code = pref.getInt("Practicing_Location", -1);
		if (Practicing_Location_Code == 1) {
			Practicing_Location = this.getResources().getString(R.string.urban);
		} else if (Practicing_Location_Code == 2) {
			Practicing_Location = this.getResources().getString(
					R.string.semi_urban);
		} else if (Practicing_Location_Code == 3) {
			Practicing_Location = this.getResources().getString(R.string.rural);
		}

		Prescribing_Mehtod_Code = pref.getInt("Prescribing_Method", -1);
		if (Prescribing_Mehtod_Code == 1) {
			Prescribing_Method = this.getResources().getString(
					R.string.dispense_drugs);
		} else if (Prescribing_Mehtod_Code == 2) {
			Prescribing_Method = this.getResources().getString(
					R.string.write_prescription);
		}

		Mode_Of_Service_Code = pref.getInt("Mode_Of_Service", -1);
		if (Mode_Of_Service_Code == 1) {
			Mode_Of_Service = this.getResources()
					.getString(R.string.stationary);
		} else if (Mode_Of_Service_Code == 2) {
			Mode_Of_Service = this.getResources().getString(
					R.string.mobile_mode);
		}

		Selected_Vehicle = pref.getString("Selected_Vehicle", "");
		Vehicle = pref.getInt("Vehicle", -1);
		if (Vehicle == 88) {
			Vehicle_Other = pref.getString("Vehicle_Other", "");
			Selected_Vehicle = Selected_Vehicle + Vehicle_Other;
		}

		Qualification = pref.getString("Qualification", "");
		if (Qualification.substring(0, 2).equals("88"))
			Qualification = Qualification
					+ pref.getString("Qualification_Other", "");
		Qualification_Location_Code = pref.getInt("Qualification_Location", -1);
		if (Qualification_Location_Code == 1)
			Qualification_Location = this.getResources().getString(
					R.string.west_bengal);
		else if (Qualification_Location_Code == 2)
			Qualification_Location = this.getResources().getString(
					R.string.outside_west_bengal);
		Qualification = Qualification + "-- From -- " + Qualification_Location;

		Bitmap bmp = BitmapFactory.decodeFile(Image_Path);
		iv_photo_captured.setImageBitmap(bmp);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
		img = bos.toByteArray();

		tv_first_name.setText(First_Name);
		tv_middle_name.setText(Middle_Name);
		tv_last_name.setText(Last_Name);
		tv_father_first_name.setText(Father_First_Name);
		tv_father_middle_name.setText(Father_Middle_Name);
		tv_father_last_name.setText(Father_Last_Name);
		tv_mother_first_name.setText(Mother_First_Name);
		tv_mother_middle_name.setText(Mother_Middle_Name);
		tv_mother_last_name.setText(Mother_Last_Name);
		tv_date_of_bith.setText(Date_Of_Birth);
		tv_gender.setText(Gender);
		tv_marital_Status.setText(Marital_Status);
		tv_location.setText(Location);
		tv_permanent_address.setText(Permanent_Address);
		tv_current_address.setText(Current_Address);
		tv_mobile.setText(Mobile_Number);
		tv_alternate_phone_number.setText(Alternate_Phone_Number);
		tv_email_id.setText(Email_Id);
		tv_identification_number.setText(Identification_Number);
		tv_qualification.setText(Qualification);
		tv_practicing_since.setText(Practicing_Since);
		tv_practicing_location.setText(Practicing_Location);
		tv_prescribing_method.setText(Prescribing_Method);
		tv_mode_of_service.setText(Mode_Of_Service);
		if (Selected_Vehicle.substring(0, 2).equals("88"))
			tv_vehicle.setText(Vehicle_Other);
		else
			tv_vehicle.setText(Selected_Vehicle);

	}

	private int insertToDatabase() {
		int insertionFlag = 0;
		Vehicle = Integer.parseInt(Selected_Vehicle.substring(0, 2));
		DataBaseHelper hhrwbDatabase = new DataBaseHelper(
				getApplicationContext());
		DataBaseHelperPreLoaded db = new DataBaseHelperPreLoaded(getApplicationContext());
		String Location_Id = db.getLocationId(Area_Type, District_Code, Block_Code,
				Panchayat_Code, Municipality_Code, Municipal_Corporation_Code);
		
		String TableName = "individual_master";
		ContentValues cvIndividual_master = new ContentValues();

		cvIndividual_master.put("Backup_Id", Backup_Id);
		cvIndividual_master.put("Location_Id", Location_Id);
		cvIndividual_master.put("First_Name", First_Name);
		cvIndividual_master.put("Middle_Name", Middle_Name);
		cvIndividual_master.put("Last_Name", Last_Name);
		cvIndividual_master.put("Father_First_Name", Father_First_Name);
		cvIndividual_master.put("Father_Middle_Name", Father_Middle_Name);
		cvIndividual_master.put("Father_Last_Name", Father_Last_Name);
		cvIndividual_master.put("Mother_First_Name", Mother_First_Name);
		cvIndividual_master.put("Mother_Middle_Name", Mother_Middle_Name);
		cvIndividual_master.put("Mother_Last_Name", Mother_Last_Name);
		cvIndividual_master.put("Date_Of_Birth", Date_Of_Birth);
		cvIndividual_master.put("Gender", Gender_Code);
		cvIndividual_master.put("Marital_Status", Marital_Status_Code);
		cvIndividual_master.put("Mobile_Number", Mobile_Number);
		cvIndividual_master.put("Alternate_Phone_Number",
				Alternate_Phone_Number);
		cvIndividual_master.put("Email_Id", Email_Id);
		cvIndividual_master.put("Pan_Card", Pan);
		cvIndividual_master.put("Voter_Id", Voter_Id);
		cvIndividual_master.put("Aadhar_Card", Aadhar_Card);
		cvIndividual_master.put("Type_Of_Service", Type_Of_Service_Provider);
		cvIndividual_master.put("Employment_Type", Employment_Type_Individual);
		cvIndividual_master.put("Data_Entered_On", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		cvIndividual_master.put("Data_Entered_By", Data_Entered_By);
		cvIndividual_master.put("photo", img);
		cvIndividual_master.put("Update_Status", "no");
		
		try {
			if (hhrwbDatabase.insertValues(TableName,
					cvIndividual_master) != -1) {
				Toast.makeText(getApplicationContext(),
						"Successuly inserted to individual master",
						Toast.LENGTH_LONG).show();
				int count = hhrwbDatabase.getCount("individual_master");
				Toast.makeText(getApplicationContext(), "Count = " + count,
						Toast.LENGTH_LONG).show();
				insertionFlag++;
			} else
				Toast.makeText(getApplicationContext(),
						"Not Successuly inserted to individual master",
						Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Not Successuly inserted to individual master",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		String address = "";
		
		GPSService mGPSService = new GPSService(getApplicationContext());
		mGPSService.getLocation();

		if (mGPSService.isLocationAvailable == false) {
			Toast.makeText(getApplicationContext(),
					"Your location is not available, please try again.",
					Toast.LENGTH_LONG).show();
		} else {
			latitude = mGPSService.getLatitude();
			longitude = mGPSService.getLongitude();
			address = mGPSService.getLocationAddress();
		}

		// make sure you close the gps after using it. Save user's battery power
		mGPSService.closeGPS();
		
		TableName = "location_of_individual";
		ContentValues cvLocation = new ContentValues();
		cvLocation.put("Backup_Id", Backup_Id);
		cvLocation.put("Location_Id", Location_Id);
		cvLocation.put("Permanent_Address", Permanent_Address);
		cvLocation.put("Current_Address", Current_Address);
		cvLocation.put("Data_Entered_From_Latitude", latitude);
		cvLocation.put("Data_Entered_From_Longitude", longitude);
		cvLocation.put("Data_Entered_From_Address", address);
		cvLocation.put("Update_Status", "no");
		try {
			if (hhrwbDatabase
					.insertValues(TableName, cvLocation) != -1) {
				Toast.makeText(getApplicationContext(),
						"Successuly inserted to location of individual",
						Toast.LENGTH_LONG).show();
				int count = hhrwbDatabase.getCount("location_of_individual");
				Toast.makeText(getApplicationContext(), "Count = " + count,
						Toast.LENGTH_LONG).show();
				insertionFlag++;
			} else
				Toast.makeText(getApplicationContext(),
						"Not Successuly inserted", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Not Successuly inserted",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		ContentValues cvIFHCP = new ContentValues();
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		cvIFHCP.put("Backup_Id", Backup_Id);
		cvIFHCP.put("Practicing_Since", Practicing_Since);
		cvIFHCP.put("Practicing_Location", Practicing_Location);
		cvIFHCP.put("Mode_Of_Service", Mode_Of_Service_Code);
		cvIFHCP.put("Vehicle", Vehicle);
		cvIFHCP.put("Update_Status", "no");
		if (Vehicle != 88) {
			cvIFHCP.put("Vehicle_Other", Vehicle_Other);
		}
		cvIFHCP.put("Prescribing_Method", Prescribing_Mehtod_Code);
		try {
			long rowid;
			TableName = "informal_health_care_providers";
			rowid = hhrwbDatabase.insertValues(TableName, cvIFHCP);
			if (rowid == -1) {
				Toast.makeText(getApplicationContext(),
						"Not Successuly inserted", Toast.LENGTH_LONG).show();
			} else {
				insertionFlag++;
				Toast.makeText(getApplicationContext(), "Successuly inserted",
						Toast.LENGTH_LONG).show();
			
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Not Successuly inserted",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		ContentValues cvQualification = new ContentValues();
		cvQualification.put("Backup_Id", Backup_Id);
		cvQualification.put("Qualification", Qualification);
		cvQualification.put("Qualification_Location", Qualification_Location);
		cvQualification.put("Update_Status", "no");
		try {
			TableName = "qualification";
			long rowid;
			rowid = hhrwbDatabase.insertValues(TableName, cvQualification);
			if (rowid == -1) {
				Toast.makeText(getApplicationContext(),
						"Not Successuly inserted", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "Successuly inserted",
						Toast.LENGTH_LONG).show();
				insertionFlag++;
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Not Successuly inserted",
					Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}

		return insertionFlag;
	}

}
