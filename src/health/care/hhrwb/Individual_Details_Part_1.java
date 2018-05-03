package health.care.hhrwb;

import java.nio.channels.SelectableChannel;
import java.util.Calendar;
import java.util.UUID;

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
import android.content.SharedPreferences.Editor;
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
import android.widget.TextView;
import android.widget.Toast;

public class Individual_Details_Part_1 extends Home {

	public Bundle bundleIndividualMaster;
	static final int DATE_DIALOG_ID = 1;
	int mYear, mMonth, mDay;
	EditText et_first_name, et_middle_name, et_last_name;
	EditText et_father_first_name, et_father_middle_name, et_father_last_name;
	EditText et_mother_first_name, et_mother_middle_name, et_mother_last_name;
	EditText et_date_of_birth;
	String selectedGender;
	RadioGroup rg_area_type, rg_gender;
	Button next, openCalendar;
	String Mode;
	SharedPreferences pref;
	String First_Name, Middle_Name, Last_Name, Date_Of_Birth="";
	String Father_First_Name, Father_Middle_Name, Father_Last_Name;
	String Mother_First_Name, Mother_Middle_Name, Mother_Last_Name;
	int Gender;
	UUID backupId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_details_part_1);

		pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 -
																			// for
																			// private
																			// mode
		Mode = pref.getString("Mode_Check", "Insert");
		et_first_name = (EditText) findViewById(R.id.etFirstName);
		et_middle_name = (EditText) findViewById(R.id.etMiddletName);
		et_last_name = (EditText) findViewById(R.id.etLastName);

		et_father_first_name = (EditText) findViewById(R.id.etFatherFirstName);
		et_father_middle_name = (EditText) findViewById(R.id.etFatherMiddletName);
		et_father_last_name = (EditText) findViewById(R.id.etFatherLastName);

		et_mother_first_name = (EditText) findViewById(R.id.etMotherFirstName);
		et_mother_middle_name = (EditText) findViewById(R.id.etMotherMiddletName);
		et_mother_last_name = (EditText) findViewById(R.id.etMotherLastName);

		et_date_of_birth = (EditText) findViewById(R.id.etDateOfBirth);
		rg_gender = (RadioGroup) findViewById(R.id.rgGender);

		if(Mode.equals("Update"))
		{
			populateControls();
		}
		else
		{
			backupId = UUID.randomUUID();
			pref.edit().putString("Backup_Id", backupId.toString()).commit();
			
		}
		
		next = (Button) findViewById(R.id.btnNext);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				getData();
				boolean validationFlag = false;
				validationFlag = validateFields();
				if (validationFlag) {
					saveData();
					Intent objIntent = new Intent(getApplicationContext(),
							Individual_Details_Part_2.class);

					startActivity(objIntent);
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
		if(Date_Of_Birth.isEmpty())
		{
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
		}
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
		et_date_of_birth.setText(new StringBuilder()
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
	
	

	private boolean validateFields() {
		boolean validationFlag = false;
		if (First_Name.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.first_name_blank_error), Toast.LENGTH_LONG)
					.show();
		
		else if(Middle_Name.isEmpty() && Last_Name.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.middle_last_name_blank_error), Toast.LENGTH_LONG)
					.show();
		
		else if (Father_First_Name.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.father_first_name_blank_error), Toast.LENGTH_LONG)
					.show();
		
		else if(Father_Middle_Name.isEmpty() && Father_Last_Name.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.father_middle_last_name_blank_error), Toast.LENGTH_LONG)
					.show();
		
		else if (Mother_First_Name.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.mother_first_name_blank_error), Toast.LENGTH_LONG)
					.show();
		
		else if(Mother_Middle_Name.isEmpty() && Mother_Last_Name.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.mother_middle_last_name_blank_error), Toast.LENGTH_LONG)
					.show();
		else
			validationFlag = true;
		
		return validationFlag;
	}
	private void getData()
	{
		First_Name = et_first_name.getText().toString();
		Middle_Name = et_middle_name.getText().toString();
		Last_Name = et_last_name.getText().toString();

		Father_First_Name = et_father_first_name.getText().toString();
		Father_Middle_Name = et_father_middle_name.getText().toString();
		Father_Last_Name = et_father_last_name.getText().toString();

		Mother_First_Name = et_mother_first_name.getText().toString();
		Mother_Middle_Name = et_mother_middle_name.getText().toString();
		Mother_Last_Name = et_mother_last_name.getText().toString();

		Date_Of_Birth = et_date_of_birth.getText().toString();

		int selectedGenderId = rg_gender.getCheckedRadioButtonId();

		switch (selectedGenderId) {
		case R.id.radioMale:
			Gender = 1;
			break;

		case R.id.radioFemale:
			Gender = 2;
			break;

		case R.id.radioOthers:
			Gender = 3;
			break;

		default:
			break;
		}

	}
	private void populateControls()
	{
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
		Gender = pref.getInt("Gender", -1);

		et_first_name.setText(First_Name);
		et_middle_name.setText(Middle_Name);
		et_last_name.setText(Last_Name);

		et_father_first_name.setText(Father_First_Name);
		et_father_middle_name.setText(Father_Middle_Name);
		et_father_last_name.setText(Father_Last_Name);

		et_mother_first_name.setText(Mother_First_Name);
		et_mother_middle_name.setText(Mother_Middle_Name);
		et_mother_last_name.setText(Mother_Last_Name);

		et_date_of_birth.setText(Date_Of_Birth);

		if (Gender == 1)
			rg_gender.check(R.id.radioMale);
		else if (Gender == 2)
			rg_gender.check(R.id.radioFemale);
		else
			rg_gender.check(R.id.radioOthers);

	}
	private void saveData() {
		pref.edit().putString("First_Name", First_Name).commit();
		pref.edit().putString("Middle_Name", Middle_Name).commit();
		pref.edit().putString("Last_Name", Last_Name).commit();

		pref.edit().putString("Father_First_Name", Father_First_Name).commit();
		pref.edit().putString("Father_Middle_Name", Father_Middle_Name)
				.commit();
		pref.edit().putString("Father_Last_Name", Father_Last_Name).commit();

		pref.edit().putString("Mother_First_Name", Mother_First_Name).commit();
		pref.edit().putString("Mother_Middle_Name", Mother_Middle_Name)
				.commit();
		pref.edit().putString("Mother_Last_Name", Mother_Last_Name).commit();

		pref.edit().putString("Date_Of_Birth", Date_Of_Birth).commit();
		pref.edit().putInt("Gender", Gender).commit();
	}

}
