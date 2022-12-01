package com.mido.roomdemo

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mido.roomdemo.databinding.ActivityCustomerBinding

class CustomerActivity : AppCompatActivity() {
    private var binding : ActivityCustomerBinding?=null

    private lateinit var phoneNumber : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        phoneNumber = intent.getStringExtra("PHONE").toString()

        binding?.tvNameValue?.text = intent.getStringExtra("NAME").toString()
        binding?.tvEmailValue?.text = intent.getStringExtra("MAIL").toString()
        binding?.tvPhoneValue?.text = phoneNumber

        binding?.ivPhone?.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:<$phoneNumber>")
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}