package com.yomplex.tests.fragment

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.yomplex.tests.R
import com.yomplex.tests.activity.SignInActivity
import com.yomplex.tests.activity.WriteToUsActivity
import com.yomplex.tests.database.DatabaseHandler
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.ConstantPath.NOTIFICATION
import com.yomplex.tests.utils.ConstantPath.SOUNDS
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.setting_layout.view.*

class SettingFragment : Fragment(), View.OnClickListener {

    private val FEEDBACK_CONFIG_KEY = "feedback"
    private val WRITETOUS_CONFIG_KEY = "writetous"
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    var notification: Boolean = false
    val TAG: String = "Settings Fragment"
    var databaseHandler: QuizGameDataBase?= null

    var databaseHandler1: DatabaseHandler? = null
    private lateinit var auth: FirebaseAuth
    var mLastClickTime:Long = 0;
    var remoteConfig: FirebaseRemoteConfig? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.setting_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.settings.elevation = 15f
        try {
            var currentVersion = activity!!.packageManager.getPackageInfo(activity!!.packageName, 0).versionName
            var currentCode = activity!!.packageManager.getPackageInfo(activity!!.packageName, 0).versionCode
            //versionname.text = "V "+currentVersion+" ("+currentCode+")"
            view.versionname.text = "V "+currentVersion+" ("+currentCode+")"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        sharedPrefs = SharedPrefs()
        databaseHandler = QuizGameDataBase(activity);
        databaseHandler1 = DatabaseHandler(activity);

        /*remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()

        remoteConfig!!.setConfigSettings(configSettings)

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        remoteConfig!!.setDefaults(R.xml.remote_config_defaults)
        fetchVersion()*/
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)

        firebaseAnalytics.setCurrentScreen(activity!!, "Settings", null /* class override */)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this!!.activity!!, gso)

        sound = sharedPrefs?.getBooleanPrefVal(context!!, SOUNDS) ?: true
        view.cb_sounds_settings.isChecked = !sound
        if(!sound){
            sharedPrefs?.setBooleanPrefVal(context!!, SOUNDS, sound)
            view.sound_state_tv.text = "Sound On"
            /*val bundle = Bundle()
            bundle.putString("Category", "Settings")
            bundle.putString("Action", "Toggle")
            bundle.putString("Label", "Sounds")
            bundle.putString("Value", "1")
            firebaseAnalytics?.logEvent("Settings", bundle)*/

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sounds")
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
            bundle.putString(FirebaseAnalytics.Param.VALUE, "1")
            firebaseAnalytics?.logEvent("Toggle", bundle)
        }
        else{
            view.sound_state_tv.text = "Sound Off"
            /*val bundle = Bundle()
            bundle.putString("Category", "Settings")
            bundle.putString("Action", "Toggle")
            bundle.putString("Label", "Sounds")
            bundle.putString("Value", "0")
            firebaseAnalytics?.logEvent("Settings", bundle)*/

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sounds")
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
            bundle.putString(FirebaseAnalytics.Param.VALUE, "0")
            firebaseAnalytics?.logEvent("Toggle", bundle)
        }
        view.cb_sounds_settings.setOnClickListener {
            sound = !sound
            sharedPrefs?.setBooleanPrefVal(context!!, SOUNDS, sound)
            view.cb_sounds_settings.isChecked = !sound
            if(!sound){
                view.sound_state_tv.text = "Sound On"
                /*val bundle = Bundle()
                bundle.putString("Category", "Settings")
                bundle.putString("Action", "Toggle")
                bundle.putString("Label", "Sounds")
                bundle.putString("Value", "1")
                firebaseAnalytics?.logEvent("Settings", bundle)*/

                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sounds")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                bundle.putString(FirebaseAnalytics.Param.VALUE, "1")
                firebaseAnalytics?.logEvent("Toggle", bundle)
            }
            else{
                view.sound_state_tv.text = "Sound Off"
                /*val bundle = Bundle()
                bundle.putString("Category", "Settings")
                bundle.putString("Action", "Toggle")
                bundle.putString("Label", "Sounds")
                bundle.putString("Value", "0")
                firebaseAnalytics?.logEvent("Settings", bundle)*/

                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Sounds")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                bundle.putString(FirebaseAnalytics.Param.VALUE, "0")
                firebaseAnalytics?.logEvent("Toggle", bundle)
            }
        }
        notification = sharedPrefs?.getBooleanPrefVal(context!!, NOTIFICATION) ?: true
        view.cb_notifications_settings.isChecked = !notification
        if(!notification){
            sharedPrefs?.setBooleanPrefVal(context!!, NOTIFICATION, notification)
            view.notification_state_tv.text = "Notifications On"
            /*val bundle = Bundle()
            bundle.putString("Category", "Settings")
            bundle.putString("Action", "Toggle")
            bundle.putString("Label", "Notifications")
            bundle.putString("Value", "1")
            firebaseAnalytics?.logEvent("Settings", bundle)*/

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Notifications")
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
            bundle.putString(FirebaseAnalytics.Param.VALUE, "1")
            firebaseAnalytics?.logEvent("Toggle", bundle)
        }
        else{
            view.notification_state_tv.text = "Notifications Off"
            /*val bundle = Bundle()
            bundle.putString("Category", "Settings")
            bundle.putString("Action", "Toggle")
            bundle.putString("Label", "Notifications")
            bundle.putString("Value", "0")
            firebaseAnalytics?.logEvent("Settings", bundle)*/

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Notifications")
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
            bundle.putString(FirebaseAnalytics.Param.VALUE, "0")
            firebaseAnalytics?.logEvent("Toggle", bundle)
        }
        view.cb_notifications_settings.setOnClickListener{
            notification=!notification
            sharedPrefs?.setBooleanPrefVal(context!!, NOTIFICATION, notification)
            view.cb_notifications_settings.isChecked = !notification
            if(!notification){
                view.notification_state_tv.text = "Notifications On"
                /*val bundle = Bundle()
                bundle.putString("Category", "Settings")
                bundle.putString("Action", "Toggle")
                bundle.putString("Label", "Notifications")
                bundle.putString("Value", "1")
                firebaseAnalytics?.logEvent("Settings", bundle)*/

                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Notifications")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                bundle.putString(FirebaseAnalytics.Param.VALUE, "1")
                firebaseAnalytics?.logEvent("Toggle", bundle)
            }
            else{
                view.notification_state_tv.text = "Notifications Off"
                /*val bundle = Bundle()
                bundle.putString("Category", "Settings")
                bundle.putString("Action", "Toggle")
                bundle.putString("Label", "Notifications")
                bundle.putString("Value", "0")
                firebaseAnalytics?.logEvent("Settings", bundle)*/

                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Notifications")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                bundle.putString(FirebaseAnalytics.Param.VALUE, "0")
                firebaseAnalytics?.logEvent("Toggle", bundle)
            }
        }

        if((sharedPrefs?.getBooleanPrefVal(context!!, ConstantPath.ISNOTLOGIN) ?: true)){
            //sign in
            view.txt_sign_in.text = "Sign Out"
        }else{
            //not sign in
            view.txt_sign_in.text = "Sign In"
        }

        view.cl_terms_and_conditions.setOnClickListener(this)
        view.cl_write_to_us.setOnClickListener(this)
        view.signout.setOnClickListener(this)
        view.cl_feedback.setOnClickListener(this)
    }

    /*fun fetchVersion(){
        var cacheExpiration: Long = 3600 // 1 hour in seconds.
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

                val writetous = remoteConfig!!.getString(WRITETOUS_CONFIG_KEY)
                val feedback = remoteConfig!!.getString(FEEDBACK_CONFIG_KEY)

                Log.e("settings","....feedback...."+feedback);
                Log.e("settings","....writetous...."+writetous);
                //displayUpdateAlert()
            })
    }*/

    override fun onClick(v: View?) {
        val intent: Intent
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.cl_terms_and_conditions -> {
                sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                /*val bundle = Bundle()
                bundle.putString("Category", "Settings")
                bundle.putString("Action", "Policy")
                bundle.putString("Label", "PrivacyPolicy")
                firebaseAnalytics?.logEvent("Settings", bundle)*/

                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "PrivacyPolicy")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                firebaseAnalytics?.logEvent("Policy", bundle)
                intent = Intent(context, WriteToUsActivity::class.java)
                intent.putExtra("activityname", "termsandconditions")
                startActivity(intent)
            }

            R.id.cl_write_to_us -> {
                sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                /*val bundle = Bundle()
                bundle.putString("Category", "Settings")
                bundle.putString("Action", "Contact")
                bundle.putString("Label", "WriteToUs")
                firebaseAnalytics?.logEvent("Settings", bundle)*/

                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "WriteToUs")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                firebaseAnalytics?.logEvent("Contact", bundle)
                intent = Intent(context, WriteToUsActivity::class.java)
                intent.putExtra("activityname", "writetous")
                startActivity(intent)
            }

            R.id.cl_feedback -> {
                sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Feedback")
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
                firebaseAnalytics?.logEvent("Feedback", bundle)
                intent = Intent(context, WriteToUsActivity::class.java)
                intent.putExtra("activityname", "feedback")
                startActivity(intent)
            }

            R.id.signout -> {
                sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                if(view!!.txt_sign_in.text.equals("Sign In")) {
                    val intent = Intent(context!!, SignInActivity::class.java)
                    startActivity(intent)
                    //txt_sign_in.text = "Sign Out"
                } else if(view!!.txt_sign_in.text.equals("Sign Out")){
                    //passtxt_sign_in.text = "Sign In"

                    showDialog(context!!)
                }
            }
        }
    }


    private fun showDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder!!.setTitle("Sign Out")
        builder.setMessage("Are you sure, You want to sign out?")
        //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            sharedPrefs?.setBooleanPrefVal(context!!, ConstantPath.ISNOTLOGIN, false)
            sharedPrefs?.setBooleanPrefVal(context!!, ConstantPath.IS_FIRST_TIME, true)

            //sharedPrefs?.setBooleanPrefVal(context!!, ConstantPath.IS_LOGGED_IN, false)
            /*val bundle = Bundle()
            bundle.putString("Category", "Settings")
            bundle.putString("Action", "SignOut")
            bundle.putString("Label", "SignOut")
            firebaseAnalytics?.logEvent("Settings", bundle)*/

            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "SignOut")
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Settings")
            firebaseAnalytics?.logEvent("SignOut", bundle)

            databaseHandler!!.deleteAllQuizTopicsLatPlayed()
            databaseHandler!!.deleteAllQuizPlayFinal()
            databaseHandler1!!.deleteAllRevisions()
            databaseHandler1!!.deleteAllBookStatus()
            // Firebase sign out
            auth.signOut()

            // Google sign out

            mGoogleSignInClient!!.signOut().addOnCompleteListener(this,
                OnCompleteListener<Void> {
                    //updateUI(null)
                })
            val intent = Intent(context!!, SignInActivity::class.java)
            startActivity(intent)
            activity!!.finish()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }



}

private fun <TResult> Task<TResult>.addOnCompleteListener(settingFragment: SettingFragment, onCompleteListener: OnCompleteListener<TResult>) {

}
