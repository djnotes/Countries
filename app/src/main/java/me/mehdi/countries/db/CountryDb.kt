package me.mehdi.countries.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = [Country::class], version = 1, exportSchema = false)
public abstract class CountryDb : RoomDatabase(){

    abstract fun countryDao() : CountryDao

    private class CountryDbCallback(private val scope : CoroutineScope)
        : RoomDatabase.Callback () {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let{database ->
                scope.launch{
                    populateDatabase(database.countryDao())
                }

            }
        }

        suspend fun populateDatabase(countryDao: CountryDao) {
            val country = Country(0, "Iran")
            countryDao.insert(country)

            val country1 = Country(0, "USA")
            countryDao.insert(country1)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CountryDb? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : CountryDb? {
            val tempInstance = INSTANCE
            if(INSTANCE != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CountryDb::class.java,
                    "countries_database"
                ).addCallback(CountryDbCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }
}