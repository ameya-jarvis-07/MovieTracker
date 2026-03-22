package com.jarvis.movietracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jarvis.movietracker.data.local.dao.ReviewDao
import com.jarvis.movietracker.data.local.dao.WatchlistDao
import com.jarvis.movietracker.data.local.entities.ReviewEntity
import com.jarvis.movietracker.data.local.entities.WatchlistEntity

@Database(
    entities = [WatchlistEntity::class, ReviewEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieTrackerDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: MovieTrackerDatabase? = null

        fun getInstance(context: Context): MovieTrackerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieTrackerDatabase::class.java,
                    "movie_tracker_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

