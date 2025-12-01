package com.outracoisa.n2do2alessandro.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.outracoisa.n2do2alessandro.dao.GameSessionDao
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity

@Database(entities = [GameSessionEntity::class], version = 2, exportSchema = false)
abstract class GameSessionDatabase : RoomDatabase() {
    
    abstract fun gameSessionDao(): GameSessionDao
    
    companion object {
        @Volatile
        private var INSTANCE: GameSessionDatabase? = null
        
        fun getDatabase(context: Context): GameSessionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameSessionDatabase::class.java,
                    "game_sessions_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
