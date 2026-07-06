package com.xyz.pomotrack.Data

enum class TaskStatus(val storageValue: String) {
    TODO("todo"),
    IN_PROGRESS("in_progress"),
    DONE("done");

    companion object {
        fun fromStorageValue(value: String): TaskStatus {
            return values().firstOrNull { it.storageValue == value } ?: TODO
        }
    }
}
