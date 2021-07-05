package com.yomplex.tests.Service;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yomplex.tests.database.QuizGameDataBase;
import com.yomplex.tests.model.Books;
import com.yomplex.tests.utils.SharedPrefs;
import com.yomplex.tests.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CopyService extends JobIntentService {

    public static final String URL = "url";
    /**
     * Unique job ID for this service.
     */
    static final int DOWNLOAD_JOB_ID = 1002;
    /**
     * Actions download
     */
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    private static QuizGameDataBase dataBase;
    private static Context context1;
    private static String dirpath;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(final Context context) {
        dataBase = new QuizGameDataBase(context);
        CopyService.context1 = context;
        File f =  new File((context.getCacheDir()).getAbsolutePath());
        dirpath = f.getAbsolutePath();
        Log.e("copy service","enqueueWork.......");

        try{
            // downloadServiceFromBackground(this@DashBoardActivity,db)


            String booksJsonString = Utils.loadJSONFromAsset( context1,"books.json");
            Log.e("dashboard","booksJsonString..."+booksJsonString);
            Gson gsonFile =new Gson();
            Type userListType = new TypeToken<ArrayList<Books>>(){}.getType();
            ArrayList<Books> booksCountmodel = gsonFile.fromJson(booksJsonString, userListType);
            Log.e("dashboard","booksJsonString...booksCountmodel..."+booksCountmodel.size());
            for(int i = 0;i<booksCountmodel.size();i++){
                Books booksCount = booksCountmodel.get(i);

                int count = dataBase.getBooksCount(booksCount.getId());
                if (count == 0) {
                    Log.e("dash board","booksJsonString...count......."+count);
                    dataBase.insertBooks(booksCount);
                }
            }
        }catch (Exception e){

        }




        Intent intent = new Intent(context1, CopyService.class);
        /*intent.putExtra(URL, url);
        intent.putExtra("version", version);
        intent.putExtra("title", title);
        intent.putExtra("category", category);*/
        intent.setAction(ACTION_DOWNLOAD);
        enqueueWork(context1, CopyService.class, DOWNLOAD_JOB_ID, intent);

    }
    private static void copyFile(InputStream in, OutputStream out,String filename,String category) throws IOException {
       try{
           byte[] buffer = new byte[1024];
           int read;
           while((read = in.read(buffer)) != -1){
               out.write(buffer, 0, read);
           }

           //dataBase.updatebooksreadfilestatusfromlocal(0,filename.replace(".zip",""));
           boolean iszip = Utils.unpackZip(dirpath+"/Books/"+category+"/",filename);
           if(iszip){
               File dirFile = new File(context1.getCacheDir(),"Books/"+category+"/"+filename);
               dirFile.delete();
               // File dirFile1 = new File(context1.getCacheDir(),"Books/"+category+"/"+title.toLowerCase().replace(" ","-").replace("'","")+"/thumbnail.svg");
               //dataBase.updatebooksversionFromLocal("1",filename.replace(".zip",""));


               String path = context1.getCacheDir().toString()+"/Books/"+category+"/"+filename.replace(".zip","");
               Log.e("Files", "Path: " + path);

               String p = path+"/thumbnail.svg";
               Log.e("Files", "thumbnail path......:" + p);
               dataBase.updatebooksLocal(p,1,1,filename.replace(".zip",""),category);

              /* File directory = new File(path);
               File[] files = directory.listFiles();
               Log.e("Files", "Size: "+ files.length);
               for (int i = 0; i < files.length; i++)
               {
                   Log.e("Files", "FileName:" + files[i].getName());
                   String p = path+"/"+files[i].getName()+"/thumbnail.svg";
                   Log.e("Files", "thumbnail path......:" + p);
                   dataBase.updatebooksLocal(p,1,1,files[i].getName(),filename.replace(".zip",""));

                   *//*dataBase.updatebooksthumbnail(p,files[i].getName(),filename.replace(".zip",""));
                   dataBase.updatebookscopystatus(1,files[i].getName(),filename.replace(".zip",""));
                   dataBase.updatebooksreadfilestatus(1,files[i].getName(),filename.replace(".zip",""));*//*
               }*/
            /*SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String date = dataBase.getBookContentDate();
            if(date != null){
                dataBase.updateBookContentDate(format1.format(Utils.date),1);
            }else{
                //Log.e("content download","date......................."+format1.format(Utils.date));
                dataBase.insertBookContentUpdateDate(format1.format(Utils.date));
            }*/
               dataBase.updatebooksdownloadstatusfromlocal(1,category,filename.replace(".zip",""));


           }
       }catch (Exception e){
           e.printStackTrace();
       }

    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_DOWNLOAD:
                    Log.e("copy service","onHandleWork.......");
                    /*AssetManager assetManager = context1.getAssets();
            *//*val files: Array<String>?
            files = assetManger.list(name)
            for (filenames in files){

            }*//*
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        File dirFile1 = new File(context1.getCacheDir(),"Books/");
                        in = assetManager.open("Books/"+"ALGEBRA");   // if files resides inside the "Files" directory itself
                        out = new FileOutputStream(dirFile1);
                        copyFile(in, out);
                        in.close();
                        in = null;
                        out.flush();
                        out.close();
                        out = null;
                    } catch(Exception e) {
                       Log.e("copy service","exception...."+e);
                    }*/

                    AssetManager assetManager = getAssets();
                    String[] files = null;

                    String state = Environment.getExternalStorageState();

                    if (Environment.MEDIA_MOUNTED.equals(state)) {
                        // We can read and write the media
                        // Checking file on assets subfolder
                        try {
                            files = assetManager.list("Books");
                        } catch (IOException e) {
                            Log.e("ERROR", "Failed to get asset file list.", e);
                        }
                        // Analyzing all file on assets subfolder
                        for(String filename : files) {
                            InputStream in;
                            OutputStream out;
                            // First: checking if there is already a target folder
                            File folder = new File(context1.getCacheDir() + "/Books");
                            boolean success = true;
                            if (!folder.exists()) {
                                success = folder.mkdir();
                            }
                            if (success) {
                                // Moving all the files on external SD
                                try {
                                    in = assetManager.open("Books" + "/" +filename);
                                    Log.e("copy service","filename......"+filename);

                                    String category = dataBase.getBooksCategory(filename.replace(".zip",""));
                                    File folder1 = new File(context1.getCacheDir() + "/Books"+ "/" + category);
                                    if (!folder1.exists()) {
                                        success = folder1.mkdir();
                                    }
                                    out = new FileOutputStream(context1.getCacheDir() + "/Books" + "/" + category + "/" + filename);
                                    Log.i("WEBVIEW", context1.getCacheDir() + "/Books" + "/" + filename);
                                    /*Runnable runnable = new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            try {
                                                copyFile(in, out,filename);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };*/
                                    copyFile(in, out,filename,category);
                                    /*new Thread(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            try {

                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();//to work in Background*/

                                    //copyFile(in, out,filename);

                                    in.close();
                                    in = null;
                                    out.flush();
                                    out.close();
                                    out = null;
                                } catch(IOException e) {
                                    Log.e("ERROR", "Failed to copy asset file: " + filename, e);
                                } finally {
                                    // Edit 3 (after MMs comment)
                                    /*try {
                                        in.close();
                                        in = null;
                                        out.flush();
                                        out.close();
                                        out = null;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/

                                }
                            }
                            else {
                                // Do something else on failure
                            }
                        }
                    } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                        // We can only read the media
                    } else {
                        // Something else is wrong. It may be one of many other states, but all we need
                        // is to know is we can neither read nor write
                    }

                    break;
            }
        }
    }
}
