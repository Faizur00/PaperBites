package com.example.paperbites.data.database


// AppDatabase.kt
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.paperbites.data.database.Dao.BookmarkDao
import com.example.paperbites.data.database.Dao.PaperDao
import com.example.paperbites.data.database.Entity.BookmarkEntity
import com.example.paperbites.data.database.Entity.PaperEntity

@Database(entities = [PaperEntity::class, BookmarkEntity::class], version = 5)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paperDao(): PaperDao
    abstract fun bookmarkDao(): BookmarkDao

    /**
     * The companion object provides a Thread-Safe Singleton pattern for the database instance.
     * This ensures that only one instance of the database is opened at a time, which is
     * expensive to create and maintains data consistency.
     */

//  that's what's the AI said
    companion object {
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE papers ADD COLUMN subfield TEXT")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bookmarks ADD COLUMN publicationYear INTEGER")
                db.execSQL("ALTER TABLE bookmarks ADD COLUMN fieldName TEXT")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE bookmarks ADD COLUMN isExpanded INTEGER NOT NULL DEFAULT 0")
            }
        }

        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "papers.db"
                )
                    .addMigrations(MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
    }
}
