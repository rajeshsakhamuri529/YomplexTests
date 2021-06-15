package com.yomplex.tests.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.ContextThemeWrapper
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.yomplex.tests.R
import com.yomplex.tests.Service.BooksDownloadService
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.activity_open_book.*
import java.io.File


class OpenBookActivity : BaseActivity() {

    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    var title:String=""
    var foldername:String=""
    var category:String=""
    private var mPDialog: ProgressDialog? = null
    override var layoutID: Int = R.layout.activity_open_book
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 1000 //3 seconds
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    internal val mRunnable: Runnable = Runnable {
        //if(!isDataFromFirebase){
            showProgressDialog("Please wait...");
        //}

    }
    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "BookView "+title)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "OpenBookActivity")
        }
    }
    override fun initView() {
        appBarID.elevation = 20F
        sharedPrefs = SharedPrefs()
        firebaseAnalytics = Firebase.analytics
        Log.e("open book activity", "initView...");
        //Initialize the Handler
      //  mDelayHandler = Handler()
        //Navigate with delay
      //  mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)
        showProgressDialog("Please wait...");
        backRL.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
            if(!sound) {
                //MusicManager.getInstance().play(context, R.raw.amount_low);
                // Is the sound loaded already?
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..." + Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
                // finish()
                val i = Intent(this, DashBoardActivity::class.java)
                i.putExtra("fragment", "books")
                startActivity(i)

            }else{
                val i = Intent(this, DashBoardActivity::class.java)
                i.putExtra("fragment", "books")
                startActivity(i)
                //finish()
            }
        }

        title = intent.getStringExtra("title")

        tv_title.text = title
        category = intent.getStringExtra("category")

        foldername = intent.getStringExtra("foldername")
        webView!!.setOnLongClickListener {
            true
        }
        webView!!.setLongClickable(false)
        // Below line prevent vibration on Long click
        webView!!.setHapticFeedbackEnabled(false);
        webView!!.settings.javaScriptEnabled = true

       //webView!!.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
       // webView!!.setScrollbarFadingEnabled(true);
      //  webView!!.setVerticalScrollBarEnabled(true);
        val dirpath = File((getCacheDir())!!.absolutePath)
        val dirFile1 = File(dirpath, "Books/" + category + "/" + foldername + "/index.html")

        //var bookpath = File()
        Log.e("open book activity","dirFile1.absolutePath......"+dirFile1.absolutePath)
        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                hideProgressDialog()
            }
        })
        if(dirFile1.exists()){
            webView!!.loadUrl(ConstantPath.WEBVIEW_FILE_PATH + dirFile1.absolutePath)
        }else{
            downloadServiceFromBackground(this@OpenBookActivity,db)
            webView!!.loadUrl(ConstantPath.WEBVIEW_PATH + "Books1/" + category + "/" + foldername + "/index.html")
        }

    }

    private fun downloadServiceFromBackground(
            mainActivity: Activity, db: FirebaseFirestore
    ) {
        BooksDownloadService.enqueueWork(mainActivity, db)
    }

    fun showProgressDialog(loadText: String) {
        hideProgressDialog()
        try {
            mPDialog = ProgressDialog.show(
                ContextThemeWrapper(this@OpenBookActivity, R.style.DialogCustom),
                "",
                loadText
            )
            mPDialog!!.setCancelable(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun hideProgressDialog() {
        try {
            if (mPDialog != null && mPDialog!!.isShowing()) {
                mPDialog!!.dismiss()
                mPDialog = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
        if(!sound) {
            //MusicManager.getInstance().play(context, R.raw.amount_low);
            // Is the sound loaded already?
            if (Utils.loaded) {
                Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                Log.e("Test", "Played sound...volume..." + Utils.volume);
                //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
            }
        }
        val intent = Intent(this, DashBoardActivity::class.java)
        intent.putExtra("fragment","books")
        startActivity(intent)
        finish()
    }
}