package com.bs.sriwilispetugas.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.bs.sriwilispetugas.data.room.dao.LoginResponseDao
import com.bs.sriwilispetugas.data.room.dao.NasabahDao
import com.bs.sriwilispetugas.data.room.dao.PesananSampahDao
import com.bs.sriwilispetugas.data.room.dao.PesananSampahKeranjangDao

@Database(
    entities = [LoginResponseEntity::class, PesananSampahKeranjangEntity::class, PesananSampahEntity::class, NasabahEntity::class],
    version = 4, // Update the version when modifying database structure
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun loginResponseDao(): LoginResponseDao
    abstract fun pesananSampahkeranjangDao(): PesananSampahKeranjangDao
    abstract fun pesananSampahDao(): PesananSampahDao
    abstract fun nasabahDao(): NasabahDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
