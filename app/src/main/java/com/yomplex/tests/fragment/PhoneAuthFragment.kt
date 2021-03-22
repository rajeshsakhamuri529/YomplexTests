package com.yomplex.tests.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskExecutors
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
import com.yomplex.tests.activity.DashBoardActivity
import com.yomplex.tests.activity.SignInActivity
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.model.User
import com.yomplex.tests.model.UserContentVersion
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.phone_auth.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class PhoneAuthFragment: Fragment() {

    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    // [END declare_auth]
    var databaseHandler: QuizGameDataBase?= null
    private var mPDialog: ProgressDialog? = null
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.phone_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPrefs = SharedPrefs()
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Obtain the FirebaseAnalytics instance.
        firebaseAnalytics = Firebase.analytics

        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        firestore!!.firestoreSettings = settings
        databaseHandler = QuizGameDataBase(activity);
        phoneRL.visibility = View.VISIBLE
        otpRL.visibility = View.GONE
        send_otp_btn.visibility = View.VISIBLE
        confirm_btn.visibility = View.GONE
        error_txt.visibility = View.GONE
        error_otp_txt.visibility = View.GONE

        edtPhoneNumber.showKeyboard()

        edtPhoneNumber.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {

                if(s.length == 0){
                    country_code_txt.setTextColor(Color.parseColor("#C0C0C0"))
                    error_txt.visibility = View.VISIBLE
                    error_txt.text = "Please enter mobile number"
                }else if(s.length < 10){
                    country_code_txt.setTextColor(Color.parseColor("#424242"))
                    error_txt.visibility = View.VISIBLE
                    error_txt.text = "Please enter a valid mobile number"
                }
                else if(s.length == 10){
                    country_code_txt.setTextColor(Color.parseColor("#424242"))
                    error_txt.visibility = View.GONE

                }


            }

        })

        send_otp_btn.setOnClickListener {
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
            if(Utils.isOnline(activity)){

                signIn()
            }else{
                Toast.makeText(activity,"Please connect internet!",Toast.LENGTH_SHORT).show()
            }

        }


        confirm_btn.setOnClickListener {
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
            if(Utils.isOnline(activity)){
                if (TextUtils.isEmpty(edtotpnumber.getText().toString())){
                    //when mobile number text field is empty displaying a toast message.
                    //Toast.makeText(activity, "Please enter OTP.",Toast.LENGTH_SHORT).show()
                    error_otp_txt.visibility = View.VISIBLE
                    error_otp_txt.text = "Please enter OTP"
                }else{
                    firebaseAnalytics.logEvent("Confirm OTP") {

                    }
                    it.hideKeyboard()
                    error_otp_txt.visibility = View.GONE
                    verifyPhoneNumberWithCode(storedVerificationId,edtotpnumber.text.toString())
                }

            }else{
                Toast.makeText(activity,"Please connect internet!",Toast.LENGTH_SHORT).show()
            }

        }

        back_btn.setOnClickListener {
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

            if(phoneRL.visibility == View.VISIBLE){
                val intent = Intent(context!!, SignInActivity::class.java)
                startActivity(intent)
            }else if(otpRL.visibility == View.VISIBLE){
                phoneRL.visibility = View.VISIBLE
                otpRL.visibility = View.GONE
                send_otp_btn.visibility = View.VISIBLE
                confirm_btn.visibility = View.GONE
                error_txt.visibility = View.GONE
                error_otp_txt.visibility = View.GONE
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



    fun EditText.showKeyboard() {
        post {
            requestFocus()
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun signIn() {
        if (TextUtils.isEmpty(edtPhoneNumber.getText().toString())){
            //when mobile number text field is empty displaying a toast message.
            //Toast.makeText(activity, "Please enter mobile number.",Toast.LENGTH_SHORT).show()
            error_txt.visibility = View.VISIBLE
            error_txt.text = "Please enter mobile number"
        }else if(edtPhoneNumber.getText().toString().length < 10){
            Toast.makeText(activity, "Please enter a valid mobile number.",Toast.LENGTH_SHORT).show()
            error_txt.visibility = View.VISIBLE
            error_txt.text = "Please enter a valid mobile number"
        }else{
            firebaseAnalytics.logEvent("Send OTP") {

            }
            //if the text field is not empty we are calling our send OTP method for gettig OTP from Firebase.
            var phone = "+91" + edtPhoneNumber.getText().toString()
            error_txt.visibility = View.GONE
            sendVerificationCode(phone)
        }
    }

    private fun sendVerificationCode(phoneNumber: String) {
        phoneRL.visibility = View.GONE
        otpRL.visibility = View.VISIBLE
        send_otp_btn.visibility = View.GONE
        confirm_btn.visibility = View.VISIBLE
        edtPhoneNumber.hideKeyboard()
        send_otp_phone.text = "+91 "+edtPhoneNumber.text.toString()
        edtotpnumber.showKeyboard()
        Log.e("phone auth fragment","sendVerificationCode....phoneNumber.."+phoneNumber)
        //this method is used for getting OTP on user phone number.
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {

        // [START verify_with_code]
        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential)
    }

    // [START sign_in_with_phone]
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showProgressDialog("Signing In...")
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.e("phone auth fragment", "signInWithCredential:success")

                    val user = task.result?.user

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
                ContextThemeWrapper(activity, R.style.DialogCustom),
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
                sharedPrefs?.setBooleanPrefVal(activity!!, ConstantPath.ISNOTLOGIN, true)
                sharedPrefs?.setBooleanPrefVal(activity!!, ConstantPath.IS_FIRST_TIME, false)
                //hideProgressDialog()

                var android_id = Settings.Secure.getString(activity!!.getContentResolver(), Settings.Secure.ANDROID_ID);
                Log.e("sign in activity","android_id....."+android_id)
                Log.e("sign in activity","user.phoneNumber....."+user.phoneNumber)
                sharedPrefs?.setPrefVal(activity!!, ConstantPath.UID, user!!.uid)
                sharedPrefs?.setPrefVal(activity!!, "email", "")
                sharedPrefs?.setPrefVal(activity!!, "phonenumber", user.phoneNumber!!)
                val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val currentDate = sdf.format(Date())
                //sharedPrefs.setPrefVal(this@GradeActivity, "created_date", currentDate)
                // firebaseAnalytics.setUserId(user!!.uid)




                //firebaseAnalytics.setUserProperty("email", user.email)

                firebaseAnalytics.logEvent("On_sucessful_Login") {
                    param("LoginSuccess", "LoggedIn")

                }
                var token:String = sharedPrefs!!.getPrefVal(activity!!,"firebasetoken")!!

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
                                        val data = hashMapOf("createdon" to currentDate)

                                        firestore!!.collection("users").document(document.id)
                                            .set(data, SetOptions.merge())
                                        activity!!.runOnUiThread {
                                            hideProgressDialog()
                                            Toast.makeText(activity!!,"Sign-In success!",Toast.LENGTH_SHORT).show()
                                            val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                                            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                                            val isConnected: Boolean = activeNetwork?.isConnected == true
                                            Log.d("isConnected",isConnected.toString()+"!")
                                            if(isNetworkConnected()) {
                                                downloadServiceFromBackground((activity as SignInActivity?)!!,firestore!!)
                                            }
                                            val intent = Intent(activity!!, DashBoardActivity::class.java)
                                            startActivity(intent)
                                            activity!!.finish()
                                        }

                                    }
                                }else{
                                    var userObj = User()
                                    userObj.username = ""
                                    userObj.phonenumber = user.phoneNumber
                                    userObj.deviceuniqueid = android_id
                                    userObj.createdon = currentDate
                                    userObj.firebaseToken = token

                                    databaseHandler!!.insertUserSync(userObj,0)
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
        mainActivity: SignInActivity,db: FirebaseFirestore
    ) {
        ContentDownloadService.enqueueWork(mainActivity, db)
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager = activity!!.getSystemService(Context.
            CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }


}