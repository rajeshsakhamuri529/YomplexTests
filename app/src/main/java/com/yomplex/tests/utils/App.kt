package com.yomplex.tests.utils

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
//import org.junit.experimental.results.ResultMatchers.isSuccessful
import com.google.android.gms.tasks.Task

import com.downloader.PRDownloader
import com.google.android.gms.tasks.OnCompleteListener
//import javax.swing.UIManager.put
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.analytics.GoogleAnalytics
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.google.android.gms.analytics.Tracker
import android.R
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.yomplex.tests.utils.Utils


private val TAG = App::class.java.simpleName


class App : Application() {
    private var sAnalytics: GoogleAnalytics? = null
    private var sTracker: Tracker? = null


    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        FirebaseApp.initializeApp(this);

        Log.d("onCreate","app")
        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        PRDownloader.initialize(applicationContext)
        sAnalytics = GoogleAnalytics.getInstance(this);
        sTracker = Utils.getDefaultTracker(sAnalytics);

        // set in-app defaults
        /*var remoteConfigDefaults = HashMap<String,Any>()
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false)
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0")
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
            "https://play.google.com/store/apps/details?id=com.sembozdemir.renstagram"
        )

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults)
        firebaseRemoteConfig.fetch() // fetch every minutes
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "remote config is fetched.")
                    firebaseRemoteConfig.activateFetched()
                }
            }*/

        firebaseRemoteConfig.fetch(5) // fetch 5 seconds
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "remote config is fetched.")
                    firebaseRemoteConfig.activate();
                    //firebaseRemoteConfig.activateFetched()
                }
            }
    }



}