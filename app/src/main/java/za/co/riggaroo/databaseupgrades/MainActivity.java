package za.co.riggaroo.databaseupgrades;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import za.co.riggaroo.databaseupgrades.db.BookEntry;
import za.co.riggaroo.databaseupgrades.db.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView textViewBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(this);


        //Note - you shouldn't do this kind of stuff on the main thread in production. This should go onto a background thread.
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(BookEntry.COL_BOOKNAME, "Life of Pi");
        contentValues.put(BookEntry.COL_DESCRIPTION, "Yann Martel's Life of Pi is the story of a young man who survives a harrowing shipwreck and months in a lifeboat with a large Bengal tiger named Richard Parker. The beginning of the novel covers Pi's childhood and youth.");
        contentValues.put(BookEntry.COL_NO_PAGES, 24325);
        database.insert(BookEntry.TABLE_NAME, null, contentValues);

        ContentValues contentValues2 = new ContentValues();
        contentValues2.put(BookEntry.COL_BOOKNAME, "Gone Girl");
        contentValues2.put(BookEntry.COL_DESCRIPTION, "In Carthage, Mo., former New York-based writer Nick Dunne (Ben Affleck) and his glamorous wife Amy (Rosamund Pike) present a portrait of a blissful marriage to the public. However, when Amy goes missing on the couple's fifth wedding anniversary, Nick becomes the prime suspect in her disappearance. The resulting police pressure and media frenzy cause the Dunnes' image of a happy union to crumble, leading to tantalizing questions about who Nick and Amy truly are.");
        contentValues2.put(BookEntry.COL_NO_PAGES, 45425);
        database.insert(BookEntry.TABLE_NAME, null, contentValues2);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor c = db.query(BookEntry.TABLE_NAME, null, null, null, null, null, null);

        String books = "";
        while (c.moveToNext()) {
            String bookName =  c.getString(c.getColumnIndex(BookEntry.COL_BOOKNAME)) ;
            String bookDescription = c.getString(c.getColumnIndex(BookEntry.COL_DESCRIPTION));
            books+= bookName + " - " + bookDescription + "\r\n"  ;
            Log.d(TAG, "Book Name:" + bookName);

        }
        c.close();
        textViewBooks = (TextView) findViewById(R.id.text_view_books);
        textViewBooks.setText(books);

    }
}
