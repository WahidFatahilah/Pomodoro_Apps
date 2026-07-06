package com.xyz.pomotrack.Data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.*

@Database(
    entities = [
        Task::class,
        ProjectEntity::class,
        TaskEntity::class,
        FocusSessionEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class, RefactorConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDAO(): TaskDAO
    abstract fun projectDao(): ProjectDao
    abstract fun taskEntityDao(): TaskEntityDao
    abstract fun focusSessionDao(): FocusSessionDao

    companion object {
        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val nowSql = "(CAST(strftime('%s','now') AS INTEGER) * 1000)"

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `projects` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `description` TEXT,
                        `accent_color` TEXT,
                        `cover_label` TEXT,
                        `default_ringtone` TEXT,
                        `created_at` INTEGER NOT NULL,
                        `updated_at` INTEGER NOT NULL,
                        `archived_at` INTEGER
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `tasks` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `project_id` INTEGER NOT NULL,
                        `title` TEXT NOT NULL,
                        `description` TEXT,
                        `status` TEXT NOT NULL,
                        `due_date` INTEGER,
                        `estimated_minutes` INTEGER NOT NULL,
                        `estimated_pomodoros` INTEGER,
                        `completed_pomodoros` INTEGER NOT NULL,
                        `sort_order` INTEGER NOT NULL,
                        `created_at` INTEGER NOT NULL,
                        `updated_at` INTEGER NOT NULL,
                        `completed_at` INTEGER,
                        `archived_at` INTEGER,
                        FOREIGN KEY(`project_id`) REFERENCES `projects`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `focus_sessions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `task_id` INTEGER NOT NULL,
                        `session_type` TEXT NOT NULL,
                        `planned_minutes` INTEGER NOT NULL,
                        `actual_seconds` INTEGER NOT NULL,
                        `started_at` INTEGER NOT NULL,
                        `ended_at` INTEGER,
                        `is_completed` INTEGER NOT NULL,
                        `created_at` INTEGER NOT NULL,
                        FOREIGN KEY(`task_id`) REFERENCES `tasks`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE
                    )
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    INSERT INTO `projects` (`name`, `description`, `accent_color`, `cover_label`, `default_ringtone`, `created_at`, `updated_at`, `archived_at`)
                    SELECT DISTINCT
                        CASE
                            WHEN TRIM(`project`) = '' THEN 'Inbox'
                            ELSE `project`
                        END AS `name`,
                        NULL,
                        NULL,
                        NULL,
                        NULL,
                        $nowSql,
                        $nowSql,
                        NULL
                    FROM `Task`
                    """.trimIndent()
                )

                database.execSQL(
                    """
                    INSERT INTO `tasks` (
                        `project_id`,
                        `title`,
                        `description`,
                        `status`,
                        `due_date`,
                        `estimated_minutes`,
                        `estimated_pomodoros`,
                        `completed_pomodoros`,
                        `sort_order`,
                        `created_at`,
                        `updated_at`,
                        `completed_at`,
                        `archived_at`
                    )
                    SELECT
                        (
                            SELECT `id`
                            FROM `projects`
                            WHERE `name` = CASE
                                WHEN TRIM(old_task.`project`) = '' THEN 'Inbox'
                                ELSE old_task.`project`
                            END
                            LIMIT 1
                        ) AS `project_id`,
                        old_task.`title`,
                        old_task.`description`,
                        CASE
                            WHEN old_task.`isDone` = 1 THEN 'done'
                            ELSE 'todo'
                        END AS `status`,
                        old_task.`deadline`,
                        25 AS `estimated_minutes`,
                        NULL AS `estimated_pomodoros`,
                        CASE
                            WHEN old_task.`isDone` = 1 THEN 1
                            ELSE 0
                        END AS `completed_pomodoros`,
                        old_task.`id` AS `sort_order`,
                        $nowSql AS `created_at`,
                        $nowSql AS `updated_at`,
                        CASE
                            WHEN old_task.`isDone` = 1 THEN old_task.`deadline`
                            ELSE NULL
                        END AS `completed_at`,
                        NULL AS `archived_at`
                    FROM `Task` AS old_task
                    """.trimIndent()
                )

                database.execSQL("CREATE INDEX IF NOT EXISTS `index_projects_name` ON `projects` (`name`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_project_id` ON `tasks` (`project_id`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_status` ON `tasks` (`status`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_due_date` ON `tasks` (`due_date`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_completed_at` ON `tasks` (`completed_at`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_tasks_project_id_status` ON `tasks` (`project_id`, `status`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_focus_sessions_task_id` ON `focus_sessions` (`task_id`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_focus_sessions_started_at` ON `focus_sessions` (`started_at`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_focus_sessions_session_type` ON `focus_sessions` (`session_type`)")
                database.execSQL("CREATE INDEX IF NOT EXISTS `index_focus_sessions_task_id_started_at` ON `focus_sessions` (`task_id`, `started_at`)")
            }
        }
    }

}

class Converters {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}
