package me.mehdi.countries.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class MyCountriesDbHelper (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "sqlite_countries.db"
        const val DB_VERSION = 1
        const val SQL_CREATE_ENTRIES = "CREATE TABLE ${CountryContract.TABLE_COUNTRIES} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${CountryContract.COLUMN_COUNTRY_NAME} TEXT, " +
                "${CountryContract.COLUMN_COUNTRY_CAPITAL} TEXT, " +
                "${CountryContract.COLUMN_IMAGE_URI} TEXT)"
        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${CountryContract.TABLE_COUNTRIES}"
        const val SQL_ADD_DEFAULT_COUNTRY ="INSERT INTO ${CountryContract.TABLE_COUNTRIES} " +
                " (${CountryContract.COLUMN_COUNTRY_NAME}, ${CountryContract.COLUMN_COUNTRY_CAPITAL}) " +
                " VALUES ('Iran', 'Tehran')"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
        db?.execSQL(SQL_ADD_DEFAULT_COUNTRY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

}