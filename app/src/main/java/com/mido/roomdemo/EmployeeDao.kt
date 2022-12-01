package com.mido.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {

    ///// Store data
    @Insert

    ///// Suspend fun to be done on the background thread
    suspend fun insert(employeeEntity: EmployeeEntity)     // Store employeeEntity in our database

    @Update
    suspend fun update(employeeEntity: EmployeeEntity)     // Update employeeEntity in our database

    @Delete
    suspend fun delete(employeeEntity: EmployeeEntity)     // Delete employeeEntity in our database

    ///// Retrieve data
    @Query("SELECT * FROM `employee-table`")         // Using sql code
    fun fetchAllEmployees(): Flow<List<EmployeeEntity>>

    @Query("SELECT * FROM `employee-table` where id=:id")         // Using sql code
    fun fetchEmployeeById(id:Int): Flow<EmployeeEntity>



}