package health.care.hhrwb;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Individual_Details_Part_3 extends Home {

	RadioGroup rg_marital_status;
	EditText et_mobile_number, et_alternate_phone_number, et_email_id,
			et_pan_card, et_voter_id, et_aadhar_card;
	Button next;
	String Mode;
	SharedPreferences pref;
	String Mobile_Number, Alternate_Phone_Number, Email_Id, Pan_Card, Voter_Id,
			Aadhar_Card;
	int Marital_Status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_details_part_3);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		rg_marital_status = (RadioGroup) findViewById(R.id.rgMaritalStatus);

		et_mobile_number = (EditText) findViewById(R.id.etMobileNumber);
		et_alternate_phone_number = (EditText) findViewById(R.id.etAlternateNumber);
		et_email_id = (EditText) findViewById(R.id.etEmailId);
		et_pan_card = (EditText) findViewById(R.id.etPanCard);
		et_voter_id = (EditText) findViewById(R.id.etVoterId);
		et_aadhar_card = (EditText) findViewById(R.id.etAadhar);

		Mode = pref.getString("Mode_Check", "Insert");
		if (Mode.equals("Update")) {
			populateControls();
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
					Intent objIndent = new Intent(getApplicationContext(),
							Individual_Details_Part_4.class);
					startActivity(objIndent);
				}
			}
		});

	}

	private void populateControls() {
		Marital_Status = pref.getInt("Marital_Status", 1);
		Mobile_Number = pref.getString("Mobile_Number", "");
		Alternate_Phone_Number = pref.getString("Alternate_Phone_Number", "");
		Email_Id = pref.getString("Email_Id", "");
		Pan_Card = pref.getString("Pan_Card", "");
		Voter_Id = pref.getString("Voter_Id", "");
		Aadhar_Card = pref.getString("Aadhar_Card", "");

		switch (Marital_Status) {
		case 1:
			rg_marital_status.check(R.id.radioSingle);
			break;
		case 2:
			rg_marital_status.check(R.id.radioMarried);
			break;
		case 3:
			rg_marital_status.check(R.id.radioDivorcee);
			break;
		default:
			break;
		}

		et_mobile_number.setText(Mobile_Number);
		et_alternate_phone_number.setText(Alternate_Phone_Number);
		et_email_id.setText(Email_Id);
		et_pan_card.setText(Pan_Card);
		et_voter_id.setText(Voter_Id);
		et_aadhar_card.setText(Aadhar_Card);

	}

	private boolean validateFields() {
		boolean validationFlag = false;
		if (Mobile_Number.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.mobile_number_blank_error),
					Toast.LENGTH_LONG).show();
		
		else if(Email_Id.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.email_id_blank_error),
					Toast.LENGTH_LONG).show();
		
		else if(!(android.util.Patterns.EMAIL_ADDRESS.matcher(Email_Id).matches()))
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.email_id_error),
					Toast.LENGTH_LONG).show();
		else if((Pan_Card.isEmpty() && Voter_Id.isEmpty()) || (Pan_Card.isEmpty() && Aadhar_Card.isEmpty()) || (Voter_Id.isEmpty() && Aadhar_Card.isEmpty()))
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.identification_number_error),
					Toast.LENGTH_LONG).show();
		
		else 
			validationFlag = true;
		return validationFlag;
	}

	private void getData() {
		int selectedMaritalStatus = rg_marital_status.getCheckedRadioButtonId();
		switch (selectedMaritalStatus) {
		case R.id.radioSingle:
			Marital_Status = 1;
			break;
		case R.id.radioMarried:
			Marital_Status = 2;
			break;
		case R.id.radioDivorcee:
			Marital_Status = 3;
			break;
		default:
			break;
		}

		Mobile_Number = et_mobile_number.getText().toString();
		Alternate_Phone_Number = et_alternate_phone_number.getText().toString();
		Email_Id = et_email_id.getText().toString();
		Pan_Card = et_pan_card.getText().toString();
		Voter_Id = et_voter_id.getText().toString();
		Aadhar_Card = et_aadhar_card.getText().toString();

	}

	private void saveData() {

		pref.edit().putInt("Marital_Status", Marital_Status).commit();
		pref.edit().putString("Mobile_Number", Mobile_Number).commit();
		pref.edit().putString("Alternate_Phone_Number", Alternate_Phone_Number)
				.commit();
		pref.edit().putString("Email_Id", Email_Id).commit();
		pref.edit().putString("Pan_Card", Pan_Card).commit();
		pref.edit().putString("Voter_Id", Voter_Id).commit();
		pref.edit().putString("Aadhar_Card", Aadhar_Card).commit();

	}
}