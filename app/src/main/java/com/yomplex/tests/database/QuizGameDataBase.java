package com.yomplex.tests.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;


import com.yomplex.tests.BuildConfig;
import com.yomplex.tests.model.Books;
import com.yomplex.tests.model.Challenge;
import com.yomplex.tests.model.Course;
import com.yomplex.tests.model.PlayCount;
import com.yomplex.tests.model.QuizScore;
import com.yomplex.tests.model.TestDownload;
import com.yomplex.tests.model.TestQuiz;
import com.yomplex.tests.model.TestQuizFinal;
import com.yomplex.tests.model.User;
import com.yomplex.tests.utils.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.os.Build.*;

public class QuizGameDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quizgame";

    private static final String TABLE_TEST_TIMER= "TIMER";
    private static final String KEY_TEST_TIMER_TIME = "timertime";



    private static final String TABLE_TEST_CONTENT_DOWNLOAD = "testcontentdownload";
    private static final String KEY_TEST_CONTENT_VERSION = "TestContentVersion";
    private static final String KEY_TEST_CONTENT_URL = "TestContentUrl";
    private static final String KEY_TEST_TYPE = "testtype";
    private static final String KEY_TEST_DOWNLOAD_STATUS = "downloadstatus";

    private static final String TABLE_QUIZ_PLAY = "quizplay";
    private static final String KEY_TOTAL_QUESTIONS = "totalquestions";
    private static final String KEY_ANSWERS = "answers";
    private static final String KEY_QUESTIONS = "questions";
    private static final String KEY_OPTIONS = "options";
    private static final String KEY_STATUS = "status";

    private static final String KEY_QUESTION_ANSWER = "questionanswer";
    private static final String KEY_TITLE = "title";
    private static final String KEY_QUESTION_PATH_TYPE = "quepathtype";

    private static final String TABLE_QUIZ_TOPICS = "quiztopics";
    private static final String KEY_SERIAL_NUMBER = "serialno";
    private static final String KEY_TOPIC_NAME = "topicname";
    private static final String KEY_LAST_PLAYED = "lastplayed";


    private static final String TABLE_QUIZ_PLAY_WITH_TIME = "quizplaywithtime";

    private static final String TABLE_QUIZ_WITH_TIMER_FINAL = "quizwithtimer";
    private static final String KEY_TYPE_OF_PLAY = "typeofplay";
    private static final String KEY_READ_DATA = "readdata";
    private static final String KEY_ORIGINAL_TEST_NAME = "originalname";
    private static final String KEY_ANSWER_STATUS = "answerstatus";
    private static final String KEY_PRESENT_DATE = "pdate";
    private static final String KEY_TIME_TAKEN = "timetaken";
    private static final String KEY_REVIEW_EXIST = "reviewexist";

    private static final String KEY_ID = "id";
    private static final String TABLE_CONTENT_CHECK_DATE = "contentcheckdate";
    private static final String BOOK_CONTENT_CHECK_DATE = "bookcontentcheckdate";

    private static final String TABLE_CHALLENGE_STATUS = "challenge_status";
    private static final String KEY_DATE = "date";
    private static final String KEY_CHALLENGE_STATUS = "challengestatus";


    private static final String TABLE_CHALLENGE = "challenge";

    private static final String KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS = "challengequizans";
    private static final String KEY_CHALLENGE_TEST_CORRECT_ANSWERS = "challengetestans";
    private static final String KEY_CHALLENGE_QUIZ_STATUS = "challengequizstatus";
    private static final String KEY_CHALLENGE_TEST_STATUS = "challengeteststatus";

    private static final String TABLE_CHALLENGE_WEEKLY = "weeklychallenge";

    private static final String KEY_CHALLENGE_DATE_FROM = "challengedatefrom";
    private static final String KEY_CHALLENGE_DATE_TO = "challengeto";
    private static final String KEY_CHALLENGE_TEST_PASS_COUNT = "challengetestpasscount";


    private static final String TABLE_QUIZ_PLAY_SCORE = "quizplayscore";
    private static final String KEY_WEEK_YEAR = "weekofyear";
    private static final String KEY_HIGHEST_SCORE = "highestscore";


    private static final String TABLE_USER_SYNC = "usersync";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_DEVICE_ID = "userdeviceid";
    private static final String KEY_USER_PHONE = "userphone";
    private static final String KEY_USER_CREATEDON = "createdon";
    private static final String KEY_USER_UPDATEDON = "updatedon";
    private static final String KEY_USER_FIREBASE_TOKEN = "firebasetoken";
    private static final String KEY_SYNC_STATUS = "syncstatus";

    private static final String TABLE_PLAY_COUNT = "playcount";

    private static final String KEY_COURSE = "course";
    private static final String KEY_TOPIC = "topic";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_PLAY_COUNT = "playcount";

    private static final String TABLE_PLAY_COUNT_FILE = "playcountfile";

    private static final String TABLE_COURSE = "course";
    private static final String KEY_COURSE_EXIST= "courseexist";

    private static final String TABLE_BOOKS = "books";

    private static final String KEY_CATEGORY= "Category";
    private static final String KEY_SOURCE_URL = "SourceUrl";
    private static final String KEY_THUMB_NAIL = "Thumbnail";
    private static final String KEY_PUBLISHED_ON = "PublishedOn";
    private static final String KEY_READ_STATUS = "readstatus";
    private static final String KEY_STARRED_STATUS = "starredstatus";

    private static final String KEY_SORT_ORDER = "sortorder";
    private static final String KEY_VERSION = "version";
    private static final String KEY_BOOK_DOWNLOAD_STATUS = "bookdownloadstatus";

    private static final String KEY_COPY_STATUS = "copystatus";
    private static final String KEY_FOLDER_NAME = "foldername";
    private static final String KEY_READ_FILE_STATUS = "readfilestatus";
    private static final String KEY_VISIBILITY = "visibility";

    String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_TITLE + " TEXT, "
            + KEY_FOLDER_NAME + " TEXT,"
            + KEY_CATEGORY + " TEXT, "
            + KEY_SOURCE_URL + " TEXT, "
            + KEY_THUMB_NAIL + " TEXT,"
            + KEY_PUBLISHED_ON + " TEXT,"
            + KEY_READ_STATUS + " TEXT,"
            + KEY_STARRED_STATUS + " TEXT,"
            + KEY_SORT_ORDER + " INTEGER,"
            + KEY_VERSION + " TEXT,"
            + KEY_VISIBILITY + " TEXT,"
            + KEY_COPY_STATUS + " TEXT,"
            + KEY_READ_FILE_STATUS + " TEXT,"
            + KEY_BOOK_DOWNLOAD_STATUS + " TEXT)";

    String CREATE_TABLE_COURSE = "CREATE TABLE " + TABLE_COURSE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_COURSE + " TEXT, "
            + KEY_COURSE_EXIST + " INTEGER)";

    String CREATE_TABLE_PLAY_COUNT = "CREATE TABLE " + TABLE_PLAY_COUNT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_COURSE + " TEXT, "
            + KEY_TOPIC + " TEXT, "
            + KEY_LEVEL + " TEXT, "
            + KEY_PLAY_COUNT + " INTEGER)";

    String CREATE_TABLE_USER_SYNC = "CREATE TABLE " + TABLE_USER_SYNC + "("
            + KEY_USER_EMAIL + " TEXT, "
            + KEY_USER_DEVICE_ID + " TEXT, "
            + KEY_USER_PHONE + " TEXT, "
            + KEY_USER_CREATEDON + " TEXT,"
            + KEY_USER_UPDATEDON + " TEXT,"
            + KEY_USER_FIREBASE_TOKEN + " TEXT,"
            + KEY_SYNC_STATUS + " TEXT)";

    String CREATE_TABLE_QUIZ_PLAY_SCORE = "CREATE TABLE " + TABLE_QUIZ_PLAY_SCORE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_WEEK_YEAR + " INTEGER, "
            + KEY_HIGHEST_SCORE + " INTEGER, "
            + KEY_TEST_TYPE + " TEXT)";

    String CREATE_TABLE_CHALLENGE_STATUS = "CREATE TABLE " + TABLE_CHALLENGE_STATUS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATE + " TEXT, " + KEY_CHALLENGE_STATUS + " INTEGER)";

    String CREATE_TABLE_CHALLENGE_WEEKLY = "CREATE TABLE " + TABLE_CHALLENGE_WEEKLY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CHALLENGE_DATE_FROM + " TEXT, "
            + KEY_CHALLENGE_DATE_TO + " TEXT, "
            + KEY_CHALLENGE_TEST_PASS_COUNT + " INTEGER,"
            + KEY_TEST_TYPE + " TEXT)";

    String CREATE_TABLE_CHALLENGE = "CREATE TABLE " + TABLE_CHALLENGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DATE + " TEXT, "
            + KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS + " INTEGER, "
            + KEY_CHALLENGE_QUIZ_STATUS + " INTEGER, "
            + KEY_CHALLENGE_TEST_CORRECT_ANSWERS + " INTEGER, "
            + KEY_CHALLENGE_TEST_STATUS + " INTEGER,"
            + KEY_TEST_TYPE + " TEXT)";


    String CREATE_TABLE_CONTENT_CHECK_DATE = "CREATE TABLE " + TABLE_CONTENT_CHECK_DATE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DATE + " TEXT)";

    String CREATE_BOOK_CONTENT_CHECK_DATE = "CREATE TABLE " + BOOK_CONTENT_CHECK_DATE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DATE + " TEXT)";



    String CREATE_QUIZ_TABLE_WITH_TIMER = "CREATE TABLE " + TABLE_QUIZ_WITH_TIMER_FINAL + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_SERIAL_NUMBER + " TEXT, "
            + KEY_TITLE + " TEXT, "
            + KEY_TYPE_OF_PLAY + " TEXT, "
            + KEY_TOTAL_QUESTIONS + " TEXT, "
            + KEY_ANSWER_STATUS + " TEXT, "
            + KEY_QUESTION_ANSWER + " TEXT, "
            + KEY_QUESTION_PATH_TYPE + " TEXT, "
            + KEY_TIME_TAKEN + " TEXT, "
            + KEY_OPTIONS + " TEXT, "
            + KEY_STATUS + " TEXT, "
            + KEY_PRESENT_DATE + " TEXT,"
            + KEY_READ_DATA + " TEXT,"
            + KEY_ORIGINAL_TEST_NAME + " TEXT,"
            + KEY_REVIEW_EXIST + " INTEGER,"
            + KEY_TEST_TYPE + " TEXT)";

    String CREATE_TABLE_TEST_CONTENT_DOWNLOAD = "CREATE TABLE " + TABLE_TEST_CONTENT_DOWNLOAD + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TEST_CONTENT_VERSION + " TEXT, " + KEY_TEST_CONTENT_URL + " TEXT, "+ KEY_TEST_TYPE + " TEXT, " + KEY_SYNC_STATUS + " INTEGER, "+ KEY_TEST_DOWNLOAD_STATUS + " INTEGER)";

    String CREATE_QUIZ_PLAY_TABLE_WITH_TIMER = "CREATE TABLE " + TABLE_QUIZ_PLAY_WITH_TIME + "("
            + KEY_SERIAL_NUMBER + " TEXT, " + KEY_TITLE + " TEXT, " + KEY_LAST_PLAYED + " TEXT,"+ KEY_TEST_TYPE + " TEXT )";


    String CREATE_TABLE_TEST_TIMER = "CREATE TABLE " + TABLE_TEST_TIMER + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TEST_TIMER_TIME + " INTEGER)";


    String CREATE_QUIZ_PLAY_TABLE = "CREATE TABLE " + TABLE_QUIZ_PLAY + "("
            + KEY_TITLE + " TEXT, " + KEY_TOTAL_QUESTIONS + " TEXT, " + KEY_QUESTIONS + " TEXT, " + KEY_ANSWERS + " TEXT, " + KEY_QUESTION_ANSWER + " TEXT, " + KEY_QUESTION_PATH_TYPE + " TEXT, " + KEY_OPTIONS + " TEXT)";


    String CREATE_QUIZ_TOPICS_TABLE = "CREATE TABLE " + TABLE_QUIZ_TOPICS + "("
            + KEY_SERIAL_NUMBER + " TEXT PRIMARY KEY, " + KEY_TOPIC_NAME + " TEXT, " + KEY_LAST_PLAYED + " TEXT)";
    public Context context;

    public QuizGameDataBase(Context context) {
        super(context, DATABASE_NAME, null, Utils.DATABASE_VERSION);
        this.context = context;
        //3rd argument to be passed is CursorFactory instance
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUIZ_PLAY_TABLE);
        db.execSQL(CREATE_QUIZ_TOPICS_TABLE);
        db.execSQL(CREATE_QUIZ_PLAY_TABLE_WITH_TIMER);
        db.execSQL(CREATE_QUIZ_TABLE_WITH_TIMER);
        db.execSQL(CREATE_TABLE_TEST_CONTENT_DOWNLOAD);
        db.execSQL(CREATE_TABLE_TEST_TIMER);
        db.execSQL(CREATE_TABLE_QUIZ_PLAY_SCORE);
        db.execSQL(CREATE_TABLE_USER_SYNC);

        db.execSQL(CREATE_TABLE_CHALLENGE_STATUS);
        db.execSQL(CREATE_TABLE_CHALLENGE);
        db.execSQL(CREATE_TABLE_CHALLENGE_WEEKLY);

        db.execSQL(CREATE_TABLE_CONTENT_CHECK_DATE);
        db.execSQL(CREATE_BOOK_CONTENT_CHECK_DATE);


        db.execSQL(CREATE_TABLE_PLAY_COUNT);
        db.execSQL(CREATE_TABLE_COURSE);
        db.execSQL(CREATE_TABLE_BOOKS);




    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.e("quiz game","onUpgrade......oldversion...."+oldVersion);
            Log.e("quiz game","onUpgrade......newVersion...."+newVersion);

            //if (BuildConfig.VERSION_CODE <= 25) {
                /*try{
                     File dirFile1 = new File(context.getCacheDir(),"algebra/ii-algebra");
                     boolean isdeleted = Utils.deleteFolder(dirFile1);

                    File dirFile2 = new File(context.getCacheDir(),"calculus1/jee-calculus-1");
                    boolean isdeleted1 = Utils.deleteFolder(dirFile2);

                    File dirFile3 = new File(context.getCacheDir(),"calculus2/jee-calculus-2");
                    boolean isdeleted2 = Utils.deleteFolder(dirFile3);

                    File dirFile4 = new File(context.getCacheDir(),"other/other");
                    boolean isdeleted3 = Utils.deleteFolder(dirFile4);

                    File dirFile5 = new File(context.getCacheDir(),"geometry/iii-geometry");
                    boolean isdeleted4 = Utils.deleteFolder(dirFile5);

                }catch(Exception e){
                    e.printStackTrace();
                }*/
        try {
            db.execSQL(CREATE_TABLE_BOOKS);
        } catch (Exception e) {
            e.printStackTrace();
            //db.execSQL(CREATE_TABLE_COURSE);
        }

        try {
            db.execSQL(CREATE_BOOK_CONTENT_CHECK_DATE);
        } catch (Exception e) {
            e.printStackTrace();
            //db.execSQL(CREATE_TABLE_COURSE);
        }

        /*try {
            db.execSQL("alter table " + TABLE_BOOKS + "  add column " + KEY_BOOK_DOWNLOAD_STATUS + " INTEGER default 0");
        } catch (Exception e) {
            e.printStackTrace();
            //db.execSQL(CREATE_TABLE_COURSE);
        }
        try {
            db.execSQL("alter table " + TABLE_BOOKS + "  add column " + KEY_SORT_ORDER + " TEXT default null");
        } catch (Exception e) {
            e.printStackTrace();
            //db.execSQL(CREATE_TABLE_COURSE);
        }
        try {
            db.execSQL("alter table " + TABLE_BOOKS + "  add column " + KEY_VERSION + " TEXT default null");
        } catch (Exception e) {
            e.printStackTrace();
            //db.execSQL(CREATE_TABLE_COURSE);
        }*/
                try {
                    db.execSQL("alter table " + TABLE_QUIZ_WITH_TIMER_FINAL + "  add column " + KEY_REVIEW_EXIST + " INTEGER default 0");
                } catch (Exception e) {
                    e.printStackTrace();
                    //db.execSQL(CREATE_TABLE_COURSE);
                }
                try {
                    db.execSQL(CREATE_TABLE_COURSE);
                } catch (Exception e) {
                    e.printStackTrace();
                    //db.execSQL(CREATE_TABLE_COURSE);
                }
                try {
                    db.execSQL("alter table " + TABLE_TEST_CONTENT_DOWNLOAD + "  add column " + KEY_SYNC_STATUS + " INTEGER default -1");
                } catch (Exception e) {
                    e.printStackTrace();
                    //db.execSQL(CREATE_TABLE_COURSE);
                }
                try {
                    db.execSQL(CREATE_TABLE_PLAY_COUNT);
                } catch (Exception e) {
                    e.printStackTrace();
                    //db.execSQL(CREATE_TABLE_COURSE);
                }

           // }



        /*db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_PLAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_TOPICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_PLAY_WITH_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_WITH_TIMER_FINAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_CONTENT_DOWNLOAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_TIMER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_PLAY_SCORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_SYNC);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE_WEEKLY);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTENT_CHECK_DATE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAY_COUNT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAY_COUNT_FILE);
        // Create tables again
        onCreate(db);*/
    }



    public void insertBooks(Books books) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, books.getTitle());
        values.put(KEY_CATEGORY, books.getCategory());
        values.put(KEY_SOURCE_URL, books.getSourceUrl());
        values.put(KEY_THUMB_NAIL, books.getThumbnail());
        values.put(KEY_PUBLISHED_ON, books.getPublishedOn());
        values.put(KEY_READ_STATUS, books.getReadstatus());
        values.put(KEY_STARRED_STATUS, books.getStarredstatus());

        values.put(KEY_BOOK_DOWNLOAD_STATUS, books.getBookdownloadstatus());
        values.put(KEY_SORT_ORDER, books.getSortorder());
        values.put(KEY_VERSION, books.getVersion());

        values.put(KEY_COPY_STATUS, books.getCopystatus());
        values.put(KEY_READ_FILE_STATUS, books.getReadfilestatus());

        values.put(KEY_FOLDER_NAME, books.getFolderName());
        values.put(KEY_ID, books.getId());
        values.put(KEY_VISIBILITY, books.getVisibility());








        // Inserting Row
        db.insert(TABLE_BOOKS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public int updatebookscategorybasedonId(String category,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_CATEGORY, category);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public int updatebooksurlbasedonId(String url,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_SOURCE_URL, url);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public int updatebookspublishedonbasedonId(String publishdate,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_PUBLISHED_ON, publishdate);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public int updatebookssortorderbasedonId(String sortorder,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_SORT_ORDER, sortorder);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public int updatebooksvisibilitybasedonId(String visibility,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_VISIBILITY, visibility);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public String getBooksVersion(String Id) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        String version = "";

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + KEY_ID + "=?";
        Cursor cur = db.rawQuery(query, new String[]{Id});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        if (cur.moveToFirst()) {
            do {
                /*TestDownload testDownload=new TestDownload();
                testDownload.setTestdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));
                testDownload.setTesturl((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL))));
                testDownload.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));
                testDownload.setTestversion((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION))));*/

                version = ((cur.getString(cur.getColumnIndex(KEY_VERSION))));



            } while (cur.moveToNext());
        }

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        Log.e("dash board","booksJsonString...version......."+version);
        return version;
    }

    public List<Integer> getbooksdownloadstatus() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        List<Integer> statusList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                /*TestDownload testDownload=new TestDownload();
                testDownload.setTestdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));
                testDownload.setTesturl((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL))));
                testDownload.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));
                testDownload.setTestversion((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION))));*/

                statusList.add((cur.getInt(cur.getColumnIndex(KEY_BOOK_DOWNLOAD_STATUS))));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","getbooksdownloadstatus...statusList......."+statusList.size());
        // return contact
        return statusList;
    }

    public int getAllBooksCount() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS , new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public int getBooksCount(String Id) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + KEY_ID + "=?";
        Cursor cur = db.rawQuery(query, new String[]{Id});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        Log.e("dash board","booksJsonString...getBooksCount......."+count);
        return count;
    }
    public int updatebooksversionFromLocal(String version,String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_VERSION, version);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_CATEGORY+" =?", new String[] { category });
        db.close();
        // updating row
        return updatevalue;
    }
    public int updatebooksversionbasedonId(String version,String id) {
        Log.e("quiz game database","updatetestcontentdownloadstatus...version.....id..."+version+"....."+id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_VERSION, version);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
        db.close();
        // updating row
        return updatevalue;
    }
    public int updatebooksversion(String version,String title, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_VERSION, version);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_TITLE + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(title),category });
    }

    public int updatebookstitlebasedonId(String title,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TITLE, title);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public int updatebooksthumbnailbasedonId(String thumbnail,String id) {
        //Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_THUMB_NAIL, thumbnail);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
    }
    public int updatebooksthumbnail(String thumbnail,String foldername, String category) {
        Log.e("quiz game database","updatebooksthumbnail...statys.....type..."+foldername);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_THUMB_NAIL, thumbnail);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_FOLDER_NAME + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(foldername),category });
        db.close();
        // updating row
        return updatevalue;
    }

    public int updatebooksLocal(String thumbnail,int copystatus,int readfilestatus,String foldername,String category) {
        // Log.e("quiz game database","updatebooksreadfilestatusfromlocal...statys.....type..."+status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_COPY_STATUS, copystatus);
        values.put(KEY_READ_FILE_STATUS, readfilestatus);
        values.put(KEY_THUMB_NAIL, thumbnail);



        int updatevalue = db.update(TABLE_BOOKS, values, KEY_FOLDER_NAME + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(foldername),category });
        db.close();
        // updating row
        return updatevalue;

    }
    public int updatebooksdownloadstatusfromlocal(int status, String category) {
        Log.e("quiz game database","updatetestcontentdownloadstatus...statys.....type..."+status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_BOOK_DOWNLOAD_STATUS, status);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_CATEGORY+" =?",
                new String[] { category });
        // updating row
        db.close();
        // updating row
        return updatevalue;
    }
    public int updatebooksdownloadstatus(int status,String title, String category) {
        Log.e("quiz game database","updatetestcontentdownloadstatus...statys.....type..."+status+"....."+title);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_BOOK_DOWNLOAD_STATUS, status);

        // updating row
        return db.update(TABLE_BOOKS, values, KEY_TITLE + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(title),category });
    }
    public int updatebooksdownloadstatusbasedonId(int status,String id) {
       // Log.e("quiz game database","updatetestcontentdownloadstatus...statys.....id..."+status+"....."+id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_BOOK_DOWNLOAD_STATUS, status);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] { id });
        // updating row
        db.close();
        // updating row
        return updatevalue;

    }

    public int updateBooksStarredStatus(int count,String title, String category) {
        Log.e("quiz database","count....."+count);
        Log.e("quiz database","title....."+title);
        Log.e("quiz database","category....."+category);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_STARRED_STATUS, count);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_TITLE + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(title),category });
        db.close();
        // updating row
        return updatevalue;

    }

    public int updateBooksReadStatus(int count,String title, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_READ_STATUS, count);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_TITLE + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(title),category });
        db.close();
        // updating row
        return updatevalue;

    }

    public int updatebookscopystatus(int status,String title, String category) {
        Log.e("quiz game database","updatebookscopystatus...statys.....type..."+status+"....."+title+"....."+category);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_COPY_STATUS, status);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_FOLDER_NAME + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(title),category });
        db.close();
        // updating row
        return updatevalue;

    }
    public int updatebooksreadfilestatusbasedonId(int status,String id) {
       // Log.e("quiz game database","updatebooksreadfilestatus...statys.....id..."+status+"....."+id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_READ_FILE_STATUS, status);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_ID + " = ?",
                new String[] {id});
        db.close();
        // updating row
        return updatevalue;
        // updating row

    }
    public int updatebooksreadfilestatus(int status,String title, String category) {
        Log.e("quiz game database","updatebooksreadfilestatus...statys.....type..."+status+"....."+title);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_READ_FILE_STATUS, status);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_FOLDER_NAME + " = ? AND "+KEY_CATEGORY+" =?",
                new String[] { String.valueOf(title),category });
        db.close();
        // updating row
        return updatevalue;

    }
    public int updatebooksreadfilestatusfromlocal(int status,String category) {
        Log.e("quiz game database","updatebooksreadfilestatusfromlocal...statys.....type..."+status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_READ_FILE_STATUS, status);
        int updatevalue = db.update(TABLE_BOOKS, values, KEY_CATEGORY+" =?",
                new String[] { category });
        db.close();
        // updating row
        return updatevalue;

    }
    public int updatebooksValues(String id,String version,int downloadstatus,int readfilestatus,String thumbnail,String title,String url,String sortorder,String publishdate,String category,String visibility) {
       // Log.e("quiz game database","updatebooksreadfilestatusfromlocal...statys.....type..."+status);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_VERSION, version);
        values.put(KEY_BOOK_DOWNLOAD_STATUS, downloadstatus);
        values.put(KEY_READ_FILE_STATUS, readfilestatus);
        values.put(KEY_THUMB_NAIL, thumbnail);
        values.put(KEY_SOURCE_URL, url);
        values.put(KEY_TITLE, title);
        values.put(KEY_SORT_ORDER, sortorder);
        values.put(KEY_PUBLISHED_ON, publishdate);
        values.put(KEY_CATEGORY, category);
        values.put(KEY_VISIBILITY, visibility);


        int updatevalue = db.update(TABLE_BOOKS, values, KEY_ID+" =?",
                new String[] { id });
        db.close();
        // updating row
        return updatevalue;

    }


    public List<Integer> getbookscopystatusList() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        List<Integer> statusList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                /*TestDownload testDownload=new TestDownload();
                testDownload.setTestdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));
                testDownload.setTesturl((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL))));
                testDownload.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));
                testDownload.setTestversion((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION))));*/

                statusList.add((cur.getInt(cur.getColumnIndex(KEY_COPY_STATUS))));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","getbooksdownloadstatus...statusList......."+statusList.size());
        // return contact
        return statusList;
    }

    public int getBooksReadFileStatus(String title, String category) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        // Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        int status = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + KEY_TITLE + "=? AND "+KEY_CATEGORY+"=?";
        Cursor cur = db.rawQuery(query, new String[]{title.replaceAll("'","\\'"),category});
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS +" WHERE " +KEY_TITLE+"='"+title+"' AND "+KEY_CATEGORY+"='"+category+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                status = (cur.getInt(cur.getColumnIndex(KEY_READ_FILE_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }
    public int getBooksCopyStatus(String title, String category) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        // Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        int status = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + KEY_TITLE + "=? AND "+KEY_CATEGORY+"=?";
        Cursor cur = db.rawQuery(query, new String[]{title.replaceAll("'","\\'"),category});
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS +" WHERE " +KEY_TITLE+"='"+title+"' AND "+KEY_CATEGORY+"='"+category+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                status = (cur.getInt(cur.getColumnIndex(KEY_COPY_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }
    public int getBooksStarredStatus(String title, String category) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        // Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        int status = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_BOOKS + " WHERE " + KEY_TITLE + "=? AND "+KEY_CATEGORY+"=?";
        Cursor cur = db.rawQuery(query, new String[]{title.replaceAll("'","\\'"),category});
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS +" WHERE " +KEY_TITLE+"='"+title+"' AND "+KEY_CATEGORY+"='"+category+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                status = (cur.getInt(cur.getColumnIndex(KEY_STARRED_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }
    public ArrayList<Books> getAllBooks() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;
        ArrayList<Books> booksArrayList = new ArrayList<Books>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS+" WHERE "+KEY_VISIBILITY+"=1 ORDER BY "+KEY_SORT_ORDER, new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});

        if (cur.moveToFirst()) {
            do {

                Books books=new Books();
                books.setTitle((cur.getString(cur.getColumnIndex(KEY_TITLE))));
                books.setCategory((cur.getString(cur.getColumnIndex(KEY_CATEGORY))));
                books.setSourceUrl((cur.getString(cur.getColumnIndex(KEY_SOURCE_URL))));
                books.setThumbnail((cur.getString(cur.getColumnIndex(KEY_THUMB_NAIL))));
                books.setPublishedOn((cur.getString(cur.getColumnIndex(KEY_PUBLISHED_ON))));
                books.setReadstatus((cur.getInt(cur.getColumnIndex(KEY_READ_STATUS))));
                books.setStarredstatus((cur.getInt(cur.getColumnIndex(KEY_STARRED_STATUS))));

                books.setBookdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_BOOK_DOWNLOAD_STATUS))));
                books.setSortorder((cur.getString(cur.getColumnIndex(KEY_SORT_ORDER))));
                books.setVersion((cur.getString(cur.getColumnIndex(KEY_VERSION))));

                books.setCopystatus((cur.getInt(cur.getColumnIndex(KEY_COPY_STATUS))));
                books.setReadfilestatus((cur.getInt(cur.getColumnIndex(KEY_READ_FILE_STATUS))));

                books.setFolderName((cur.getString(cur.getColumnIndex(KEY_FOLDER_NAME))));
                //Log.e("quiz game database","(cur.getString(cur.getColumnIndex(KEY_ID))).........."+(cur.getString(cur.getColumnIndex(KEY_ID))));
               // Log.e("quiz game database","(cur.getInt(cur.getColumnIndex(KEY_ID))).........."+(cur.getInt(cur.getColumnIndex(KEY_ID))));
                books.setId((cur.getString(cur.getColumnIndex(KEY_ID))));
                books.setVisibility(cur.getString(cur.getColumnIndex(KEY_VISIBILITY)));


                booksArrayList.add(books);
            } while (cur.moveToNext());
        }



        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return booksArrayList;
    }
    public ArrayList<Books> getAllBooksFromWeek() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Utils.date);
        calendar.add(Calendar.DATE, -7);
        SimpleDateFormat format1 =new SimpleDateFormat("yyyy-MM-dd");
        String s = format1.format(calendar.getTime());
        int count = 0;
        ArrayList<Books> booksArrayList = new ArrayList<Books>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS+" WHERE "+KEY_PUBLISHED_ON+">"+"'"+s+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});

        if (cur.moveToFirst()) {
            do {

                Books books=new Books();
                books.setTitle((cur.getString(cur.getColumnIndex(KEY_TITLE))));
                books.setCategory((cur.getString(cur.getColumnIndex(KEY_CATEGORY))));
                books.setSourceUrl((cur.getString(cur.getColumnIndex(KEY_SOURCE_URL))));
                books.setThumbnail((cur.getString(cur.getColumnIndex(KEY_THUMB_NAIL))));
                books.setPublishedOn((cur.getString(cur.getColumnIndex(KEY_PUBLISHED_ON))));
                books.setReadstatus((cur.getInt(cur.getColumnIndex(KEY_READ_STATUS))));
                books.setStarredstatus((cur.getInt(cur.getColumnIndex(KEY_STARRED_STATUS))));
                books.setBookdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_BOOK_DOWNLOAD_STATUS))));
                books.setSortorder((cur.getString(cur.getColumnIndex(KEY_SORT_ORDER))));
                books.setVersion((cur.getString(cur.getColumnIndex(KEY_VERSION))));
                books.setCopystatus((cur.getInt(cur.getColumnIndex(KEY_COPY_STATUS))));
                books.setReadfilestatus((cur.getInt(cur.getColumnIndex(KEY_READ_FILE_STATUS))));
                books.setFolderName((cur.getString(cur.getColumnIndex(KEY_FOLDER_NAME))));
                books.setId((cur.getString(cur.getColumnIndex(KEY_ID))));
                books.setVisibility(cur.getString(cur.getColumnIndex(KEY_VISIBILITY)));
                booksArrayList.add(books);
            } while (cur.moveToNext());
        }



        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return booksArrayList;
    }

    public ArrayList<Books> getAllBooksWithStarred() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;
        ArrayList<Books> booksArrayList = new ArrayList<Books>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS+" WHERE "+KEY_STARRED_STATUS+"=1", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});

        if (cur.moveToFirst()) {
            do {

                Books books=new Books();
                books.setTitle((cur.getString(cur.getColumnIndex(KEY_TITLE))));
                books.setCategory((cur.getString(cur.getColumnIndex(KEY_CATEGORY))));
                books.setSourceUrl((cur.getString(cur.getColumnIndex(KEY_SOURCE_URL))));
                books.setThumbnail((cur.getString(cur.getColumnIndex(KEY_THUMB_NAIL))));
                books.setPublishedOn((cur.getString(cur.getColumnIndex(KEY_PUBLISHED_ON))));
                books.setReadstatus((cur.getInt(cur.getColumnIndex(KEY_READ_STATUS))));
                books.setStarredstatus((cur.getInt(cur.getColumnIndex(KEY_STARRED_STATUS))));
                books.setBookdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_BOOK_DOWNLOAD_STATUS))));
                books.setSortorder((cur.getString(cur.getColumnIndex(KEY_SORT_ORDER))));
                books.setVersion((cur.getString(cur.getColumnIndex(KEY_VERSION))));
                books.setCopystatus((cur.getInt(cur.getColumnIndex(KEY_COPY_STATUS))));
                books.setReadfilestatus((cur.getInt(cur.getColumnIndex(KEY_READ_FILE_STATUS))));
                books.setFolderName((cur.getString(cur.getColumnIndex(KEY_FOLDER_NAME))));
                books.setId((cur.getString(cur.getColumnIndex(KEY_ID))));
                books.setVisibility(cur.getString(cur.getColumnIndex(KEY_VISIBILITY)));
                booksArrayList.add(books);
            } while (cur.moveToNext());
        }



        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return booksArrayList;
    }
    public ArrayList<Books> getAllBooksWithUnread() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;
        ArrayList<Books> booksArrayList = new ArrayList<Books>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_BOOKS+" WHERE "+KEY_READ_STATUS+"=0", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});

        if (cur.moveToFirst()) {
            do {

                Books books=new Books();
                books.setTitle((cur.getString(cur.getColumnIndex(KEY_TITLE))));
                books.setCategory((cur.getString(cur.getColumnIndex(KEY_CATEGORY))));
                books.setSourceUrl((cur.getString(cur.getColumnIndex(KEY_SOURCE_URL))));
                books.setThumbnail((cur.getString(cur.getColumnIndex(KEY_THUMB_NAIL))));
                books.setPublishedOn((cur.getString(cur.getColumnIndex(KEY_PUBLISHED_ON))));
                books.setReadstatus((cur.getInt(cur.getColumnIndex(KEY_READ_STATUS))));
                books.setStarredstatus((cur.getInt(cur.getColumnIndex(KEY_STARRED_STATUS))));
                books.setBookdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_BOOK_DOWNLOAD_STATUS))));
                books.setSortorder((cur.getString(cur.getColumnIndex(KEY_SORT_ORDER))));
                books.setVersion((cur.getString(cur.getColumnIndex(KEY_VERSION))));
                books.setCopystatus((cur.getInt(cur.getColumnIndex(KEY_COPY_STATUS))));
                books.setReadfilestatus((cur.getInt(cur.getColumnIndex(KEY_READ_FILE_STATUS))));
                books.setFolderName((cur.getString(cur.getColumnIndex(KEY_FOLDER_NAME))));
                books.setId((cur.getString(cur.getColumnIndex(KEY_ID))));
                books.setVisibility(cur.getString(cur.getColumnIndex(KEY_VISIBILITY)));
                booksArrayList.add(books);
            } while (cur.moveToNext());
        }



        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return booksArrayList;
    }




    public void insertCourse(Course playCount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_COURSE, playCount.getCoursename());
        values.put(KEY_COURSE_EXIST, playCount.getCourseexist());

        // Inserting Row
        db.insert(TABLE_COURSE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int getAllCoursesCount() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_COURSE , new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public int getCoursesCount(String course) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_COURSE +" where "+KEY_COURSE+"= "+course, new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {



                count = ((cur.getInt(cur.getColumnIndex(KEY_COURSE_EXIST))));



            } while (cur.moveToNext());
        }

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }



    public ArrayList<Course> getAllCourses() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;
        ArrayList<Course> courseArrayList = new ArrayList<Course>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_COURSE, new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});

        if (cur.moveToFirst()) {
            do {

                Course playCount=new Course();
                playCount.setCoursename((cur.getString(cur.getColumnIndex(KEY_COURSE))));
                playCount.setCourseexist((cur.getInt(cur.getColumnIndex(KEY_COURSE_EXIST))));

                courseArrayList.add(playCount);
            } while (cur.moveToNext());
        }



        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return courseArrayList;
    }

    public int updateCourseExist(String course, int count) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_COURSE_EXIST, count);

        // updating row
        return db.update(TABLE_COURSE, values, KEY_COURSE + " = ?",
                new String[] { String.valueOf(course) });
    }



    public void insertPlayCount(PlayCount playCount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_COURSE, playCount.getCourse());
        values.put(KEY_TOPIC, playCount.getTopic());
        values.put(KEY_LEVEL, playCount.getLevel());
        values.put(KEY_PLAY_COUNT, playCount.getPlaycount());




        // Inserting Row
        db.insert(TABLE_PLAY_COUNT, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int getAllPlayCount() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_PLAY_COUNT, new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public int getPlayCount(String course, String topic,String level) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_PLAY_COUNT + " WHERE " + KEY_COURSE + "='" + course+"' AND "+KEY_TOPIC+"='"+topic+"' AND "+KEY_LEVEL+"='"+level+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public PlayCount getPlayCountPlayRecord(String course) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_PLAY_COUNT + " WHERE " + KEY_COURSE + "='" + course+"' ORDER BY "+KEY_PLAY_COUNT+" ASC LIMIT 0,1", new String[]{});
        PlayCount playCount=new PlayCount();
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});

        if (cur.moveToFirst()) {
            do {


                playCount.setCourse((cur.getString(cur.getColumnIndex(KEY_COURSE))));
                playCount.setLevel((cur.getString(cur.getColumnIndex(KEY_LEVEL))));
                playCount.setPlaycount((cur.getInt(cur.getColumnIndex(KEY_PLAY_COUNT))));
                playCount.setTopic((cur.getString(cur.getColumnIndex(KEY_TOPIC))));





            } while (cur.moveToNext());
        }



        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return playCount;
    }

    public int updatePlayCount(int count,String course, String topic,String level) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_PLAY_COUNT, count);

        // updating row
        return db.update(TABLE_PLAY_COUNT, values, KEY_COURSE + " = ? AND "+KEY_TOPIC+" =? AND "+KEY_LEVEL+" =?",
                new String[] { String.valueOf(course),topic,level });
    }

    public int deletePlayCount(String course, String topic,String level) {
        SQLiteDatabase db = this.getWritableDatabase();

        //ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        //values.put(KEY_PLAY_COUNT, count);

        // updating row
        return db.delete(TABLE_PLAY_COUNT, KEY_COURSE + " = ? AND "+KEY_TOPIC+" =? AND "+KEY_LEVEL+" =?",
                new String[] { String.valueOf(course),topic,level });
    }


    public void insertUserSync(User user,int syncstatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_USER_EMAIL, user.getUsername());
        values.put(KEY_USER_DEVICE_ID, user.getDeviceuniqueid());
        values.put(KEY_USER_PHONE, user.getPhonenumber());
        values.put(KEY_USER_CREATEDON, user.getCreatedon());
        values.put(KEY_USER_UPDATEDON, user.getUpdatedon());
        values.put(KEY_USER_FIREBASE_TOKEN, user.getFirebaseToken());
        values.put(KEY_SYNC_STATUS, syncstatus);



        // Inserting Row
        db.insert(TABLE_USER_SYNC, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int updateUserSyncStatus(String email,int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_SYNC_STATUS, status);

        // updating row
        return db.update(TABLE_USER_SYNC, values, KEY_USER_EMAIL + " = ?",
                new String[] { String.valueOf(email) });
    }

    public String getUserSyncStatus() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        // Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        String status = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_USER_SYNC , new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                status = (cur.getString(cur.getColumnIndex(KEY_SYNC_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }

    public User getUserDetails() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        User user = new User();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_USER_SYNC, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {


                user.setUsername((cur.getString(cur.getColumnIndex(KEY_USER_EMAIL))));
                user.setPhonenumber((cur.getString(cur.getColumnIndex(KEY_USER_PHONE))));
                user.setFirebaseToken((cur.getString(cur.getColumnIndex(KEY_USER_FIREBASE_TOKEN))));
                user.setUpdatedon((cur.getString(cur.getColumnIndex(KEY_USER_UPDATEDON))));
                user.setCreatedon((cur.getString(cur.getColumnIndex(KEY_USER_CREATEDON))));
                user.setDeviceuniqueid((cur.getString(cur.getColumnIndex(KEY_USER_DEVICE_ID))));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        //Log.e("quiz game database","user........."+user);
        // return contact
        return user;
    }

    public void insertBookContentUpdateDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DATE, date);

        // Inserting Row
        db.insert(BOOK_CONTENT_CHECK_DATE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deletebookcontentdate() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + BOOK_CONTENT_CHECK_DATE);
        db.close();
    }



    public int updateBookContentDate(String date,int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_DATE, date);

        // updating row
        return db.update(BOOK_CONTENT_CHECK_DATE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public String getBookContentDate() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        // Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        String date = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + BOOK_CONTENT_CHECK_DATE , new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                date = (cur.getString(cur.getColumnIndex(KEY_DATE)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","date.........."+date);
        // return contact
        return date;
    }


    public void insertContentUpdateDate(String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DATE, date);

        // Inserting Row
        db.insert(TABLE_CONTENT_CHECK_DATE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int updateContentDate(String date,int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_DATE, date);

        // updating row
        return db.update(TABLE_CONTENT_CHECK_DATE, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }



    public String getContentDate() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
       // Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        String date = null;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CONTENT_CHECK_DATE , new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                date = (cur.getString(cur.getColumnIndex(KEY_DATE)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","date.........."+date);
        // return contact
        return date;
    }

    public void insertQuizPlayScore(QuizScore quizscore) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_WEEK_YEAR, quizscore.getWeekofyear());
        values.put(KEY_HIGHEST_SCORE, quizscore.getHighestscore());
        values.put(KEY_TEST_TYPE, quizscore.getTesttype());

        // Inserting Row
        db.insert(TABLE_QUIZ_PLAY_SCORE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int updateQuizPlayScore(int weekofyear,String type,int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_HIGHEST_SCORE, score);

        // updating row
        return db.update(TABLE_QUIZ_PLAY_SCORE, values, KEY_WEEK_YEAR + " = ? AND "+KEY_TEST_TYPE+" =?",
                new String[] { String.valueOf(weekofyear),type });
    }

    public ArrayList<QuizScore> getScoresForCurrentWeek(int weekofyear) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        ArrayList<QuizScore> quizScoreList = new ArrayList<QuizScore>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY_SCORE +" WHERE "+KEY_WEEK_YEAR+"='"+weekofyear+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                QuizScore quizScore=new QuizScore();
                quizScore.setWeekofyear((cur.getString(cur.getColumnIndex(KEY_WEEK_YEAR))));
                quizScore.setHighestscore((cur.getString(cur.getColumnIndex(KEY_HIGHEST_SCORE))));
                quizScore.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));


                quizScoreList.add(quizScore);



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","quizScoreList.size.........."+quizScoreList.size());
        // return contact
        return quizScoreList;
    }

    public int getWeekTotalScore(int weekofyear) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        Log.e("quiz database","getWeekTotalScore....weekofyear..."+weekofyear);
        int total = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT SUM("+KEY_HIGHEST_SCORE+") AS TOTAL FROM " + TABLE_QUIZ_PLAY_SCORE + " WHERE " + KEY_WEEK_YEAR + "='" + weekofyear+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                total = (cur.getInt(cur.getColumnIndex("TOTAL")));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

          Log.e("quiz game database","total.........."+total);
        // return contact
        return total;
    }

    public int getQuizScore(int weekofyear,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        //Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY_SCORE + " WHERE " + KEY_WEEK_YEAR + "='" + weekofyear+"' AND "+KEY_TEST_TYPE+"='"+type+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        //count = cur.getCount();
        if (cur.moveToFirst()) {
            do {

                count = (cur.getInt(cur.getColumnIndex(KEY_HIGHEST_SCORE)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }


    public void insertChallengeWeekly(String fromdate, String todate, int testpasscount,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CHALLENGE_DATE_FROM, fromdate);
        values.put(KEY_CHALLENGE_DATE_TO, todate);
        values.put(KEY_CHALLENGE_TEST_PASS_COUNT, testpasscount);
        values.put(KEY_TEST_TYPE, type);


        // Inserting Row
        db.insert(TABLE_CHALLENGE_WEEKLY, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int getChallengeForWEEKLY(String fromdate, String todate,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE_WEEKLY + " WHERE " + KEY_CHALLENGE_DATE_FROM + "='" + fromdate+"' AND "+KEY_CHALLENGE_DATE_TO+"='"+todate+"' AND "+KEY_TEST_TYPE+"='"+type+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();

        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public int updateChallengeweeklystatus(String fromdate, String todate, int status,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_CHALLENGE_TEST_PASS_COUNT, status);

        // updating row
        return db.update(TABLE_CHALLENGE_WEEKLY, values, KEY_CHALLENGE_DATE_FROM + " = ? AND "+KEY_CHALLENGE_DATE_TO + " = ? AND "+KEY_TEST_TYPE+" =?",
                new String[] { String.valueOf(fromdate),todate,type });
    }

    public int getChallengeWeeklystatus(String fromdate, String todate,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int count = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE_WEEKLY + " WHERE " + KEY_CHALLENGE_DATE_FROM + "='" + fromdate+"' AND "+KEY_CHALLENGE_DATE_TO + " ='"+todate+"' AND "+KEY_TEST_TYPE+"='"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                count = (cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_TEST_PASS_COUNT)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","count.........."+count);
        // return contact
        return count;
    }


    public void insertChallenge(Challenge challenge) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_DATE, challenge.getDate());
        values.put(KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS, challenge.getChallengeQuizCorrectAnswers());
        values.put(KEY_CHALLENGE_QUIZ_STATUS, challenge.getChallengeQuizStatus());
        values.put(KEY_CHALLENGE_TEST_CORRECT_ANSWERS, challenge.getChallengeTestCorrectAnswers());
        values.put(KEY_CHALLENGE_TEST_STATUS, challenge.getChallengeTestStatus());
        values.put(KEY_TEST_TYPE, challenge.getTesttype());

        // Inserting Row
        db.insert(TABLE_CHALLENGE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }



    public List<Challenge> getChallengeFrombetweenDates(String fromdate, String todate) {

        Log.e("quiz game db","getChallengeFrombetweenDates...fromdate.."+fromdate);
        Log.e("quiz game db","getChallengeFrombetweenDates...todate.."+todate);
        //DailyChallenge dailyChallenge=new DailyChallenge();
        List<Challenge> challengeList = new ArrayList<Challenge>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM (SELECT * FROM " + TABLE_CHALLENGE+" WHERE "+KEY_DATE+" BETWEEN '"+fromdate+"' AND '"+todate+"') WHERE "+KEY_CHALLENGE_QUIZ_STATUS+"='1' AND "+KEY_CHALLENGE_TEST_STATUS+"='1'" , new String[]{});
        Log.e("quiz game","query......"+"SELECT * FROM (SELECT * FROM " + TABLE_CHALLENGE+" WHERE "+KEY_DATE+" BETWEEN '"+fromdate+"' AND '"+todate+"') WHERE "+KEY_CHALLENGE_QUIZ_STATUS+"='1' AND "+KEY_CHALLENGE_TEST_STATUS+"='1'" );
        //select * from (select * from challenge where date between '22/06/2020' AND '28/06/2020') where challengequizstatus='1' AND challengeteststatus = '1'
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                Challenge challenge =new Challenge();
                challenge.setDate((cur.getString(cur.getColumnIndex(KEY_DATE))));
                challenge.setChallengeQuizCorrectAnswers((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS))));
                challenge.setChallengeQuizStatus((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_QUIZ_STATUS))));
                challenge.setChallengeTestCorrectAnswers((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_TEST_CORRECT_ANSWERS))));
                challenge.setChallengeTestStatus((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_TEST_STATUS))));
                challengeList.add(challenge);
            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

      //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return challengeList;
    }

    public int getChallengeForDate(String date,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        Log.e("quiz database","getChallengeForDate....date..."+date);
        int count = 0;
        Challenge challenge =new Challenge();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE + " WHERE " + KEY_DATE + "='" + date+"' AND "+KEY_TEST_TYPE+"= '"+type+"'", new String[]{});

        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/

        /*if (cur.moveToFirst()) {
            do {

                challenge.setDate((cur.getString(cur.getColumnIndex(KEY_DATE))));
                challenge.setChallengeQuizCorrectAnswers((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS))));
                challenge.setChallengeQuizStatus((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_QUIZ_STATUS))));
                challenge.setChallengeTestCorrectAnswers((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_TEST_CORRECT_ANSWERS))));
                challenge.setChallengeTestStatus((cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_TEST_STATUS))));

            } while (cur.moveToNext());
        }*/
        count = cur.getCount();
        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public int getChallengeForQuizStatus(String date) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int status = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE + " WHERE " + KEY_DATE + "='" + date+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                status = (cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_QUIZ_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }

    public int getChallengeForTestStatus(String date,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int status = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE + " WHERE " + KEY_DATE + "='" + date+"' AND "+KEY_TEST_TYPE+"= '"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                status = (cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_TEST_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }



    public boolean updateChallengeQuiz(String date,
                                       int correctanswers,
                                       int status) {

        boolean isUpdateCustomer;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();


        initialValues.put(KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS, correctanswers);
        initialValues.put(KEY_CHALLENGE_QUIZ_STATUS, status);


        String args[] = {date};

        isUpdateCustomer = db.update(TABLE_CHALLENGE, initialValues, KEY_DATE + "=?", args) > 0;
        db.close();
        return isUpdateCustomer;
    }

    public boolean updateChallengeTest(String date,
                                       int correctanswers,
                                       int status,String type) {

        boolean isUpdateCustomer;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();


        initialValues.put(KEY_CHALLENGE_TEST_CORRECT_ANSWERS, correctanswers);
        initialValues.put(KEY_CHALLENGE_TEST_STATUS, status);


        String args[] = {date,type};

        isUpdateCustomer = db.update(TABLE_CHALLENGE, initialValues, KEY_DATE + "=? AND "+KEY_TEST_TYPE+ "= ?", args) > 0;
        db.close();
        return isUpdateCustomer;
    }

    public void insertChallengeStatus(String date, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CHALLENGE_STATUS, status); // Contact Name
        values.put(KEY_DATE, date);

        // Inserting Row
        db.insert(TABLE_CHALLENGE_STATUS, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }



    public int updateChallengeStatus(int status, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_CHALLENGE_STATUS, status);

        // updating row
        return db.update(TABLE_CHALLENGE_STATUS, values, KEY_DATE + " = ?",
                new String[] { String.valueOf(date) });
    }

    public int getChallengeStatus(String date) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int status = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE_STATUS + " WHERE " + KEY_DATE + "='" + date+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                status = (cur.getInt(cur.getColumnIndex(KEY_CHALLENGE_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","status.........."+status);
        // return contact
        return status;
    }



    /*public void inserttesttimer(int id,int value) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEST_TIMER_TIME, value); // Contact Name
        values.put(KEY_ID, id);



        // Inserting Row
        db.insert(TABLE_TEST_TIMER, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deleteAlltime() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEST_TIMER);
        db.close();
    }

    // code to update the single contact
    public int updatetesttimer(int version) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEST_TIMER_TIME, version);

        // updating row
        return db.update(TABLE_TEST_TIMER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(1) });
    }

    public int gettesttime() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_TIMER + " WHERE " + KEY_ID + "='" + 1+"'", new String[]{});
        *//*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*//*
        if (cur.moveToFirst()) {
            do {

                version = (cur.getInt(cur.getColumnIndex(KEY_TEST_TIMER_TIME)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","version.........."+version);
        // return contact
        return version;
    }*/


    public void insertTESTCONTENTDOWNLOAD(String version, String URL,String type, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEST_CONTENT_VERSION, version); // Contact Name
        values.put(KEY_TEST_CONTENT_URL, URL); // Contact Phone
        values.put(KEY_TEST_TYPE, type); // Contact Phone
        values.put(KEY_TEST_DOWNLOAD_STATUS, status);
        values.put(KEY_SYNC_STATUS, -1);

        // Inserting Row
        db.insert(TABLE_TEST_CONTENT_DOWNLOAD, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deleteAlltestcontent() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TEST_CONTENT_DOWNLOAD);
        db.close();
    }

    // code to update the single contact
    public int updatetestcontentsyncstatus(int status,String type) {
        Log.e("quiz game database","updatetestcontentdownloadstatus...statys.....type..."+status+"....."+type);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_SYNC_STATUS, status);

        // updating row
        return db.update(TABLE_TEST_CONTENT_DOWNLOAD, values, KEY_TEST_TYPE + " = ?",
                new String[] { String.valueOf(type) });
    }

    public List<Integer> gettesttopicsyncstatus() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        List<Integer> statusList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                /*TestDownload testDownload=new TestDownload();
                testDownload.setTestdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));
                testDownload.setTesturl((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL))));
                testDownload.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));
                testDownload.setTestversion((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION))));*/

                statusList.add((cur.getInt(cur.getColumnIndex(KEY_SYNC_STATUS))));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","statusList.size.........."+statusList.size());
        // return contact
        return statusList;
    }

    // code to update the single contact
    public int updatetestcontentdownloadstatus(int status,String type) {
        Log.e("quiz game database","updatetestcontentdownloadstatus...statys.....type..."+status+"....."+type);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEST_DOWNLOAD_STATUS, status);

        // updating row
        return db.update(TABLE_TEST_CONTENT_DOWNLOAD, values, KEY_TEST_TYPE + " = ?",
                new String[] { String.valueOf(type) });
    }

    public List<Integer> gettesttopicdownloadstatus() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        List<Integer> statusList = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                /*TestDownload testDownload=new TestDownload();
                testDownload.setTestdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));
                testDownload.setTesturl((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL))));
                testDownload.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));
                testDownload.setTestversion((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION))));*/

                statusList.add((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","statusList.size.........."+statusList.size());
        // return contact
        return statusList;
    }

    public String gettestContentDownloadStatus(String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String downloadstatus = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD + " WHERE " + KEY_TEST_TYPE + "='" +type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                downloadstatus = (cur.getString(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","downloadstatus.........."+downloadstatus);
        // return contact
        return downloadstatus;
    }

    public List<TestDownload> gettestContent() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        List<TestDownload> statusList = new ArrayList<TestDownload>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {
                TestDownload testDownload=new TestDownload();
                testDownload.setTestdownloadstatus((cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS))));
                testDownload.setTesturl((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL))));
                testDownload.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));
                testDownload.setTestversion((cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION))));

                statusList.add(testDownload);



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","statusList.size.........."+statusList.size());
        // return contact
        return statusList;
    }

    // code to update the single contact
    public int updatetestcontentversion(String version,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEST_CONTENT_VERSION, version);

        // updating row
        return db.update(TABLE_TEST_CONTENT_DOWNLOAD, values, KEY_TEST_TYPE + " = ?",
                new String[] { String.valueOf(type) });
    }

    public String gettesttopicversion() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String version = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD + " WHERE " + KEY_ID + "='" + 1+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                version = (cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_VERSION)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","version.........."+version);
        // return contact
        return version;
    }
    public int updatetestcontenturl(String url,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEST_CONTENT_URL, url);

        // updating row
        return db.update(TABLE_TEST_CONTENT_DOWNLOAD, values, KEY_TEST_TYPE + " = ?",
                new String[] { String.valueOf(type) });
    }
    public String gettesttopicurl() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String url = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD + " WHERE " + KEY_ID + "='" + 1+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                url = (cur.getString(cur.getColumnIndex(KEY_TEST_CONTENT_URL)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","answers.........."+url);
        // return contact
        return url;
    }

    public void insertquiztopiclastplayed(String title, int serialno, String lastplayed,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIAL_NUMBER, serialno); // Contact Name
        values.put(KEY_TITLE, title); // Contact Phone
        values.put(KEY_LAST_PLAYED, lastplayed); // Contact Name
        values.put(KEY_TEST_TYPE, type); // Contact Name

        // Inserting Row
        db.insert(TABLE_QUIZ_PLAY_WITH_TIME, KEY_SERIAL_NUMBER, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public void deleteAllQuizTopicsLatPlayed(String type) {
        Log.e("quiz game","deleteAllQuizTopicsLatPlayed...type..."+type);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_QUIZ_PLAY_WITH_TIME+ " WHERE "+KEY_TEST_TYPE + " ='"+type+"'");
        db.close();
    }

    public TestQuiz getQuizTopicsForTimerLastPlayed(String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        TestQuiz testQuiz =new TestQuiz();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY_WITH_TIME+ " WHERE "+KEY_TEST_TYPE + " ='"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                testQuiz.setLastplayed((cur.getString(cur.getColumnIndex(KEY_LAST_PLAYED))));
                testQuiz.setTitle((cur.getString(cur.getColumnIndex(KEY_TITLE))));
                testQuiz.setSerialNo((cur.getString(cur.getColumnIndex(KEY_SERIAL_NUMBER))));
                testQuiz.setTesttype((cur.getString(cur.getColumnIndex(KEY_TEST_TYPE))));

            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","answers.........."+answers);
        // return contact
        return testQuiz;
    }

    public void insertquizplayFinal(TestQuizFinal testQuizFinal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIAL_NUMBER, testQuizFinal.getSerialNo()); // Contact Name
        values.put(KEY_TITLE, testQuizFinal.getTitle()); // Contact Phone
        values.put(KEY_TYPE_OF_PLAY, testQuizFinal.getTypeofPlay()); // Contact Name
        values.put(KEY_TOTAL_QUESTIONS, testQuizFinal.getTotalQuestions());
        values.put(KEY_ANSWER_STATUS, testQuizFinal.getAnswerstatus());
        values.put(KEY_QUESTION_ANSWER, testQuizFinal.getQuestionAnswers());
        values.put(KEY_QUESTION_PATH_TYPE, testQuizFinal.getQuestionPathType());
        values.put(KEY_PRESENT_DATE, testQuizFinal.getPdate());
        values.put(KEY_TIME_TAKEN, testQuizFinal.getTimetaken());
        values.put(KEY_OPTIONS, testQuizFinal.getOptions());
        values.put(KEY_STATUS, testQuizFinal.getStatus());
        values.put(KEY_TEST_TYPE, testQuizFinal.getTesttype());
        values.put(KEY_READ_DATA, testQuizFinal.getReaddata());
        values.put(KEY_ORIGINAL_TEST_NAME, testQuizFinal.getOriginalname());
        values.put(KEY_REVIEW_EXIST, 0);


        // Inserting Row
        db.insert(TABLE_QUIZ_WITH_TIMER_FINAL, KEY_SERIAL_NUMBER, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void deleteAllQuizPlayFinal() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_QUIZ_WITH_TIMER_FINAL);
        db.close();
    }

    public int updatequizplayReviewstatus(String title, int status, String pdate, String typeofplay,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_REVIEW_EXIST, status);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ? AND "+KEY_TEST_TYPE+ " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay,type });
    }

    public List<TestQuizFinal> getTestQuizList() {
        List<TestQuizFinal> dailychallengeList = new ArrayList<TestQuizFinal>();

        // SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_DAILY_CHALLENGE);
        String selectQuery = "SELECT  * FROM (select * from " + TABLE_QUIZ_WITH_TIMER_FINAL+" WHERE "+KEY_STATUS+"=1 order by "+KEY_ID+" DESC limit 10 ) order by "+KEY_ID+" DESC";
        //select * from (select * from tblmessage order by sortfield ASC limit 10) order by sortfield DESC;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery(selectQuery, null);
        if (cur.moveToFirst()) {
            do {
                TestQuizFinal testQuizFinal=new TestQuizFinal();

                testQuizFinal.setSerialNo(cur.getString(cur.getColumnIndex(KEY_SERIAL_NUMBER)));
                testQuizFinal.setTitle(cur.getString(cur.getColumnIndex(KEY_TITLE)));
                testQuizFinal.setTimetaken(cur.getString(cur.getColumnIndex(KEY_TIME_TAKEN)));
                testQuizFinal.setPdate(cur.getString(cur.getColumnIndex(KEY_PRESENT_DATE)));
                testQuizFinal.setTypeofPlay(cur.getString(cur.getColumnIndex(KEY_TYPE_OF_PLAY)));
                testQuizFinal.setTotalQuestions(cur.getInt(cur.getColumnIndex(KEY_TOTAL_QUESTIONS)));
                testQuizFinal.setAnswerstatus(cur.getString(cur.getColumnIndex(KEY_ANSWER_STATUS)));
                testQuizFinal.setQuestionAnswers(cur.getString(cur.getColumnIndex(KEY_QUESTION_ANSWER)));
                testQuizFinal.setQuestionPathType(cur.getString(cur.getColumnIndex(KEY_QUESTION_PATH_TYPE)));
                testQuizFinal.setOptions(cur.getString(cur.getColumnIndex(KEY_OPTIONS)));
                testQuizFinal.setStatus(cur.getString(cur.getColumnIndex(KEY_STATUS)));
                testQuizFinal.setTesttype(cur.getString(cur.getColumnIndex(KEY_TEST_TYPE)));
                testQuizFinal.setReaddata(cur.getString(cur.getColumnIndex(KEY_READ_DATA)));
                testQuizFinal.setOriginalname(cur.getString(cur.getColumnIndex(KEY_ORIGINAL_TEST_NAME)));

                testQuizFinal.setReviewexist(cur.getInt(cur.getColumnIndex(KEY_REVIEW_EXIST)));


                dailychallengeList.add(testQuizFinal);

            } while (cur.moveToNext());
        }
        cur.close();
        db.close();


        // return contact
        return dailychallengeList;
    }

    public int updatequizplayFinalstatus(String title, String status, String pdate, String typeofplay,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_STATUS, status);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ? AND "+KEY_TEST_TYPE+ " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay,type });
    }

    public int updatequizplayFinaloptions(String title, String options, String pdate, String typeofplay,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_OPTIONS, options);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ? AND "+KEY_TEST_TYPE+ " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay,type });
    }

    public String getQuizFinalOptions(String title, String pdate, String typeofplay,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String options = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"' AND "+KEY_TEST_TYPE+"='"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                options = (cur.getString(cur.getColumnIndex(KEY_OPTIONS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","answers.........."+options);
        // return contact
        return options;
    }

    public int updatequizplayFinalTimeTaken(String title, String time, String pdate, String typeofplay,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TIME_TAKEN, time);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ? AND "+KEY_TEST_TYPE+ " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay,type });
    }

    public String getQuiztimetakens(String title, String pdate, String typeofplay,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"' AND "+KEY_TEST_TYPE+"= '"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                answers = (cur.getString(cur.getColumnIndex(KEY_TIME_TAKEN)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","getQuizAnswerStatus.........."+answers);
        // return contact
        return answers;
    }

    public int updatequizplayFinalanswers(String title, String answers, String pdate, String typeofplay,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_ANSWER_STATUS, answers);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ? AND "+KEY_TEST_TYPE + "= ?",
                new String[] { String.valueOf(title),pdate,typeofplay,type });
    }

    public String getQuizAnswerStatus(String title, String pdate, String typeofplay,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"' AND "+KEY_TEST_TYPE+"= '"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                answers = (cur.getString(cur.getColumnIndex(KEY_ANSWER_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","getQuizAnswerStatus.........."+answers);
        // return contact
        return answers;
    }

    public int updatequizplayquestionanswersFinal(String title, String questionanswers, String pdate, String typeofplay,String type) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_QUESTION_ANSWER, questionanswers);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ? AND "+KEY_TEST_TYPE+ " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay,type  });
    }

    public String getQuizQuestionAnswersFinal(String title, String pdate, String typeofplay,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String questionanswers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"' AND "+KEY_TEST_TYPE+"= '"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                questionanswers = (cur.getString(cur.getColumnIndex(KEY_QUESTION_ANSWER)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","questionanswers.........."+questionanswers);
        // return contact
        return questionanswers;
    }

    public String getQuizQuestionPathFinal(String title, String pdate, String typeofplay,String type) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String questionanswers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"' AND "+KEY_TEST_TYPE +"= '"+type+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                questionanswers = (cur.getString(cur.getColumnIndex(KEY_QUESTION_PATH_TYPE)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","questionanswers.........."+questionanswers);
        // return contact
        return questionanswers;
    }


    public void insertquiztopics(String title, String serialno, String lastplayed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIAL_NUMBER, serialno); // Contact Name
        values.put(KEY_TOPIC_NAME, title); // Contact Phone
        values.put(KEY_LAST_PLAYED, lastplayed); // Contact Name

        // Inserting Row
        db.insert(TABLE_QUIZ_TOPICS, KEY_SERIAL_NUMBER, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public void deleteAllQuizTopics() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_QUIZ_TOPICS);
        db.close();
    }

    public int updatequiztopicslastplayed(String title, String lastplayed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_LAST_PLAYED, lastplayed);

        // updating row
        return db.update(TABLE_QUIZ_TOPICS, values, KEY_TOPIC_NAME + " = ?",
                new String[] { String.valueOf(title) });
    }

    public String getQuizTopicsLastPlayed(String title) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_TOPICS + " WHERE " + KEY_TOPIC_NAME + "='" + title+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                answers = (cur.getString(cur.getColumnIndex(KEY_LAST_PLAYED)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","answers.........."+answers);
        // return contact
        return answers;
    }



    public void insertquizplay(String title, int questioncount, String questions, String answers, String questionanswer, String path, String options) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title); // Contact Name
        values.put(KEY_TOTAL_QUESTIONS, questioncount); // Contact Phone
        values.put(KEY_QUESTIONS, questions); // Contact Name
        values.put(KEY_ANSWERS, answers); // Contact Phone
        values.put(KEY_QUESTION_ANSWER, questionanswer);
        values.put(KEY_QUESTION_PATH_TYPE, path);
        values.put(KEY_OPTIONS, options);

        // Inserting Row
        db.insert(TABLE_QUIZ_PLAY, KEY_TITLE, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public int updatequizplayoptions(String title, String options) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_OPTIONS, options);

        // updating row
        return db.update(TABLE_QUIZ_PLAY, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(title) });
    }

    public String getQuizOptions(String title) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String options = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY + " WHERE " + KEY_TITLE + "='" + title+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                options = (cur.getString(cur.getColumnIndex(KEY_OPTIONS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","answers.........."+options);
        // return contact
        return options;
    }

    public int updatequizplayanswers(String title, String answers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_ANSWERS, answers);

        // updating row
        return db.update(TABLE_QUIZ_PLAY, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(title) });
    }


    public String getQuizAnswers(String title) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY + " WHERE " + KEY_TITLE + "='" + title+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                answers = (cur.getString(cur.getColumnIndex(KEY_ANSWERS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","answers.........."+answers);
        // return contact
        return answers;
    }

    public String getQuizQuestionAnswers(String title) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String questionanswers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY + " WHERE " + KEY_TITLE + "='" + title+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                questionanswers = (cur.getString(cur.getColumnIndex(KEY_QUESTION_ANSWER)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","questionanswers.........."+questionanswers);
        // return contact
        return questionanswers;
    }
    public int updatequizplayquestionanswers(String title, String questionanswers) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_QUESTION_ANSWER, questionanswers);

        // updating row
        return db.update(TABLE_QUIZ_PLAY, values, KEY_TITLE + " = ?",
                new String[] { String.valueOf(title) });
    }

    public String getQuizQuestionPath(String title) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String questionanswers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY + " WHERE " + KEY_TITLE + "='" + title+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                questionanswers = (cur.getString(cur.getColumnIndex(KEY_QUESTION_PATH_TYPE)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","questionanswers.........."+questionanswers);
        // return contact
        return questionanswers;
    }

    public void deleteQuizPlayRecord(String title) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_QUIZ_PLAY + " WHERE " + KEY_TITLE + " = '" + title+"'");
        db.close();
    }

}
