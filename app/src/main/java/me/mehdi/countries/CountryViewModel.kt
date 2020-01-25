package me.mehdi.countries

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.mehdi.countries.db.CountryDb
import me.mehdi.countries.db.Country
import me.mehdi.countries.db.CountryRepository

class CountryViewModel(application: Application) : AndroidViewModel(application) {
    private val repository : CountryRepository
    val allCountries : LiveData<List<Country>>
    init{
        val countryDao = CountryDb.getDatabase(application, viewModelScope)!!.countryDao()
        repository = CountryRepository(countryDao)
        allCountries = repository.allCountries
    }

    fun insert(country: Country) = viewModelScope.launch {
        repository.insert(country)
    }
}