package com.ibrahim.salla.adapters

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.ibrahim.salla.R
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.models.ProductModel
import com.squareup.picasso.Picasso

class DefaultGridAdapter (val context:Context, private val arrayList: ArrayList<ProductModel>) :RecyclerView.Adapter<DefaultGridAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val image: ImageView = itemView.findViewById(R.id.imgViewPro)
        var txtName: TextView = itemView.findViewById(R.id.txtProName)
        var txtPrice: TextView = itemView.findViewById(R.id.txtProPrice)
        val btnFavorite: ImageButton = itemView.findViewById(R.id.imgBtnFavorite)
        val btnAddCart: ImageButton = itemView.findViewById(R.id.imgBtnCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.grid_item,parent,false)
        return MyViewHolder(view)
    }

    @SuppressLint("InflateParams", "NewApi", "NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Picasso.get().load(arrayList[position].proImage).into(holder.image)
        holder.txtName.text = arrayList[position].proName
        holder.txtPrice.text = arrayList[position].proPrice

        if(Constants.arrayFavId.contains(arrayList[position].proId))
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_red)
        else
            holder.btnFavorite.setImageResource(R.drawable.ic_favorite_grey)


        holder.btnFavorite.setOnClickListener{
            if(Constants.arrayFavId.contains(arrayList[position].proId))
            {
                FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("favorites")
                    .child(arrayList[position].proId!!).removeValue().addOnSuccessListener {
                        holder.btnFavorite.setImageResource(R.drawable.ic_favorite_grey)
                        notifyItemRangeChanged(position,arrayList.size)
                    }.addOnFailureListener{
                        Constants.toast(context, it.message!!)
                    }
            }
            else
            {
                val map = arrayList[position].toMap()
                FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("favorites")
                    .child(arrayList[position].proId!!).updateChildren(map).addOnSuccessListener {
                        holder.btnFavorite.setImageResource(R.drawable.ic_favorite_red)
                        notifyItemRangeChanged(position,arrayList.size)
                    }.addOnFailureListener{
                        Constants.toast(context, it.message!!)
                    }
            }
        }
        holder.btnAddCart.setOnClickListener{
            val inflater:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val plusAndMinusView = inflater.inflate(R.layout.plus_minus,null)
            val dialog = Dialog(context)
            dialog.setContentView(plusAndMinusView)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog

            val btnOk = plusAndMinusView.findViewById<TextView>(R.id.txtOk)
            val btnCancel = plusAndMinusView.findViewById<TextView>(R.id.txtCancel)
            val btnAdd = plusAndMinusView.findViewById<ImageButton>(R.id.imgBtnAdd)
            val btnMinus = plusAndMinusView.findViewById<ImageButton>(R.id.imgBtnMinus)
            val txtNum = plusAndMinusView.findViewById<TextView>(R.id.txtNumber)

            btnOk.setOnClickListener {
//                val progressDialog = ProgressDialog(context)
//                progressDialog.setMessage("Please Wait")
                if(txtNum.text.toString().toInt()>0)
                {
                    //progressDialog.show()
                    val priceSplit = arrayList[position].proPrice!!.split("$")
                    val totalPrice = priceSplit[0].toInt()*txtNum.text.toString().toInt()
                    val map = arrayList[position].toMap()
                    map["quantity"]=txtNum.text.toString().toInt()
                    map["totalPrice"]= "$totalPrice$"
                    FirebaseDatabase.getInstance().reference.child("Users").child(Constants.uId!!).child("cart")
                        .child(arrayList[position].proId.toString()).updateChildren(map).addOnSuccessListener {
                            //arrayList.clear()
                            //arrayList[position].quantity=txtNum.text.toString().toInt()
                            //progressDialog.dismiss()
                            dialog.dismiss()
                            Constants.toast(context,"Done !")
                            //notifyDataSetChanged()
                            notifyItemRangeChanged(position,arrayList.size)
                        }
                        .addOnFailureListener {
                            dialog.dismiss()
                            Constants.toast(context,it.message.toString())
                        }
                }
                else
                    Constants.toast(context,"Number must be grater than zero")
            }
            btnCancel.setOnClickListener {
                dialog.dismiss()
                notifyItemRangeChanged(position,arrayList.size)
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