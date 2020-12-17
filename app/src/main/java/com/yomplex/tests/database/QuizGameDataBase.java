package com.yomplex.tests.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.yomplex.tests.model.Challenge;
import com.yomplex.tests.model.TestQuiz;
import com.yomplex.tests.model.TestQuizFinal;
import com.yomplex.tests.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class QuizGameDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "quizgame";

    private static final String TABLE_TEST_TIMER= "TIMER";
    private static final String KEY_TEST_TIMER_TIME = "timertime";



    private static final String TABLE_TEST_CONTENT_DOWNLOAD = "testcontentdownload";
    private static final String KEY_TEST_CONTENT_VERSION = "TestContentVersion";
    private static final String KEY_TEST_CONTENT_URL = "TestContentUrl";
    private static final String KEY_TEST_DOWNLOAD_STATUS = "doenloadstatus";

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
    private static final String KEY_ANSWER_STATUS = "answerstatus";
    private static final String KEY_PRESENT_DATE = "pdate";
    private static final String KEY_TIME_TAKEN = "timetaken";

    private static final String KEY_ID = "id";


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



    String CREATE_TABLE_CHALLENGE_STATUS = "CREATE TABLE " + TABLE_CHALLENGE_STATUS + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_DATE + " TEXT, " + KEY_CHALLENGE_STATUS + " INTEGER)";

    String CREATE_TABLE_CHALLENGE_WEEKLY = "CREATE TABLE " + TABLE_CHALLENGE_WEEKLY + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CHALLENGE_DATE_FROM + " TEXT, "
            + KEY_CHALLENGE_DATE_TO + " TEXT, "
            + KEY_CHALLENGE_TEST_PASS_COUNT + " INTEGER)";

    String CREATE_TABLE_CHALLENGE = "CREATE TABLE " + TABLE_CHALLENGE + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_DATE + " TEXT, "
            + KEY_CHALLENGE_QUIZ_CORRECT_ANSWERS + " INTEGER, "
            + KEY_CHALLENGE_QUIZ_STATUS + " INTEGER, "
            + KEY_CHALLENGE_TEST_CORRECT_ANSWERS + " INTEGER, "
            + KEY_CHALLENGE_TEST_STATUS + " INTEGER)";


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
            + KEY_PRESENT_DATE + " TEXT)";

    String CREATE_TABLE_TEST_CONTENT_DOWNLOAD = "CREATE TABLE " + TABLE_TEST_CONTENT_DOWNLOAD + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TEST_CONTENT_VERSION + " TEXT, " + KEY_TEST_CONTENT_URL + " TEXT, " + KEY_TEST_DOWNLOAD_STATUS + " INTEGER)";

    String CREATE_QUIZ_PLAY_TABLE_WITH_TIMER = "CREATE TABLE " + TABLE_QUIZ_PLAY_WITH_TIME + "("
            + KEY_SERIAL_NUMBER + " TEXT, " + KEY_TITLE + " TEXT, " + KEY_LAST_PLAYED + " TEXT)";


    String CREATE_TABLE_TEST_TIMER = "CREATE TABLE " + TABLE_TEST_TIMER + "("
            + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_TEST_TIMER_TIME + " INTEGER)";


    String CREATE_QUIZ_PLAY_TABLE = "CREATE TABLE " + TABLE_QUIZ_PLAY + "("
            + KEY_TITLE + " TEXT, " + KEY_TOTAL_QUESTIONS + " TEXT, " + KEY_QUESTIONS + " TEXT, " + KEY_ANSWERS + " TEXT, " + KEY_QUESTION_ANSWER + " TEXT, " + KEY_QUESTION_PATH_TYPE + " TEXT, " + KEY_OPTIONS + " TEXT)";


    String CREATE_QUIZ_TOPICS_TABLE = "CREATE TABLE " + TABLE_QUIZ_TOPICS + "("
            + KEY_SERIAL_NUMBER + " TEXT PRIMARY KEY, " + KEY_TOPIC_NAME + " TEXT, " + KEY_LAST_PLAYED + " TEXT)";


    public QuizGameDataBase(Context context) {
        super(context, DATABASE_NAME, null, Utils.DATABASE_VERSION);
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

        db.execSQL(CREATE_TABLE_CHALLENGE_STATUS);
        db.execSQL(CREATE_TABLE_CHALLENGE);
        db.execSQL(CREATE_TABLE_CHALLENGE_WEEKLY);



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_PLAY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_TOPICS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_PLAY_WITH_TIME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ_WITH_TIMER_FINAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_CONTENT_DOWNLOAD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEST_TIMER);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHALLENGE_WEEKLY);
        // Create tables again
        onCreate(db);
    }


    public void insertChallengeWeekly(String fromdate, String todate, int testpasscount) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CHALLENGE_DATE_FROM, fromdate);
        values.put(KEY_CHALLENGE_DATE_TO, todate);
        values.put(KEY_CHALLENGE_TEST_PASS_COUNT, testpasscount);


        // Inserting Row
        db.insert(TABLE_CHALLENGE_WEEKLY, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public int getChallengeForWEEKLY(String fromdate, String todate) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        Log.e("quiz database","getChallengeForDate....fromdate..."+fromdate);
        int count = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE_WEEKLY + " WHERE " + KEY_CHALLENGE_DATE_FROM + "='" + fromdate+"' AND "+KEY_CHALLENGE_DATE_TO+"='"+todate+"'", new String[]{});

        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
        count = cur.getCount();
        cur.close();
        db.close();

        //  Log.e("quiz game database","answers.........."+answers);
        // return contact
        return count;
    }

    public int updateChallengeweeklystatus(String fromdate, String todate, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_CHALLENGE_TEST_PASS_COUNT, status);

        // updating row
        return db.update(TABLE_CHALLENGE_WEEKLY, values, KEY_CHALLENGE_DATE_FROM + " = ? AND "+KEY_CHALLENGE_DATE_TO + " = ?",
                new String[] { String.valueOf(fromdate),todate });
    }

    public int getChallengeWeeklystatus(String fromdate, String todate) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int count = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE_WEEKLY + " WHERE " + KEY_CHALLENGE_DATE_FROM + "='" + fromdate+"' AND "+KEY_CHALLENGE_DATE_TO + " ='"+todate+"'", new String[]{});
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

    public int getChallengeForDate(String date) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        Log.e("quiz database","getChallengeForDate....date..."+date);
        int count = 0;
        Challenge challenge =new Challenge();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE + " WHERE " + KEY_DATE + "='" + date+"'", new String[]{});
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

    public int getChallengeForTestStatus(String date) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int status = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_CHALLENGE + " WHERE " + KEY_DATE + "='" + date+"'", new String[]{});
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
                                       int status) {

        boolean isUpdateCustomer;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues initialValues = new ContentValues();


        initialValues.put(KEY_CHALLENGE_TEST_CORRECT_ANSWERS, correctanswers);
        initialValues.put(KEY_CHALLENGE_TEST_STATUS, status);


        String args[] = {date};

        isUpdateCustomer = db.update(TABLE_CHALLENGE, initialValues, KEY_DATE + "=?", args) > 0;
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


    public void insertTESTCONTENTDOWNLOAD(String version, String URL, int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEST_CONTENT_VERSION, version); // Contact Name
        values.put(KEY_TEST_CONTENT_URL, URL); // Contact Phone
        values.put(KEY_TEST_DOWNLOAD_STATUS, status);

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
    public int updatetestcontentdownloadstatus(int status) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEST_DOWNLOAD_STATUS, status);

        // updating row
        return db.update(TABLE_TEST_CONTENT_DOWNLOAD, values, KEY_ID + " = ?",
                new String[] { String.valueOf(1) });
    }

    public int gettesttopicdownloadstatus() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        int version = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_TEST_CONTENT_DOWNLOAD + " WHERE " + KEY_ID + "='" + 1+"'", new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                version = (cur.getInt(cur.getColumnIndex(KEY_TEST_DOWNLOAD_STATUS)));



            } while (cur.moveToNext());
        }
        cur.close();
        db.close();

        Log.e("quiz game database","version.........."+version);
        // return contact
        return version;
    }

    // code to update the single contact
    public int updatetestcontentversion(String version) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_TEST_CONTENT_VERSION, version);

        // updating row
        return db.update(TABLE_TEST_CONTENT_DOWNLOAD, values, KEY_ID + " = ?",
                new String[] { String.valueOf(1) });
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

    public void insertquiztopiclastplayed(String title, int serialno, String lastplayed) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SERIAL_NUMBER, serialno); // Contact Name
        values.put(KEY_TITLE, title); // Contact Phone
        values.put(KEY_LAST_PLAYED, lastplayed); // Contact Name

        // Inserting Row
        db.insert(TABLE_QUIZ_PLAY_WITH_TIME, KEY_SERIAL_NUMBER, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
    public void deleteAllQuizTopicsLatPlayed() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_QUIZ_PLAY_WITH_TIME);
        db.close();
    }

    public TestQuiz getQuizTopicsForTimerLastPlayed() {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        TestQuiz testQuiz =new TestQuiz();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_PLAY_WITH_TIME, new String[]{});
        /*Cursor cursor = db.query(TABLE_DAILY_CHALLENGE, new String[] { KEY_ID,
                        KEY_VERSION }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);*/
        if (cur.moveToFirst()) {
            do {

                testQuiz.setLastplayed((cur.getString(cur.getColumnIndex(KEY_LAST_PLAYED))));
                testQuiz.setTitle((cur.getString(cur.getColumnIndex(KEY_TITLE))));
                testQuiz.setSerialNo((cur.getString(cur.getColumnIndex(KEY_SERIAL_NUMBER))));

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

    public List<TestQuizFinal> getTestQuizList() {
        List<TestQuizFinal> dailychallengeList = new ArrayList<TestQuizFinal>();

        // SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_DAILY_CHALLENGE);
        String selectQuery = "SELECT  * FROM (select * from " + TABLE_QUIZ_WITH_TIMER_FINAL+" WHERE "+KEY_STATUS+"=1 order by "+KEY_ID+" DESC limit 5 ) order by "+KEY_ID+" DESC";
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

                dailychallengeList.add(testQuizFinal);

            } while (cur.moveToNext());
        }
        cur.close();
        db.close();


        // return contact
        return dailychallengeList;
    }

    public int updatequizplayFinalstatus(String title, String status, String pdate, String typeofplay) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_STATUS, status);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay });
    }

    public int updatequizplayFinaloptions(String title, String options, String pdate, String typeofplay) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_OPTIONS, options);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay });
    }

    public String getQuizFinalOptions(String title, String pdate, String typeofplay) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String options = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
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

    public int updatequizplayFinalTimeTaken(String title, String time, String pdate, String typeofplay) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TIME_TAKEN, time);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay });
    }

    public String getQuiztimetakens(String title, String pdate, String typeofplay) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
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

    public int updatequizplayFinalanswers(String title, String answers, String pdate, String typeofplay) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_ANSWER_STATUS, answers);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay });
    }

    public String getQuizAnswerStatus(String title, String pdate, String typeofplay) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String answers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
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

    public int updatequizplayquestionanswersFinal(String title, String questionanswers, String pdate, String typeofplay) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //values.put(KEY_NAME, contact.getName());
        values.put(KEY_QUESTION_ANSWER, questionanswers);

        // updating row
        return db.update(TABLE_QUIZ_WITH_TIMER_FINAL, values, KEY_TITLE + " = ? AND "+KEY_PRESENT_DATE + " = ? AND "+KEY_TYPE_OF_PLAY + " = ?",
                new String[] { String.valueOf(title),pdate,typeofplay  });
    }

    public String getQuizQuestionAnswersFinal(String title, String pdate, String typeofplay) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String questionanswers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
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

    public String getQuizQuestionPathFinal(String title, String pdate, String typeofplay) {
        //DailyChallenge dailyChallenge=new DailyChallenge();
        String questionanswers = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_QUIZ_WITH_TIMER_FINAL + " WHERE " + KEY_TITLE + "='" + title+"' AND "+KEY_PRESENT_DATE + " ='"+pdate+"' AND "+KEY_TYPE_OF_PLAY +"= '"+typeofplay+"'", new String[]{});
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
