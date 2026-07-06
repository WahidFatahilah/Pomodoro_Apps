# Pomodoro Apps Refactor Blueprint

This document turns the current redesign discussion into a concrete implementation target.
It focuses on the new data model, Room structure, migration strategy, and how each screen
in the app should consume the data.

## 1. Product model

The app should move from a single `Task` table to a project-based focus planner:

- One `Project` has many `Task`
- One `Task` has many `FocusSession`
- Project progress is derived from child tasks
- Statistics are derived from both finished tasks and logged focus sessions

Recommended high-level ownership:

- `projects`: planning and grouping
- `tasks`: execution units
- `focus_sessions`: time and productivity history
- `DataStore`: app preferences such as theme, onboarding, and default ringtone

## 2. Proposed Room entities

### 2.1 `projects`

Suggested columns:

```text
id                  INTEGER PRIMARY KEY AUTOINCREMENT
name                TEXT NOT NULL
description         TEXT NULL
accent_color        TEXT NULL
cover_label         TEXT NULL
default_ringtone    TEXT NULL
created_at          INTEGER NOT NULL
updated_at          INTEGER NOT NULL
archived_at         INTEGER NULL
```

Recommended notes:

- `accent_color` supports the project card style from Figma.
- `cover_label` can store the short label/initial shown in the project carousel.
- `default_ringtone` should only exist if ringtone is intended to follow the project.

### 2.2 `tasks`

Suggested columns:

```text
id                       INTEGER PRIMARY KEY AUTOINCREMENT
project_id               INTEGER NOT NULL
title                    TEXT NOT NULL
description              TEXT NULL
status                   TEXT NOT NULL
due_date                 INTEGER NULL
estimated_minutes        INTEGER NOT NULL DEFAULT 25
estimated_pomodoros      INTEGER NULL
completed_pomodoros      INTEGER NOT NULL DEFAULT 0
sort_order               INTEGER NOT NULL DEFAULT 0
created_at               INTEGER NOT NULL
updated_at               INTEGER NOT NULL
completed_at             INTEGER NULL
archived_at              INTEGER NULL
FOREIGN KEY(project_id) REFERENCES projects(id) ON DELETE CASCADE
```

Recommended `status` values:

- `todo`
- `in_progress`
- `done`

Recommended notes:

- Keep progress primarily at project level.
- `completed_pomodoros` becomes useful when later measuring effort instead of only binary completion.
- `sort_order` is needed because the Figma project screen clearly expects manual task ordering.

### 2.3 `focus_sessions`

Suggested columns:

```text
id                  INTEGER PRIMARY KEY AUTOINCREMENT
task_id             INTEGER NOT NULL
session_type        TEXT NOT NULL
planned_minutes     INTEGER NOT NULL
actual_seconds      INTEGER NOT NULL DEFAULT 0
started_at          INTEGER NOT NULL
ended_at            INTEGER NULL
is_completed        INTEGER NOT NULL DEFAULT 0
created_at          INTEGER NOT NULL
FOREIGN KEY(task_id) REFERENCES tasks(id) ON DELETE CASCADE
```

Recommended `session_type` values:

- `focus`
- `short_break`
- `long_break`

Recommended notes:

- This table is the backbone for daily, weekly, monthly, and yearly charts.
- If breaks should not affect productivity charts, filter by `session_type = 'focus'`.

## 3. Recommended Room package structure

Suggested package layout:

```text
app/src/main/java/com/moa/pomodoroapps/data/local/entity/
app/src/main/java/com/moa/pomodoroapps/data/local/dao/
app/src/main/java/com/moa/pomodoroapps/data/local/model/
app/src/main/java/com/moa/pomodoroapps/data/local/relation/
```

Suggested file targets:

- `ProjectEntity.kt`
- `TaskEntity.kt`
- `FocusSessionEntity.kt`
- `ProjectDao.kt`
- `TaskDao.kt`
- `FocusSessionDao.kt`
- `ProjectWithTasks.kt`
- `TaskWithSessions.kt`
- `StatsModels.kt`

If a package move is too large for the first pass, keep the current `Data/` package and
rename later after the app compiles with the new schema.

## 4. Recommended relations

### `ProjectWithTasks`

```kotlin
data class ProjectWithTasks(
    @Embedded val project: ProjectEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "project_id"
    )
    val tasks: List<TaskEntity>
)
```

### `TaskWithSessions`

```kotlin
data class TaskWithSessions(
    @Embedded val task: TaskEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "task_id"
    )
    val sessions: List<FocusSessionEntity>
)
```

## 5. Index strategy

Recommended indexes:

- `projects(name)`
- `tasks(project_id)`
- `tasks(status)`
- `tasks(due_date)`
- `tasks(completed_at)`
- `tasks(project_id, status)`
- `focus_sessions(task_id)`
- `focus_sessions(started_at)`
- `focus_sessions(session_type)`
- `focus_sessions(task_id, started_at)`

These indexes matter because the new app will repeatedly filter by:

- current period
- active vs done tasks
- project grouping
- time-based statistics

## 6. DAO contract blueprint

### `ProjectDao`

Recommended operations:

- insert project
- update project
- delete project
- search projects by name
- load all projects
- load `ProjectWithTasks`
- load project summaries with done/total counts

Recommended query targets:

- `getAllProjects()`
- `searchProjects(query: String)`
- `getProjectWithTasks(projectId: Long)`
- `getProjectsForDateRange(start: Date, end: Date)`

### `TaskDao`

Recommended operations:

- insert one task
- insert many tasks in a single save flow
- update task
- delete task
- mark task done
- load tasks by project
- load tasks by period

Recommended query targets:

- `getTasksByProject(projectId: Long)`
- `getTasksDueBetween(start: Date, end: Date)`
- `getActiveTasksDueBetween(start: Date, end: Date)`
- `getCompletedTasksByProject(projectId: Long)`
- `getTaskById(taskId: Long)`

Important note:

Avoid equality queries like `deadline = :today`.
Use day-range queries instead:

```sql
WHERE due_date >= :startOfDay AND due_date < :nextDay
```

### `FocusSessionDao`

Recommended operations:

- insert session start
- update session end/completion
- load sessions by task
- aggregate focus minutes by period

Recommended query targets:

- `getSessionsForTask(taskId: Long)`
- `getFocusMinutesByDateRange(start: Date, end: Date)`
- `getCompletedFocusSessionsByDateRange(start: Date, end: Date)`
- `getWeeklyFocusChart(start: Date, end: Date)`
- `getTopTasksByFocusTime(start: Date, end: Date)`

## 7. Statistics model

Two data sources are required:

- Task outcome statistics from `tasks`
- Time usage statistics from `focus_sessions`

### Daily statistics should support:

- total finished tasks today
- total focus minutes today
- total active projects today
- completion percentage for today

### Weekly statistics should support:

- finished tasks in current week
- focus minutes in current week
- bar chart by day of week
- most active project/task this week

### Monthly statistics should support:

- finished tasks in month
- focus time in month
- project completion distribution

### Yearly statistics should support:

- tasks completed per month
- focus hours per month
- top projects this year

## 8. How the Figma screens map to the new schema

### Home

The Home screen should consume a grouped view:

- date filter: today / tomorrow / this week
- global summary metrics
- list of projects with active tasks for the selected period

Required data:

- count of projects with tasks in selected range
- count of tasks in selected range
- total focus minutes in selected range
- grouped `ProjectWithTasks`

### Project

The Project screen should become a project-task composer and editor:

- choose date
- choose existing project or create a new one
- add multiple task drafts
- save all drafts in one action

This flow should call:

- insert project if needed
- bulk insert tasks

### Pomodoro

The Pomodoro screen should run against a real task ID:

- load task
- load task's project
- create a `focus_sessions` row when a focus session starts
- update that row when completed or cancelled

This screen should no longer receive a serialized task string in the route.
It should receive `taskId`.

### Statistik

The statistics screen should read period-specific aggregates from:

- `tasks.completed_at`
- `focus_sessions.started_at`
- `focus_sessions.actual_seconds`

## 9. Migration plan from the current database

Current schema:

- single `Task` table
- fields: `id`, `project`, `title`, `description`, `isDone`, `deadline`

Target schema:

- `projects`
- `tasks`
- `focus_sessions`

### Safe migration path

1. Create `projects` table.
2. Create new `tasks` table with the target schema.
3. Insert distinct current task project names into `projects`.
4. Copy old tasks into new `tasks`, mapping old `project` string to the new `projects.id`.
5. Set:
   - `status = 'done'` if `isDone = 1`, otherwise `todo`
   - `estimated_minutes = 25`
   - `created_at = currentTimeMillis` or fallback migration timestamp
   - `updated_at = currentTimeMillis`
   - `completed_at = deadline` only if a true completion timestamp is unavailable and you choose a fallback
6. Drop old `Task` table.
7. Rename new `tasks` table to the final table name if needed.
8. Create `focus_sessions` table empty.
9. Add indexes.

### Important migration caveat

The old database does not store:

- task creation time
- task completion time
- session history
- project metadata

So migration can preserve task content, but some analytics fields must start fresh.

## 10. Recommended implementation order

1. Add new entities and DAO interfaces.
2. Add `AppDatabase` version bump and migration.
3. Make the app compile with the new schema while keeping old UI behavior as stable as possible.
4. Refactor `ProjectScreen` into the multi-task composer.
5. Refactor Home grouping by project.
6. Refactor timer to work with `taskId` and session logging.
7. Build the statistics screen from the new aggregates.

## 11. Concrete file-level refactor targets

Current files that will be impacted most:

- `app/src/main/java/com/moa/pomodoroapps/Data/AppDatabase.kt`
- `app/src/main/java/com/moa/pomodoroapps/Data/Task.kt`
- `app/src/main/java/com/moa/pomodoroapps/Data/TaskDAO.kt`
- `app/src/main/java/com/moa/pomodoroapps/di/Module.kt`
- `app/src/main/java/com/moa/pomodoroapps/presentation/ui/screen/HOME/MainScreenViewModel.kt`
- `app/src/main/java/com/moa/pomodoroapps/presentation/ui/screen/Project/ProjectScreen.kt`
- `app/src/main/java/com/moa/pomodoroapps/presentation/ui/screen/Pomodoro/PomodoroScreen.kt`
- `app/src/main/java/com/moa/pomodoroapps/presentation/navigation/BottomNavGraph.kt`

## 12. Immediate next step

The best next coding step is:

- create the new Room entities
- create the new DAO interfaces
- update `AppDatabase`
- keep UI temporarily compiling

That gives the app a stable foundation for the redesign without trying to solve Home,
Project, Pomodoro, and Statistik in one risky jump.
