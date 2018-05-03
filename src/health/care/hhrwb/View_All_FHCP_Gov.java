package health.care.hhrwb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class View_All_FHCP_Gov extends Home {

	private List<String> List = new ArrayList<String>();
	private DataBaseHelper db;
	private ListView lv_fhcp_gov;
	SharedPreferences pref;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_all_fhcp_gov);

		db = new DataBaseHelper(getApplicationContext());
		lv_fhcp_gov = (ListView) findViewById(R.id.lvFHCPGov);
		/*try {
			writeToSD();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		showList();
	}

	private void showList() {

		ArrayList<ListHCP> fhcpList = new ArrayList<ListHCP>();
		fhcpList.clear();
		DataBaseHelper db = new DataBaseHelper(getApplicationContext());

		String query = "SELECT a.Backup_Id,a.First_Name,a.Middle_Name,a.Last_Name,a.Gender,b.Role FROM individual_master a, employment_government b where a.Backup_Id = b.Backup_Id";
		Cursor c1 = db.selectQuery(query);
		if (c1 != null && c1.getCount() != 0) {
			if (c1.moveToFirst()) {
				do {
					ListHCP fhcpListItems = new ListHCP();

					fhcpListItems.setBackupId(c1.getString(c1
							.getColumnIndex("Backup_Id")));
					fhcpListItems.setFirstName(c1.getString(c1
							.getColumnIndex("First_Name")));
					fhcpListItems.setMiddleName(c1.getString(c1
							.getColumnIndex("Middle_Name")));
					fhcpListItems.setLastName(c1.getString(c1
							.getColumnIndex("Last_Name")));
					fhcpListItems.setGender(c1.getInt(c1
							.getColumnIndex("Gender")));
					fhcpListItems.setRole(c1.getInt(c1
							.getColumnIndex("Role")));
					fhcpList.add(fhcpListItems);

				} while (c1.moveToNext());
			}
		}
		c1.close();

		FHCPListAdapter fhcpListAdapter = new FHCPListAdapter(
				View_All_FHCP_Gov.this, fhcpList);
		lv_fhcp_gov.setAdapter(fhcpListAdapter);
		lv_fhcp_gov.setOnItemClickListener(new OnItemClickListener() {
			  public void onItemClick(AdapterView<?> parent, View view,
			      int position, long id) {
				  
				// Getting the Container Layout of the ListView
	                RelativeLayout linearLayoutParent = (RelativeLayout) view;

	                // Getting the inner Linear Layout
	                TextView tv_backup_id = (TextView) linearLayoutParent.getChildAt(0);
	                String Backup_Id = tv_backup_id.getText().toString();
	                pref = getApplicationContext().getSharedPreferences("MyPref", 0);
	                pref.edit().putString("Backup_Id",Backup_Id).commit();

	                Toast.makeText(getBaseContext(), tv_backup_id.getText().toString(), Toast.LENGTH_SHORT).show();
				  	
			        Intent intent = new Intent(getApplicationContext(), Retrieve_Details_FHCP.class);
			        startActivity(intent);
			  }
			});
	}
	
}
