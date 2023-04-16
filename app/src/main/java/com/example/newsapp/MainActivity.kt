package com.example.newsapp

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ViewUtils
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.adapter.NewsAdapter
import com.example.newsapp.api.RetrofitInstance
import com.example.newsapp.data.NewsModel
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.models.NewsData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

var country: List<String> = listOf<String>("us","in","ae","ar","at","au","be","bg","br","ca")

class MainActivity : AppCompatActivity() {
    var urlToBase = "https://play-lh.googleusercontent.com/_ahCmEdTn8h5omlAg0jg9Y15KArlptm4qcbnyWSzGU-jM4mR1LeArqbMM7DzgZjNywO2"
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    lateinit var bottomNav : BottomNavigationView
    lateinit var  data: ArrayList<NewsModel>
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "News App"
        val colorDrawable = ColorDrawable(Color.parseColor("#663399"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerView = findViewById(R.id.recyclerview)
        drawerLayout = findViewById(R.id.my_drawer_layout)
        navView = findViewById(R.id.nav_view)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.randomNews->{
                    Toast.makeText(this@MainActivity, "Hello",Toast.LENGTH_SHORT)
                    true
                }
                else -> {false}
            }
        }
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        recyclerView.layoutManager = LinearLayoutManager(this)
        data = ArrayList<NewsModel>()
        getNews()
        loadFragment(ShuffleFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(ShuffleFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                R.id.location -> {
                    loadFragment(LocationFragment())
                    true
                }
                else -> {
                    Toast.makeText(this, "Something Fishy!", Toast.LENGTH_SHORT).show()
                    true
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.news_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        }
        when (item.itemId) {
            R.id.random -> {
                getNewsRandom()
                return true
            }
            R.id.help -> {
                val dialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.bottom_menu, null)
                val btnClose = view.findViewById<Button>(R.id.closebutton)
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.setCancelable(false)
                dialog.setContentView(view)
                dialog.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun getNews() {
        data.clear()
        RetrofitInstance.apiInterface.getNews(country[0]).enqueue(object : Callback<NewsData?> {
            override fun onResponse(call: Call<NewsData?>, response: Response<NewsData?>) {
                response.body()?.articles?.forEach {
                    var imageUri: String = if (it.urlToImage!=null){
                        it.urlToImage.toString()
                    }else{
                        urlToBase
                    }
                    this@MainActivity.data.add(NewsModel(imageUri,it.title.toString()))
                }
                this@MainActivity.recyclerView.adapter = NewsAdapter(this@MainActivity.data, this@MainActivity)
            }
            override fun onFailure(call: Call<NewsData?>, t: Throwable) {
                Toast.makeText(this@MainActivity,"${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun getNewsRandom() {
        data.clear()
        RetrofitInstance.apiInterface.getNews(country[Random.nextInt(country.size)]).enqueue(object : Callback<NewsData?> {
            override fun onResponse(call: Call<NewsData?>, response: Response<NewsData?>) {
                response.body()?.articles?.forEach {
                    var imageUri: String = if (it.urlToImage!=null){
                        it.urlToImage.toString()
                    }else{
                        urlToBase
                    }
                    this@MainActivity.data.add(NewsModel(imageUri,it.title.toString()))
                }
                this@MainActivity.recyclerView.adapter = NewsAdapter(this@MainActivity.data, this@MainActivity)
            }
            override fun onFailure(call: Call<NewsData?>, t: Throwable) {
                Toast.makeText(this@MainActivity,"${t.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
