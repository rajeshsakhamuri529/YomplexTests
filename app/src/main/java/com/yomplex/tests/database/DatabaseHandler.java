package com.yomplex.tests.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.yomplex.tests.entity.DailyChallenge;
import com.yomplex.tests.entity.RevisionEntity;
import com.yomplex.tests.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "revision";
    private static final String TABLE_CONTACTS = "revision_status";
    private static final String KEY_ID = "doc_id";
    private static final String KEY_VERSION = "pdf_version";


    private static final String TABLE_BOOKS_STATUS = "books_status";

    private static final String KEY_RID = "doc_rid";
    private static final String KEY_READ_STATUS = "bookstatus";

    private static final String TABLE_DAILY_CHALLENGE = "DailyChallenge";
    private static final String KEY_QID = "qid";
    private static final String KEY_DOC_ID = "docid";
    private static final String KEY_QUESTION_VERSION = "questionversion";
    private static final String KEY_QTYPE = "qtype";
    private static final String KEY_DATE = "date";
    private static final String KEY_TITLE = "title";
    private static final String KEY_ATTEMPT = "attempt";



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, Utils.DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_BOOKS_STATUS_TABLE = "CREATE TABLE " + TABLE_BOOKS_STATUS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_READ_STATUS + ","+ KEY_RID + " TEXT)";
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " TEXT PRIMARY KEY," + KEY_VERSION + " TEXT)";

        String CREATE_DAILY_CHALLENGE_TABLE = "CREATE TABLE " + TABLE_DAILY_CHALLENGE + "("
                + KEY_DOC_ID + " TEXT PRIMARY KEY," + KEY_QID + " TEXT, " + KEY_QUESTION_VERSION + " TEXT, " + KEY_QTYPE + " TEXT, " + KEY_DATE + " TEXT, " + KEY_TITLE + " TEXT, " + KEY_ATTEMPT + " INTEGER)";


        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_DAILY_CHALLENGE_TABLE);
        db.execSQL(CREATE_BOOKS_STATUS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_CHALLENGE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS_STATUS);
        // Create tables again
        onCreate(db);

    }

    public void insertBooksStatus(String id, String rid, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); // Contact Name
        values.put(KEY_RID, rid); // Contact Phone
        values.put(KEY_READ_STATUS, status);
        // Inserting Row
        db.insert(TABLE_BOOKS_STATUS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deleteAllBookStatus() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_BOOKS_STATUS);
        db.close();
    }

    public int updateBookaReadStatus(String rid, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_READ_STATUS, status);

        // updating row
        return db.update(TABLE_BOOKS_STATUS, values, KEY_RID + " = ?",
                new String[] { String.valueOf(rid) });
    }

    public String getBookReadStatus(String rid) {

        String status="";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS_STATUS + " WHERE " + KEY_RID + "='" + rid+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                status = (cur.getString(cur.getColumnIndex(KEY_READ_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();


        // return contact
        return status;
    }

    public void insertDailyChallenge(DailyChallenge dailyChallenge) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_QID, dailyChallenge.getQid()); // Contact Name
        values.put(KEY_QTYPE, dailyChallenge.getQtype()); // Contact Phone
        values.put(KEY_DATE, dailyChallenge.getDate()); // Contact Name
        values.put(KEY_TITLE, dailyChallenge.getTitle()); // Contact Phone
        values.put(KEY_ATTEMPT, dailyChallenge.getAttempt()); // Contact Name
        values.put(KEY_DOC_ID, dailyChallenge.getDocid());
        values.put(KEY_QUESTION_VERSION, dailyChallenge.getQuestionversion());
        // Inserting Row
        db.insert(TABLE_DAILY_CHALLENGE, KEY_DOC_ID, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }



    // code to update the single contact
    public int updateDailyChallengeAttempt(String attempt, String docid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_ATTEMPT, attempt);

        // updating row
        return db.update(TABLE_DAILY_CHALLENGE, values, KEY_DOC_ID + " = ?",
                new String[] { String.valueOf(docid) });
    }

    public int updateQuestionVersion(String questionversion, String docid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_QUESTION_VERSION, questionversion);

        // updating row
        return db.update(TABLE_DAILY_CHALLENGE, values, KEY_DOC_ID + " = ?",
                new String[] { String.valueOf(docid) });
    }

    // code to get the single contact
    public DailyChallenge getDailyChallenge(String qid) {
        DailyChallenge dailyChallenge=new DailyChallenge();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_DAILY_CHALLENGE + " WHERE " + KEY_DOC_ID + "='" + qid+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                dailyChallenge.setQid(cur.getString(cur.getColumnIndex(KEY_QID)));
                dailyChallenge.setQtype(cur.getString(cur.getColumnIndex(KEY_QTYPE)));
                dailyChallenge.setTitle(cur.getString(cur.getColumnIndex(KEY_TITLE)));
                dailyChallenge.setDate(cur.getString(cur.getColumnIndex(KEY_DATE)));
                dailyChallenge.setAttempt(cur.getString(cur.getColumnIndex(KEY_ATTEMPT)));

                dailyChallenge.setDocid(cur.getString(cur.getColumnIndex(KEY_DOC_ID)));
                dailyChallenge.setQuestionversion(cur.getString(cur.getColumnIndex(KEY_QUESTION_VERSION)));


            } while (cur.moveToNext());
        }
        cur.close();
        db.close();


        // return contact
        return dailyChallenge;
    }

    // code to get the single contact
    public List<DailyChallenge> getDailyChallengeList() {
        List<DailyChallenge> dailychallengeList = new ArrayList<DailyChallenge>();

       // SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_DAILY_CHALLENGE);
        String selectQuery = "SELECT  * FROM " + TABLE_DAILY_CHALLENGE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        if (cur.moveToFirst()) {
            do {
                DailyChallenge dailyChallenge=new DailyChallenge();
                dailyChallenge.setQid(cur.getString(cur.getColumnIndex(KEY_QID)));
                dailyChallenge.setQtype(cur.getString(cur.getColumnIndex(KEY_QTYPE)));
                dailyChallenge.setTitle(cur.getString(cur.getColumnIndex(KEY_TITLE)));
                dailyChallenge.setDate(cur.getString(cur.getColumnIndex(KEY_DATE)));
                dailyChallenge.setAttempt(cur.getString(cur.getColumnIndex(KEY_ATTEMPT)));
                dailyChallenge.setDocid(cur.getString(cur.getColumnIndex(KEY_DOC_ID)));
                dailyChallenge.setQuestionversion(cur.getString(cur.getColumnIndex(KEY_QUESTION_VERSION)));
                dailychallengeList.add(dailyChallenge);

            } while (cur.moveToNext());
        }
        cur.close();
        db.close();


        // return contact
        return dailychallengeList;
    }

    public void addContact(RevisionEntity contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getDocumentId()); // Contact Name
        values.put(KEY_VERSION, contact.getPdfVersion()); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deleteAllRevisions() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONTACTS);
        db.close();
    }

    // code to get the single contact
    RevisionEntity getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        RevisionEntity contact = new RevisionEntity();
        contact.setDocumentId(cursor.getString(0));
        contact.setPdfVersion(cursor.getString(1));
        // return contact
        return contact;
    }

    // code to get all contacts in a list view
    public List<RevisionEntity> getAllContacts() {
        List<RevisionEntity> contactList = new ArrayList<RevisionEntity>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RevisionEntity contact = new RevisionEntity();
                //contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setDocumentId(cursor.getString(0));
                contact.setPdfVersion(cursor.getString(1));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }

    // code to update the single contact
    public int updateContact(RevisionEntity contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_VERSION, contact.getPdfVersion());

        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getDocumentId()) });
    }
}
