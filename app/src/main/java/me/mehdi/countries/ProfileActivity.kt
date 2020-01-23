package me.mehdi.countries

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import com.google.android.material.snackbar.Snackbar
import java.io.FileDescriptor
import java.io.FileNotFoundException

class ProfileActivity : AppCompatActivity() {

    private lateinit var mRootView : View
    private val requestFileIntent : Intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = "image/jpeg"
        addCategory(Intent.CATEGORY_OPENABLE)

    }

    private lateinit var profilePicture : ImageView

    companion object {
        const val PERMISSION_STORAGE = 100
        const val REQUEST_FILE = 200
        const val PREF_PROFILE_URI = "me.mehdi.countries.PROFILE_PICTURE_URI"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mRootView = findViewById(android.R.id.content)

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

        //Check preferences for existing image
        val prefs = getSharedPreferences(Constants.APP_SHARED_PREFS, Context.MODE_PRIVATE)
        if(prefs.contains(PREF_PROFILE_URI)){
            val uri = Uri.parse(
                prefs.getString(PREF_PROFILE_URI, "")
            )
            val fd = try{
                contentResolver.openFileDescriptor(uri, "r")
            } catch(e: FileNotFoundException) {
                Log.e("ProfileActivity", "onCreate: ${e.message}")
                Snackbar.make(mRootView, R.string.file_not_found, Snackbar.LENGTH_SHORT).show()
                return
            } catch(e: SecurityException){
                Log.e("ProfileActivity", "onCreate: ${e.message}")
                Snackbar.make(mRootView, R.string.unable_to_access_file, Snackbar.LENGTH_SHORT).show()
                return
            }

            val bitmap = BitmapFactory.decodeFileDescriptor(fd?.fileDescriptor)
            profilePicture.setImageBitmap(bitmap)

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
                        val pFD : ParcelFileDescriptor?
                        try{
                            pFD = contentResolver.openFileDescriptor(it, "r")
                            contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        } catch(e: FileNotFoundException){
                            e.printStackTrace()
                            Log.e("ProfileActivity", "onActivityResult: File not found" )
                            Snackbar.make(mRootView, R.string.file_not_found, Snackbar.LENGTH_LONG).show()
                            return
                        }
                        val bmp = BitmapFactory.decodeFileDescriptor(pFD?.fileDescriptor)
                        profilePicture.setImageBitmap(bmp)

                        //Save the image in preferences
                        getSharedPreferences(Constants.APP_SHARED_PREFS, Context.MODE_PRIVATE)
                            .edit {
                                this.putString(PREF_PROFILE_URI, returnIntent.data.toString())
                                apply()
                            }
                        Toast.makeText(applicationContext, R.string.picture_successfully_updated, Toast.LENGTH_SHORT).show()

                    }

                }
            }
        }
    }


}