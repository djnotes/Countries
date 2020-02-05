package me.mehdi.countries.sqlite

import android.provider.BaseColumns

class CountryContract {
    companion object CountryEntry : BaseColumns {
        const val TABLE_COUNTRIES = "table_countries"
        const val COLUMN_COUNTRY_NAME = "name"
        const val COLUMN_COUNTRY_CAPITAL = "capital"
        const val COLUMN_IMAGE_URI = "image"
    }


}