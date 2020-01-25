package me.mehdi.countries.db

import androidx.lifecycle.LiveData

class CountryRepository (private val countryDao: CountryDao){

    val allCountries : LiveData<List<Country>> = countryDao.getCountries()

    suspend fun insert(country: Country){
        countryDao.insert(country)
    }
}