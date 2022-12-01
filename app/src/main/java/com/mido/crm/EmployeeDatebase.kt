package com.mido.crm

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

///// Define the version & the Entity
@Database(entities = [EmployeeEntity::class], version = 2)
abstract class EmployeeDatabase:RoomDatabase() {

    ///// Connect the Database to DAO
    abstract fun employeeDao():EmployeeDao

    ///// define companion object to allow us to add functions to employee database class
    companion object {

        @Volatile
        private var INSTANCE: EmployeeDatabase? = null

        fun getInstance(context: Context):EmployeeDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, EmployeeDatabase::class.java, "employee_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }


        }

    }
}