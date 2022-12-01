package com.mido.crm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mido.crm.databinding.ItemsRowBinding

///// Passing EmployeeEntity as the items array because it's the data model (data class of room)
class ItemAdapter(
    private val items: ArrayList<EmployeeEntity>,
    private val updateListener: (id: Int) -> Unit,
    private val deleteListener: (id: Int) -> Unit,
    private val listener: (name:String,mail:String,phone:String) -> Unit

    ) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    ///// ViewHolder class using ViewBinding instead of View
    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val tvPhone = binding.tvPhone
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        ///// Return ViewHolder that require binding object
        ///// from(parent.context) get the context from the parent cause we're in a different context not the main activity
        return ViewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        ///// Get the context from the item itself
        val context = holder.itemView.context

        ///// Item of the RV & will be an EmployeeEntity object
        val item = items[position]

        ///// Assign the views value
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
        holder.tvPhone.text = item.phone

        ///// Add the colored background
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.colorLightGray))
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        }

        holder.ivEdit.setOnClickListener {
            updateListener.invoke(item.id)
        }

        holder.ivDelete.setOnClickListener {
            deleteListener.invoke(item.id)
        }

        holder.itemView.setOnClickListener {
            listener.invoke(item.name,item.email, item.phone)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}