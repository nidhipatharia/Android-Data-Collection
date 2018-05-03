package health.care.hhrwb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DatabaseErrorHandler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity {

	private TextView txt_uid;
	private Button add, view, btn_view_fhcp_gov, btn_view_fhcp_non_gov,
			btn_view_ifhcp;
	private SharedPreferences pref;
	ProgressDialog prgDialog;
	DataBaseHelper db;
	String TableName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		pref = getApplicationContext().getSharedPreferences("MyPref", 0);
		setContentView(R.layout.activity_home);
		ActionBar actionBar = getActionBar();
		actionBar.show();
		txt_uid = (TextView) findViewById(R.id.txtUserName);
		Intent i = getIntent();
		final String uid = i.getStringExtra("uid");
		txt_uid.setText(uid);
		btn_view_fhcp_gov = (Button) findViewById(R.id.btnViewFHCPGov);
		btn_view_fhcp_non_gov = (Button) findViewById(R.id.btnViewFHCPNonGov);
		btn_view_ifhcp = (Button) findViewById(R.id.btnViewIFHCP);

		btn_view_fhcp_gov.setVisibility(View.GONE);
		btn_view_fhcp_non_gov.setVisibility(View.GONE);
		btn_view_ifhcp.setVisibility(View.GONE);

		prgDialog = new ProgressDialog(this);
		prgDialog
				.setMessage("Synching SQLite Data with Remote MySQL DB. Please wait...");
		prgDialog.setCancelable(false);
		view = (Button) findViewById(R.id.btnViewData);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btn_view_fhcp_gov.setVisibility(btn_view_fhcp_gov.isShown() ? View.GONE
						: View.VISIBLE);
				btn_view_fhcp_non_gov.setVisibility(btn_view_fhcp_non_gov
						.isShown() ? View.GONE : View.VISIBLE);
				btn_view_ifhcp.setVisibility(btn_view_ifhcp.isShown() ? View.GONE
						: View.VISIBLE);
			}
		});

		add = (Button) findViewById(R.id.btnFillData);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pref.edit().putString("Mode_Check", "Insert").commit();
				Intent objIndent = new Intent(getApplicationContext(),
						Individual_Details_Part_1.class);
				startActivity(objIndent);
			}
		});
		btn_view_fhcp_gov.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						View_All_FHCP_Gov.class);
				startActivity(intent);
			}
		});
		btn_view_fhcp_non_gov.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						View_All_FHCP_Non_Gov.class);
				startActivity(intent);
			}
		});
		btn_view_ifhcp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						View_All_IFHCP.class);
				startActivity(intent);
			}
		});
	}

	public void onLogoutClicked(MenuItem menuItem) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(true);
		// builder.setIcon(R.drawable.dialog_question);
		builder.setTitle("Are you sure you want to logout \n আপনি লগ আউট করতে চান");
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				moveTaskToBack(true);
				Home.this.finish();
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	public void onUploadClicked(MenuItem menuItem) {
		Toast.makeText(this, "Upload Selected", Toast.LENGTH_LONG).show();
		syncSQLiteMySQLDB();
		
		  /* try {
			writeToSD();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	public void syncSQLiteMySQLDB() {
		// Create AsycHttpClient object

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		db = new DataBaseHelper(getApplicationContext());
		//db.updateSyncStatustoYes();
		ArrayList<HashMap<String, String>> individualList = db
				.getAllIndividuals();
		ArrayList<HashMap<String, String>> locationOfIndividualList = db
				.getLocationOfIndividuals();
		ArrayList<HashMap<String, String>> FHCPGovList = db
				.getAllEmploymentGovernment();
		ArrayList<HashMap<String, String>> FHCPNonGovList = db
				.getAllEmploymentNonGovernment();
		ArrayList<HashMap<String, String>> IFHCPList = db.getAllIFHCP();
		ArrayList<HashMap<String, String>> qualificationList = db
				.getAllQualification();
		TableName = "individual_master";
		if (individualList.size() != 0) {
			/*if (db.dbSyncCount("individual_master") != 0
					&& db.dbSyncCount("location_of_individual") != 0
					&& (db.dbSyncCount("employment_government") != 0
							|| db.dbSyncCount("employment_non_government") != 0 || db
							.dbSyncCount("informal_health_care_providers") != 0)
					&& db.dbSyncCount("qualification") != 0) {*/
				prgDialog.show();
				params.put("IndividualJSON",
						db.composeJSONfromSQLiteIndividualMaster());
				params.put("LocationJSON",
						db.composeJSONfromSQLiteLocationOfIndividual());
				params.put("FHCPGovJSON",
						db.composeJSONfromSQLiteEmploymentGovernment());
				params.put("FHCPNonGovJSON",
						db.composeJSONfromSQLiteEmploymentNonGovernment());
				params.put("IFHCPJSON",
						db.composeJSONfromSQLiteIFHCP());
				params.put("QualificationJSON",
						db.composeJSONfromSQLiteQualification());
				client.post("http://kemhrcvadu.org/HHRWB/insertIndividual.php",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for (int i = 0; i < arr.length(); i++) {
										JSONObject obj = (JSONObject) arr
												.get(i);
										System.out.println(obj.get("Backup_Id"));
										System.out.println(obj
												.get("Update_Status"));
										db.updateSyncStatus(
												"individual_master",
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
										db.updateSyncStatus(
												"location_of_individual",
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
										db.updateSyncStatus(
												"employment_government",
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
										db.updateSyncStatus(
												"employment_non_government",
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
										db.updateSyncStatus(
												"informal_health_care_providers",
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
										db.updateSyncStatus(
												"qualification",
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
									}
									Toast.makeText(
											getApplicationContext(),
											"DB Sync completed for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(
											getApplicationContext(),
											"Error Occured [Server's JSON response might be invalid] for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Requested resource not found",
											Toast.LENGTH_LONG).show();
								} else if (statusCode == 500) {
									Toast.makeText(
											getApplicationContext(),
											"Something went wrong at server end",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			/*} else {
				Toast.makeText(getApplicationContext(),
						"SQLite and Remote MySQL DBs are in Sync!",
						Toast.LENGTH_LONG).show();
			}*/
		} else {
			Toast.makeText(
					getApplicationContext(),
					"No data in SQLite DB, please do enter User name to perform Sync action",
					Toast.LENGTH_LONG).show();
		}

		/*TableName = "location_of_individual";
		if (locationOfIndividualList.size() != 0) {
			if (db.dbSyncCount(TableName) != 0) {
				prgDialog.show();
				params.put("usersJSON",
						db.composeJSONfromSQLiteLocationOfIndividual());
				client.post(
						"http://kemhrcvadu.org/HHRWB/insertLocationOfIndividual.php",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for (int i = 0; i < arr.length(); i++) {
										JSONObject obj = (JSONObject) arr
												.get(i);
										System.out.println(obj.get("Backup_Id"));
										System.out.println(obj
												.get("Update_Status"));
										db.updateSyncStatus(
												TableName,
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
									}
									Toast.makeText(
											getApplicationContext(),
											"DB Sync completed for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(
											getApplicationContext(),
											"Error Occured [Server's JSON response might be invalid] for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Requested resource not found",
											Toast.LENGTH_LONG).show();
								} else if (statusCode == 500) {
									Toast.makeText(
											getApplicationContext(),
											"Something went wrong at server end",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			} else {
				Toast.makeText(getApplicationContext(),
						"SQLite and Remote MySQL DBs are in Sync!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"No data in SQLite DB, please do enter User name to perform Sync action",
					Toast.LENGTH_LONG).show();
		}

		TableName = "employment_government";
		if (FHCPGovList.size() != 0) {
			if (db.dbSyncCount(TableName) != 0) {
				prgDialog.show();
				params.put("usersJSON",
						db.composeJSONfromSQLiteEmploymentGovernment());
				client.post("http://kemhrcvadu.org/HHRWB/insertFHCPGov.php",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for (int i = 0; i < arr.length(); i++) {
										JSONObject obj = (JSONObject) arr
												.get(i);
										System.out.println(obj.get("Backup_Id"));
										System.out.println(obj
												.get("Update_Status"));
										db.updateSyncStatus(
												TableName,
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
									}
									Toast.makeText(
											getApplicationContext(),
											"DB Sync completed for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(
											getApplicationContext(),
											"Error Occured [Server's JSON response might be invalid] for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Requested resource not found",
											Toast.LENGTH_LONG).show();
								} else if (statusCode == 500) {
									Toast.makeText(
											getApplicationContext(),
											"Something went wrong at server end",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			} else {
				Toast.makeText(getApplicationContext(),
						"SQLite and Remote MySQL DBs are in Sync!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"No data in SQLite DB, please do enter User name to perform Sync action",
					Toast.LENGTH_LONG).show();
		}

		TableName = "employment_non_government";
		if (FHCPNonGovList.size() != 0) {
			if (db.dbSyncCount(TableName) != 0) {
				prgDialog.show();
				params.put("usersJSON",
						db.composeJSONfromSQLiteEmploymentNonGovernment());
				client.post("http://kemhrcvadu.org/HHRWB/insertFHCPNonGov.php",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for (int i = 0; i < arr.length(); i++) {
										JSONObject obj = (JSONObject) arr
												.get(i);
										System.out.println(obj.get("Backup_Id"));
										System.out.println(obj
												.get("Update_Status"));
										db.updateSyncStatus(
												TableName,
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
									}
									Toast.makeText(
											getApplicationContext(),
											"DB Sync completed for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(
											getApplicationContext(),
											"Error Occured [Server's JSON response might be invalid] for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Requested resource not found",
											Toast.LENGTH_LONG).show();
								} else if (statusCode == 500) {
									Toast.makeText(
											getApplicationContext(),
											"Something went wrong at server end",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			} else {
				Toast.makeText(getApplicationContext(),
						"SQLite and Remote MySQL DBs are in Sync!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"No data in SQLite DB, please do enter User name to perform Sync action",
					Toast.LENGTH_LONG).show();
		}

		TableName = "informal_health_care_providers";
		if (IFHCPList.size() != 0) {
			if (db.dbSyncCount(TableName) != 0) {
				prgDialog.show();
				params.put("usersJSON", db.composeJSONfromSQLiteIFHCP());
				client.post("http://kemhrcvadu.org/HHRWB/insertIFHCP.php",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for (int i = 0; i < arr.length(); i++) {
										JSONObject obj = (JSONObject) arr
												.get(i);
										System.out.println(obj.get("Backup_Id"));
										System.out.println(obj
												.get("Update_Status"));
										db.updateSyncStatus(
												TableName,
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
									}
									Toast.makeText(
											getApplicationContext(),
											"DB Sync completed for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(
											getApplicationContext(),
											"Error Occured [Server's JSON response might be invalid] for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Requested resource not found",
											Toast.LENGTH_LONG).show();
								} else if (statusCode == 500) {
									Toast.makeText(
											getApplicationContext(),
											"Something went wrong at server end",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			} else {
				Toast.makeText(getApplicationContext(),
						"SQLite and Remote MySQL DBs are in Sync!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"No data in SQLite DB, please do enter User name to perform Sync action",
					Toast.LENGTH_LONG).show();
		}

		TableName = "qualification";
		if (qualificationList.size() != 0) {
			if (db.dbSyncCount(TableName) != 0) {
				prgDialog.show();
				params.put("usersJSON", db.composeJSONfromSQLiteQualification());
				client.post(
						"http://kemhrcvadu.org/HHRWB/insertQualification.php",
						params, new AsyncHttpResponseHandler() {
							@Override
							public void onSuccess(String response) {
								System.out.println(response);
								prgDialog.hide();
								try {
									JSONArray arr = new JSONArray(response);
									System.out.println(arr.length());
									for (int i = 0; i < arr.length(); i++) {
										JSONObject obj = (JSONObject) arr
												.get(i);
										System.out.println(obj.get("Backup_Id"));
										System.out.println(obj
												.get("Update_Status"));
										db.updateSyncStatus(
												TableName,
												obj.get("Backup_Id").toString(),
												obj.get("Update_Status")
														.toString());
									}
									Toast.makeText(
											getApplicationContext(),
											"DB Sync completed for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									Toast.makeText(
											getApplicationContext(),
											"Error Occured [Server's JSON response might be invalid] for "
													+ TableName + "!",
											Toast.LENGTH_LONG).show();
									e.printStackTrace();
								}
							}

							@Override
							public void onFailure(int statusCode,
									Throwable error, String content) {
								// TODO Auto-generated method stub
								prgDialog.hide();
								if (statusCode == 404) {
									Toast.makeText(getApplicationContext(),
											"Requested resource not found",
											Toast.LENGTH_LONG).show();
								} else if (statusCode == 500) {
									Toast.makeText(
											getApplicationContext(),
											"Something went wrong at server end",
											Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(
											getApplicationContext(),
											"Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]",
											Toast.LENGTH_LONG).show();
								}
							}
						});
			} else {
				Toast.makeText(getApplicationContext(),
						"SQLite and Remote MySQL DBs are in Sync!",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(
					getApplicationContext(),
					"No data in SQLite DB, please do enter User name to perform Sync action",
					Toast.LENGTH_LONG).show();
		}*/
	}

	private void writeToSD() throws IOException {
		File sd = Environment.getExternalStorageDirectory();
		File data = Environment.getDataDirectory();
		FileChannel source = null;
		FileChannel destination = null;
		String currentDBPath = "/data/" + "health.care.hhrwb"
				+ "/databases/hhrwb.db";
		String backupDBPath = "/HHRWB/hhrwb_1.db";
		File currentDB = new File(data, currentDBPath);
		File backupDB = new File(sd, backupDBPath);
		try {
			source = new FileInputStream(currentDB).getChannel();
			destination = new FileOutputStream(backupDB).getChannel();
			destination.transferFrom(source, 0, source.size());
			source.close();
			destination.close();
			Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
