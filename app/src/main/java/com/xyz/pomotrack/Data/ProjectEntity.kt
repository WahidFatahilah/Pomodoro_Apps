package com.xyz.pomotrack.Data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "projects",
    indices = [
        Index(value = ["name"])
    ]
)
data class ProjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    @ColumnInfo(name = "accent_color")
    val accentColor: String? = null,
    @ColumnInfo(name = "cover_label")
    val coverLabel: String? = null,
    @ColumnInfo(name = "default_ringtone")
    val defaultRingtone: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Date = Date(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Date = Date(),
    @ColumnInfo(name = "archived_at")
    val archivedAt: Date? = null
)
