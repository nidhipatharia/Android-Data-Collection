package health.care.hhrwb;

import java.util.Calendar;
import java.util.StringTokenizer;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.CommaTokenizer;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Formal_Health_Care_Provider_Gov_Part_3 extends Home {

	private MultiAutoCompleteTextView mt_additional_qualification,
			mt_addtional_qualification_location;
	private RadioGroup rg_qualification_location;
	private TextView tv_qualification, tv_additionalQualification;
	private Spinner spn_qualification;
	private EditText et_qualification_other;
	String additional_qualification_location[] = { "01: West Bengal",
			"02: Outside West Bengal" };
	String Additional_Qualifications, Additional_Qualification_Locations;
	StringTokenizer Additional_Qualification,
			Additional_Qualification_Location;
	int TotalQualifications, TotalQualification_Locations;
	int Qualification_Location;
	DataBaseHelper hhrwbDatabase;
	Button next;
	SharedPreferences pref;
	String Qualification,Qualification_Other;

	ArrayAdapter<String> qualification_list, additional_qualification_list,
			additional_qualification_location_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formal_health_care_provider_gov_part_3);

		tv_qualification = (TextView) findViewById(R.id.tvQualification);
		tv_additionalQualification = (TextView) findViewById(R.id.tvAdditionalQualification);

		mt_additional_qualification = (MultiAutoCompleteTextView) findViewById(R.id.mtAdditionalQualification);
		mt_addtional_qualification_location = (MultiAutoCompleteTextView) findViewById(R.id.mtAdditionalQualificationLocation);

		spn_qualification = (Spinner) findViewById(R.id.spnQualification);
		et_qualification_other = (EditText) findViewById(R.id.etQualificationOther);
		rg_qualification_location = (RadioGroup) findViewById(R.id.rgQualificationLocation);
		et_qualification_other = (EditText) findViewById(R.id.etQualificationOther);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 -
		// for
		// private
		// mode
		String Mode = pref.getString("Mode_Check", "Insert");
		int roleCode = pref.getInt("Role", -1);
		switch (roleCode) {
		case 1:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_doctor));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_doctor));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			additional_qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getResources()
							.getStringArray(
									R.array.additional_qualification_doctor));
			mt_additional_qualification
					.setAdapter(additional_qualification_list);
			mt_additional_qualification.setThreshold(1);
			mt_additional_qualification
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			additional_qualification_location_list = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1,
					additional_qualification_location);
			mt_addtional_qualification_location
					.setAdapter(additional_qualification_location_list);
			mt_addtional_qualification_location.setThreshold(1);
			mt_addtional_qualification_location
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			break;
		case 2:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_nurse));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_nurse));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			mt_additional_qualification.setHint(R.string.NA);
			mt_additional_qualification.setEnabled(false);
			mt_addtional_qualification_location.setHint(R.string.NA);
			mt_addtional_qualification_location.setEnabled(false);

			break;
		case 3:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_lab_technician));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_technician));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			additional_qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					getResources().getStringArray(
							R.array.additional_qualification_technician));
			mt_additional_qualification
					.setAdapter(additional_qualification_list);
			mt_additional_qualification.setThreshold(1);
			mt_additional_qualification
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			additional_qualification_location_list = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1,
					additional_qualification_location);
			mt_addtional_qualification_location
					.setAdapter(additional_qualification_location_list);
			mt_addtional_qualification_location.setThreshold(1);
			mt_addtional_qualification_location
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			break;
		case 4:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_xray_technician));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_technician));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			additional_qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					getResources().getStringArray(
							R.array.additional_qualification_technician));
			mt_additional_qualification
					.setAdapter(additional_qualification_list);
			mt_additional_qualification.setThreshold(1);
			mt_additional_qualification
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			additional_qualification_location_list = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1,
					additional_qualification_location);
			mt_addtional_qualification_location
					.setAdapter(additional_qualification_location_list);
			mt_addtional_qualification_location.setThreshold(1);
			mt_addtional_qualification_location
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			break;
		case 5:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_pharmacist));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_pharmacist));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			mt_additional_qualification.setHint(R.string.NA);
			mt_additional_qualification.setEnabled(false);
			mt_addtional_qualification_location.setHint(R.string.NA);
			mt_addtional_qualification_location.setEnabled(false);

			break;
		case 6:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_physiotherapist));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(
									R.array.qualification_physiotherapist));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			mt_additional_qualification.setHint(R.string.NA);
			mt_additional_qualification.setEnabled(false);
			mt_addtional_qualification_location.setHint(R.string.NA);
			mt_addtional_qualification_location.setEnabled(false);

			break;
		case 7:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_other_technician));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_technician));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			additional_qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					getResources().getStringArray(
							R.array.additional_qualification_technician));
			mt_additional_qualification
					.setAdapter(additional_qualification_list);
			mt_additional_qualification.setThreshold(1);
			mt_additional_qualification
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			additional_qualification_location_list = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1,
					additional_qualification_location);
			mt_addtional_qualification_location
					.setAdapter(additional_qualification_location_list);
			mt_addtional_qualification_location.setThreshold(1);
			mt_addtional_qualification_location
					.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

			break;
		case 8:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_ha));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_ha));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			additional_qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getResources()
							.getStringArray(
									R.array.additional_qualification_doctor));
			mt_additional_qualification.setHint(R.string.NA);
			mt_additional_qualification.setEnabled(false);
			mt_addtional_qualification_location.setHint(R.string.NA);
			mt_addtional_qualification_location.setEnabled(false);
			break;
		case 9:

			tv_qualification.setText(getResources().getString(
					R.string.qualification_asha));
			qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, getResources()
							.getStringArray(R.array.qualification_asha));
			qualification_list
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spn_qualification.setAdapter(qualification_list);

			additional_qualification_list = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, getResources()
							.getStringArray(
									R.array.additional_qualification_doctor));
			mt_additional_qualification.setEnabled(false);
			mt_addtional_qualification_location.setEnabled(false);
			break;

		default:
			break;
		}
		if(Mode.equals("Update"))
			populateControls();
		
		next = (Button) findViewById(R.id.btnNext);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData();
				boolean validationFlag = false;
				validationFlag = validateFields();
				if (validationFlag) {
					saveData();
					Intent objIndent = new Intent(getApplicationContext(),
							Confirm_Details_FHCP.class);
					startActivity(objIndent);
				}
					/*String Backup_Id;
					ContentValues cvQualifications = new ContentValues();
					String Qualification = spn_qualification.getSelectedItem()
							.toString();

					hhrwbDatabase = new DataBaseHelper(getApplicationContext());
					Backup_Id = pref.getString("Backup_Id", "No Value");
					cvQualifications.put("Backup_Id", Backup_Id.toString());
					if (Qualification.substring(3, 5).equals("888"))
						Qualification = et_qualification_other.getText()
								.toString();
					cvQualifications.put("Qualification", Qualification);
					cvQualifications.put("Qualification_Location",
							Qualification_Location);
					try {
						if (hhrwbDatabase.insertValues("qualification",
								cvQualifications) != -1) {
							Toast.makeText(getApplicationContext(),
									"Successuly inserted Qualification",
									Toast.LENGTH_SHORT).show();
							int count = hhrwbDatabase.getCount("qualification");
							Toast.makeText(getApplicationContext(),
									"Count = " + count, Toast.LENGTH_SHORT)
									.show();

						} else
							Toast.makeText(getApplicationContext(),
									"Not Successuly inserted",
									Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(),
								"Not Successuly inserted", Toast.LENGTH_SHORT)
								.show();
						e.printStackTrace();
					}
					// -1 to leave the last token which is blank as a comma is
					// automatically inserted after every qualification is
					// selected from the dropdown
					String Addotional_Qualification_Locations="";
					while (TotalQualifications - 1 > 0
							&& TotalQualification_Locations - 1 > 0) {
						
						Qualification = Additional_Qualification.nextToken();
						Additional_Qualification_Locations = Additional_Qualification_Location
								.nextToken().trim().substring(0,2);
						Qualification_Location = Integer.parseInt(Additional_Qualification_Locations);
						cvQualifications.put("Backup_Id", Backup_Id.toString());
						cvQualifications.put("Qualification", Qualification);
						cvQualifications.put("Qualification_Location",
								Qualification_Location);
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
					}*/
					
				
			}
		});

	}
	private void populateControls()
	{
		Qualification = pref.getString("Qualification", "");
		Qualification_Other = pref.getString("Qualification_Other", "");
		Qualification_Location=pref.getInt("Qualification_Location", -1);
		Additional_Qualifications = pref.getString("Additional_Qualifications", "");
		Additional_Qualification_Locations = pref.getString("Additional_Qualification_Locations", "");
		
		for (int i = 0; i < spn_qualification.getAdapter()
				.getCount(); i++) {
			if (spn_qualification.getItemAtPosition(i).toString().equals(
					Qualification)) {
				spn_qualification.setSelection(i);
			}
		}
		et_qualification_other.setText(Qualification_Other);
		switch (Qualification_Location) {
		case 1:
			rg_qualification_location.check(R.id.radioWB);
			break;
		case 2:
			rg_qualification_location.check(R.id.radioOutsideWB);
			break;
		default:
			break;
		}
		mt_additional_qualification.setText(Additional_Qualifications);
		mt_addtional_qualification_location.setText(Additional_Qualification_Locations);
	}
	private void getData()
	{
		Qualification = spn_qualification.getSelectedItem()
				.toString();
		Qualification_Other = et_qualification_other.getText().toString();
		
		int selectedQualificationLocation = rg_qualification_location
				.getCheckedRadioButtonId();

		switch (selectedQualificationLocation) {
		case R.id.radioWB:
			Qualification_Location = 1;
			break;
		case R.id.radioOutsideWB:
			Qualification_Location = 2;
			break;
		default:
			Qualification_Location = 1;
			break;
		}
		Additional_Qualifications = mt_additional_qualification.getText()
				.toString();
		Additional_Qualification = new StringTokenizer(
				Additional_Qualifications, ",");
		TotalQualifications = Additional_Qualification.countTokens();
		Additional_Qualification_Locations = mt_addtional_qualification_location.getText()
				.toString();
		Additional_Qualification_Location = new StringTokenizer(
				Additional_Qualification_Locations, ",");
		TotalQualification_Locations = Additional_Qualification_Location
				.countTokens();

	}
	
	private boolean validateFields() {
		boolean validationFlag = false;
		if (Qualification.equals("-----"))
			Toast.makeText(getApplicationContext(),
					this.getResources().getString(R.string.qualification_error),
					Toast.LENGTH_SHORT).show();
		else if (Qualification.substring(3, 5).equals("888")
				&& Qualification_Other.equals(""))
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(R.string.qualification_other_error),
					Toast.LENGTH_SHORT).show();
		else if (TotalQualifications != TotalQualification_Locations)
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(R.string.additional_qualification_error),
					Toast.LENGTH_LONG).show();
		else
			validationFlag = true;
		return validationFlag;
	}
	private void saveData()
	{
		pref.edit().putString("Qualification", Qualification).commit();
		pref.edit().putString("Qualification_Other",Qualification_Other).commit();
		pref.edit().putInt("Qualification_Location", Qualification_Location).commit();
		pref.edit().putString("Additional_Qualifications", Additional_Qualifications).commit();
		pref.edit().putString("Additional_Qualification_Locations", Additional_Qualification_Locations).commit();
	}
}
