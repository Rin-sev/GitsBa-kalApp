package com.example.kalapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kalapp.model.TriageMessageEntity
import com.example.kalapp.model.TriageMessageDao

@Database(entities = [TriageMessageEntity::class], version = 1)
abstract class KalAppDatabase : RoomDatabase() {

    abstract fun triageMessageDao(): TriageMessageDao

    companion object {
        @Volatile
        private var INSTANCE: KalAppDatabase? = null

        fun getInstance(context: Context): KalAppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    KalAppDatabase::class.java,
                    "kalapp_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}