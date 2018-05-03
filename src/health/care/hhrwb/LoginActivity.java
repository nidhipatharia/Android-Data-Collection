package health.care.hhrwb;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText username,password;
	SharedPreferences pref;
	Button login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	username= (EditText)findViewById(R.id.etUserName);
	password= (EditText)findViewById(R.id.etPassword);
	login= (Button)findViewById(R.id.btnLogin);
	login.setOnClickListener(new OnClickListener()
	{
		
		@Override
		public void onClick(View v) 
		{
		  if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin"))
		   {
			  pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
			  pref.edit().putString("Data_Entered_By", username.getText().toString()).commit();
			  
			   Toast.makeText(LoginActivity.this,"Valid user", Toast.LENGTH_LONG).show();
			   Intent  objIndent = new Intent(getApplicationContext(),Home.class);
				  //objIndent.putExtra("sdate", vd); 
				//objIndent.putExtra("username",u);
			   startActivity(objIndent);
			   
		   }
		   else
			   Toast.makeText(LoginActivity.this,"InValid user", Toast.LENGTH_LONG).show();
			
			   
			// TODO Auto-generated method stub 
			
		}
	});
	
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	

}
