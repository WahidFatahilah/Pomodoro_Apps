package com.xyz.pomotrack.di

import android.util.Log
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory

class BusyTimeoutFactory : SupportSQLiteOpenHelper.Factory {
    private val delegate = FrameworkSQLiteOpenHelperFactory()

    override fun create(configuration: SupportSQLiteOpenHelper.Configuration): SupportSQLiteOpenHelper {
        val original = delegate.create(configuration)
        return object : SupportSQLiteOpenHelper {
            override val databaseName: String?
                get() = original.databaseName

            override fun setWriteAheadLoggingEnabled(enabled: Boolean) {
                original.setWriteAheadLoggingEnabled(enabled)
            }

            override val writableDatabase: SupportSQLiteDatabase
                get() {
                    val db = original.writableDatabase
                    try {
                        db.execSQL("PRAGMA busy_timeout = 5000")
                    } catch (e: Exception) {
                        Log.w("BusyTimeoutFactory", "Failed to set busy_timeout", e)
                    }
                    return db
                }

            override val readableDatabase: SupportSQLiteDatabase
                get() {
                    val db = original.readableDatabase
                    try {
                        db.execSQL("PRAGMA busy_timeout = 5000")
                    } catch (e: Exception) {
                        Log.w("BusyTimeoutFactory", "Failed to set busy_timeout", e)
                    }
                    return db
                }

            override fun close() = original.close()
        }
    }
}
