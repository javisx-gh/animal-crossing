package com.example.cartas7.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Main Room database for the project.
 */
@Database(entities = [Jugador::class, Partida::class, Participacion::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Returns DAO implementation.
     */
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns a singleton database instance.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cartas7.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
