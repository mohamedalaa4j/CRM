package com.mido.crm

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mido.crm.databinding.ActivityMainBinding
import com.mido.crm.databinding.DialogUpdateBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        ///// EmployeeDao instance
        val employeeDao = (application as EmployeeApp).db.employeeDao()

        ///// Add button listener
        binding?.btnAdd?.setOnClickListener {
            addRecord(employeeDao)
        }

        ///// Fetch data from room DB in the background (Dispatcher.Default) && pass it to the RecyclerView setup function
        lifecycleScope.launch {

            employeeDao.fetchAllEmployees().collect {
                val list = ArrayList(it)
                setupListOfDataIntoRecyclerView(list, employeeDao)
            }

        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private fun setupListOfDataIntoRecyclerView(employeesList: ArrayList<EmployeeEntity>, employeeDao: EmployeeDao) {

        if (employeesList.isNotEmpty()) {


            // Adapter class is initialized and list is passed in the param.
            val itemAdapter = ItemAdapter(employeesList, { updateId ->
                updateRecordDialog(updateId, employeeDao)
            },{ deleteId ->
                lifecycleScope.launch {
                    employeeDao.fetchEmployeeById(deleteId).collect {
                        if (it != null) {
                            deleteRecordAlertDialog(deleteId, employeeDao)
                        }
                    }
                }

            },{name,mail,phone->
                val intent = Intent(this, CustomerActivity::class.java)
                intent.putExtra("NAME", name)
                intent.putExtra("MAIL", mail)
                intent.putExtra("PHONE", phone)
                startActivity(intent)
            })
            // Set the LayoutManager that this RecyclerView will use.
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            // adapter instance is set to the recyclerview to inflate the items.
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        } else {

            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }

    private fun addRecord(employeeDao: EmployeeDao) {
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()
        val phone = binding?.etPhone?.text.toString()

        if (name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {

            ///// lifecycleScope is an extension for LifeCycleOwner % bound to Activity or Fragment's lifCycle
            //where scope is canceled when that Activity or Fragment is destroyed.
            lifecycleScope.launch {
                employeeDao.insert(EmployeeEntity(name = name, email = email, phone = phone))
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_SHORT).show()

                ///// Clear editTexts
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
                binding?.etPhone?.text?.clear()
            }
        } else {
            Toast.makeText(applicationContext, "Fields cannot be blank", Toast.LENGTH_LONG)
                .show()

        }
    }

    private fun updateRecordDialog(id: Int, employeeDao: EmployeeDao) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)

        val binding = DialogUpdateBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        ///// Populate data in the EditTexts
        lifecycleScope.launch {
            employeeDao.fetchEmployeeById(id).collect {
                if (it != null) {
                    binding.etUpdateName.setText(it.name)
                    binding.etUpdateEmailId.setText(it.email)
                    binding.etUpdatePhone.setText(it.phone)
                }
            }
        }

        ///// Update button listener
        binding.tvUpdate.setOnClickListener {
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()
            val phone = binding.etUpdatePhone.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty()) {
                lifecycleScope.launch {

                    ///// Update Entity via EmployeeEntity object
                    employeeDao.update(EmployeeEntity(id, name, email,phone))

                    Toast.makeText(applicationContext, "Record updated.", Toast.LENGTH_LONG).show()

                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(applicationContext, "Name or Email cannot be blank.", Toast.LENGTH_LONG).show()
            }
        }

        binding.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }

        updateDialog.show()
    }

    private fun deleteRecordAlertDialog(id: Int, employeeDao: EmployeeDao) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")

        ///// github
        builder.setMessage("Are you sure you wants to delete .")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, _ ->
            lifecycleScope.launch {
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(applicationContext, "Record deleted successfully.", Toast.LENGTH_LONG).show()

                dialogInterface.dismiss() // Dialog will be dismissed
            }

        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }
}