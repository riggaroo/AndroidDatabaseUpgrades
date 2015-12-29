package za.co.riggaroo.databaseupgrades.db;


import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "database.db";
    private static final String TAG = DatabaseHelper.class.getName();

    private static DatabaseHelper mInstance = null;
    private final Context context;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized DatabaseHelper getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BookEntry.SQL_CREATE_BOOK_ENTRY_TABLE);
        // The rest of your create scripts go here.

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "Updating table from " + oldVersion + " to " + newVersion);
        // You will not need to modify this unless you need to do some android specific things.
        // When upgrading the database, all you need to do is add a file to the raw folder and name it:
        // from_1_to_2.sql with the version that you are upgrading to as the last version.
        try {
            for (int i = oldVersion; i < newVersion; ++i) {

                String migrationName = String.format("from_%d_to_%d", i, (i + 1));
                Log.e(TAG, "Looking for migration file: " + migrationName);

                int migrationFileResId = context.getResources()
                        .getIdentifier(migrationName, "raw", context.getPackageName());

                if (migrationFileResId != 0) {
                    Log.e(TAG, "Migration script found executing: " + migrationName);
                    readAndExecuteSQLScript(db, context, migrationFileResId);
                } else {
                    Log.e(TAG, "Migration script not found: " + migrationName);
                }
            }
        } catch (Exception exception) {
            Log.e(TAG, "Exception running upgrade script:", exception);
        }

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void readAndExecuteSQLScript(SQLiteDatabase db, Context ctx, int sqlScriptResId) {
        if (sqlScriptResId == 0) {
            throw new IllegalArgumentException("No SQL script found for specified resource.");
        }

        Log.d(TAG, "Script found. Executing...");
        Resources res = ctx.getResources();
        BufferedReader reader = null;

        try {
            InputStream is = res.openRawResource(sqlScriptResId);
            InputStreamReader isr = new InputStreamReader(is);
            reader = new BufferedReader(isr);
            executeSQLScript(db, reader);
        } catch (IOException e) {
            Log.e(TAG, "IOException:", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException:", e);
                }
            }
        }

    }

    private void executeSQLScript(SQLiteDatabase db, BufferedReader reader) throws IOException {
        String line;
        StringBuilder statement = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            statement.append(line);
            statement.append("\n");
            if (line.endsWith(";")) {
                db.execSQL(statement.toString());
                statement = new StringBuilder();
            }
        }
    }

}

