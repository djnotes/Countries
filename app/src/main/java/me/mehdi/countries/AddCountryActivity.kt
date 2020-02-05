package me.mehdi.countries

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddCountryActivity : AppCompatActivity() {

    companion object {
        const val REPLY_COUNTRY_NAME = "me.mehdi.countries.COUNTRY_NAME"
        const val REPLY_CAPITAL_NAME = "me.mehdi.countries.CAPITAL_NAME"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_country)

        val add : Button = findViewById(R.id.add)

        add.setOnClickListener{
            val name = findViewById<EditText>(R.id.countryName).text.toString()
            val capital = findViewById<EditText>(R.id.capitalName).text.toString()

            if(name.isEmpty() || capital.isEmpty()) {
                Toast.makeText(this, R.string.fill_in_required_fields, Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_CANCELED)
            }
            else {
                val reply = Intent()
                reply.putExtra(REPLY_COUNTRY_NAME, name)
                reply.putExtra(REPLY_CAPITAL_NAME, capital)
                setResult(RESULT_OK, reply)
            }

            finish()
        }
    }
}