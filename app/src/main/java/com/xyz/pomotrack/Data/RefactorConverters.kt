package com.xyz.pomotrack.Data

import androidx.room.TypeConverter

class RefactorConverters {
    @TypeConverter
    fun toTaskStatus(value: String?): TaskStatus {
        return value?.let(TaskStatus::fromStorageValue) ?: TaskStatus.TODO
    }

    @TypeConverter
    fun fromTaskStatus(status: TaskStatus?): String {
        return status?.storageValue ?: TaskStatus.TODO.storageValue
    }

    @TypeConverter
    fun toFocusSessionType(value: String?): FocusSessionType {
        return value?.let(FocusSessionType::fromStorageValue) ?: FocusSessionType.FOCUS
    }

    @TypeConverter
    fun fromFocusSessionType(type: FocusSessionType?): String {
        return type?.storageValue ?: FocusSessionType.FOCUS.storageValue
    }
}
