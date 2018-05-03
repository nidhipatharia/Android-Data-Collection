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

public class Informal_Health_Care_Provider extends Home {

	static final int DATE_DIALOG_ID = 1;
	private int mYear, mMonth, mDay;
	private EditText et_practicing_since, et_qualification_other,
			et_vehcile_other;
	private RadioGroup rg_qualification_location,rg_practicing_location, rg_mode_of_service,
			rg_prescribing_method;
	private Button next, openCalendar;
	private Spinner spn_qualification, spn_vehicle;
	String Qualification, Qualification_Other, Practicing_Since="",
			Selected_Vehicle, Vehicle_Other;
	int Practicing_Location, Mode_Of_Service,
			Prescribing_Method, Vehicle;
	int Qualification_Location;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informal_health_care_provider);
		initializeControls();
	}

	private void initializeControls() {
		et_practicing_since = (EditText) findViewById(R.id.etPracticingSince);
		et_qualification_other = (EditText) findViewById(R.id.etQualificationOther);
		et_vehcile_other = (EditText) findViewById(R.id.etVehicleOther);

		rg_qualification_location = (RadioGroup) findViewById(R.id.rgQualificationLocation);
		rg_practicing_location = (RadioGroup) findViewById(R.id.rgPracticingLocation);
		rg_mode_of_service = (RadioGroup) findViewById(R.id.rgModeOfService);
		rg_prescribing_method = (RadioGroup) findViewById(R.id.rgPrescribingMethod);

		spn_qualification = (Spinner) findViewById(R.id.spnQualification);
		spn_vehicle = (Spinner) findViewById(R.id.spnVehicle);

		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		String Mode = pref.getString("Mode_Check", "Insert");
		if(Mode.equals("Update"))
			populateControls();

				next = (Button) findViewById(R.id.btnNext);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getData();
				boolean validationFlag = false;
				validationFlag = validateFields();
				if(validationFlag)
				{
					saveData();
						Intent objIndent = new Intent(getApplicationContext(),
								Confirm_Details_IFHCP.class);
						startActivity(objIndent);
				}
				
			}
		});

		openCalendar = (Button) findViewById(R.id.btnOpenCalendar);
		openCalendar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		if(Practicing_Since.isEmpty())
			updateDisplay();
	}
	private void getData()
	{
		Practicing_Since = et_practicing_since.getText().toString();
		Qualification = spn_qualification.getSelectedItem().toString();
		Qualification_Other = et_qualification_other.getText().toString();
		Selected_Vehicle = spn_vehicle.getSelectedItem().toString();
		Vehicle_Other = et_vehcile_other.getText().toString();
		
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

		
		int selectedPracticingLocation = rg_practicing_location
				.getCheckedRadioButtonId();
		switch (selectedPracticingLocation) {
		case R.id.radioUrban:
			Practicing_Location = 1;
			break;

		case R.id.radioSemiUrban:
			Practicing_Location = 2;
			break;

		case R.id.radioRural:
			Practicing_Location = 3;
			break;

		default:
			Practicing_Location = 1;
			break;
		}

		int selectedModeOfServide = rg_mode_of_service
				.getCheckedRadioButtonId();
		switch (selectedModeOfServide) {
		case R.id.radioStationary:
			Mode_Of_Service = 1;
			break;

		case R.id.radioMobile:
			Mode_Of_Service = 2;
			break;

		default:
			Mode_Of_Service = 1;
			break;
		}

		int selectedPrescribingMethod = rg_prescribing_method
				.getCheckedRadioButtonId();
		switch (selectedPrescribingMethod) {
		case R.id.radioDispenseDrugs:
			Prescribing_Method = 1;
			break;

		case R.id.radioWritePrescription:
			Prescribing_Method = 2;
			break;

		default:
			Prescribing_Method = 1;
			break;
		}


	}
	private void populateControls()
	{
		
		Qualification = pref.getString("Qualification", "");
		Qualification_Other = pref.getString("Qualification_Other", "");
		Qualification_Location = pref.getInt("Qualification_Location", -1);
		Practicing_Since = pref.getString("Practicing_Since", "");
		Practicing_Location = pref.getInt("Practicing_Location", -1);
		Prescribing_Method = pref.getInt("Prescribing_Method", -1);
		Mode_Of_Service = pref.getInt("Mode_Of_Service", -1);
		Selected_Vehicle = pref.getString("Selected_Vehicle", "");
		Vehicle = pref.getInt("Vehicle", -1);
		Vehicle_Other = pref.getString("Vehicle_Other", "");
		
		for (int i = 0; i < spn_qualification.getAdapter()
				.getCount(); i++) {
			if (spn_qualification.getItemAtPosition(i).toString().equals(
					Qualification)) {
				spn_qualification.setSelection(i);
			}
		}
		et_qualification_other.setText(Qualification_Other);
		et_practicing_since.setText(Practicing_Since);
		
		switch (Practicing_Location) {
		case 1:
			rg_practicing_location.check(R.id.radioUrban);
			break;
		case 2:
			rg_practicing_location.check(R.id.radioSemiUrban);
			break;
		case 3:
			rg_practicing_location.check(R.id.radioRural);
			break;
		default:
			rg_practicing_location.check(R.id.radioUrban);
		}
		
		switch (Prescribing_Method) {
		case 1:
			rg_prescribing_method.check(R.id.radioDispenseDrugs);
			break;
		case 2:
			rg_prescribing_method.check(R.id.radioWritePrescription);
			break;
		default:
			rg_prescribing_method.check(R.id.radioDispenseDrugs);
		}
		
		switch (Mode_Of_Service) {
		case 1:
			rg_mode_of_service.check(R.id.radioStationary);
			break;
		case 2:
			rg_mode_of_service.check(R.id.radioMobile);
			break;
		default:
			rg_mode_of_service.check(R.id.radioStationary);
		}
		
		for (int i = 0; i < spn_vehicle.getAdapter()
				.getCount(); i++) {
			if (spn_vehicle.getItemAtPosition(i).toString().equals(
					Selected_Vehicle)) {
				spn_vehicle.setSelection(i);
			}
		}
		
		et_vehcile_other.setText(Vehicle_Other);
	}
	private boolean validateFields() {
		boolean validationFlag = false;
		String Qualification_Other = et_qualification_other.getText()
				.toString();
		String vehicle = Selected_Vehicle.substring(0, 2);

		if (Qualification.substring(3, 5).equals("--"))
			Toast.makeText(getApplicationContext(),
					"You must select a qualification from the list",
					Toast.LENGTH_LONG).show();
		else if (Qualification.substring(3, 5).equals("88")
				&& Qualification_Other.equals(""))
			Toast.makeText(
					getApplicationContext(),
					"Please type a value for Qualification as option other is chosen",
					Toast.LENGTH_SHORT).show();
		else if (vehicle.equals("--"))
			Toast.makeText(getApplicationContext(),
					"You must select a vehicle from the list",
					Toast.LENGTH_LONG).show();
		else if (vehicle.equals("88") && Vehicle_Other.equals(""))
			Toast.makeText(
					getApplicationContext(),
					"Please type a value for Vehicle as option other is chosen",
					Toast.LENGTH_SHORT).show();
		else
			validationFlag = true;
		return validationFlag;
	}
	private void saveData()
	{
		pref.edit().putString("Qualification", Qualification).commit();
		pref.edit().putString("Qualification_Other", Qualification_Other).commit();
		pref.edit().putInt("Qualification_Location", Qualification_Location).commit();
		pref.edit().putString("Practicing_Since", Practicing_Since).commit();
		pref.edit().putInt("Practicing_Location", Practicing_Location).commit();
		pref.edit().putInt("Prescribing_Method", Prescribing_Method).commit();
		pref.edit().putInt("Mode_Of_Service", Mode_Of_Service).commit();
		pref.edit().putString("Selected_Vehicle", Selected_Vehicle).commit();
		pref.edit().putInt("Vehicle", Vehicle).commit();
		pref.edit().putString("Vehicle_Other", Vehicle_Other).commit();
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {

		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	private void updateDisplay() {
		et_practicing_since.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(mYear).append("-").append(mMonth + 1).append("-")
				.append(mDay));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

}
