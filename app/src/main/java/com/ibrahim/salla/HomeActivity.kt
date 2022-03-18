package com.ibrahim.salla

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.ibrahim.salla.constants.Constants
import com.ibrahim.salla.fragments.HomeFragment
import com.ibrahim.salla.fragments.KidsFragment
import com.ibrahim.salla.fragments.MenFragment
import com.ibrahim.salla.fragments.WomenFragment
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class HomeActivity : AppCompatActivity() {
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var name : TextView
    private lateinit var image : CircleImageView
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        drawerLayout = findViewById(R.id.drawerLayout)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        drawerLayout.visibility=View.GONE

        if(Constants.internetConnection(this))
        {
            var fragment:Fragment = HomeFragment()
            val firebaseStorage = FirebaseStorage.getInstance().reference
            val toolBar = findViewById<Toolbar>(R.id.toolBarHome)
            val navMenu = findViewById<NavigationView>(R.id.navMenu)
            val bottomNavBar = findViewById<BottomNavigationView>(R.id.bottomNavView)
            val headerLayout = navMenu.getHeaderView(0)
            image = headerLayout.findViewById(R.id.imageHeaderLayout)
            name = headerLayout.findViewById(R.id.txtHeaderLayout)

            setSupportActionBar(toolBar)
            val actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.open,R.string.close)
            drawerLayout.addDrawerListener(actionBarDrawerToggle)
            actionBarDrawerToggle.syncState()
            supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit()

            name.text = Constants.user.name

            firebaseStorage.child(Constants.uId!!).downloadUrl.addOnSuccessListener {
                progressBar.visibility=View.GONE
                drawerLayout.visibility=View.VISIBLE
                Picasso.get().load(it).into(image)
                Constants.imageProfile = it
            }.addOnFailureListener {
                progressBar.visibility=View.GONE
                drawerLayout.visibility=View.VISIBLE
                Constants.imageProfile = null
            }

            image.setOnClickListener {
                drawerLayout.closeDrawer(GravityCompat.START)
                startActivity(Intent(this,ProfileActivity::class.java))
            }

            //navMenu.setNavigationItemSelectedListener(this)
            navMenu.setNavigationItemSelectedListener {
                drawerLayout.closeDrawer(GravityCompat.START)
                if(it.itemId==R.id.favorite)
                    startActivity(Intent(this,FavoriteActivity::class.java))
                if(it.itemId==R.id.cart)
                    startActivity(Intent(this,CartActivity::class.java))
                if(it.itemId==R.id.orders)
                    startActivity(Intent(this,OrdersActivity::class.java))
                if(it.itemId==R.id.logOut)
                {
                    if (FirebaseAuth.getInstance().currentUser != null) {
                        //clear all constants to log out
                        Constants.imageProfile = null
                        Constants.uId = null

                        //clear shared
                        val sharedPreferences = getSharedPreferences("MyShared", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putBoolean("Remember",false)
                        editor.putString("uId",null)
                        editor.putString("name",null)
                        editor.putString("phone", null)
                        editor.apply()

                        //sign out from firebase
                        FirebaseAuth.getInstance().signOut()
                        startActivity(Intent(this@HomeActivity, MainActivity::class.java))
                        finish()
                    }
                }
                return@setNavigationItemSelectedListener true
            }

            bottomNavBar.setOnItemSelectedListener {
                if(it.itemId==R.id.home)
                    fragment= HomeFragment()
                if(it.itemId==R.id.men)
                    fragment=MenFragment()
                if(it.itemId==R.id.women)
                    fragment=WomenFragment()
                if(it.itemId==R.id.kids)
                    fragment=KidsFragment()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout,fragment).commit()
                return@setOnItemSelectedListener true
            }

        }
        else
        {
            progressBar.visibility=View.GONE
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.alert_connectivity)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
            val btnTryAgain = dialog.findViewById<Button>(R.id.btnTryAgain)
            btnTryAgain.setOnClickListener{
                recreate()
            }
            dialog.show()
        }

    }

//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        drawerLayout.closeDrawer(GravityCompat.START)
//        Constants.toast(this,item.title.toString())
//        return true
//    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.search)
            startActivity(Intent(this,SearchActivity::class.java))
        return true
    }

    override fun onResume() {
        super.onResume()
        if(Constants.imageProfile!=null && Constants.user.name != null)
        {
            Picasso.get().load(Constants.imageProfile).into(image)
            name.text = Constants.user.name
        }
    }
}