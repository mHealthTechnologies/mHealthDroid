package communicationManager.storage;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

class DatabaseHelper extends SQLiteOpenHelper {

	
	private static String external = "Environment.getExternalStorageDirectory()" + "/";
	private static String standarDatabaseName = "MyDB";
	
	/**
	 * Constructor. Creates a new DB
	 * @param context  IU current activity context
	 */
	DatabaseHelper(Context context) {
		super(context, Environment.getExternalStorageDirectory() + "/"
				+ DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		try {

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
}