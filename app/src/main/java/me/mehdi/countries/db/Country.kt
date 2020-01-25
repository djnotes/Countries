package me.mehdi.countries.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
class Country (@PrimaryKey(autoGenerate = true) @ColumnInfo (name = "id") val id : Int,
               @ColumnInfo (name = "name") val name: String)
