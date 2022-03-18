package com.ibrahim.salla.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.ibrahim.salla.R
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.CartModel
import com.squareup.picasso.Picasso

class CartAdapter (val context: Context, private val arrayList: ArrayList<CartModel>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val image:ImageView = itemView.findViewById(R.id.imgViewCartActivity)
        val txtName: TextView = itemView.findViewById(R.id.txtNameCartActivity)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPriceCartActivity)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantityCartActivity)
        val btnDelete: ImageButton = itemView.findViewById(R.id.imgBtnDelCartActivity)
        val btnEdit:ImageButton = itemView.findViewById(R.id.imgBtnEditCartActivity)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_cart_item,parent,false)
        return ViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n", "InflateParams")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Picasso.get().load(arrayList[position].proImage).into(holder.image)
        holder.txtName.text = "Name : "+arrayList[position].proName
        holder.txtPrice.text = "Total : "+arrayList[position].totalPrice
        holder.txtQuantity.text = "Quantity : "+arrayList[position].quantity.toString()


        holder.btnDelete.setOnClickListener{
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setMessage("Do you want to delete this item from your cart ?").setPositiveButton("OK"){
                    dialog, _ ->
                FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("cart")
                .child(arrayList[position].proId.toString()).removeValue().addOnSuccessListener {
                        dialog.dismiss()
                        notifyItemRangeChanged(position,arrayList.size)
                        Constants.toast(context,"Item is Removed")
                }.
                addOnFailureListener {
                    dialog.dismiss()
                    Constants.toast(context,it.message.toString())
                }
            }.setNeutralButton("Cancel"){
                dialog,_->
                dialog.dismiss()
            }.show()

        }
        holder.btnEdit.setOnClickListener{

            val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val plusAndMinusView = inflater.inflate(R.layout.plus_minus,null)
            val dialog = Dialog(context)
            dialog.setContentView(plusAndMinusView)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog

            val btnOk = plusAndMinusView.findViewById<TextView>(R.id.txtOk)
            val btnCancel = plusAndMinusView.findViewById<TextView>(R.id.txtCancel)
            val btnAdd = plusAndMinusView.findViewById<ImageButton>(R.id.imgBtnAdd)
            val btnMinus = plusAndMinusView.findViewById<ImageButton>(R.id.imgBtnMinus)
            val txtNum = plusAndMinusView.findViewById<TextView>(R.id.txtNumber)

            txtNum.text = arrayList[position].quantity.toString()
            btnOk.setOnClickListener {
//                val progressDialog = ProgressDialog(context)
//                progressDialog.setMessage("Please Wait")
                if(txtNum.text.toString().toInt()>0)
                {
                    //progressDialog.show()
                    val priceSplit = arrayList[position].proPrice!!.split("$")
                    val totalPrice = priceSplit[0].toInt()*txtNum.text.toString().toInt()
                    val map = HashMap<String,Any>()
                    map["quantity"]=txtNum.text.toString().toInt()
                    map["totalPrice"]= "$totalPrice$"
                    FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("cart")
                        .child(arrayList[position].proId.toString()).updateChildren(map).addOnSuccessListener {
                            dialog.dismiss()
                            notifyItemRangeChanged(position,arrayList.size)
                        }.addOnFailureListener {
                            dialog.dismiss()
                            Constants.toast(context,it.message.toString())
                        }
                }
                else
                    Constants.toast(context,"Number must be grater than zero")
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
                notifyItemRangeChanged(0,arrayList.size)
            }
            btnAdd.setOnClickListener{
                var num = txtNum.text.toString().toInt()
                num++
                txtNum.text = num.toString()
            }
            btnMinus.setOnClickListener{
                var num = txtNum.text.toString().toInt()
                num--
                txtNum.text = num.toString()
            }
            dialog.show()
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}