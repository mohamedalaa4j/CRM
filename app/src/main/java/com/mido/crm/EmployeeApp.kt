package com.mido.crm

import android.app.Application

class EmployeeApp:Application(){

    val db by lazy {
        EmployeeDatabase.getInstance(this)
    }

}