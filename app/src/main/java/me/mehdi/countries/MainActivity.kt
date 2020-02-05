package me.mehdi.countries

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import me.mehdi.countries.sqlite.CountryContract
import me.mehdi.countries.sqlite.MyCountriesDbHelper

class MainActivity : AppCompatActivity(), CountryListAdapter.OnItemClickListener {
    companion object {
        const val ADD_COUNTRY = 10
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView : RecyclerView = findViewById(R.id.countriesRecyclerView)
        val adapter = CountryListAdapter(applicationContext)
        adapter.itemClickListener = this
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.HORIZONTAL, false)



        //ViewModel
        val countriesViewModel = ViewModelProvider(this).get(CountryViewModel::class.java)
        countriesViewModel.allCountries.observe(this, Observer{ countries->
            countries?.let{
                adapter.setCountries(it)
            }

        })

        val fab : FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            startActivityForResult(Intent(this, AddCountryActivity::class.java), ADD_COUNTRY)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.profile -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(position: Int) {
        Toast.makeText(applicationContext, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ADD_COUNTRY && resultCode == RESULT_OK){
            val name = data?.getStringExtra(AddCountryActivity.REPLY_COUNTRY_NAME)
            val capital = data?.getStringExtra(AddCountryActivity.REPLY_CAPITAL_NAME)

            val db = MyCountriesDbHelper(this).writableDatabase
            val values = ContentValues()
            values.put(CountryContract.COLUMN_COUNTRY_NAME, name)
            values.put(CountryContract.COLUMN_COUNTRY_CAPITAL, capital)

            val rowId = db.insert(CountryContract.TABLE_COUNTRIES, null, values)
            if(rowId != -1L){
                Snackbar.make(findViewById(android.R.id.content), R.string.item_added_successfully,
                    Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}