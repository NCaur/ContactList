package com.example.labtest2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "contactdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    public static final String TABLE_NAME = "mycontacts";

    // below variable is for our id column.
    public static final String ID_COL = "id";

    // below variable is for our  name column
    public static final String NAME_COL = "name";

    // below variable id for our contact number column.
    public static final String PHNNUMBER_COL = "ContactNumber";
    public static final String COLUMN_PICTURE = "picture";
    // below variable for our email address column.
    public static final String EMailAddress_COL = "email";

    // below variable is for our address column.
    public static final String Address_COL = "address";




    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + PHNNUMBER_COL + " TEXT,"
                + EMailAddress_COL + " TEXT,"
                + Address_COL + " TEXT,"
                +COLUMN_PICTURE+" BLOB)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new contact to our sqlite database.
    public void addNewContact(String Name, String contactNumber, String email, String homeaddress, byte[] profile_image) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, Name);
        values.put(PHNNUMBER_COL, contactNumber);
        values.put(EMailAddress_COL, email);
        values.put(Address_COL, homeaddress);
        values.put(COLUMN_PICTURE, profile_image);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public Cursor getRecords()
    {
        String selectQuery = "Select * from "+TABLE_NAME+" Order By "+NAME_COL;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery,null);
    }
    public int deleteRecord(int id )
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,ID_COL +" = "+id,null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // we have created a new method for searching the contacts
    public ArrayList<ContactModal> searchContacts(String Name) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        //  Cursor cursorContacts = db.rawQuery("SELECT * FROM "
        //         + TABLE_NAME + " where " + " name " + " like '%" + new String[]{Name}
        //         + "%'", null);

        String query = "SELECT * FROM "+ TABLE_NAME +" WHERE name LIKE '%"+ Name +"%'";
        //    "LIKE '%"+ Name +"%'";

        Cursor cursorContacts = db.rawQuery(query, null);

        // on below line we are creating a new array list.
        ArrayList<ContactModal> contactModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorContacts != null)

        {
            if (cursorContacts.moveToFirst()) {
                do {
                    // on below line we are adding the data from cursor to our array list.
                    contactModalArrayList.add(new ContactModal(cursorContacts.getString(1),
                            cursorContacts.getString(2),
                            cursorContacts.getString(3),
                            cursorContacts.getString(4)));
                } while (cursorContacts.moveToNext());
                // moving our cursor to next.
            }
        }
        // at last closing our cursor
        // and returning our array list.
        cursorContacts.close();
        return contactModalArrayList;
    }

    public Cursor getRecord(int id) {
        String selectQuery = "Select * from "+TABLE_NAME+" where "+ID_COL+" = "+id;
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery(selectQuery,null);
    }

    public void updateContact(String name, String contact, String email, String homeaddress, byte[] byteArray, int id) {
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, name);
        values.put(PHNNUMBER_COL, contact);
        values.put(EMailAddress_COL, email);
        values.put(Address_COL, homeaddress);
        values.put(COLUMN_PICTURE, byteArray);

        // after adding all values we are passing
        // content values to our table.
        db.update(TABLE_NAME, values,ID_COL +" = "+id,null);

        // at last we are closing our
        // database after adding database.
        db.close();
    }
}
