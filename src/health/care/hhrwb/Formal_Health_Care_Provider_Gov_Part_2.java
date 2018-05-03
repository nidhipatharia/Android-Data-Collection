package health.care.hhrwb;

import java.util.Calendar;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Formal_Health_Care_Provider_Gov_Part_2 extends Home {

	Button next;
	RadioGroup rg_type_of_service;
	Spinner spn_registering_authority;
	EditText et_registration_number, et_registering_authority_other;
	int Type_Of_Service, Registering_Authority;
	String Registration_Number, Selected_Registering_Authority, Registering_Authority_Other;
	SharedPreferences pref;
	DataBaseHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formal_health_care_provider_gov_part_2);
		InitializeControls();
	}

	private void InitializeControls() {
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		rg_type_of_service = (RadioGroup) findViewById(R.id.rgTypeOfService);
		et_registration_number = (EditText) findViewById(R.id.etRegistrationNumber);
		spn_registering_authority = (Spinner) findViewById(R.id.spnRegisteringAuthority);
		et_registering_authority_other = (EditText) findViewById(R.id.etRegisteringAuthorityOther);
		next = (Button) findViewById(R.id.btnNext);
		String Mode = pref.getString("Mode_Check", "Insert");
		int Role = pref.getInt("Role", -1);
		if(Mode.equals("Update"))
			populateControls();
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData();
				boolean validationFlag = false, insertionFlag = false;
				validationFlag = validateFields();
				if (validationFlag) {
						Intent objIndent = new Intent(getApplicationContext(),
								Formal_Health_Care_Provider_Gov_Part_3.class);
						saveData();
						startActivity(objIndent);
					
				}
			}
		});
	}

	/*private boolean insertToDatabase() {
		boolean insertionFlag = false;
		int Employed_In, Employment_Type, Role, Practicing_Location;
		String Practicing_Since, Backup_Id;
		Backup_Id = pref.getString("Backup_Id", "No Value");
		Employed_In = pref.getInt("Employed_In", -1);
		Employment_Type = pref.getInt("Employment_Type", -1);
		Role = pref.getInt("Role", -1);
		Practicing_Since = pref.getString("Practicing_Since", "0000-00-00");
		Practicing_Location = pref.getInt("Practicing_Location", -1);

		ContentValues cvFHCP_Gov = new ContentValues();
		cvFHCP_Gov.put("Backup_Id", Backup_Id);
		cvFHCP_Gov.put("Employed_In", Employed_In);
		cvFHCP_Gov.put("Employment_Type", Employment_Type);
		cvFHCP_Gov.put("Role", Role);
		cvFHCP_Gov.put("Practicing_Since", Practicing_Since);
		cvFHCP_Gov.put("Practicing_Location", Practicing_Location);
		cvFHCP_Gov.put("Type_Of_Service", Type_Of_Service);
		cvFHCP_Gov.put("Registration_Number", Registration_Number);
		cvFHCP_Gov.put("Registering_Authority", Registering_Authority);
		cvFHCP_Gov.put("Registering_Authority_Other",
				Registering_Authority_Other);
		try {
			db = new DataBaseHelper(getApplicationContext());
			if (db.insertValues("employment_government", cvFHCP_Gov) == -1)
				Toast.makeText(getApplicationContext(),
						"Failure during insertion to database",
						Toast.LENGTH_SHORT).show();
			else {
				insertionFlag = true;
				Toast.makeText(getApplicationContext(),
						"Successfully inserted to database", Toast.LENGTH_SHORT)
						.show();
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Failure during insertion to database", Toast.LENGTH_SHORT)
					.show();
			e.printStackTrace();
		}
		return insertionFlag;

	}*/
	private void getData()
	{
		int selectedTypeOfService = rg_type_of_service.getCheckedRadioButtonId();
		switch (selectedTypeOfService) {
		case R.id.radioCurative:
			Type_Of_Service = 1;
			break;
		case R.id.radioPublicHealth:
			Type_Of_Service = 2;
			break;
		case R.id.radioAdministrative:
			Type_Of_Service = 3;
			break;
		case R.id.radioEducational:
			Type_Of_Service = 4;
			break;
		default:
			Type_Of_Service = 1;
			break;
		}
		Registration_Number = et_registration_number.getText().toString();
		Selected_Registering_Authority = spn_registering_authority.getSelectedItem()
				.toString();
		Registering_Authority_Other = et_registering_authority_other.getText()
				.toString();
	
		
	}
	private void populateControls()
	{
		Type_Of_Service = pref.getInt("Type_Of_Service", -1);
		Registration_Number=pref.getString("Registration_Number", "");
		Selected_Registering_Authority = pref.getString("Selected_Registering_Authority", "");
		Registering_Authority = pref.getInt("Registering_Authority", -1);
		Registering_Authority_Other=pref.getString("Registering_Authority_Other", "");
		switch (Type_Of_Service) {
		case 1:
			rg_type_of_service.check(R.id.radioCurative);
			break;
		case 2:
			rg_type_of_service.check(R.id.radioPublicHealth);
			break;
		case 3:
			rg_type_of_service.check(R.id.radioAdministrative);
			break;
		case 4:
			rg_type_of_service.check(R.id.radioEducational);
			break;

		default:
			break;
		}
		et_registration_number.setText(Registration_Number);
		for (int i = 0; i < spn_registering_authority.getAdapter()
				.getCount(); i++) {
			if (spn_registering_authority.getItemAtPosition(i).equals(
					Selected_Registering_Authority)) {
				spn_registering_authority.setSelection(i);
			}
		}
		if(Registering_Authority == 88)
			et_registering_authority_other.setText(Registering_Authority_Other);
	}
	private boolean validateFields() {
		boolean validationFlag = false;

			if (Registration_Number.equals(""))
			Toast.makeText(getApplicationContext(),
					this.getResources().getString(R.string.registration_number_blank_error),
					Toast.LENGTH_SHORT).show();
		else if (Selected_Registering_Authority.equals("--"))
			Toast.makeText(getApplicationContext(),
					this.getResources().getString(R.string.registering_authority_error),
					Toast.LENGTH_SHORT).show();
		else if (Selected_Registering_Authority.substring(0,2).equals("88")
				&& Registering_Authority_Other.equals(""))
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(R.string.registering_authority_other_blank_error),
					Toast.LENGTH_SHORT).show();
		else {
			Registering_Authority = Integer.parseInt(Selected_Registering_Authority.substring(0,2));
			validationFlag = true;
		}

		return validationFlag;
	}
	private void saveData()
	{
		pref.edit().putInt("Type_Of_Service", Type_Of_Service).commit();
		pref.edit().putString("Registration_Number", Registration_Number).commit();
		pref.edit().putString("Selected_Registering_Authority", Selected_Registering_Authority).commit();
		pref.edit().putInt("Registering_Authority", Registering_Authority).commit();
		pref.edit().putString("Registering_Authority_Other", Registering_Authority_Other).commit();
	}
}

