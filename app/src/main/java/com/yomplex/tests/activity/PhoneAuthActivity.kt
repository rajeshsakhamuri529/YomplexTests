package com.yomplex.tests.activity

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.yomplex.tests.R
import com.yomplex.tests.Service.ContentDownloadService
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.model.TestDownload
import com.yomplex.tests.model.User
import com.yomplex.tests.model.UserContentVersion
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.phone_auth.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PhoneAuthActivity : BaseActivity() {

    lateinit var minutescountDownTimer: CountDownTimer
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    var firestore: FirebaseFirestore? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var databaseHandler: QuizGameDataBase?= null
    private lateinit var auth: FirebaseAuth
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var mPDialog: ProgressDialog? = null
    private var backPressedTime: Long = 0
    private var backPressToastMessage: Toast? = null
    override var layoutID: Int = R.layout.phone_auth

    override fun initView() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPrefs = SharedPrefs()
        firestore = FirebaseFirestore.getInstance()
        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        firestore!!.firestoreSettings = settings
        databaseHandler = QuizGameDataBase(this);

        phoneRL.visibility = View.VISIBLE
        otpRL.visibility = View.GONE
        send_otp_btn.visibility = View.VISIBLE
        confirm_btn.visibility = View.GONE
        error_txt.visibility = View.INVISIBLE
        error_otp_txt.visibility = View.INVISIBLE
        timerTXT.visibility = View.INVISIBLE
        //Utils.startTimer(90000)
        //Log.e("phone auth","timer time...."+Utils.getTimerTime())



        edtPhoneNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(s.length == 0){
                    country_code_txt.setTextColor(Color.parseColor("#C0C0C0"))
                    error_txt.visibility = View.INVISIBLE
                    //error_txt.visibility = View.VISIBLE
                    //error_txt.text = "Please enter mobile number"
                }else if(s.length < 10){
                    country_code_txt.setTextColor(Color.parseColor("#424242"))
                    error_txt.visibility = View.INVISIBLE
                    //error_txt.visibility = View.VISIBLE
                    //error_txt.text = "Please enter a valid mobile number"
                }
                else if(s.length == 10){
                    country_code_txt.setTextColor(Color.parseColor("#424242"))
                    error_txt.visibility = View.INVISIBLE
                    //error_txt.visibility = View.INVISIBLE

                }


            }

        })


        edtotpnumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {


                error_otp_txt.visibility = View.INVISIBLE
                if(s.length == 0){
                    back_btn.isEnabled = true
                    back_btn.background = resources.getDrawable(R.drawable.close_button)
                }else{
                    back_btn.isEnabled = false
                    back_btn.background = resources.getDrawable(R.drawable.review_no_exist)
                }

            }

        })



        //edtPhoneNumber.showKeyboard()
        send_otp_btn.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(this@PhoneAuthActivity, ConstantPath.SOUNDS) ?: true
            Log.e("phone auth","sound......."+sound)
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            if(Utils.isOnline(this@PhoneAuthActivity)){

                signIn()
            }else{
                Toast.makeText(this@PhoneAuthActivity,"Please connect internet!", Toast.LENGTH_SHORT).show()
            }

        }


        confirm_btn.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(this@PhoneAuthActivity, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            if(Utils.isOnline(this@PhoneAuthActivity)){
                if (TextUtils.isEmpty(edtotpnumber.getText().toString())){
                    //when mobile number text field is empty displaying a toast message.
                    //Toast.makeText(activity, "Please enter OTP.",Toast.LENGTH_SHORT).show()
                    error_otp_txt.visibility = View.VISIBLE
                    error_otp_txt.text = "Please enter OTP"
                }else{
                    firebaseAnalytics.logEvent("Confirm_OTP") {

                    }
                    it.hideKeyboard()
                    error_otp_txt.visibility = View.INVISIBLE
                    verifyPhoneNumberWithCode(storedVerificationId,edtotpnumber.text.toString())
                }

            }else{
                Toast.makeText(this@PhoneAuthActivity,"Please connect internet!", Toast.LENGTH_SHORT).show()
            }

        }

        back_btn.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(this@PhoneAuthActivity, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }

            if(phoneRL.visibility == View.VISIBLE){
                Log.e("phone activity","on back pressed.......");
                val intent = Intent(this@PhoneAuthActivity, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }else if(otpRL.visibility == View.VISIBLE){
                phoneRL.visibility = View.VISIBLE
                otpRL.visibility = View.GONE
                send_otp_btn.visibility = View.VISIBLE
                confirm_btn.visibility = View.GONE
                error_txt.visibility = View.INVISIBLE
                error_otp_txt.visibility = View.INVISIBLE
                timerTXT.visibility = View.INVISIBLE
            }

        }





        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.e("phone auth fragment", "onVerificationCompleted:$credential")

//below line is used for getting OTP code which is sent in phone auth credentials.
                val code = credential.getSmsCode()
                //checking if the code is null or not.
                if (code != null) {
                    //if the code is not null then we are setting that code to our OTP edittext field.
                    edtotpnumber.setText(code)
                    //after setting this code to OTP edittext field we are calling our verifycode method.
                    //verifyCode(code)

                }


                //signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.e("phone auth fragment", "onVerificationFailed", e)
                phoneRL.visibility = View.VISIBLE
                otpRL.visibility = View.GONE
                send_otp_btn.visibility = View.VISIBLE
                confirm_btn.visibility = View.GONE
                if (e is FirebaseAuthInvalidCredentialsException) {
                    // Invalid request

                } else if (e is FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    error_txt.visibility = View.VISIBLE
                    error_txt.text = "Too many requests, try again later."

                }

                // Show a message and update the UI
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.e("phone auth fragment", "onCodeSent:$verificationId")

                // Save verification ID and resending token so we can use them later
                storedVerificationId = verificationId
                resendToken = token
            }
        }


    }

    override fun onBackPressed() {
        //super.onBackPressed()
        sound = sharedPrefs?.getBooleanPrefVal(this@PhoneAuthActivity, ConstantPath.SOUNDS) ?: true
        if(!sound){
            // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
            //  mediaPlayer.start()
            if (Utils.loaded) {
                Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                Log.e("Test", "Played sound...volume..."+ Utils.volume);
                //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
            }
        }
        if(phoneRL.visibility == View.VISIBLE){
            Log.e("phone activity","on back pressed.......");
            val intent = Intent(this@PhoneAuthActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()

        }else if(otpRL.visibility == View.VISIBLE){
            phoneRL.visibility = View.VISIBLE
            otpRL.visibility = View.GONE
            send_otp_btn.visibility = View.VISIBLE
            confirm_btn.visibility = View.GONE
            error_txt.visibility = View.INVISIBLE
            error_otp_txt.visibility = View.INVISIBLE
            timerTXT.visibility = View.INVISIBLE

        }

    }

    fun EditText.showKeyboard() {
        post {
            requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun View.hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }



    private fun signIn() {
        if (TextUtils.isEmpty(edtPhoneNumber.getText().toString())){
            //when mobile number text field is empty displaying a toast message.
            //Toast.makeText(activity, "Please enter mobile number.",Toast.LENGTH_SHORT).show()
            error_txt.visibility = View.VISIBLE
            error_txt.text = "Please enter mobile number"
        }else if(edtPhoneNumber.getText().toString().length < 10){
           // Toast.makeText(this@PhoneAuthActivity, "Please enter a valid mobile number.",Toast.LENGTH_SHORT).show()
            error_txt.visibility = View.VISIBLE
            error_txt.text = "Please enter a valid mobile number"
        }else{


            var prevNumber = sharedPrefs?.getPrefVal(this@PhoneAuthActivity,"prevnumber")
            Log.e("phone auth","prevnumber............"+prevNumber)
            var phone = "+91" + edtPhoneNumber.getText().toString()
            if(prevNumber == null){
                firebaseAnalytics.logEvent("Send_OTP") {

                }
                //if the text field is not empty we are calling our send OTP method for gettig OTP from Firebase.
                //var phone = "+91" + edtPhoneNumber.getText().toString()
                error_txt.visibility = View.INVISIBLE
                sendVerificationCode(phone)
            }else if(phone.equals(prevNumber)){
                if(Utils.getTimerTime() == 0){
                    firebaseAnalytics.logEvent("Send_OTP") {

                    }

                    //if the text field is not empty we are calling our send OTP method for gettig OTP from Firebase.
                    //var phone = "+91" + edtPhoneNumber.getText().toString()
                    error_txt.visibility = View.INVISIBLE
                    sendVerificationCode(phone)
                }else{
                    phoneRL.visibility = View.GONE
                    otpRL.visibility = View.VISIBLE
                    send_otp_btn.visibility = View.GONE
                    confirm_btn.visibility = View.VISIBLE
                    timerTXT.visibility = View.VISIBLE
                    edtPhoneNumber.hideKeyboard()
                    send_otp_phone.text = "+91 "+edtPhoneNumber.text.toString()
                    edtotpnumber.showKeyboard()
                    Utils.stopTimer()
                    Utils.startTimer((Utils.getTimerTime()*1000).toLong(),this@PhoneAuthActivity,timerTXT)
                }

            }else{
                firebaseAnalytics.logEvent("Send_OTP") {

                }

                //if the text field is not empty we are calling our send OTP method for gettig OTP from Firebase.
                //var phone = "+91" + edtPhoneNumber.getText().toString()
                error_txt.visibility = View.INVISIBLE
                sendVerificationCode(phone)
            }



        }
    }




    private fun sendVerificationCode(phoneNumber: String) {
        phoneRL.visibility = View.GONE
        otpRL.visibility = View.VISIBLE
        send_otp_btn.visibility = View.GONE
        confirm_btn.visibility = View.VISIBLE
        timerTXT.visibility = View.VISIBLE
        edtPhoneNumber.hideKeyboard()
        send_otp_phone.text = "+91 "+edtPhoneNumber.text.toString()
        edtotpnumber.showKeyboard()
        sharedPrefs?.setPrefVal(this@PhoneAuthActivity, "prevnumber", phoneNumber)
        Utils.stopTimer()
        Utils.startTimer(90000,this@PhoneAuthActivity,timerTXT)

        Log.e("phone auth fragment","sendVerificationCode....phoneNumber.."+phoneNumber)
        //this method is used for getting OTP on user phone number.
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(90, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@PhoneAuthActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {

        Log.e("phone auth","verificationId........"+verificationId)
        if(verificationId != ""){
            // [START verify_with_code]
              val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
            // [END verify_with_code]
              signInWithPhoneAuthCredential(credential)
        }else{
            error_otp_txt.visibility = View.VISIBLE
            error_otp_txt.text = "Please enter valid OTP"
            back_btn.isEnabled = true
            back_btn.background = resources.getDrawable(R.drawable.close_button)
        }

    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showProgressDialog("Signing In...")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this@PhoneAuthActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("phone auth fragment", "signInWithCredential:success")

                    val user = task.result?.user
                    Utils.stopTimer()
                    Utils.timerTime = 0
                    //val user = auth.currentUser
                    updateUI(user)
                } else {
                    hideProgressDialog()
                    // Sign in failed, display a message and update the UI
                    Log.e("phone auth fragment", "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        //if the code is not correct then we are displaying an error message to the user.
                        //Toast.makeText(activity, task.exception!!.message, Toast.LENGTH_LONG).show()
                        error_otp_txt.visibility = View.VISIBLE
                        error_otp_txt.text = "Please enter valid OTP"
                    }
                    // Update UI
                }
            }
    }
    // [END sign_in_with_phone]

    fun showProgressDialog(loadText: String) {
        hideProgressDialog()
        try {
            mPDialog = ProgressDialog.show(
                ContextThemeWrapper(this@PhoneAuthActivity, R.style.DialogCustom),
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

    private fun updateUI(user: FirebaseUser?) {
        try {

            if (user != null) { // 8099256159
                Log.e("sign in activity","...update ui.....");
                sharedPrefs?.setBooleanPrefVal(this@PhoneAuthActivity, ConstantPath.ISNOTLOGIN, true)
                sharedPrefs?.setBooleanPrefVal(this@PhoneAuthActivity, ConstantPath.IS_FIRST_TIME, false)
                //hideProgressDialog()

                var android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.e("sign in activity","android_id....."+android_id)
                Log.e("sign in activity","user.phoneNumber....."+user.phoneNumber)
                sharedPrefs?.setPrefVal(this@PhoneAuthActivity, ConstantPath.UID, user!!.uid)
                sharedPrefs?.setPrefVal(this@PhoneAuthActivity, "email", "")
                sharedPrefs?.setPrefVal(this@PhoneAuthActivity, "phonenumber", user.phoneNumber!!)
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val currentDate = sdf.format(Date())

                val sdf1 = SimpleDateFormat("dd-MM-yyyy")
                val updatedDate = sdf1.format(Date())
                //sharedPrefs.setPrefVal(this@GradeActivity, "created_date", currentDate)
                // firebaseAnalytics.setUserId(user!!.uid)




                //firebaseAnalytics.setUserProperty("email", user.email)

                firebaseAnalytics.logEvent("On_sucessful_phone_number_Login") {
                    param("LoginSuccess", "LoggedIn")

                }
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                    //param("LoginSuccess", "LoggedIn")
                    //param(FirebaseAnalytics.Param.SCREEN_CLASS, "SignInActivity")
                }
                var token:String = sharedPrefs!!.getPrefVal(this@PhoneAuthActivity,"firebasetoken")!!

                val docRef = firestore!!.collection("users")
                docRef.whereEqualTo("phonenumber",user.phoneNumber)
                    .get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                        override fun onComplete(task: Task<QuerySnapshot>) {
                            if (task.isSuccessful){

                                Log.e("sign in activity","...task.isSuccessful......");
                                Log.e("sign in activity","...task.isSuccessful.....task.getResult()."+task.getResult()+".......size..."+task.getResult().size());

                                if(task.getResult().size() > 0){
                                    task.getResult().forEachIndexed { index, document ->
                                        Log.e("dashboard","document id......"+document.id)

                                        //var token:String = sharedPrefs!!.getPrefVal(this@DashBoardActivity,"firebasetoken")!!
                                        val data = hashMapOf("updatedon" to updatedDate,"firebaseToken" to token)

                                        firestore!!.collection("users").document(document.id)
                                            .set(data, SetOptions.merge())
                                        runOnUiThread {
                                            hideProgressDialog()

                                            val intent = Intent(this@PhoneAuthActivity, DashBoardActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }

                                        /*val docRef = firestore!!.collection("usercontentversion")
                                        docRef.whereEqualTo("phonenumber",user.phoneNumber).whereEqualTo("userid",user.uid)
                                            .get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                                                override fun onComplete(task: Task<QuerySnapshot>) {
                                                    if (task.isSuccessful) {
                                                        if (task.getResult().size() > 0) {
                                                            if(task.getResult().size() == 1){
                                                                runOnUiThread {
                                                                    hideProgressDialog()
                                                                    //Toast.makeText(this@PhoneAuthActivity,"Sign-In success!",Toast.LENGTH_SHORT).show()
                                                                    *//*val connectivityManager = getSystemService(
                                                                        Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                                                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                                                                    val isConnected: Boolean = activeNetwork?.isConnected == true
                                                                    Log.d("isConnected",isConnected.toString()+"!")
                                                                    if(isNetworkConnected()) {
                                                                        downloadServiceFromBackground(this@PhoneAuthActivity,firestore!!)
                                                                    }*//*
                                                                    val intent = Intent(this@PhoneAuthActivity, DashBoardActivity::class.java)
                                                                    startActivity(intent)
                                                                    finish()
                                                                }
                                                            }else{
                                                                for(i in 0 until task.getResult().size()) {
                                                                    firestore!!.collection("usercontentversion").document(task.getResult().documents.get(i).id)
                                                                        .delete()
                                                                }
                                                                var userContentVersion = UserContentVersion()
                                                                userContentVersion.setUseremail("");
                                                                userContentVersion.setUserid(user.uid);
                                                                userContentVersion.setPhonenumber(user.phoneNumber);
                                                                var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                                                for(i in 0 until testcontentlist!!.size) {
                                                                    if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                                        userContentVersion.setAlgebraversion(testcontentlist.get(i).testversion);
                                                                    }
                                                                    if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                                        userContentVersion.setCalculus1version(testcontentlist.get(i).testversion);
                                                                    }
                                                                    if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                                        userContentVersion.setCalculus2version(testcontentlist.get(i).testversion);
                                                                    }
                                                                    if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                                        userContentVersion.setGeometryversion(testcontentlist.get(i).testversion);
                                                                    }
                                                                    if (testcontentlist.get(i).testtype.equals("other")) {
                                                                        userContentVersion.setOtherversion(testcontentlist.get(i).testversion);
                                                                    }


                                                                }

                                                                firestore!!.collection("usercontentversion")
                                                                    .add(userContentVersion)
                                                                    .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                                                        override fun onComplete(task: Task<DocumentReference>) {
                                                                            if (task.isSuccessful) {
                                                                                Log.e("user", "user added successfully")
                                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")

                                                                                runOnUiThread {
                                                                                    hideProgressDialog()
                                                                                    //Toast.makeText(this@PhoneAuthActivity,"Sign-In success!",Toast.LENGTH_SHORT).show()
                                                                                    *//*val connectivityManager = getSystemService(
                                                                                        Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                                                                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                                                                                    val isConnected: Boolean = activeNetwork?.isConnected == true
                                                                                    Log.d("isConnected",isConnected.toString()+"!")
                                                                                    if(isNetworkConnected()) {
                                                                                        downloadServiceFromBackground(this@PhoneAuthActivity,firestore!!)
                                                                                    }*//*
                                                                                    val intent = Intent(this@PhoneAuthActivity, DashBoardActivity::class.java)
                                                                                    startActivity(intent)
                                                                                    finish()
                                                                                }
                                                                            } else {
                                                                                Log.e("user", task.exception.toString())
                                                                            }
                                                                        }
                                                                    })




                                                            }
                                                        }else{
                                                            //Toast.makeText(this@SignInActivity,"task.getResult().size()..."+task.getResult().size(),Toast.LENGTH_SHORT).show()
                                                            var userContentVersion = UserContentVersion()
                                                            userContentVersion.setUseremail("");
                                                            userContentVersion.setUserid(user!!.uid);
                                                            userContentVersion.setPhonenumber(user.phoneNumber);
                                                            userContentVersion.setAlgebraversion("-1");
                                                            userContentVersion.setCalculus1version("-1");
                                                            userContentVersion.setCalculus2version("-1");
                                                            userContentVersion.setGeometryversion("-1");
                                                            userContentVersion.setOtherversion("-1");

                                                            firestore!!.collection("usercontentversion")
                                                                .add(userContentVersion)
                                                                .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                                                    override fun onComplete(task: Task<DocumentReference>) {
                                                                        if (task.isSuccessful) {
                                                                            Log.e("user", "user added successfully")
                                                                            runOnUiThread {
                                                                                hideProgressDialog()
                                                                                //Toast.makeText(this@PhoneAuthActivity,"Sign-In success!",Toast.LENGTH_SHORT).show()
                                                                                *//*val connectivityManager = getSystemService(
                                                                                    Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                                                                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                                                                                val isConnected: Boolean = activeNetwork?.isConnected == true
                                                                                Log.d("isConnected",isConnected.toString()+"!")
                                                                                if(isNetworkConnected()) {
                                                                                    downloadServiceFromBackground(this@PhoneAuthActivity,firestore!!)
                                                                                }*//*
                                                                                val intent = Intent(this@PhoneAuthActivity, DashBoardActivity::class.java)
                                                                                startActivity(intent)
                                                                                finish()
                                                                            }

                                                                        } else {
                                                                            Log.e("user", task.exception.toString())
                                                                        }
                                                                    }
                                                                })
                                                        }
                                                    }

                                                }
                                            })*/


                                    }
                                }else{
                                    var userObj = User()
                                    userObj.username = ""
                                    userObj.phonenumber = user.phoneNumber
                                    userObj.deviceuniqueid = android_id
                                    userObj.createdon = currentDate
                                    userObj.updatedon = updatedDate
                                    userObj.firebaseToken = token

                                    databaseHandler!!.insertUserSync(userObj,0)

                                    /*var userContentVersion = UserContentVersion()
                                    userContentVersion.setUseremail("");
                                    userContentVersion.setUserid(user!!.uid);
                                    userContentVersion.setPhonenumber(user.phoneNumber);
                                    userContentVersion.setAlgebraversion("-1");
                                    userContentVersion.setCalculus1version("-1");
                                    userContentVersion.setCalculus2version("-1");
                                    userContentVersion.setGeometryversion("-1");
                                    userContentVersion.setOtherversion("-1");

                                    firestore!!.collection("usercontentversion")
                                        .add(userContentVersion)
                                        .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                            override fun onComplete(task: Task<DocumentReference>) {
                                                if (task.isSuccessful) {
                                                    Log.e("user", "user added successfully")


                                                } else {
                                                    Log.e("user", task.exception.toString())
                                                }
                                            }
                                        })*/



                                   // Toast.makeText(this@PhoneAuthActivity,"Sign-In success!",Toast.LENGTH_SHORT).show()
                                    /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                                    val isConnected: Boolean = activeNetwork?.isConnected == true
                                    Log.d("isConnected",isConnected.toString()+"!")
                                    if(isNetworkConnected()) {
                                        downloadServiceFromBackground(this@PhoneAuthActivity,firestore!!)
                                    }*/
                                    hideProgressDialog()
                                    val intent = Intent(this@PhoneAuthActivity, DashBoardActivity::class.java)
                                    startActivity(intent)

                                    finish()


                                    /*firestore!!.collection("users")
                                        .add(userObj)
                                        .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                            override fun onComplete(task: Task<DocumentReference>) {
                                                if (task.isSuccessful) {
                                                    Log.e("user", "user added successfully")
                                                    Toast.makeText(activity!!,"Sign-In success!",Toast.LENGTH_SHORT).show()
                                                    val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                                                    val isConnected: Boolean = activeNetwork?.isConnected == true
                                                    Log.d("isConnected",isConnected.toString()+"!")
                                                    if(isNetworkConnected()) {
                                                        downloadServiceFromBackground((activity as SignInActivity?)!!,firestore!!)
                                                    }
                                                    hideProgressDialog()
                                                    val intent = Intent(activity!!, DashBoardActivity::class.java)
                                                    startActivity(intent)
                                                    activity!!.finish()
                                                } else {
                                                    hideProgressDialog()
                                                    Log.e("user", task.exception.toString())
                                                }
                                            }
                                        })*/




                                }


                            }
                        }
                    })



            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun downloadServiceFromBackground(
        mainActivity: PhoneAuthActivity,db: FirebaseFirestore
    ) {
        ContentDownloadService.enqueueWork(mainActivity, db)
    }

    /*fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }*/
}
