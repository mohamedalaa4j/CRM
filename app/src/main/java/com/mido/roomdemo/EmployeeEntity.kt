package com.mido.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee-table")
data class EmployeeEntity(

    ///// Columns
    @PrimaryKey(autoGenerate = true)     // @PrimaryKey to make each id is unique
    var id: Int = 0,
    var name: String = "",
    @ColumnInfo(name = "email-id")     // Internally name
    var email: String = "",

    var phone: String = ""

)
