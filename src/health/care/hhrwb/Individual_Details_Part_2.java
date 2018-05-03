package health.care.hhrwb;

import java.util.List;

import org.w3c.dom.Text;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Individual_Details_Part_2 extends Home implements
		OnItemSelectedListener {

	RadioGroup rg_area_type;
	Spinner spn_district, spn_block, spn_panchayat, spn_municipality,
			spn_municipal_corporation;
	Button next;
	TextView tv_gram_panchayat;
	EditText et_permanent_address, et_current_address;
	DataBaseHelperPreLoaded db;
	String strDistrict;
	int Area_Type = 1;
	String Mode;
	String notApplicalbe[] = { "000 - Not Applicable" };
	String District_Code, Block_Code, Panchayat_Code, Municipality_Code,
			Municipal_Corporation_Code, Permanent_Address, Current_Address;
	String Selected_District, Selected_Block, Selected_Panchayat,
			Selected_Municipality, Selected_Municipal_Corporation;
	int Selected_District_Position, Selected_Block_Position,
			Selected_Panchayat_Position, Selected_Municipality_Position,
			Selected_Municipal_Corporation_Position;

	RelativeLayout rl_panchayat, rl_municipality, rl_municipal_corporation;
	SharedPreferences pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.individual_details_part_2);
		db = new DataBaseHelperPreLoaded(this);
		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		Mode = pref.getString("Mode_Check", "Insert");
		try {
			db.createDataBase();
		} catch (Exception e) {
			new Error("Database could not be loaded");
		}

		rg_area_type = (RadioGroup) findViewById(R.id.rgAreaType);

		spn_district = (Spinner) findViewById(R.id.spnDistrict);

		rl_panchayat = (RelativeLayout) findViewById(R.id.rlPanchayat);
		rl_municipality = (RelativeLayout) findViewById(R.id.rlMunicipality);
		rl_municipal_corporation = (RelativeLayout) findViewById(R.id.rlMunicipalCorporation);

		spn_block = (Spinner) findViewById(R.id.spnBlock);
		tv_gram_panchayat = (TextView) findViewById(R.id.tvGramPanchayat);
		spn_panchayat = (Spinner) findViewById(R.id.spnGramPanchayat);

		spn_municipality = (Spinner) findViewById(R.id.spnMunicipality);
		spn_municipal_corporation = (Spinner) findViewById(R.id.spnMunicipalCorporation);

		et_permanent_address = (EditText) findViewById(R.id.etPermanentAddress);
		et_current_address = (EditText) findViewById(R.id.etCurrentAddress);

		List<String> districts = db.getAllDistricts();
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, districts);
		dataAdapter.setDropDownViewResource(R.layout.spinner_item);

		spn_district.setAdapter(dataAdapter);

		spn_district.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (Mode.equals("Insert")) {
					Selected_District = spn_district.getSelectedItem()
							.toString();
					fillBlockAdapter(Selected_District);

					fillMunicipality(Selected_District);

					fillMunicipalCorporation(Selected_District);
				}
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});

		spn_block.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				if (Mode.equals("Insert")) {
					Selected_Block = spn_block.getSelectedItem().toString();

					fillPanchayatAdapter(Selected_District, Selected_Block);
				}
			}

			public void onNothingSelected(AdapterView<?> parentView) {
				// your code here
			}

		});
		if (Mode.equals("Update"))
			populateControls();

		rg_area_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

				int checkedRadioButton = radioGroup.getCheckedRadioButtonId();
				switch (checkedRadioButton) {
				case R.id.radioPanchayat:
					rl_panchayat.setVisibility(RelativeLayout.VISIBLE);
					rl_municipality.setVisibility(RelativeLayout.INVISIBLE);
					rl_municipal_corporation
							.setVisibility(RelativeLayout.INVISIBLE);
					Area_Type = 1;
					break;

				case R.id.radioMunicipality:
					rl_panchayat.setVisibility(RelativeLayout.INVISIBLE);
					rl_municipality.setVisibility(RelativeLayout.VISIBLE);
					rl_municipal_corporation
							.setVisibility(RelativeLayout.INVISIBLE);
					Area_Type = 2;
					break;

				case R.id.radioMunicipalCorporation:
					rl_panchayat.setVisibility(RelativeLayout.INVISIBLE);
					rl_municipality.setVisibility(RelativeLayout.INVISIBLE);
					rl_municipal_corporation
							.setVisibility(RelativeLayout.VISIBLE);
					Area_Type = 3;
					break;
				}

			}
		});

		next = (Button) findViewById(R.id.btnNext);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Permanent_Address = et_permanent_address.getText().toString();
				Current_Address = et_current_address.getText().toString();

				Selected_District_Position = spn_district
						.getSelectedItemPosition();
				Selected_District = spn_district.getSelectedItem().toString();

				District_Code = Selected_District.substring(0, 2);
				if (Area_Type == 1) {

					Selected_Block_Position = spn_block
							.getSelectedItemPosition();
					Selected_Block = spn_block.getSelectedItem().toString();
					Block_Code = Selected_Block.substring(0, 2);

					Selected_Panchayat_Position = spn_panchayat
							.getSelectedItemPosition();
					Selected_Panchayat = spn_panchayat.getSelectedItem()
							.toString();
					Panchayat_Code = Selected_Panchayat.substring(0, 2);
				} else {
					Selected_Block = "00 - Not Applicable";
					Block_Code = "000";
					Selected_Panchayat = "00 - Not Applicable";
					Panchayat_Code = "000";
				}
				if (Area_Type == 2) {

					Selected_Municipality_Position = spn_municipality
							.getSelectedItemPosition();
					Selected_Municipality = spn_municipality.getSelectedItem()
							.toString();
					Municipality_Code = Selected_Municipality.substring(0, 3);

				} else {
					Selected_Municipality = "00 - Not Applicable";
					Municipality_Code = "000";
				}

				if (Area_Type == 3) {

					Selected_Municipal_Corporation_Position = spn_municipal_corporation
							.getSelectedItemPosition();
					Selected_Municipal_Corporation = spn_municipal_corporation
							.getSelectedItem().toString();
					Municipal_Corporation_Code = Selected_Municipal_Corporation
							.substring(0, 3);
				} else {
					Selected_Municipal_Corporation = "00 - Not Applicable";
					Municipal_Corporation_Code = "000";
				}
				boolean validationFlag = false;
				validationFlag = validateFields();
				if (validationFlag) {
					saveData();
					db.close();
					Intent objIndent = new Intent(getApplicationContext(),
							Individual_Details_Part_3.class);
					startActivity(objIndent);
				}
			}
		});

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	private void populateControls() {

		Area_Type = pref.getInt("Area_Type", 1);
		Selected_District = pref.getString("District", "");
		District_Code = pref.getString("District_Code", "000");
		if (Mode.equals("Update")) {
			fillBlockAdapter(Selected_District);
			Selected_Block = pref.getString("Block", "");
			Block_Code = pref.getString("Block_Code", "000");
			fillPanchayatAdapter(Selected_District, Selected_Block);
			Selected_Panchayat = pref.getString("Panchayat", "");
			Panchayat_Code = pref.getString("Panchayat_Code", "000");

			fillMunicipality(Selected_District);
			Selected_Municipality = pref.getString("Municipality", "");
			Municipality_Code = pref.getString("Municipality_Code", "000");

			fillMunicipalCorporation(Selected_District);
			Selected_Municipal_Corporation = pref.getString(
					"Municipal_Corporation", "");
			Municipal_Corporation_Code = pref.getString(
					"Municipal_Corporation_Code", "000");

			Permanent_Address = pref.getString("Permanent_Address", "");
			Current_Address = pref.getString("Current_Address", "");

			et_permanent_address.setText(Permanent_Address);
			et_current_address.setText(Current_Address);

			if (!Selected_District.isEmpty())
				for (int i = 0; i < spn_district.getAdapter().getCount(); i++) {
					if (spn_district.getItemAtPosition(i).equals(
							Selected_District)) {
						spn_district.setSelection(i);
						break;
					}
				}
			if (Area_Type == 1 && !Selected_Block.isEmpty()) {
				rg_area_type.check(R.id.radioPanchayat);

				rl_panchayat.setVisibility(RelativeLayout.VISIBLE);
				rl_municipality.setVisibility(RelativeLayout.INVISIBLE);
				rl_municipal_corporation
						.setVisibility(RelativeLayout.INVISIBLE);

				for (int i = 0; i < spn_block.getAdapter().getCount(); i++) {
					if (spn_block.getItemAtPosition(i).equals(Selected_Block)) {
						spn_block.setSelection(i);
						break;
					}
				}
				for (int i = 0; i < spn_panchayat.getAdapter().getCount(); i++) {
					if (spn_panchayat.getItemAtPosition(i).equals(
							Selected_Panchayat)) {
						spn_panchayat.setSelection(i);
						break;
					}
				}

			} else if (Area_Type == 2 && !Selected_Municipality.isEmpty()) {
				rg_area_type.check(R.id.radioMunicipality);

				rl_panchayat.setVisibility(RelativeLayout.INVISIBLE);
				rl_municipality.setVisibility(RelativeLayout.VISIBLE);
				rl_municipal_corporation
						.setVisibility(RelativeLayout.INVISIBLE);

				try {
					for (int i = 0; i < spn_municipality.getAdapter()
							.getCount(); i++) {
						if (spn_municipality.getItemAtPosition(i).equals(
								Selected_Municipality)) {
							spn_municipality.setSelection(i);
							break;
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} else if (Area_Type == 3
					&& !Selected_Municipal_Corporation.isEmpty()) {
				rg_area_type.check(R.id.radioMunicipalCorporation);

				rl_panchayat.setVisibility(RelativeLayout.INVISIBLE);
				rl_municipality.setVisibility(RelativeLayout.INVISIBLE);
				rl_municipal_corporation.setVisibility(RelativeLayout.VISIBLE);
				try {
					int count = spn_municipal_corporation.getAdapter()
							.getCount();
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 0; i < spn_municipal_corporation.getAdapter()
						.getCount(); i++) {
					if (spn_municipal_corporation.getItemAtPosition(i).equals(
							Selected_Municipal_Corporation)) {
						spn_municipal_corporation.setSelection(i);
						break;
					}
				}
			}
		}
	}

	// These methods are required to fill the spinner Adapter specially for the
	// update purpose.

	private void fillBlockAdapter(String District) {
		List<String> blocks = db.getAllBlocks(District);

		ArrayAdapter<String> dataAdapterBlock = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, blocks);
		if (dataAdapterBlock.getCount() == 0) {
			dataAdapterBlock = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.spinner_item,
					notApplicalbe);
			spn_panchayat.setVisibility(Spinner.INVISIBLE);
			tv_gram_panchayat.setVisibility(TextView.INVISIBLE);
		} else {
			spn_panchayat.setVisibility(Spinner.VISIBLE);
			tv_gram_panchayat.setVisibility(TextView.VISIBLE);
		}
		dataAdapterBlock.setDropDownViewResource(R.layout.spinner_item);
		spn_block.setAdapter(dataAdapterBlock);
	}

	private void fillPanchayatAdapter(String District, String Block) {
		List<String> panchayats = db.getAllGramPanchayat(District, Block);
		ArrayAdapter<String> dataAdapterPanchayat = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, panchayats);
		dataAdapterPanchayat.setDropDownViewResource(R.layout.spinner_item);
		spn_panchayat.setAdapter(dataAdapterPanchayat);

	}

	private void fillMunicipality(String District) {
		List<String> municipalities = db.getAllMunicipality(District);
		ArrayAdapter<String> dataAdapterMunicipality = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item, municipalities);
		if (dataAdapterMunicipality.getCount() == 0) {
			dataAdapterMunicipality = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.spinner_item,
					notApplicalbe);
		}
		dataAdapterMunicipality.setDropDownViewResource(R.layout.spinner_item);
		spn_municipality.setAdapter(dataAdapterMunicipality);

	}

	private void fillMunicipalCorporation(String District) {
		List<String> municipalCorporations = db
				.getAllMunicipaCorporation(District);
		ArrayAdapter<String> dataAdapterMunicipalCorporation = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.spinner_item,
				municipalCorporations);
		if (dataAdapterMunicipalCorporation.getCount() == 0) {
			dataAdapterMunicipalCorporation = new ArrayAdapter<String>(
					getApplicationContext(), R.layout.spinner_item,
					notApplicalbe);
		}
		dataAdapterMunicipalCorporation
				.setDropDownViewResource(R.layout.spinner_item);
		spn_municipal_corporation.setAdapter(dataAdapterMunicipalCorporation);

	}

	private boolean validateFields() {
		boolean validationFlag = false;
		if (Area_Type == 1 && Block_Code.contains("000"))
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.invalid_district_for_block),
					Toast.LENGTH_LONG).show();
		else if (Area_Type == 2 && Municipality_Code.contains("000"))
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.invalid_district_for_municipality),
					Toast.LENGTH_LONG).show();

		else if (Area_Type == 3 && Municipal_Corporation_Code.contains("000"))
			Toast.makeText(
					getApplicationContext(),
					this.getResources()
							.getString(
									R.string.invalid_district_for_municipal_corporation),
					Toast.LENGTH_LONG).show();
		else if (Permanent_Address.isEmpty())
			Toast.makeText(
					getApplicationContext(),
					this.getResources().getString(
							R.string.permanent_address_blank_error),
					Toast.LENGTH_LONG).show();
		else
			validationFlag = true;

		return validationFlag;
	}

	private void saveData() {
		pref.edit().putInt("Area_Type", Area_Type).commit();

		pref.edit().putString("District", Selected_District).commit();
		pref.edit().putString("District_Code", District_Code).commit();

		pref.edit().putString("Block", Selected_Block).commit();
		pref.edit().putString("Block_Code", Block_Code).commit();

		pref.edit().putString("Panchayat", Selected_Panchayat).commit();
		pref.edit().putString("Panchayat_Code", Panchayat_Code).commit();

		pref.edit().putString("Municipality", Selected_Municipality).commit();
		pref.edit().putString("Municipality_Code", Municipality_Code).commit();

		pref.edit()
				.putString("Municipal_Corporation",
						Selected_Municipal_Corporation).commit();
		pref.edit()
				.putString("Municipal_Corporation_Code",
						Municipal_Corporation_Code).commit();

		pref.edit().putString("Permanent_Address", Permanent_Address).commit();
		pref.edit().putString("Current_Address", Current_Address).commit();

	}
}