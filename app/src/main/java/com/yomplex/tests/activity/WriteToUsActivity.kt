package com.yomplex.tests.activity

import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.os.SystemClock

import android.util.Log
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.yomplex.tests.BuildConfig
import com.yomplex.tests.R
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.activity_write_to_us.*

class WriteToUsActivity : AppCompatActivity(), View.OnClickListener {


    private val FEEDBACK_CONFIG_KEY = "feedback"
    private val WRITETOUS_CONFIG_KEY = "writetous"
    var url = ""
    var writetous = ""
    var feedback = ""
    //var remoteConfig: FirebaseRemoteConfig? = null
    var remoteConfig = Firebase.remoteConfig
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    var mLastClickTime:Long = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_to_us)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        appBarID.elevation = 20F
        //remoteConfig = FirebaseRemoteConfig.getInstance()

        /*val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()

        remoteConfig!!.setConfigSettings(configSettings)

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        remoteConfig!!.setDefaults(R.xml.remote_config_defaults)*/
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchVersion()

        sharedPrefs = SharedPrefs()
        writetous = sharedPrefs!!.getPrefVal(this,"writetous")!!
        feedback = sharedPrefs!!.getPrefVal(this,"feedback")!!
        val activityname = intent.getStringExtra("activityname")
        if(activityname == "writetous"){
            url = writetous
        } else if(activityname == "termsandconditions"){
            url = "https://www.yomplex.com/privacy.html"
        }else if(activityname == "feedback"){
            url = feedback
        }
        backRL.setOnClickListener(this)
        wv_write_to_us.settings.javaScriptEnabled = true
        wv_write_to_us.webViewClient = object : WebViewClient() {

            /*override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if(url!!.startsWith("https://google.com")){
                    wv_write_to_us.loadUrl(url)
                }
                else{
                    val intent = Intent(ACTION_VIEW, Uri.parse(url))
                    view?.context?.startActivity(intent)
                }
                return super.shouldOverrideUrlLoading(view, url)
            }*/

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                Log.e("write to us activity","onpage started....");
                view?.visibility = View.INVISIBLE
                progress_bar_wv.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                Log.e("write to us activity","onPageFinished....");
                view?.visibility = View.VISIBLE
                progress_bar_wv.visibility = View.INVISIBLE
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)

                Log.e("write to us activity","onReceivedError...."+error);
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                Log.e("write to us activity","onReceivedHttpError...."+errorResponse);
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                super.onReceivedSslError(view, handler, error)
                Log.e("write to us activity","onReceivedSslError...."+error);
            }

            override fun onRenderProcessGone(
                view: WebView?,
                detail: RenderProcessGoneDetail?
            ): Boolean {
                return super.onRenderProcessGone(view, detail)
                Log.e("write to us activity","onRenderProcessGone....");
            }




        }
        wv_write_to_us.loadUrl(url)

    }
    fun fetchVersion(){
        /*var cacheExpiration: Long = 3600 // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (remoteConfig!!.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0
        }

        remoteConfig!!.fetch(cacheExpiration)
            .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    remoteConfig!!.activateFetched()
                }

                 writetous = remoteConfig!!.getString(WRITETOUS_CONFIG_KEY)
                 feedback = remoteConfig!!.getString(FEEDBACK_CONFIG_KEY)

                Log.e("settings","....feedback...."+feedback);
                Log.e("settings","....writetous...."+writetous);
                //displayUpdateAlert()
            })*/

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.e("grade activity", "Config params updated: $updated")
                   // Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(this, "Fetch failed", Toast.LENGTH_SHORT).show()
                }
                writetous = remoteConfig!!.getString(WRITETOUS_CONFIG_KEY)
                feedback = remoteConfig!!.getString(FEEDBACK_CONFIG_KEY)

                Log.e("settings","....feedback...."+feedback);
                Log.e("settings","....writetous...."+writetous);
                //displayUpdateAlert()
            }
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.backRL -> {
                sound = sharedPrefs?.getBooleanPrefVal(this, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                val intent = Intent(this, DashBoardActivity::class.java)
                intent.putExtra("fragment","Settings")
                startActivity(intent)
                finish()
            }

        }
    }
}