package health.care.hhrwb;

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

public class Retrieve_Details_FHCP extends Home {

	ImageView photo;
	Button next;
	DataBaseHelper db;
	private byte[] img = null;
	private TextView tv_first_name, tv_middle_name, tv_last_name,
			tv_father_first_name, tv_father_middle_name, tv_father_last_name,
			tv_mother_first_name, tv_mother_middle_name, tv_mother_last_name,
			tv_date_of_bith, tv_practicing_since, tv_practicing_location,tv_gender, tv_marital_Status,
			tv_location, tv_permanent_address, tv_current_address, tv_mobile,
			tv_alternate_phone_number, tv_email_id, tv_identification_number,
			tv_type_of_service_provider, tv_employment_type, tv_role,tv_type_of_service,tv_qualification,
		    tv_registration_number,	tv_registering_authority;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.retrieve_details_fhcp);
		initializeControls();
	}

	public void initializeControls() {

		db = new DataBaseHelper(getApplicationContext());
		photo = (ImageView) findViewById(R.id.imgPhoto);
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
		tv_registration_number = (TextView) findViewById(R.id.tvRegistrationNumberProvided);
		tv_registering_authority = (TextView) findViewById(R.id.tvRegisteringAuthoritySelected);
		next = (Button) findViewById(R.id.btnNext);
		retriveFields();
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent objIndent = new Intent(getApplicationContext(),
						Home.class);

				startActivity(objIndent);
			}

		});
	}

	private void retrieveImage(String Backup_Id) {
		img = db.getPhoto(Backup_Id);
		if (img == null) {
			Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
		} else {
			Bitmap b1 = BitmapFactory.decodeByteArray(img, 0, img.length);

			photo.setImageBitmap(b1);
			Toast.makeText(this, "Retrive successfully", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void retriveFields() {

		String Fist_Name = "", Middle_Name = "", Last_Name = "", Father_First_Name = "", Father_Middle_Name = "", Father_Last_Name = "";
		String Mother_First_Name = "", Mother_Middle_Name = "", Mother_Last_Name = "", Date_Of_Birth = "", Location = "", Permanent_Address = "";
		String Current_Address = "", Mobile_Number = "", Alternate_Phone_Number = "", Email_Id = "", Identification_Number = "";
		String Type_Of_Service_Provider = "", Role = "", Practicing_Since = "", Practicing_Location = "", Type_Of_Service = "";
		String Qualification = "", Qualification_Location = "", Registration_Number = "", Registering_Authority = "";
		String Registering_Authority_Other = "",Employed_In="";

		String Location_Id = "", Gender = "", Marital_Status = "", Pan = "", Voter_Id = "", Aadhar_Card = "", Employmemt_Type = "";

		String District_Code = "", Block_Code = "", Municipality_Code = "", Municipal_Corporation_Code = "";

		int Gender_Code = -1, Marital_Status_Code = -1, Type_Of_Service_Provider_Code = -1, Employment_Type_Code_Individual = -1;
		int Role_Code = -1, Employed_In_Code = -1, Employment_Type_Code = -1, Practicing_Location_Code = -1, Type_Of_Service_Code = -1;
		int Qualification_Location_Code = -1, Registering_Authority_Code = -1;
		
		DataBaseHelperPreLoaded preLoadeadDatabase = new DataBaseHelperPreLoaded(getApplicationContext());
		String TABLE_NAME;

		SharedPreferences pref = getApplicationContext().getSharedPreferences(
				"MyPref", 0);

		Employment_Type_Code_Individual = 0;
		Qualification = "";

		String Backup_Id = pref.getString("Backup_Id", "No Value");
		retrieveImage(Backup_Id);
		TABLE_NAME = "individual_master";
		Cursor cursor;
		try {
			cursor = db.FetchValuesFor(TABLE_NAME, Backup_Id);
			if (cursor.moveToFirst()) {
				do {
					Fist_Name = cursor.getString(1);
					Middle_Name = cursor.getString(2);
					Last_Name = cursor.getString(3);
					Father_First_Name = cursor.getString(4);
					Father_Middle_Name = cursor.getString(5);
					Father_Last_Name = cursor.getString(6);
					Mother_First_Name = cursor.getString(7);
					Mother_Middle_Name = cursor.getString(8);
					Mother_Last_Name = cursor.getString(9);
					Date_Of_Birth = cursor.getString(10);
					Gender_Code = cursor.getInt(11);

					if (Gender_Code == 1)
						Gender = this.getResources().getString(R.string.male);
					else if (Gender_Code == 2)
						Gender = this.getResources().getString(R.string.female);
					else
						Gender = this.getResources().getString(R.string.others);

					Marital_Status_Code = cursor.getInt(12);

					if (Marital_Status_Code == 1)
						Marital_Status = this.getResources().getString(
								R.string.single);
					else if (Marital_Status_Code == 2)
						Marital_Status = this.getResources().getString(
								R.string.married);
					else
						Marital_Status = this.getResources().getString(
								R.string.divorcee);

					Mobile_Number = cursor.getString(13);
					Alternate_Phone_Number = cursor.getString(14);
					Email_Id = cursor.getString(15);

					Pan = cursor.getString(16);
					Voter_Id = cursor.getString(17);
					Aadhar_Card = cursor.getString(18);
					Identification_Number = "PAN: " + Pan + " Voter_Id: "
							+ Voter_Id + " Aadhar_Card: " + Aadhar_Card;

					Type_Of_Service_Provider_Code = cursor.getInt(19);
					Employment_Type_Code_Individual = cursor.getInt(20);
					if (Type_Of_Service_Provider_Code == 1
							&& Employment_Type_Code_Individual == 1)
						Type_Of_Service_Provider = this.getResources().getString(
								R.string.fhcp_gov);
					else if (Type_Of_Service_Provider_Code == 1
							&& Employment_Type_Code_Individual == 2)
						Type_Of_Service_Provider = this.getResources()
								.getString(R.string.fhcp_non_gov);

				} while (cursor.moveToNext());
			}
			TABLE_NAME = "location_of_individual";
			cursor = db.FetchValuesFor(TABLE_NAME, Backup_Id);
			if (cursor.moveToFirst()) {
				do {
					Location_Id = cursor.getString(1);
					Location = preLoadeadDatabase.getLocation(Location_Id);
					Permanent_Address = cursor.getString(2);
					Current_Address = cursor.getString(3);
					if(Current_Address.equals(""))
						Current_Address = "Same as Permanent Address";

				} while (cursor.moveToNext());
			}
			cursor.close();
			if (Employment_Type_Code_Individual == 1) {
				TABLE_NAME = "employment_government";
				cursor = db.FetchValuesFor(TABLE_NAME, Backup_Id);
				if (cursor.moveToFirst()) {
					do {
						Employed_In_Code = cursor.getInt(1);
						if(Employed_In_Code == 1)
						{
							Employed_In = this.getResources().getString(R.string.central_gov);
						}
						else if(Employed_In_Code == 2)
						{
							Employed_In = this.getResources().getString(R.string.state_gov);
						}
						else if(Employed_In_Code == 3)
						{
							Employed_In = this.getResources().getString(R.string.public_sector);
						}
						Employment_Type_Code = cursor.getInt(2);
						if(Employment_Type_Code == 1)
						{
							Employmemt_Type = this.getResources().getString(R.string.permanent);
							Employmemt_Type = Employmemt_Type + " - " + Employed_In;
						}
						else if(Employment_Type_Code == 2)
						{
							Employmemt_Type = this.getResources().getString(R.string.contractual);
							Employmemt_Type = Employmemt_Type + " - " + Employed_In;
						}
						Role_Code = cursor.getInt(3);
						if(Role_Code == 1)
						{
							Role = this.getResources().getString(R.string.doctor);
						}
						else if(Role_Code == 2)
						{
							Role = this.getResources().getString(R.string.nurse);
						}
						else if(Role_Code == 3)
						{
							Role = this.getResources().getString(R.string.lab_technician);
						}
						else if(Role_Code == 4)
						{
							Role = this.getResources().getString(R.string.xray_technician);
						}
						else if(Role_Code == 5)
						{
							Role = this.getResources().getString(R.string.pharmacist);
						}
						else if(Role_Code == 6)
						{
							Role = this.getResources().getString(R.string.physiotherapist);
						}
						else if(Role_Code == 7)
						{
							Role = this.getResources().getString(R.string.other_technical_services);
						}
						else if(Role_Code == 8)
						{
							Role = this.getResources().getString(R.string.ha);
						}
						else if(Role_Code == 9)
						{
							Role = this.getResources().getString(R.string.asha);
						}
						Practicing_Since = cursor.getString(4);
						Practicing_Location_Code = cursor.getInt(5);
						if(Practicing_Location_Code == 1)
						{
							Practicing_Location = this.getResources().getString(R.string.urban);
						}
						else if(Practicing_Location_Code == 2)
						{
							Practicing_Location = this.getResources().getString(R.string.semi_urban);
						}
						else if(Practicing_Location_Code == 3)
						{
							Practicing_Location = this.getResources().getString(R.string.rural);
						}
						Type_Of_Service_Code = cursor.getInt(6);
						if(Type_Of_Service_Code == 1)
						{
							Type_Of_Service = this.getResources().getString(R.string.curative);
						}
						else if(Type_Of_Service_Code == 2)
						{
							Type_Of_Service = this.getResources().getString(R.string.public_health);
						}
						else if(Type_Of_Service_Code == 3)
						{
							Type_Of_Service = this.getResources().getString(R.string.administrative);
						}
						else if(Type_Of_Service_Code == 4)
						{
							Type_Of_Service = this.getResources().getString(R.string.educational);
						}
						Registration_Number = cursor.getString(7);
						Registering_Authority_Code = cursor.getInt(8);
						Registering_Authority_Other = cursor.getString(9);
						switch (Registering_Authority_Code) {
						case 1:
							Registering_Authority = this.getResources().getString(R.string.ra01);
							break;
						case 2:
							Registering_Authority = this.getResources().getString(R.string.ra02);
							break;
						case 3:
							Registering_Authority = this.getResources().getString(R.string.ra03);
							break;
						case 4:
							Registering_Authority = this.getResources().getString(R.string.ra04);
							break;
						case 5:
							Registering_Authority = this.getResources().getString(R.string.ra05);
							break;
						case 6:
							Registering_Authority = this.getResources().getString(R.string.ra06);
							break;
						case 7:
							Registering_Authority = this.getResources().getString(R.string.ra07);
							break;
						case 8:
							Registering_Authority = this.getResources().getString(R.string.ra08);
							break;
						case 9:
							Registering_Authority = this.getResources().getString(R.string.ra09);
							break;
						case 10:
							Registering_Authority = this.getResources().getString(R.string.ra10);
							break;
						case 11:
							Registering_Authority = this.getResources().getString(R.string.ra11);
							break;
						case 12:
							Registering_Authority = this.getResources().getString(R.string.ra12);
							break;
						case 13:
							Registering_Authority = this.getResources().getString(R.string.ra13);
							break;
						case 14:
							Registering_Authority = this.getResources().getString(R.string.ra14);
							break;
						case 88:
							Registering_Authority = this.getResources().getString(R.string.ra88) + " - " + Registering_Authority_Other;
							break;
							
						default:
							break;
						}
						
					} while (cursor.moveToNext());
				}
				cursor.close();
			} 
			else if (Employment_Type_Code_Individual == 2) {
				TABLE_NAME = "employment_non_government";
				cursor = db.FetchValuesFor(TABLE_NAME, Backup_Id);
				if (cursor.moveToFirst()) {
					do {
						Employment_Type_Code = cursor.getInt(1);
						if(Employment_Type_Code == 1)
						{
							Employmemt_Type = this.getResources().getString(R.string.private_services);
						}
						else if(Employment_Type_Code == 2)
						{
							Employmemt_Type = this.getResources().getString(R.string.self_employed);
						}
						Role_Code = cursor.getInt(2);
						if(Role_Code == 1)
						{
							Role = this.getResources().getString(R.string.doctor);
						}
						else if(Role_Code == 2)
						{
							Role = this.getResources().getString(R.string.nurse);
						}
						else if(Role_Code == 3)
						{
							Role = this.getResources().getString(R.string.lab_technician);
						}
						else if(Role_Code == 4)
						{
							Role = this.getResources().getString(R.string.xray_technician);
						}
						else if(Role_Code == 5)
						{
							Role = this.getResources().getString(R.string.pharmacist);
						}
						else if(Role_Code == 6)
						{
							Role = this.getResources().getString(R.string.physiotherapist);
						}
						else if(Role_Code == 7)
						{
							Role = this.getResources().getString(R.string.other_technical_services);
						}
						Practicing_Since = cursor.getString(3);
						Practicing_Location_Code = cursor.getInt(4);
						if(Practicing_Location_Code == 1)
						{
							Practicing_Location = this.getResources().getString(R.string.urban);
						}
						else if(Practicing_Location_Code == 2)
						{
							Practicing_Location = this.getResources().getString(R.string.semi_urban);
						}
						else if(Practicing_Location_Code == 3)
						{
							Practicing_Location = this.getResources().getString(R.string.rural);
						}
						Type_Of_Service_Code = cursor.getInt(5);
						if(Type_Of_Service_Code == 1)
						{
							Type_Of_Service = this.getResources().getString(R.string.curative);
						}
						else if(Type_Of_Service_Code == 2)
						{
							Type_Of_Service = this.getResources().getString(R.string.administrative);
						}
						else if(Type_Of_Service_Code == 3)
						{
							Type_Of_Service = this.getResources().getString(R.string.educational);
						}
						Registration_Number = cursor.getString(6);
						Registering_Authority_Code = cursor.getInt(7);
						Registering_Authority_Other = cursor.getString(8);
						switch (Registering_Authority_Code) {
						case 1:
							Registering_Authority = this.getResources().getString(R.string.ra01);
							break;
						case 2:
							Registering_Authority = this.getResources().getString(R.string.ra02);
							break;
						case 3:
							Registering_Authority = this.getResources().getString(R.string.ra03);
							break;
						case 4:
							Registering_Authority = this.getResources().getString(R.string.ra04);
							break;
						case 5:
							Registering_Authority = this.getResources().getString(R.string.ra05);
							break;
						case 6:
							Registering_Authority = this.getResources().getString(R.string.ra06);
							break;
						case 7:
							Registering_Authority = this.getResources().getString(R.string.ra07);
							break;
						case 8:
							Registering_Authority = this.getResources().getString(R.string.ra08);
							break;
						case 9:
							Registering_Authority = this.getResources().getString(R.string.ra09);
							break;
						case 10:
							Registering_Authority = this.getResources().getString(R.string.ra10);
							break;
						case 11:
							Registering_Authority = this.getResources().getString(R.string.ra11);
							break;
						case 12:
							Registering_Authority = this.getResources().getString(R.string.ra12);
							break;
						case 13:
							Registering_Authority = this.getResources().getString(R.string.ra13);
							break;
						case 14:
							Registering_Authority = this.getResources().getString(R.string.ra14);
							break;
						case 88:
							Registering_Authority = this.getResources().getString(R.string.ra88) + " - " + Registering_Authority_Other;
							break;
							
						default:
							break;
						}
					} while (cursor.moveToNext());
				}
				cursor.close();
			}
			TABLE_NAME = "qualification";
			
			String temp_qualification ="",temp_qualification_locations="";
			cursor = db.FetchValuesFor(TABLE_NAME, Backup_Id);
			if (cursor.moveToFirst()) {
				do {
					temp_qualification = cursor.getString(1);
					Qualification_Location_Code = cursor.getInt(2);

					if (Qualification_Location_Code == 1)
						temp_qualification_locations = this
								.getResources().getString(R.string.west_bengal);

					else if (Qualification_Location_Code == 2)
						temp_qualification_locations = this
								.getResources().getString(
										R.string.outside_west_bengal);
					tv_qualification.append("\n" + temp_qualification + "\n -- From -- " + temp_qualification_locations);
					
					
				} while (cursor.moveToNext());
				cursor.close();
			}
			

		} catch (Exception e) {
			// TODO Auto-generated catch block

			Toast.makeText(getApplicationContext(),
					"Could not retreive values from database",
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		tv_first_name.setText(Fist_Name);
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
		tv_type_of_service_provider.setText(Type_Of_Service_Provider);
		tv_employment_type.setText(Employmemt_Type);
		tv_role.setText(Role);
		tv_practicing_since.setText(Practicing_Since);
		tv_practicing_location.setText(Practicing_Location);
		tv_type_of_service.setText(Type_Of_Service);
		tv_registration_number.setText(Registration_Number);
		tv_registering_authority.setText(Registering_Authority);

		
	}

}
