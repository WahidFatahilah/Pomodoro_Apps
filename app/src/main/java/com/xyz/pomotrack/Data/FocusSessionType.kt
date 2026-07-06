package com.xyz.pomotrack.Data

enum class FocusSessionType(val storageValue: String) {
    FOCUS("focus"),
    SHORT_BREAK("short_break"),
    LONG_BREAK("long_break");

    companion object {
        fun fromStorageValue(value: String): FocusSessionType {
            return values().firstOrNull { it.storageValue == value } ?: FOCUS
        }
    }
}
