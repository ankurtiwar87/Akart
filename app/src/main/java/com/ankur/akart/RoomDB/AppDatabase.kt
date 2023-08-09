package com.ankur.akart.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductModel::class], version = 1)
abstract class AppDatabase:RoomDatabase() {

    abstract fun productDao():ProductDao


    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "Akart"
                    ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE!!
        }
    }

}