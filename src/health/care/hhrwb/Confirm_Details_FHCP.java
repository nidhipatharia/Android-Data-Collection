package health.care.hhrwb;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Area;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

public class Confirm_Details_FHCP extends Home {

	ImageView iv_photo_captured;
	Button btnModify, btnInsertToDatabase;
	DataBaseHelper db;
	private byte[] img = null;
	private TextView tv_first_name, tv_middle_name, tv_last_name,
			tv_father_first_name, tv_father_middle_name, tv_father_last_name,
			tv_mother_first_name, tv_mother_middle_name, tv_mother_last_name,
			tv_date_of_bith, tv_practicing_since, tv_practicing_location,
			tv_gender, tv_marital_Status, tv_location, tv_permanent_address,
			tv_current_address, tv_mobile, tv_alternate_phone_number,
			tv_email_id, tv_identification_number, tv_type_of_service_provider,
			tv_employment_type, tv_role, tv_type_of_service, tv_qualification,
			tv_qualification_location, tv_registration_number,
			tv_registering_authority;
	SharedPreferences pref;

	String Backup_Id, Data_Entered_By, Image_Path, First_Name, Middle_Name,
			Last_Name, Father_First_Name, Father_Middle_Name, Father_Last_Name;
	String Mother_First_Name, Mother_Middle_Name, Mother_Last_Name,
			Date_Of_Birth, Location, Permanent_Address;
	String Current_Address, Mobile_Number, Alternate_Phone_Number, Email_Id,
			Identification_Number;
	String Type_Of_Service_Provider, Role, Practicing_Since,
			Practicing_Location, Type_Of_Service;
	String Qualification, Qualification_Other, Qualification_Location,
			Additional_Qualifications, Additional_Qualification_Locations,
			Qualification_String, Qualification_Location_String,
			Registration_Number, Registering_Authority;
	String Registering_Authority_Other, Employment;

	String Location_Id, Gender, Marital_Status, Pan, Voter_Id, Aadhar_Card,
			Employmemt_Type;

	String District, Block, Panchayat, Municipality, Municipal_Corporation;
	String District_Code, Block_Code, Panchayat_Code, Municipality_Code,
			Municipal_Corporation_Code;

	int Area_Type, Gender_Code, Marital_Status_Code,
			Type_Of_Service_Provider_Code, Employment_Type_Code_Individual;
	int Role_Code, Employment_Code, Employment_Type_Code,
			Practicing_Location_Code, Type_Of_Service_Code;
	int Qualification_Location_Code, Registering_Authority_Code;
	double latitude, longitude;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.confirm_details_fhcp);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		initializeControls();
		btnModify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pref.edit().putString("Mode_Check", "Update").commit();
				
				Intent objIndent = new Intent(getApplicationContext(),
						Individual_Details_Part_1.class);
String Mode = pref.getString("Mode_Check", "");
				startActivity(objIndent);
			}

		});
		btnInsertToDatabase.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int insertionFlag = insertToDatabase();
				if (insertionFlag == 4) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Confirm_Details_FHCP.this);
					builder.setMessage(
							"Data Successfully inserted to the database!")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											Intent objIndent = new Intent(
													getApplicationContext(),
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
		tv_type_of_service_provider = (TextView) findViewById(R.id.tvTypeOfServiceProviderSelected);
		tv_employment_type = (TextView) findViewById(R.id.tvEmploymentSelected);
		tv_role = (TextView) findViewById(R.id.tvRoleSelected);
		tv_practicing_since = (TextView) findViewById(R.id.tvPracticingSinceProvided);
		tv_practicing_location = (TextView) findViewById(R.id.tvPracticingLocationProvided);
		tv_type_of_service = (TextView) findViewById(R.id.tvTypOfServiceSelected);
		tv_qualification = (TextView) findViewById(R.id.tvQualificationSelected);
		tv_qualification_location = (TextView) findViewById(R.id.tvQualificationLocationSelected);
		tv_registration_number = (TextView) findViewById(R.id.tvRegistrationNumberProvided);
		tv_registering_authority = (TextView) findViewById(R.id.tvRegisteringAuthoritySelected);
		btnModify = (Button) findViewById(R.id.btnModify);
		btnInsertToDatabase = (Button) findViewById(R.id.btnInsertToDatabase);
		retriveFields();

	}

	private void retriveFields() {

		DataBaseHelperPreLoaded preLoadeadDatabase = new DataBaseHelperPreLoaded(
				getApplicationContext());
		Backup_Id = pref.getString("Backup_Id", "No Value");

		Image_Path = pref.getString("Image_Path", null);
		Data_Entered_By = pref.getString("Data_Entered_By", "");
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
			Gender = getApplicationContext().getResources().getString(
					R.string.male);
		else if (Gender_Code == 2)
			Gender = getApplicationContext().getResources().getString(
					R.string.female);
		else
			Gender = getApplicationContext().getResources().getString(
					R.string.others);

		Area_Type = pref.getInt("Area_Type", -1);
		
		District_Code = pref.getString("District_Code", "");
		Block_Code = pref.getString("Block_Code", "");
		Panchayat_Code = pref.getString("Panchayat_Code", "");
		Municipality_Code = pref.getString("Municipality_Code", "");
		Municipal_Corporation_Code = pref.getString("Municipal_Corporation_Code", "");

		District = pref.getString("District", "");
		Block = pref.getString("Block", "");
		Panchayat = pref.getString("Panchayat", "");
		Municipality = pref.getString("Municipality", "");
		Municipal_Corporation = pref.getString("Municipal_Corporation", "");

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
			Marital_Status = getApplicationContext().getResources().getString(
					R.string.single);
		else if (Marital_Status_Code == 2)
			Marital_Status = getApplicationContext().getResources().getString(
					R.string.married);
		else
			Marital_Status = getApplicationContext().getResources().getString(
					R.string.divorcee);

		Mobile_Number = pref.getString("Mobile_Number", "");
		Alternate_Phone_Number = pref.getString("Alternate_Phone_Number", "");
		Email_Id = pref.getString("Email_Id", "");
		Pan = pref.getString("Pan_Card", "");
		Voter_Id = pref.getString("Voter_Id", "");
		Aadhar_Card = pref.getString("Aadhar_Card", "");
		Identification_Number = "PAN Card: " + Pan + " Voter_Id: " + Voter_Id
				+ " Aadhar Card: " + Aadhar_Card;

		Type_Of_Service_Provider_Code = pref.getInt("Type_Of_Service_Provider",
				-1);
		Employment_Type_Code_Individual = pref.getInt(
				"Employment_Type_Individual", -1);

		if (Type_Of_Service_Provider_Code == 1
				&& Employment_Type_Code_Individual == 1) {
			Type_Of_Service_Provider = getApplicationContext().getResources()
					.getString(R.string.fhcp_gov);
			Employment_Type_Code = pref.getInt("Employment_Type", -1);
			if (Employment_Type_Code == 1)
				Employmemt_Type = getApplicationContext().getResources()
						.getString(R.string.central_gov);
			else if (Employment_Type_Code == 2)
				Employmemt_Type = getApplicationContext().getResources()
						.getString(R.string.state_gov);
			else if (Employment_Type_Code == 3)
				Employmemt_Type = getApplicationContext().getResources()
						.getString(R.string.public_sector);
			Employment_Code = pref.getInt("Employment", -1);
			if (Employment_Code == 1)
				Employment = Employmemt_Type
						+ " - "
						+ getApplicationContext().getResources().getString(
								R.string.permanent);
			else if (Employment_Code == 2)
				Employment = Employmemt_Type
						+ " - "
						+ getApplicationContext().getResources().getString(
								R.string.contractual);

		} else if (Type_Of_Service_Provider_Code == 1
				&& Employment_Type_Code_Individual == 2) {
			Type_Of_Service_Provider = getApplicationContext().getResources()
					.getString(R.string.fhcp_non_gov);
			Employment_Type_Code = pref.getInt("Employment_Type", -1);
			if (Employment_Type_Code == 1)
				Employment = getApplicationContext().getResources().getString(
						R.string.private_services);
			else if (Employment_Type_Code == 2)
				Employment = getApplicationContext().getResources().getString(
						R.string.self_employed);

		}
		Role_Code = pref.getInt("Role", -1);
		switch (Role_Code) {
		case 1:
			Role = getApplicationContext().getResources().getString(
					R.string.doctor);
			break;
		case 2:
			Role = getApplicationContext().getResources().getString(
					R.string.nurse);
			break;
		case 3:
			Role = getApplicationContext().getResources().getString(
					R.string.lab_technician);
			break;
		case 4:
			Role = getApplicationContext().getResources().getString(
					R.string.xray_technician);
			break;
		case 5:
			Role = getApplicationContext().getResources().getString(
					R.string.pharmacist);
			break;
		case 6:
			Role = getApplicationContext().getResources().getString(
					R.string.physiotherapist);
			break;
		case 7:
			Role = getApplicationContext().getResources().getString(
					R.string.other_technical_services);
			break;
		case 8:
			Role = getApplicationContext().getResources()
					.getString(R.string.ha);
			break;
		case 9:
			Role = getApplicationContext().getResources().getString(
					R.string.asha);
			break;

		default:
			break;
		}
		Practicing_Since = pref.getString("Practicing_Since", "");
		Practicing_Location_Code = pref.getInt("Practicing_Location", -1);
		if (Practicing_Location_Code == 1) {
			Practicing_Location = getApplicationContext().getResources()
					.getString(R.string.urban);
		} else if (Practicing_Location_Code == 2) {
			Practicing_Location = getApplicationContext().getResources()
					.getString(R.string.semi_urban);
		} else if (Practicing_Location_Code == 3) {
			Practicing_Location = getApplicationContext().getResources()
					.getString(R.string.rural);
		}
		Type_Of_Service_Code = pref.getInt("Type_Of_Service", -1);
		if (Type_Of_Service_Code == 1) {
			Type_Of_Service = getApplicationContext().getResources().getString(
					R.string.curative);
		} else if (Type_Of_Service_Code == 2) {
			Type_Of_Service = getApplicationContext().getResources().getString(
					R.string.public_health);
		} else if (Type_Of_Service_Code == 3) {
			Type_Of_Service = getApplicationContext().getResources().getString(
					R.string.administrative);
		} else if (Type_Of_Service_Code == 4) {
			Type_Of_Service = getApplicationContext().getResources().getString(
					R.string.educational);
		}
		Registration_Number = pref.getString("Registration_Number", "");
		Registering_Authority_Code = pref.getInt("Registering_Authority", -1);
		Registering_Authority_Other = pref.getString(
				"Registering_Authority_Other", "");
		switch (Registering_Authority_Code) {
		case 1:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra01);
			break;
		case 2:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra02);
			break;
		case 3:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra03);
			break;
		case 4:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra04);
			break;
		case 5:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra05);
			break;
		case 6:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra06);
			break;
		case 7:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra07);
			break;
		case 8:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra08);
			break;
		case 9:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra09);
			break;
		case 10:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra10);
			break;
		case 11:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra11);
			break;
		case 12:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra12);
			break;
		case 13:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra13);
			break;
		case 14:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra14);
			break;
		case 88:
			Registering_Authority = getApplicationContext().getResources()
					.getString(R.string.ra88)
					+ " - "
					+ Registering_Authority_Other;
			break;

		default:
			break;
		}
		Qualification = pref.getString("Qualification", "");
		Qualification_Other = pref.getString("Qualification_Other", "");
		Additional_Qualifications = pref.getString("Additional_Qualifications",
				"");
		Qualification_String = Qualification + " " + Qualification_Other + ", "
				+ Additional_Qualifications;
		Qualification_Location_Code = pref.getInt("Qualification_Location", -1);
		if (Qualification_Location_Code == 1)
			Qualification_Location = getApplicationContext().getResources()
					.getString(R.string.west_bengal);

		else if (Qualification_Location_Code == 2)
			Qualification_Location = getApplicationContext().getResources()
					.getString(R.string.outside_west_bengal);
		Additional_Qualification_Locations = pref.getString(
				"Additional_Qualification_Locations", "");
		Qualification_Location_String = Qualification_Location + ", "
				+ Additional_Qualification_Locations;

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
		tv_gender.setText(String.valueOf(Gender));
		tv_marital_Status.setText(Marital_Status);
		tv_location.setText(Location);
		tv_permanent_address.setText(Permanent_Address);
		tv_current_address.setText(Current_Address);
		tv_mobile.setText(Mobile_Number);
		tv_alternate_phone_number.setText(Alternate_Phone_Number);
		tv_email_id.setText(Email_Id);
		tv_identification_number.setText(Identification_Number);
		tv_type_of_service_provider.setText(Type_Of_Service_Provider);
		tv_employment_type.setText(Employment);
		tv_role.setText(Role);
		tv_practicing_since.setText(Practicing_Since);
		tv_practicing_location.setText(Practicing_Location);
		tv_type_of_service.setText(Type_Of_Service);
		tv_registration_number.setText(Registration_Number);
		tv_registering_authority.setText(Registering_Authority);
		tv_qualification.setText(Qualification_String);
		tv_qualification_location.setText(Qualification_Location_String);
	}

	private int insertToDatabase() {
		int insertionFlag = 0;
		DataBaseHelper hhrwbDatabase = new DataBaseHelper(
				getApplicationContext());
		DataBaseHelperPreLoaded db = new DataBaseHelperPreLoaded(
				getApplicationContext());
		String Location_Id = db.getLocationId(Area_Type, District_Code,
				Block_Code, Panchayat_Code, Municipality_Code,
				Municipal_Corporation_Code);

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
		cvIndividual_master.put("Type_Of_Service", Type_Of_Service_Provider_Code);
		cvIndividual_master.put("Employment_Type",
				Employment_Type_Code_Individual);
		cvIndividual_master.put("Data_Entered_On", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		cvIndividual_master.put("Data_Entered_By", Data_Entered_By);
		cvIndividual_master.put("photo", img);
		cvIndividual_master.put("Update_Status","no");

		try {
			if (hhrwbDatabase.insertValues(TableName, cvIndividual_master) != -1) {
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
		cvLocation.put("Update_Status","no");

		try {
			if (hhrwbDatabase.insertValues(TableName, cvLocation) != -1) {
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
		if (Type_Of_Service_Provider_Code == 1
				&& Employment_Type_Code_Individual == 1) {
			TableName = "employment_government";
			ContentValues cvFHCP_Gov = new ContentValues();
			cvFHCP_Gov.put("Backup_Id", Backup_Id);
			cvFHCP_Gov.put("Employment_Type", Employment_Type_Code);
			cvFHCP_Gov.put("Employment", Employment);
			cvFHCP_Gov.put("Role", Role_Code);
			cvFHCP_Gov.put("Practicing_Since", Practicing_Since);
			cvFHCP_Gov.put("Practicing_Location", Practicing_Location_Code);
			cvFHCP_Gov.put("Type_Of_Service", Type_Of_Service_Code);
			cvFHCP_Gov.put("Registration_Number", Registration_Number);
			cvFHCP_Gov.put("Registering_Authority", Registering_Authority_Code);
			cvFHCP_Gov.put("Registering_Authority_Other",
					Registering_Authority_Other);
			cvFHCP_Gov.put("Update_Status","no");
			try {
				if (hhrwbDatabase.insertValues(TableName, cvFHCP_Gov) == -1)
					Toast.makeText(getApplicationContext(),
							"Failure during insertion to database",
							Toast.LENGTH_SHORT).show();
				else {
					insertionFlag++;
					Toast.makeText(getApplicationContext(),
							"Successfully inserted to database",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Failure during insertion to database",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		} else if (Type_Of_Service_Provider_Code == 1
				&& Employment_Type_Code_Individual == 2) {
			ContentValues cvFHCP_Non_Gov = new ContentValues();
			cvFHCP_Non_Gov.put("Backup_Id", Backup_Id);
			cvFHCP_Non_Gov.put("Employment_Type", Employment_Type_Code);
			cvFHCP_Non_Gov.put("Role", Role_Code);
			cvFHCP_Non_Gov.put("Practicing_Since", Practicing_Since);
			cvFHCP_Non_Gov.put("Practicing_Location", Practicing_Location_Code);
			cvFHCP_Non_Gov.put("Type_Of_Service", Type_Of_Service_Code);
			cvFHCP_Non_Gov.put("Registration_Number", Registration_Number);
			cvFHCP_Non_Gov.put("Registering_Authority", Registering_Authority_Code);
			cvFHCP_Non_Gov.put("Registering_Authority_Other",
					Registering_Authority_Other);
			cvFHCP_Non_Gov.put("Update_Status","no");
			try {
				if (hhrwbDatabase.insertValues("employment_non_government",
						cvFHCP_Non_Gov) == -1)
					Toast.makeText(getApplicationContext(),
							"Failure during insertion to database",
							Toast.LENGTH_SHORT).show();
				else {
					insertionFlag++;
					Toast.makeText(getApplicationContext(),
							"Successfully inserted to database",
							Toast.LENGTH_SHORT).show();
				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Failure during insertion to database",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}

		}
		TableName = "qualification";
		int qualificationInsertionFlag = 0,totalInsertionsToQualification = 0;
		ContentValues cvQualifications = new ContentValues();
		StringTokenizer Qualifications,Qualification_Locations;
		int TotalQualifications, TotalQualification_Locations;
		Qualifications = new StringTokenizer(Qualification_String, ",");
		Qualification_Locations = new StringTokenizer(Qualification_Location_String,",");
		
		TotalQualifications = Qualifications.countTokens();
		totalInsertionsToQualification = TotalQualifications - 1;
		TotalQualification_Locations = Qualification_Locations.countTokens();
		while (TotalQualifications - 1 > 0
				&& TotalQualification_Locations - 1 > 0) {
			
			Qualification = Qualifications.nextToken();
			Qualification_Location = Qualification_Locations.nextToken().trim().substring(0,2);
			Qualification_Location_Code = Integer.parseInt(Qualification_Location);
			cvQualifications.put("Backup_Id", Backup_Id);
			cvQualifications.put("Qualification", Qualification);
			cvQualifications.put("Qualification_Location",
					Qualification_Location_Code);
			cvQualifications.put("Update_Status","no");
			try {
				if (hhrwbDatabase.insertValues("qualification",
						cvQualifications) != -1) {
					Toast.makeText(getApplicationContext(),
							"Successuly inserted Qualification",
							Toast.LENGTH_SHORT).show();
					int count = hhrwbDatabase
							.getCount("qualification");
					Toast.makeText(getApplicationContext(),
							"Count = " + count, Toast.LENGTH_SHORT)
							.show();
					qualificationInsertionFlag++;

				} else
					Toast.makeText(getApplicationContext(),
							"Not Successuly inserted",
							Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Not Successuly inserted",
						Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			TotalQualifications--;
			TotalQualification_Locations--;
		}
		if(qualificationInsertionFlag == totalInsertionsToQualification)
			insertionFlag++;
		return insertionFlag;
	}
}
