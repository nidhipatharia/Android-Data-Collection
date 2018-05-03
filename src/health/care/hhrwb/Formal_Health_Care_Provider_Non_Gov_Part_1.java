package health.care.hhrwb;

import java.util.Calendar;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
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

public class Formal_Health_Care_Provider_Non_Gov_Part_1 extends Home {

	static final int DATE_DIALOG_ID = 1;
	private int mYear, mMonth, mDay;
	EditText et_practicing_since;
	RadioGroup rg_emp_type,  rg_practicing_location;
	Button next, openCalendar;
	SharedPreferences pref;
	Spinner spn_role;
	String selectedRole;
	int Employment_Type, Role, Practicing_Location;
	private String Selected_Role,Practicing_Since="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formal_health_care_provider_non_gov_part_1);
		InitializeControls();
	}

	private void InitializeControls() {
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		rg_emp_type = (RadioGroup) findViewById(R.id.rgEmpType);
		spn_role = (Spinner) findViewById(R.id.spnRole);
		et_practicing_since = (EditText) findViewById(R.id.etPracticingSince);
		rg_practicing_location = (RadioGroup) findViewById(R.id.rgPracticingLocation);
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
					Intent objIndent = new Intent(getApplicationContext(),Formal_Health_Care_Provider_Non_Gov_Part_2.class);
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
		.append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
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
	private void getData() {
		int selectedEmploymentType = rg_emp_type.getCheckedRadioButtonId();

		switch (selectedEmploymentType) {
		case R.id.radioPrivate:
			Employment_Type = 1;
			break;
		case R.id.radioSelf:
			Employment_Type = 2;
			break;
		default:
			Employment_Type = 1;
		}
		int PracticingLocation = rg_practicing_location
				.getCheckedRadioButtonId();

		switch (PracticingLocation) {
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
		}

		Selected_Role = spn_role.getSelectedItem().toString();
		Role = Integer.parseInt(Selected_Role.substring(0, 2));

		Practicing_Since = et_practicing_since.getText().toString();

	}
	private boolean validateFields() {
		boolean validationFlag = false;
		if (Selected_Role.equals("Select from the list"))
			Toast.makeText(getApplicationContext(),
					this.getResources().getString(R.string.role_error), Toast.LENGTH_SHORT)
					.show();
		else
			validationFlag = true;
		return validationFlag;
	}
	private void populateControls() {
		Employment_Type = pref.getInt("Employment_Type", -1);
		Selected_Role = pref.getString("Selected_Role", "");
		Role = pref.getInt("Role", -1);
		Practicing_Since = pref.getString("Practicing_Since", "");
		Practicing_Location = pref.getInt("Practicing_Location", -1);
		
		switch (Employment_Type) {
		case 1:
			rg_emp_type.check(R.id.radioPrivate);
			break;
		case 2:
			rg_emp_type.check(R.id.radioSelf);
			break;
		default:
			rg_emp_type.check(R.id.radioPrivate);
			break;
		}
		
		for (int i = 0; i < spn_role.getAdapter()
				.getCount(); i++) {
			if (spn_role.getItemAtPosition(i).equals(
					Selected_Role)) {
				spn_role.setSelection(i);
			}
		}
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
	}
	private void saveData() {

		pref.edit().putInt("Employment_Type", Employment_Type).commit();
		pref.edit().putString("Selected_Role", Selected_Role).commit();
		pref.edit().putInt("Role", Role).commit();
		pref.edit().putString("Practicing_Since", Practicing_Since).commit();
		pref.edit().putInt("Practicing_Location", Practicing_Location).commit();
	}
}
