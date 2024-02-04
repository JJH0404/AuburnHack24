package com.example.meauburn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.hbb20.CountryCodePicker
import android.view.View

class LoginPhoneNum : AppCompatActivity() {

    private lateinit var countryCodePicker: CountryCodePicker
    private lateinit var phoneInput: EditText
    private lateinit var sendOtpBtn: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone_num)

        Log.d("TAG1", "message1")
        countryCodePicker = findViewById(R.id.country_code_picker)
        phoneInput = findViewById(R.id.login_mobile_number)
        sendOtpBtn = findViewById(R.id.send_otp_btn)
        progressBar = findViewById(R.id.login_progress_bar)
        progressBar.visibility = View.GONE

        countryCodePicker.registerCarrierNumberEditText(phoneInput)
        println("other message0")
        sendOtpBtn.setOnClickListener(listener)
    }

    val listener= View.OnClickListener { view ->
        when (view.getId()) {
            R.id.send_otp_btn -> {
                Log.d("TAG2", "message2")
                println("other message")
                if (!countryCodePicker.isValidFullNumber) {
                    phoneInput.error = "Phone number not valid"
                    return@OnClickListener
                }
                println("other message2")
                val intent = Intent(this, loginUser::class.java)
                intent.putExtra("phone", countryCodePicker.fullNumberWithPlus)
                startActivity(intent)
            }
        }
    }
}


