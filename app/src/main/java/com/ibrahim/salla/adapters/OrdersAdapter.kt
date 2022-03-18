package com.ibrahim.salla.adapters


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.ibrahim.salla.R
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.OrdersModel

class OrdersAdapter(val context:Context, private val arrayList: ArrayList<OrdersModel>) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val txtTotalItems : TextView = itemView.findViewById(R.id.txtTotalItems)
        val txtTotalPrice : TextView= itemView.findViewById(R.id.txtTotalPrice)
        val btnDelete : ImageButton = itemView.findViewById(R.id.imgBtnDeleteOrder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_order_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtTotalItems.text = "Total items = " + arrayList[position].totalItems.toString()
        holder.txtTotalPrice.text = "Total price = " + arrayList[position].totalPrice.toString()
        holder.btnDelete.setOnClickListener{
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("Do you want to delete this order from your orders ?").setPositiveButton("OK"){
                    dialog, _ ->
                FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("orders")
                    .child(arrayList[position].key!!).removeValue().addOnSuccessListener {
                        dialog.dismiss()
                        notifyItemRangeChanged(position,arrayList.size)
                        Constants.toast(context,"order is removed")
                    }.
                    addOnFailureListener {
                        dialog.dismiss()
                        Constants.toast(context,it.message.toString())
                    }
            }.setNegativeButton("Cancel"){
                    dialog,_->
                dialog.dismiss()
            }.show()

        }
    }
}
