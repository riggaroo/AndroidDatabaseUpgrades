package za.co.riggaroo.databaseupgrades.db;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class DatabaseUpgradesTest {


    private static final String TAG = DatabaseUpgradesTest.class.getCanonicalName();

    /**
     * This test runs through all the database versions from the /androidTest/assets/ folder. It copies the old database to the file path of the application.
     * It tests that the database upgrades to the correct version.
     * If there is an issue with the upgrade, generally a SQLiteException will be thrown and the test will fail.
     * for example:
     * android.database.sqlite.SQLiteException: duplicate column name: calculated_pages_times_rating (code 1): , while compiling: ALTER TABLE book_information ADD COLUMN calculated_pages_times_rating INTEGER;
     *
     * @throws IOException if the database cannot be copied.
     */
    @Test
    public void testDatabaseUpgrades() throws IOException {

        for (int i = 1; i < DatabaseHelper.DATABASE_VERSION; i++) {
            Log.d(TAG, "Testing upgrade from version:" + i);
            DatabaseHelper.clearInstance();
            copyDatabase(i);

            DatabaseHelper databaseHelperNew = DatabaseHelper.getInstance(InstrumentationRegistry.getTargetContext());
            Log.d(TAG, " New Database Version:" + databaseHelperNew.getWritableDatabase().getVersion());
            Assert.assertEquals(DatabaseHelper.DATABASE_VERSION, databaseHelperNew.getWritableDatabase().getVersion());
        }

    }


    private void copyDatabase(int version) throws IOException {
        String dbPath = InstrumentationRegistry.getTargetContext().getDatabasePath(DatabaseHelper.DATABASE_NAME).getAbsolutePath();

        String dbName = String.format("database_v%d.db", version);
        InputStream mInput = InstrumentationRegistry.getContext().getAssets().open(dbName);

        OutputStream mOutput = new FileOutputStream(dbPath);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }
}
