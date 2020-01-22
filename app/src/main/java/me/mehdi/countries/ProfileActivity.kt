package me.mehdi.countries

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.FileNotFoundException

class ProfileActivity : AppCompatActivity() {

    private val requestFileIntent : Intent = Intent(Intent.ACTION_PICK).apply {
        type = "image/jpg"
    }

    private lateinit var profilePicture : ImageView

    companion object {
        const val PERMISSION_STORAGE = 100
        const val REQUEST_FILE = 200
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profilePicture = findViewById(R.id.profilePicture)
        profilePicture.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this@ProfileActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_STORAGE)
            }
            else {
                changeProfilePicture()
            }
        }



    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_STORAGE -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    changeProfilePicture();
                }
                else {
                    Toast.makeText(applicationContext, R.string.storage_permission_justification, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeProfilePicture() {
        startActivityForResult(requestFileIntent, REQUEST_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, returnIntent)
        when(requestCode){
            REQUEST_FILE -> {
                if(resultCode == RESULT_OK){
                    returnIntent!!.data?.also {
                        val fd = try{
                            contentResolver.openFileDescriptor(it, "r")
                        } catch(e: FileNotFoundException){
                            e.printStackTrace()
                            Log.e("ProfileActivity", "onActivityResult: File not found" )
                            return
                        }
                        val bmp = BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor)
                        profilePicture.setImageBitmap(bmp)
                    }

                }
            }
        }
    }


}