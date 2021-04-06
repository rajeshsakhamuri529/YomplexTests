package com.yomplex.tests.utils;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.yomplex.tests.R;
import com.yomplex.tests.activity.PhoneAuthActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import static android.content.Context.AUDIO_SERVICE;

public class Utils {

    public static final int DATABASE_VERSION = 15;

    public static SoundPool soundPool;
    public static SoundPool soundPool1;
    public static SoundPool soundPool2;
    public static int soundID,soundID1,soundID2;
    public static boolean loaded = false;
    public static boolean istimerrunning = false;
    public static float volume;
    public static Date date = new Date();
    public static Tracker sTracker;
    public static CountDownTimer countDownTimer;
    public static int timerTime = 0;

    public void getApp(Context context){

    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public static Tracker getDefaultTracker(GoogleAnalytics sAnalytics) {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
        }

        return sTracker;
    }
    //public static int startTime = 0;
    /**
     * Returns the unique identifier for the device
     *
     * @return unique identifier for the device
     */
    public static String getDeviceIMEI(Context context) {
        /*String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null != tm) {
            deviceUniqueIdentifier = tm.getDeviceId();
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()) {
            deviceUniqueIdentifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }*/
        String androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidID;
    }
    public static void getPlayer(Context context){
        // Set the hardware buttons to control the music
       // context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10)
                    .build();
        } else {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }


        //soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID = soundPool.load(context, R.raw.amount_low, 1);

        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume/maxVolume;

    }


    /*public static void getPlayerForCorrect(Context context){
        // Set the hardware buttons to control the music
        // context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool1 = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        soundPool1.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID1 = soundPool1.load(context, R.raw.select_high_correct, 1);

        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume/maxVolume;

    }
    public static void getPlayerForwrong(Context context){
        // Set the hardware buttons to control the music
        // context.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // Load the sound
        soundPool2 = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        soundPool2.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId,
                                       int status) {
                loaded = true;
            }
        });
        soundID2 = soundPool2.load(context, R.raw.bounce_high_wrong, 1);

        // Getting the user sound settings
        AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = actualVolume/maxVolume;

    }*/
    public static boolean isOnline(Context _Context) {
        ConnectivityManager cm = (ConnectivityManager) _Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null)
            netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void reviewtransition(Context context, final WebView webView, Boolean isColourGreen){
        int colorFrom = context.getResources().getColor(R.color.answer_bg);
        int colorTo = context.getResources().getColor(R.color.review_wrong_answer);
        if(isColourGreen)
        {
            colorTo = context.getResources().getColor(R.color.green_right_answer);
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        final GradientDrawable background = (GradientDrawable) webView.getBackground();

        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //webView.setBackgroundColor((int) animator.getAnimatedValue());
                // Log.d("onAnimationUpdate",background.getColors()+"!")
                background.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public static void transition(Context context, final WebView webView, Boolean isColourGreen){
        int colorFrom = context.getResources().getColor(R.color.answer_bg);
        int colorTo = context.getResources().getColor(R.color.red_wrong_answer);
        if(isColourGreen)
        {
            colorTo = context.getResources().getColor(R.color.green_right_answer);
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        final GradientDrawable background = (GradientDrawable) webView.getBackground();

        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //webView.setBackgroundColor((int) animator.getAnimatedValue());
               // Log.d("onAnimationUpdate",background.getColors()+"!")
                background.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
    public static void challengetransition(Context context, final WebView webView, Boolean isColourGreen){
        int colorFrom = context.getResources().getColor(R.color.answer_bg);
        int colorTo = context.getResources().getColor(R.color.red_wrong_answer);
        if(isColourGreen)
        {
            colorTo = context.getResources().getColor(R.color.action_button);
        }

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        final GradientDrawable background = (GradientDrawable) webView.getBackground();

        colorAnimation.setDuration(1000); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //webView.setBackgroundColor((int) animator.getAnimatedValue());
                // Log.d("onAnimationUpdate",background.getColors()+"!")
                background.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public static void transitionBack(Context context, final WebView webView){
        int colorFrom = context.getResources().getColor(R.color.option_bg);
        int colorTo = context.getResources().getColor(R.color.option_bg);

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        final GradientDrawable background = (GradientDrawable) webView.getBackground();
        //final GradientDrawable background = (GradientDrawable) context.getResources().getDrawable(R.drawable.option_curved_border);
        colorAnimation.setDuration(1); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                //webView.setBackgroundColor((int) animator.getAnimatedValue());
                // Log.d("onAnimationUpdate",background.getColors()+"!")
                background.setColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }

    public static void makeDir(String path){
        File folder = new File(path);
        if (!folder.exists()){
            folder.mkdirs();
        }
    }

    public static String ofSize(String text, Integer start){
        Log.d("ofSize","!"+text+"!"+start);
        SpannableString ss1=  new SpannableString(text);
        ss1.setSpan(new RelativeSizeSpan(2f), start,start+4, 0); // set size
        //ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
        /*TextView tv= (TextView) findViewById(R.id.textview);
        tv.setText(ss1);*/
        return ss1.toString();
    }

    public static ArrayList<String> listAssetFiles(String path, Context context) {

        String[] list;
        String dummyPath = path;
        Log.d("listAssetFiles",path);
        if(path.endsWith("/")){
            dummyPath = path;
            dummyPath = dummyPath.substring(0,dummyPath.length()-1);
            Log.d("dummyPath",dummyPath);
            Log.d("path",path);
        }
        ArrayList<String> l = new ArrayList<>();
        try {
            list = context.getAssets().list(dummyPath);
            if (list.length > 0) {
                Log.d("list length",list.length+"!");
                // This is a folder
                for (String file : list) {
                    /*if (!listAssetFiles(path + "/" + file,context))
                        return false;
                    else {*/
                        l.add(file);
                        // This is a file
                        // TODO: add file name to an array list
                    //}
                }
            }else{
                Log.d("list length",list.length+"!");
            }
        } catch (IOException e) {
            return new ArrayList<String>();
        }
        Log.d("listAssetFiles",l+"!");
        return l;
    }

    public static List<String> getListOfFilesFromFolder(String path){
        File file = new File(path);
        return Arrays.asList(file.list());
    }

    public static Boolean jsoupWrapper(String path, Context context){
        //String fileName = "path/to/file.html";
        InputStream is=null;
        Document doc = null;
        /*String path = pathWrong.substring(21);
        Log.d("jsoupWrapper",path+"!"+pathWrong);*/
        try {
            //doc = Jsoup.parse(new File(path), "utf-8","http://google.com/");
            //is= context.getAssets().open(path);
//            is= context.getAssets().open("courses/Test Course/topic-one/basic-396/opt1.html");
            is= context.getAssets().open(path);
            doc = Jsoup.parse(is, "UTF-8", "http://example.com/");
            Element divTag = doc.getElementById("wrapper");
            System.out.println(divTag.text());
            if(divTag.text().toLowerCase().equals("true"))
            {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String jsoup2210(String pathWrong, Context context){
        //String fileName = "path/to/file.html";
        InputStream is=null;
        Document doc = null;
        Log.d("jsoup2210",pathWrong+"!");
        String path = pathWrong.substring(22);
        Log.d("jsoupWrapper",path+"!"+pathWrong);
        try {
            //doc = Jsoup.parse(new File(path), "utf-8","http://google.com/");
            //is= context.getAssets().open(path);
//            is= context.getAssets().open("courses/Test Course/topic-one/basic-396/opt1.html");
            is= context.getAssets().open(path);
            doc = Jsoup.parse(is, "UTF-8", "http://example.com/");
            Element divTag = doc.getElementById("wrapper");
            divTag.attr("rel", "stylesheet").attr("type", "text/css").attr("href", "AnswerQA.css");
            doc.getElementById("wrapper").attr("rel", "stylesheet").attr("type", "text/css").attr("href", "AnswerQA.css");
            System.out.println(divTag.text());
           /* if(divTag.text().equals("True"))
            {
                return true;
            }*/
            String htmldata = doc.html();
            Log.d("html",htmldata+"!");
            return htmldata;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String jsoup(String pathWrong, Context context){
        InputStream is=null;
        Document doc = null;
        Log.d("jsoup",pathWrong+"!");
        String path = pathWrong.substring(22);
        Log.d("jsoup2",path+"!"+pathWrong);
        try {
            //doc = Jsoup.parse(new File(path), "utf-8","http://google.com/");
            //is= context.getAssets().open(path);
//            is= context.getAssets().open("courses/Test Course/topic-one/basic-396/opt1.html");
            is= context.getAssets().open(path);
            doc = Jsoup.parse(is, "UTF-8", "http://example.com/");
            Element divTag = doc.getElementById("wrapper");

            Elements tables = doc.getElementsByTag("head"); //important part
            Log.d("tables",tables.toString()+"!");
            /*StringBuilder sb = new StringBuilder();
            sb.append("<HTML><HEAD><LINK href=\"styles.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
            sb.append(tables.toString());
            sb.append("</body></HTML>");
            Log.d("sb",sb.toString()+"!");*/

            divTag.attr("rel", "stylesheet").attr("type", "text/css").attr("href", "AnswerQA.css");
            //doc.getElementById("wrapper").attr("rel", "stylesheet").attr("type", "text/css").attr("href", "AnswerQA.css");

            doc.head().append("<link rel=\"stylesheet\" href=\"../../../../AnswerQA.css\" type=\"text/css\" media=\"all\" />");
            String head = doc.getElementsByTag("head").toString();
            Log.d("head",head+"!");
            System.out.println(divTag.text());
           /* if(divTag.text().equals("True"))
            {
                return true;
            }*/
            String htmldata = doc.html();
            Log.d("html",htmldata+"!");
            return htmldata;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String readFromFile(String path){
        File yourFile = new File(path);
        FileInputStream stream = null;
        String jsonStr = null;
        try {
            stream = new FileInputStream(yourFile);
            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

            jsonStr = Charset.defaultCharset().decode(bb).toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                if (stream != null){
                    stream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return jsonStr;
    }

    public static boolean unpackZip(String path, String zipname)
    {
        InputStream is;
        ZipInputStream zis;
        try
        {
            String filename;
            is = new FileInputStream(path + zipname);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null)
            {
                filename = ze.getName();
                Log.e("Utis","path........."+path);
                Log.e("Utis","filename........."+filename);
                // Need to create directories if not exists, or
                // it will generate an Exception...
                if (ze.isDirectory()) {
                    File fmd = new File(path,filename);
                    fmd.mkdirs();
                    continue;
                }

                FileOutputStream fout = new FileOutputStream(path +"/"+ filename);

                while ((count = zis.read(buffer)) != -1)
                {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }

            zis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteFolder(File removableFolder) {
        File[] files = removableFolder.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                boolean success;
                if (file.isDirectory())
                    success = deleteFolder(file);
                else success = file.delete();
                if (!success) return false;
            }
        }
        return removableFolder.delete();
    }

    public static void saveBitmap(Bitmap bitmap,Context context) {
        File imagePath = new File(context.getCacheDir() + "/screenshot.png"); ////File imagePath
        Log.e("test quiz","imagePath........"+imagePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch (IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    public static void store(Bitmap bm, String fileName,Context context){
        String dirPath = context.getCacheDir().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static String readFromFile(String filepath) {

        String ret = "";
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("names.json");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }*/

    /*public static void copyFolder(String name, String outPath, Context context){
        AssetManager assetManager = context.getAssets();
        List<String> files = getListOfFilesFromAsset(name, context);
        if (files != null) {
            for (String filename : files) {
                InputStream in = null;
                OutputStream out = null;
                try {
                    in = assetManager.open(name + "/" + filename);   // if files resides inside the "Files" directory itself
                    out = new FileOutputStream(outPath + filename);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                } catch (Exception e) {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }*/

    /*public static List<String> getListOfFilesFromAsset(String path, Context context){
        AssetManager assetManager = context.getAssets();
        try {
            return Arrays.asList(assetManager.list(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/



    public static void copyAssetFile(String name, String path, String outPath, Context context){
        AssetManager assetManager = context.getAssets();
            /*val files: Array<String>?
            files = assetManger.list(name)
            for (filenames in files){

            }*/
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(path+name);   // if files resides inside the "Files" directory itself
            out = new FileOutputStream(outPath+"/"+name);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    public static String loadJSONFromAsset(Context context, String path) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }



    void encrypt(String fileName, String path, String ePath)
            throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {
        // Here you read the cleartext.
        File extStore = Environment.getExternalStorageDirectory();
        FileInputStream fis = new FileInputStream(path + fileName);
        // This stream write the encrypted text. This stream will be wrapped by
        // another stream.
        FileOutputStream fos = new FileOutputStream(ePath + fileName);


        // Length is 16 byte
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw".getBytes(),
                "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        // Write bytes
        int b;
        byte[] d = new byte[8];
        while ((b = fis.read(d)) != -1) {
            cos.write(d, 0, b);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
    }

    void decrypt(String fileName, String path, String dPath)
            throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException {

        FileInputStream fis = new FileInputStream(path + fileName);

        FileOutputStream fos = new FileOutputStream(dPath + fileName);
        SecretKeySpec sks = new SecretKeySpec("MyDifficultPassw ".getBytes(),
                "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, sks);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
    }

    public static void countdowntimer(long time,Activity activity, TextView view){

        countDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
               // mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                istimerrunning = true;
                //Log.e("utils","countdowntimer...millisUntilFinished / 1000.."+millisUntilFinished / 1000);
                timerTime = (int) (millisUntilFinished / 1000);
                //PhoneAuthActivity phoneAuthActivity=new PhoneAuthActivity();
                //phoneAuthActivity.updatetimer(timerTime);

                activity.runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        if(timerTime <= 9){
                            //timerTXT.text = "0"+time
                            view.setText(timerTime+" s");
                        }else{
                           // timerTXT.text = ""+time
                            view.setText(timerTime+" s");
                        }
                       // view.setText("disconnect");
                    }
                });
            }

            public void onFinish() {
                //mTextField.setText("done!");
                istimerrunning = false;
            }
        };
        countDownTimer.start();


    }

    public static void startTimer(long time, Activity activity, TextView view){
        countdowntimer(time,activity,view);

    }

    public static void stopTimer(){
        if(istimerrunning){
            //timerTime = 0;
            countDownTimer.cancel();
        }

    }

    public static int getTimerTime(){

        return timerTime;
    }


}