package health.care.hhrwb;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelperPreLoaded extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String DB_PATH = "/data/data/health.care.hhrwb/databases/";

	private static String PRE_LOADED_DB_NAME = "hhrwb";

	private SQLiteDatabase myDataBase;

	private final Context myContext;

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataBaseHelperPreLoaded(Context context) {

		super(context, PRE_LOADED_DB_NAME, null, 1);
		this.myContext = context;
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			// do nothing - database already exist
		} else {

			// By calling this method and empty database will be created into
			// the default system path
			// of your applica tion so we are gonna be able to overwrite that
			// database with our database.
			

			try {
				this.getReadableDatabase();
				copyDataBase();

			} catch (IOException e) {

				throw new Error("Error copying database");

			}
		}

	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {

		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + PRE_LOADED_DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READONLY);

		} catch (SQLiteException e) {

			// database does't exist yet.

		}

		if (checkDB != null) {

			checkDB.close();

		}

		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(PRE_LOADED_DB_NAME);

		// Path to the just created empty db
		String outFileName = DB_PATH + PRE_LOADED_DB_NAME;

		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = DB_PATH + PRE_LOADED_DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
	// to you to create adapters for your views.

	public List<String> getAllDistricts() {
		List<String> districts = new ArrayList<String>();

		// Select All Query
		String selectQuery = "SELECT distinct district_code,district_name "
				+ "FROM location_master where district_code is not null";

		try {
			myDataBase = this.getReadableDatabase();
			Cursor cursor = myDataBase.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					districts.add(cursor.getString(0) + " - "
							+ cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// returning lables
		return districts;
	}

	public List<String> getAllBlocks(String district) {
		List<String> blocks = new ArrayList<String>();

		String district_code;
		district_code = district.substring(0, 2);

		// Select All Query
		String selectQuery = "SELECT distinct block_code, block_name "
				+ "FROM location_master where "
				+ "block_code is not null and district_code like '"
				+ district_code + "'";

		try {
			myDataBase = this.getReadableDatabase();
			Cursor cursor = myDataBase.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					blocks.add(cursor.getString(0) + " - "
							+ cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// returning lables
		return blocks;
	}

	public List<String> getAllGramPanchayat(String district, String block) {
		List<String> panchayats = new ArrayList<String>();

		String district_code, block_code;
		district_code = district.substring(0, 2);
		block_code = block.substring(0, 2);

		// Select All Query
		String selectQuery = "SELECT distinct gp_code, gp_name "
				+ "FROM location_master where gp_code is not null "
				+ "and district_code like '" + district_code
				+ "' and block_code like '" + block_code + "'";

		try {
			myDataBase = this.getReadableDatabase();
			Cursor cursor = myDataBase.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					panchayats.add(cursor.getString(0) + " - "
							+ cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// returning lables
		return panchayats;
	}

	public List<String> getAllMunicipality(String district) {
		List<String> municpalities = new ArrayList<String>();

		String district_code;
		district_code = district.substring(0, 2);

		// Select All Query
		String selectQuery = "SELECT distinct municipality_code, municipality_name "
				+ "FROM location_master where "
				+ "municipality_code is not null and district_code like '"
				+ district_code + "'";

		try {
			myDataBase = this.getReadableDatabase();
			Cursor cursor = myDataBase.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					municpalities.add(cursor.getString(0) + " - "
							+ cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// returning lables
		return municpalities;
	}

	public List<String> getAllMunicipaCorporation(String district) {
		List<String> municpalCorporations = new ArrayList<String>();

		String district_code;
		district_code = district.substring(0, 2);

		// Select All Query
		String selectQuery = "SELECT distinct municipal_corporation_code, municipal_corporation_name "
				+ "FROM location_master where "
				+ "municipal_corporation_code is not null and district_code like '"
				+ district_code + "'";

		try {
			myDataBase = this.getReadableDatabase();
			Cursor cursor = myDataBase.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					municpalCorporations.add(cursor.getString(0) + " - "
							+ cursor.getString(1));
				} while (cursor.moveToNext());
			}

			// closing connection
			cursor.close();
			myDataBase.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// returning lables
		return municpalCorporations;
	}
	public String getLocation(String Location_Id)
	{
		String Location = "",sql="";
		myDataBase = this.getReadableDatabase();
		int areaType = Integer.parseInt(Location_Id.substring(0,1));
		Cursor c;
		if(areaType == 1)
		{
			sql = "select district_name,block_name,gp_name from location_master where _Id like '" + Location_Id + "'";
			c = myDataBase.rawQuery(sql, null);
			c.moveToFirst();
			Location = "District: " + c.getString(0) + " Block:" + c.getString(1) + " Gram Panchayat: " + c.getString(2);
			c.close();
		}
		else if(areaType == 2)
		{	
			sql = "select district_name,municipality_name from location_master where _Id like '" + Location_Id + "'";
			c = myDataBase.rawQuery(sql, null);
			c.moveToFirst();
			Location = "District: " + c.getString(0) + " Municipality:" + c.getString(1);
			c.close();
		}
		else if(areaType == 3)
		{	
			sql = "select district_name,municipal_corporation_name from location_master where _Id like '" + Location_Id + "'";
			c = myDataBase.rawQuery(sql, null);
			c.moveToFirst();
			Location = "District: " + c.getString(0) + " Municipal Corporation:" + c.getString(1);
			c.close();
		}
		return Location;
	}
	public String getLocationId(int Area_Type, String District_Code,
			String Block_Code, String GP_Code, String Municipality_Code,
			String Municipal_Corporation_Code) {
		String Location_Id = "12345678", sqlFetchLocationId="";
		if (Area_Type == 1) {
			sqlFetchLocationId = "select _Id from location_master where area_type = "
					+ Area_Type
					+ " and district_code like '"
					+ District_Code
					+ "' and block_code like '"
					+ Block_Code
					+ "' and gp_code like '"
					+ GP_Code
					+ "' and municipality_code is null and municipal_corporation_code is null";
		}
		if (Area_Type == 2) {
			sqlFetchLocationId = "select _Id from location_master where area_type = "
					+ Area_Type
					+ " and district_code like '"
					+ District_Code
					+ "' and block_code is null and gp_code is null and"
					+ " municipality_code like '"
					+ Municipality_Code
					+ "' and municipal_corporation_code is null";
		}
		if (Area_Type == 3) {
			sqlFetchLocationId = "select _Id from location_master where area_type = "
					+ Area_Type
					+ " and district_code like '"
					+ District_Code
					+ "' and block_code is null and gp_code is null and"
					+ " municipality_code is null"
					+ " and municipal_corporation_code like '" + Municipal_Corporation_Code + "'";
		}
		if(sqlFetchLocationId !="")
		{myDataBase = this.getReadableDatabase();
		Cursor cursor = myDataBase.rawQuery(sqlFetchLocationId, null);
		cursor.moveToFirst();
		Location_Id = cursor.getString(0);
		cursor.close();
		myDataBase.close();
		}
		return Location_Id;
	}

}