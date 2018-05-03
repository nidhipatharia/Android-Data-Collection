package health.care.hhrwb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.SqlDateTypeAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "hhrwb.db";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 2);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sqlCreateTableIndividualMaster, sqlCreateTableLocationOfIndividual;
		String sqlCreateTableEmploymentGov, sqlCreateTableEmploymentNonGov;
		String sqlCreateTableIFHCP, sqlCreateTableQualification, sqlCreateTableUserMaster;

		sqlCreateTableIndividualMaster = "CREATE TABLE individual_master ("
				+ "Backup_Id varchar(36) NOT NULL, Location_Id varchar(20) NOT NULL,"
				+ "First_Name varchar(45) NOT NULL, Middle_Name varchar(45) DEFAULT NULL,Last_Name varchar(45) DEFAULT NULL,"
				+ "Father_First_Name varchar(45) NOT NULL, Father_Middle_Name varchar(45) DEFAULT NULL, Father_Last_Name varchar(45) DEFAULT NULL,"
				+ "Mother_First_Name varchar(45) DEFAULT NULL, Mother_Middle_Name varchar(45) DEFAULT NULL, Mother_Last_Name varchar(45) DEFAULT NULL,"
				+ "Date_Of_Birth date NOT NULL, Gender int(11) NOT NULL, Marital_Status int(11) DEFAULT NULL,"
				+ "Mobile_Number varchar(10) DEFAULT NULL, Alternate_Phone_Number varchar(20) DEFAULT NULL, Email_Id varchar(100) DEFAULT NULL,"
				+ "Pan_Card varchar(45) DEFAULT NULL, Voter_Id varchar(45) DEFAULT NULL, Aadhar_Card varchar(45) DEFAULT NULL,"
				+ "Type_Of_Service int(11) NOT NULL, Employment_Type int(11) NOT NULL,"
				+ "Data_Entered_On datetime NOT NULL, Data_Entered_By varchar(20) NOT NULL,"
				+ "photo blob NOT NULL, Update_Status varchar(3))";

		db.execSQL(sqlCreateTableIndividualMaster);

		sqlCreateTableLocationOfIndividual = "CREATE TABLE location_of_individual ("
				+ "Backup_Id varchar(36) NOT NULL,Location_Id varchar(20) NOT NULL,"
				+ "Permanent_Address varchar(500) DEFAULT NULL, Current_Address varchar(500) DEFAULT NULL,"
				+ "Data_Entered_From_Latitude double(15,5) DEFAULT NULL, Data_Entered_From_Longitude double(15,5) DEFAULT NULL,"
				+ "Data_Entered_From_Address varchar(500), Update_Status varchar(3))";

		db.execSQL(sqlCreateTableLocationOfIndividual);

		sqlCreateTableEmploymentGov = "CREATE TABLE employment_government ("
				+ "Backup_Id varchar(36) NOT NULL, Employment_Type int(2) not null, Employment int(2) NOT NULL,"
				+ "Role int(2) NOT NULL, Practicing_Since date NOT NULL,"
				+ "Practicing_Location int(2) NOT NULL, Type_Of_Service int(2) NOT NULL,"
				+ "Registration_Number varchar(45) NOT NULL, Registering_Authority int(2) NOT NULL,"
				+ "Registering_Authority_Other varchar(200) DEFAULT NULL, Update_Status varchar(3))";

		db.execSQL(sqlCreateTableEmploymentGov);

		sqlCreateTableEmploymentNonGov = "CREATE TABLE employment_non_government ("
				+ "Backup_Id varchar(36) NOT NULL,"
				+ "Employment_Type int(2) NOT NULL, Role int(2) NOT NULL,"
				+ "Practicing_Since date NOT NULL, Practicing_Location int(2) NOT NULL,"
				+ "Type_Of_Service int(2) NOT NULL, Registration_Number varchar(45) NOT NULL,"
				+ "Registering_Authority int(2) NOT NULL, Registering_Authority_Other varchar(200) DEFAULT NULL, Update_Status varchar(3))";

		db.execSQL(sqlCreateTableEmploymentNonGov);

		sqlCreateTableIFHCP = "CREATE TABLE informal_health_care_providers ("
				+ "Backup_Id varchar(36) NOT NULL, Practicing_Since date NOT NULL,"
				+ "Practicing_Location int(2) NOT NULL, Prescribing_Method int(2) NOT NULL, Mode_Of_Service int(2) NOT NULL,"
				+ "Vehicle int(2) NOT NULL, Vehicle_Other varchar(200) DEFAULT NULL, Update_Status varchar(3))";

		db.execSQL(sqlCreateTableIFHCP);

		sqlCreateTableQualification = "CREATE TABLE qualification ("
					+ "Backup_Id varchar(36) NOT NULL, Qualification varchar(500) NOT NULL,"
					+ " Qualification_Location int(2) NOT NULL, Update_Status varchar(3))";

		db.execSQL(sqlCreateTableQualification);

		sqlCreateTableUserMaster = "CREATE TABLE user_master ("
				+ "Username varchar(20) NOT NULL, Password varchar(10) DEFAULT NULL,"
				+ "Name varchar(100) DEFAULT NULL, Email varchar(100) DEFAULT NULL, Update_Status varchar(3))";

		db.execSQL(sqlCreateTableUserMaster);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS individual_master");
		db.execSQL("DROP TABLE IF EXISTS location_of_individual");
		db.execSQL("DROP TABLE IF EXISTS employment_government");
		db.execSQL("DROP TABLE IF EXISTS employment_non_government");
		db.execSQL("DROP TABLE IF EXISTS informal_health_care_providers");
		db.execSQL("DROP TABLE IF EXISTS qualification");
		db.execSQL("DROP TABLE IF EXISTS user_master");
		onCreate(db);
	}

	public long insertValues(String tableName, ContentValues cv) {
		SQLiteDatabase db = this.getWritableDatabase();
		long row = -1;
		try {
			row = db.insert(tableName, null, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
		return row;
	}

	public int getCount(String tableName) {
		String sql = "select * from " + tableName;
		int noOfRows;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sql, null);
		c.moveToFirst();
		noOfRows = c.getCount();
		c.close();
		return noOfRows;
	}

	public Cursor FetchValuesFor(String Table_Name, String Backup_Id) {
		SQLiteDatabase db = this.getReadableDatabase();
		String whereCondition = "Backup_Id like '" + Backup_Id + "'";
		Cursor c = db.query(Table_Name, null, whereCondition, null, null, null,
				null);
		return c;
	}

	byte[] getPhoto(String Backup_Id) {
		String[] col = { "photo" };
		String whereCondition = "Backup_Id like '" + Backup_Id + "'";
		byte[] img = null;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.query("individual_master", col, whereCondition, null,
				null, null, null);

		if (c != null) {
			c.moveToFirst();
			do {
				try {
					img = c.getBlob(c.getColumnIndex("photo"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} while (c.moveToNext());
		}
		return img;
	}

	public ArrayList<HashMap<String, String>> getAllIndividuals() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM individual_master";
		int Gender, Marital_Status, Type_Of_Service, Employment_Type;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Gender = cursor.getInt(cursor.getColumnIndex("Gender"));
				
				Marital_Status = cursor.getInt(cursor
						.getColumnIndex("Marital_Status"));
				
				Type_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Type_Of_Service"));
				
				Employment_Type = cursor.getInt(cursor
						.getColumnIndex("Employment_Type"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				
				map.put("Location_Id",
						cursor.getString(cursor.getColumnIndex("Location_Id")));
				
				map.put("First_Name",
						cursor.getString(cursor.getColumnIndex("First_Name")));
				
				map.put("Miiddle_Name",
						cursor.getString(cursor.getColumnIndex("Middle_Name")));
				
				map.put("Last_Name",
						cursor.getString(cursor.getColumnIndex("Last_Name")));
				
				map.put("Father_First_Name", cursor.getString(cursor
						.getColumnIndex("Father_First_Name")));
				
				map.put("Father_Middle_Name", cursor.getString(cursor
						.getColumnIndex("Father_Middle_Name")));
				
				map.put("Father_Last_Name", cursor.getString(cursor
						.getColumnIndex("Father_Last_Name")));
				
				map.put("Mother_First_Name", cursor.getString(cursor
						.getColumnIndex("Mother_First_Name")));
				
				map.put("Mother_Middle_Name", cursor.getString(cursor
						.getColumnIndex("Mother_Middle_Name")));
				
				map.put("Mother_Last_Name", cursor.getString(cursor
						.getColumnIndex("Mother_Last_Name")));
				
				map.put("Date_Of_Birth", cursor.getString(cursor
						.getColumnIndex("Date_Of_Birth")));
				
				map.put("Gender", String.valueOf(Gender));
				
				map.put("Marital_Status", String.valueOf(Marital_Status));
				
				map.put("Mobile_Number", cursor.getString(cursor
						.getColumnIndex("Mobile_Number")));
				
				map.put("Alternate_Phone_Number", cursor.getString(cursor
						.getColumnIndex("Alternate_Phone_Number")));
				
				map.put("Email_Id",
						cursor.getString(cursor.getColumnIndex("Email_Id")));
				
				map.put("Pan_Card",
						cursor.getString(cursor.getColumnIndex("Pan_Card")));
				
				map.put("Voter_Id",
						cursor.getString(cursor.getColumnIndex("Voter_Id")));
				
				map.put("Aadhar_Card",
						cursor.getString(cursor.getColumnIndex("Aadhar_Card")));
				
				map.put("Type_Of_Service", String.valueOf(Type_Of_Service));
				
				map.put("Employment_Type", String.valueOf(Employment_Type));
				
				map.put("Data_Entered_On", cursor.getString(cursor
						.getColumnIndex("Data_Entered_By")));
				
				map.put("Data_Entered_By", cursor.getString(cursor
						.getColumnIndex("Data_Entered_On")));
				
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLiteIndividualMaster() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM individual_master where Update_Status = '"
				+ "no" + "'";
		int Gender, Marital_Status, Type_Of_Service, Employment_Type;
		byte[] img = null;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Gender = cursor.getInt(cursor.getColumnIndex("Gender"));
				
				Marital_Status = cursor.getInt(cursor
						.getColumnIndex("Marital_Status"));
				
				Type_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Type_Of_Service"));
				
				Employment_Type = cursor.getInt(cursor
						.getColumnIndex("Employment_Type"));
				
				img = cursor.getBlob(cursor.getColumnIndex("photo"));
				String pic = Base64.encodeToString(img, Base64.DEFAULT);
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				
				map.put("Location_Id",
						cursor.getString(cursor.getColumnIndex("Location_Id")));
				
				map.put("First_Name",
						cursor.getString(cursor.getColumnIndex("First_Name")));
				
				map.put("Middle_Name",
						cursor.getString(cursor.getColumnIndex("Middle_Name")));
				
				map.put("Last_Name",
						cursor.getString(cursor.getColumnIndex("Last_Name")));
				
				map.put("Father_First_Name", cursor.getString(cursor
						.getColumnIndex("Father_First_Name")));
				
				map.put("Father_Middle_Name", cursor.getString(cursor
						.getColumnIndex("Father_Middle_Name")));
				
				map.put("Father_Last_Name", cursor.getString(cursor
						.getColumnIndex("Father_Last_Name")));
				
				map.put("Mother_First_Name", cursor.getString(cursor
						.getColumnIndex("Mother_First_Name")));
				
				map.put("Mother_Middle_Name", cursor.getString(cursor
						.getColumnIndex("Mother_Middle_Name")));
				
				map.put("Mother_Last_Name", cursor.getString(cursor
						.getColumnIndex("Mother_Last_Name")));
				
				map.put("Date_Of_Birth", cursor.getString(cursor
						.getColumnIndex("Date_Of_Birth")));
				
				map.put("Gender", String.valueOf(Gender));
				
				map.put("Marital_Status", String.valueOf(Marital_Status));
				
				map.put("Mobile_Number", cursor.getString(cursor
						.getColumnIndex("Mobile_Number")));
				
				map.put("Alternate_Phone_Number", cursor.getString(cursor
						.getColumnIndex("Alternate_Phone_Number")));
				
				map.put("Email_Id",
						cursor.getString(cursor.getColumnIndex("Email_Id")));
				
				map.put("Pan_Card",
						cursor.getString(cursor.getColumnIndex("Pan_Card")));
				
				map.put("Voter_Id",
						cursor.getString(cursor.getColumnIndex("Voter_Id")));
				
				map.put("Aadhar_Card",
						cursor.getString(cursor.getColumnIndex("Aadhar_Card")));
				
				map.put("Type_Of_Service", String.valueOf(Type_Of_Service));
				
				map.put("Employment_Type", String.valueOf(Employment_Type));
				
				map.put("Data_Entered_On", cursor.getString(cursor
						.getColumnIndex("Data_Entered_On")));
				
				map.put("Data_Entered_By", cursor.getString(cursor
						.getColumnIndex("Data_Entered_By")));
				
				map.put("photo", pic);
				
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(wordList);
		// Use GSON to serialize Array List to JSON
		return json;
	}

	public ArrayList<HashMap<String, String>> getLocationOfIndividuals() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM location_of_individual";
		double Data_Entered_From_Latitude, Data_Entered_From_Longitude;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Data_Entered_From_Latitude = cursor.getInt(cursor
						.getColumnIndex("Data_Entered_From_Latitude"));
				
				Data_Entered_From_Longitude = cursor.getInt(cursor
						.getColumnIndex("Data_Entered_From_Longitude"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));

				map.put("Location_Id",
						cursor.getString(cursor.getColumnIndex("Location_Id")));

				map.put("Permanent_Address", cursor.getString(cursor
						.getColumnIndex("Permanent_Address")));

				map.put("Current_Address", cursor.getString(cursor
						.getColumnIndex("Current_Address")));

				map.put("Data_Entered_From_Latitude",
						String.valueOf(Data_Entered_From_Latitude));

				map.put("Data_Entered_From_Longitude",
						String.valueOf(Data_Entered_From_Longitude));

				map.put("Data_Entered_From_Address", cursor.getString(cursor
						.getColumnIndex("Data_Entered_From_Address")));
				
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLiteLocationOfIndividual() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT * FROM location_of_individual where Update_Status = '"
				+ "no" + "'";
		double Data_Entered_From_Latitude, Data_Entered_From_Longitude;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Data_Entered_From_Latitude = cursor.getInt(cursor
						.getColumnIndex("Data_Entered_From_Latitude"));
				Data_Entered_From_Longitude = cursor.getInt(cursor
						.getColumnIndex("Data_Entered_From_Longitude"));
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));

				map.put("Location_Id",
						cursor.getString(cursor.getColumnIndex("Location_Id")));

				map.put("Permanent_Address", cursor.getString(cursor
						.getColumnIndex("Permanent_Address")));

				map.put("Current_Address", cursor.getString(cursor
						.getColumnIndex("Current_Address")));

				map.put("Data_Entered_From_Latitude",
						String.valueOf(Data_Entered_From_Latitude));

				map.put("Data_Entered_From_Longitude",
						String.valueOf(Data_Entered_From_Longitude));

				map.put("Data_Entered_From_Address", cursor.getString(cursor
						.getColumnIndex("Data_Entered_From_Address")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(wordList);
		// Use GSON to serialize Array List to JSON
		return json;
	}

	public ArrayList<HashMap<String, String>> getAllEmploymentGovernment() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM employment_government";
		int Employment_Type, Employment, Role, Practicing_Location, Type_Of_Service, Registering_Authority;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Employment_Type = cursor.getInt(cursor
						.getColumnIndex("Employment_Type"));
				
				Employment = cursor.getInt(cursor.getColumnIndex("Employment"));
				
				Role = cursor.getInt(cursor.getColumnIndex("Role"));
				
				Practicing_Location = cursor.getInt(cursor
						.getColumnIndex("Practicing_Location"));
				
				Type_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Type_Of_Service"));
				
				Registering_Authority = cursor.getInt(cursor
						.getColumnIndex("Registering_Authority"));

				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				
				map.put("Employment_Type", String.valueOf(Employment_Type));
				
				map.put("Employment", String.valueOf(Employment));
				
				map.put("Role", String.valueOf(Role));
				
				map.put("Practicing_Since", cursor.getString(cursor
						.getColumnIndex("Practicing_Since")));
				
				map.put("Practicing_Location", String.valueOf(Practicing_Location));
				
				map.put("Type_Of_Service", String.valueOf(Type_Of_Service));
				
				map.put("Registration_Number", cursor.getString(cursor
						.getColumnIndex("Registration_Number")));
				
				map.put("Registering_Authority", String.valueOf(Registering_Authority));
				
				map.put("Registering_Authority_Other",
						cursor.getString(cursor.getColumnIndex("Registering_Authority_Other")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLiteEmploymentGovernment() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM employment_government where Update_Status = '"
				+ "no" + "'";
		int Employment_Type, Employment, Role, Practicing_Location, Type_Of_Service, Registering_Authority;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Employment_Type = cursor.getInt(cursor
						.getColumnIndex("Employment_Type"));
				
				Employment = cursor.getInt(cursor.getColumnIndex("Employment"));
				
				Role = cursor.getInt(cursor.getColumnIndex("Role"));
				
				Practicing_Location = cursor.getInt(cursor
						.getColumnIndex("Practicing_Location"));
				
				Type_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Type_Of_Service"));
				
				Registering_Authority = cursor.getInt(cursor
						.getColumnIndex("Registering_Authority"));

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));

				map.put("Employment_Type", String.valueOf(Employment_Type));
				
				map.put("Employment", String.valueOf(Employment));
				
				map.put("Role", String.valueOf(Role));
				
				map.put("Practicing_Since", cursor.getString(cursor
						.getColumnIndex("Practicing_Since")));
				
				map.put("Practicing_Location", String.valueOf(Practicing_Location));
				
				map.put("Type_Of_Service", String.valueOf(Type_Of_Service));
				
				map.put("Registration_Number", cursor.getString(cursor
						.getColumnIndex("Registration_Number")));
				
				map.put("Registering_Authority", String.valueOf(Registering_Authority));
				
				map.put("Registering_Authority_Other",
						cursor.getString(cursor.getColumnIndex("Registering_Authority_Other")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(wordList);
		// Use GSON to serialize Array List to JSON
		return json;
	}

	public ArrayList<HashMap<String, String>> getAllEmploymentNonGovernment() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "Select * FROM employment_non_government";
		int Employment_Type, Role, Practicing_Location, Type_Of_Service, Registering_Authority;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Employment_Type = cursor.getInt(cursor
						.getColumnIndex("Employment_Type"));
				
				Role = cursor.getInt(cursor.getColumnIndex("Role"));
				
				Practicing_Location = cursor.getInt(cursor
						.getColumnIndex("Practicing_Location"));
				
				Type_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Type_Of_Service"));
				
				Registering_Authority = cursor.getInt(cursor
						.getColumnIndex("Registering_Authority"));

				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));

				map.put("Employment_Type", String.valueOf(Employment_Type));
				
				map.put("Role", String.valueOf(Role));
				
				map.put("Practicing_Since", cursor.getString(cursor
						.getColumnIndex("Practicing_Since")));
				
				map.put("Practicing_Location", String.valueOf(Practicing_Location));
				
				map.put("Type_Of_Service", String.valueOf(Type_Of_Service));
				
				map.put("Registration_Number", cursor.getString(cursor
						.getColumnIndex("Registration_Number")));
				
				map.put("Registering_Authority", String.valueOf(Registering_Authority));
				
				map.put("Registering_Authority_Other",
						cursor.getString(cursor.getColumnIndex("Registering_Authority_Other")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
return wordList;
	}

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLiteEmploymentNonGovernment() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM employment_non_government where Update_Status = '"
				+ "no" + "'";
		int Employment_Type, Role, Practicing_Location, Type_Of_Service, Registering_Authority;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Employment_Type = cursor.getInt(cursor
						.getColumnIndex("Employment_Type"));
				
				Role = cursor.getInt(cursor.getColumnIndex("Role"));
				
				Practicing_Location = cursor.getInt(cursor
						.getColumnIndex("Practicing_Location"));
				
				Type_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Type_Of_Service"));
				
				Registering_Authority = cursor.getInt(cursor
						.getColumnIndex("Registering_Authority"));

				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));

				map.put("Employment_Type", String.valueOf(Employment_Type));
				
				map.put("Role", String.valueOf(Role));
				
				map.put("Practicing_Since", cursor.getString(cursor
						.getColumnIndex("Practicing_Since")));
				
				map.put("Practicing_Location", String.valueOf(Practicing_Location));
				
				map.put("Type_Of_Service", String.valueOf(Type_Of_Service));
				
				map.put("Registration_Number", cursor.getString(cursor
						.getColumnIndex("Registration_Number")));
				
				map.put("Registering_Authority", String.valueOf(Registering_Authority));
				
				map.put("Registering_Authority_Other",
						cursor.getString(cursor.getColumnIndex("Registering_Authority_Other")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(wordList);
		// Use GSON to serialize Array List to JSON
		return json;
	}

	public ArrayList<HashMap<String, String>> getAllIFHCP() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT * FROM informal_health_care_providers";
		int Practicing_Location, Prescribing_Method, Mode_Of_Service, Vehicle;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Practicing_Location = cursor.getInt(cursor.getColumnIndex("Practicing_Location"));
				
				Prescribing_Method = cursor.getInt(cursor
						.getColumnIndex("Prescribing_Method"));
				
				Mode_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Mode_Of_Service"));
				
				Vehicle = cursor.getInt(cursor
						.getColumnIndex("Vehicle"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				map.put("Practicing_Since", cursor.getString(cursor
						.getColumnIndex("Practicing_Since")));
				
				map.put("Practicing_Location", String.valueOf(Practicing_Location));
				
				map.put("Prescribing_Method", String.valueOf(Prescribing_Method));
				
				map.put("Mode_Of_Service", String.valueOf(Mode_Of_Service));
				
				map.put("Vehicle", String.valueOf(Vehicle));
				map.put("Vehicle_Other", cursor.getString(cursor
						.getColumnIndex("Vehicle_Other")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLiteIFHCP() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM informal_health_care_providers where Update_Status = '"
				+ "no" + "'";
		int Practicing_Location, Prescribing_Method, Mode_Of_Service, Vehicle;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Practicing_Location = cursor.getInt(cursor.getColumnIndex("Practicing_Location"));
				
				Prescribing_Method = cursor.getInt(cursor
						.getColumnIndex("Prescribing_Method"));
				
				Mode_Of_Service = cursor.getInt(cursor
						.getColumnIndex("Mode_Of_Service"));
				
				Vehicle = cursor.getInt(cursor
						.getColumnIndex("Vehicle"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				map.put("Practicing_Since", cursor.getString(cursor
						.getColumnIndex("Practicing_Since")));
				
				map.put("Practicing_Location", String.valueOf(Practicing_Location));
				
				map.put("Prescribing_Method", String.valueOf(Prescribing_Method));
				
				map.put("Mode_Of_Service", String.valueOf(Mode_Of_Service));
				
				map.put("Vehicle", String.valueOf(Vehicle));
				map.put("Vehicle_Other", cursor.getString(cursor
						.getColumnIndex("Vehicle_Other")));
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(wordList);
		// Use GSON to serialize Array List to JSON
		return json;
	}

	public ArrayList<HashMap<String, String>> getAllQualification() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM qualification";
		int Qualification_Location;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Qualification_Location = cursor.getInt(cursor.getColumnIndex("Qualification_Location"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				
				map.put("Qualification",
						cursor.getString(cursor.getColumnIndex("Qualification")));
				
				map.put("Qualification_Location", String.valueOf(Qualification_Location));
				
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		return wordList;
	}
	
	

	/**
	 * Compose JSON out of SQLite records
	 * 
	 * @return
	 */
	public String composeJSONfromSQLiteQualification() {
		ArrayList<HashMap<String, String>> wordList;
		wordList = new ArrayList<HashMap<String, String>>();
		String selectQuery = "SELECT  * FROM qualification";// where Update_Status = '"
				//+ "no" + "'";
		int Qualification_Location;
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		if (cursor.moveToFirst()) {
			do {
				Qualification_Location = cursor.getInt(cursor.getColumnIndex("Qualification_Location"));
				
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put("Backup_Id",
						cursor.getString(cursor.getColumnIndex("Backup_Id")));
				
				map.put("Qualification",
						cursor.getString(cursor.getColumnIndex("Qualification")));
				
				map.put("Qualification_Location", String.valueOf(Qualification_Location));
				
				wordList.add(map);
			} while (cursor.moveToNext());
		}
		database.close();
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(wordList);
		// Use GSON to serialize Array List to JSON
		return json;
	}

	/**
	 * Get Sync status of SQLite
	 * 
	 * @return
	 */
	public String getSyncStatus() {
		String msg = null;
		if (this.dbSyncCount("individual_master") == 0
				&& this.dbSyncCount("location_of_individual") == 0
				&& this.dbSyncCount("employment_government") == 0
				&& this.dbSyncCount("employment_non_government") == 0
				&& this.dbSyncCount("informal_health_care_providers") == 0
				&& this.dbSyncCount("qualification") == 0) {
			msg = "SQLite and Remote MySQL DBs are in Sync!";
		} else {
			msg = "DB Sync needed\n";
		}
		return msg;
	}

	/**
	 * Get SQLite records that are yet to be Synced
	 * 
	 * @return
	 */
	public int dbSyncCount(String TableName) {
		int count = 0;
		String selectQuery = "SELECT  * FROM " + TableName
				+ " where Update_Status = '" + "no" + "'";
		SQLiteDatabase database = this.getWritableDatabase();
		Cursor cursor = database.rawQuery(selectQuery, null);
		count = cursor.getCount();
		database.close();
		return count;
	}

	/**
	 * Update Sync status against each User ID
	 * 
	 * @param id
	 * @param status
	 */
	public void updateSyncStatus(String TableName, String id, String status) {
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery = "Update " + TableName + " set Update_Status = '"
				+ status + "' where Backup_Id=" + "'" + id + "'";
		Log.d("query", updateQuery);
		database.execSQL(updateQuery);
		database.close();
	}
	
	public void updateSyncStatustoYes()
	{
		SQLiteDatabase database = this.getWritableDatabase();
		String updateQuery;
		updateQuery = "update individual_master set Update_Status = 'no'";
		database.execSQL(updateQuery);
		updateQuery = "update location_of_individual set Update_Status = 'no'";
		database.execSQL(updateQuery);
		updateQuery = "update employment_government set Update_Status = 'no'";
		database.execSQL(updateQuery);
		updateQuery = "update employment_non_government set Update_Status = 'no'";
		database.execSQL(updateQuery);
		updateQuery = "update informal_health_care_providers set Update_Status = 'no'";
		database.execSQL(updateQuery);
		updateQuery = "update qualification set Update_Status = 'no'";
		database.execSQL(updateQuery);
		
	}
	public Cursor selectQuery(String query)
	{
		Cursor cursor;
		SQLiteDatabase db = this.getReadableDatabase();
		cursor = db.rawQuery(query, null);
		return cursor;
	}
}
