package com.example.meauburn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit


class LoginOTP : AppCompatActivity() {
    var phoneNumber: String = ""
    var timeoutSeconds = 60L
    var verificationCode: String = ""
    var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
    var otpInput: EditText? = null
    var nextBtn: Button? = null
    var progressBar: ProgressBar? = null
    var resendOtpTextView: TextView? = null
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)
        otpInput = findViewById(R.id.login_otp)
        nextBtn = findViewById(R.id.login_next_btn)
        progressBar = findViewById(R.id.login_progress_bar)
        resendOtpTextView = findViewById(R.id.resend_otp_textview)
        phoneNumber = intent.extras!!.getString("phone")!!
        sendOtp(phoneNumber, false)
        nextBtn?.setOnClickListener(View.OnClickListener { v: View? ->
            val enteredOtp = otpInput?.getText().toString()
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationCode, enteredOtp)
            signIn(credential)
        })
        resendOtpTextView?.setOnClickListener(View.OnClickListener { v: View? ->
            sendOtp(
                phoneNumber,
                true
            )
        })
    }

    fun sendOtp(phoneNumber: String, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        print("number"+phoneNumber)
        val builder: PhoneAuthOptions.Builder = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signIn(credential)
                    setInProgress(false)
                }

                override fun onVerificationFailed(exception: FirebaseException) {
                    showToast(applicationContext, "OTP verification failed")
                    setInProgress(false)
                }

                override fun onCodeSent(
                    verificationId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                ) {
                    super.onCodeSent(verificationId, forceResendingToken)
                    verificationCode = verificationId
                    resendingToken = forceResendingToken

                    showToast(applicationContext, "OTP sent successfully")
                    setInProgress(false)
                    print("successfully")
                }
            })
        if (isResend) {
//            PhoneAuthProvider.verifyPhoneNumber(
//                builder.setForceResendingToken(resendingToken!!).build()
//            )
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }
    }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar!!.visibility = View.VISIBLE
            nextBtn!!.visibility = View.GONE
        } else {
            progressBar!!.visibility = View.GONE
            nextBtn!!.visibility = View.VISIBLE
        }
    }

    fun signIn(phoneAuthCredential: PhoneAuthCredential?) {
        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential!!)
            .addOnCompleteListener(OnCompleteListener { task ->
                setInProgress(false)
                if (task.isSuccessful) {
                    val intent = Intent(
                        this@LoginOTP,
                        loginUser::class.java
                    )
                    intent.putExtra("phone", phoneNumber)
                    startActivity(intent)
                } else {
                    showToast(applicationContext, "OTP verification failed")
                }
            })
    }

    fun startResendTimer() {
        resendOtpTextView!!.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                resendOtpTextView!!.text = "Resend OTP in $timeoutSeconds seconds"
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L
                    timer.cancel()
                    runOnUiThread { resendOtpTextView!!.isEnabled = true }
                }
            }
        }, 0, 1000)
    }
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}