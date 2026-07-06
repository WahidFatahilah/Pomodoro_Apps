package com.moa.pomodoroapps.presentation.ui.screen.Project

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.moa.pomodoroapps.Data.ProjectEntity
import com.moa.pomodoroapps.R
import com.moa.pomodoroapps.presentation.ui.theme.FontColor
import com.moa.pomodoroapps.presentation.ui.theme.Heading_H2
import com.moa.pomodoroapps.presentation.ui.theme.Ket_1
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_1
import com.moa.pomodoroapps.presentation.ui.theme.Subtitle_2
import com.moa.pomodoroapps.presentation.ui.theme.backgroundColor
import com.moa.pomodoroapps.presentation.ui.theme.surfaceElevated
import com.moa.pomodoroapps.presentation.ui.theme.surfaceMuted
import com.moa.pomodoroapps.presentation.ui.theme.textMuted
import kotlinx.coroutines.delay
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun AddTaskScreen(
    navController: NavController,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val today = remember { LocalDate.now() }
    val visibleDates = remember(today) { (0L..4L).map { today.plusDays(it) } }

    LaunchedEffect(viewModel.userMessage) {
        val message = viewModel.userMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.consumeUserMessage()
    }

    LaunchedEffect(viewModel.showSuccessBanner) {
        if (viewModel.showSuccessBanner) {
            Toast.makeText(context, "Tugas berhasil disimpan", Toast.LENGTH_SHORT).show()
            delay(900)
            viewModel.dismissSuccessBanner()
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            AddTaskHeader(
                onBack = { navController.popBackStack() },
                onClear = viewModel::clearForm
            )

            Divider(
                modifier = Modifier.padding(top = 18.dp),
                color = MaterialTheme.colors.textMuted.copy(alpha = 0.24f)
            )

            Spacer(modifier = Modifier.height(22.dp))

            AddTaskDateStrip(
                dates = visibleDates,
                selectedDate = viewModel.selectedDate,
                today = today,
                onDateSelected = viewModel::updateSelectedDate
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Nama project",
                style = Heading_H2,
                color = MaterialTheme.colors.FontColor
            )
            Spacer(modifier = Modifier.height(12.dp))

            AddTaskExistingProjectToggle(
                checked = viewModel.useExistingProject,
                onCheckedChange = viewModel::toggleExistingProject
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (viewModel.useExistingProject) {
                AddTaskInputField(
                    value = viewModel.projectSearch,
                    placeholder = "Cari project",
                    onValueChange = viewModel::updateProjectSearch
                )

                if (viewModel.filteredProjects.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    AddTaskProjectSuggestions(
                        projects = viewModel.filteredProjects,
                        selectedProjectId = viewModel.selectedProjectId,
                        onSelect = viewModel::selectExistingProject
                    )
                }
            } else {
                AddTaskInputField(
                    value = viewModel.projectName,
                    placeholder = "Masukkan nama project",
                    onValueChange = viewModel::updateProjectName
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Tugas",
                style = Heading_H2,
                color = MaterialTheme.colors.FontColor
            )
            Spacer(modifier = Modifier.height(14.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                viewModel.taskDrafts.forEachIndexed { index, taskDraft ->
                    AddTaskDraftField(
                        index = index,
                        value = taskDraft.title,
                        canRemove = viewModel.taskDrafts.size > 1 || taskDraft.title.isNotBlank(),
                        onValueChange = { viewModel.updateTaskDraft(taskDraft.id, it) },
                        onRemove = { viewModel.removeTaskDraft(taskDraft.id) }
                    )
                }
            }

            TextButton(
                onClick = viewModel::addTaskDraft,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(
                    text = "+ Tambah tugas",
                    style = Subtitle_2,
                    color = MaterialTheme.colors.primary
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
                .height(52.dp),
            onClick = viewModel::saveProjectPlan,
            enabled = viewModel.canSave,
            shape = RoundedCornerShape(22.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary,
                disabledBackgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.35f),
                contentColor = Color.White,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = if (viewModel.isSaving) "Menyimpan..." else "Simpan",
                style = Subtitle_1,
                color = Color.White
            )
        }
    }
}

@Composable
private fun AddTaskHeader(
    onBack: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Kembali",
                tint = MaterialTheme.colors.FontColor
            )
        }

        Text(
            text = "Tambah Tugas",
            modifier = Modifier.weight(1f),
            style = Heading_H2,
            color = MaterialTheme.colors.FontColor,
            textAlign = TextAlign.Center
        )

        IconButton(
            onClick = onClear,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.deletelogo),
                contentDescription = "Reset",
                tint = MaterialTheme.colors.FontColor
            )
        }
    }
}

@Composable
private fun AddTaskDateStrip(
    dates: List<LocalDate>,
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        dates.forEach { date ->
            Card(
                modifier = Modifier
                    .width(66.dp)
                    .clickable { onDateSelected(date) },
                shape = RoundedCornerShape(14.dp),
                backgroundColor = if (date == selectedDate) MaterialTheme.colors.primary else MaterialTheme.colors.surfaceElevated,
                elevation = if (date == selectedDate) 0.dp else 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = if (date == today) "Hari ini" else addTaskDayShortLabel(date.dayOfWeek),
                        style = Ket_1,
                        color = if (date == selectedDate) Color.White else MaterialTheme.colors.FontColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = date.dayOfMonth.toString(),
                        style = Heading_H2,
                        color = if (date == selectedDate) Color.White else MaterialTheme.colors.FontColor
                    )
                }
            }
        }
    }
}

@Composable
private fun AddTaskExistingProjectToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (checked) MaterialTheme.colors.primary else Color.Transparent)
                .border(
                    width = 1.dp,
                    color = if (checked) MaterialTheme.colors.primary else MaterialTheme.colors.textMuted.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Text(
                    text = "✓",
                    style = Subtitle_2,
                    color = Color.White
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Project sudah ada",
            style = Subtitle_2,
            color = MaterialTheme.colors.FontColor
        )
    }
}

@Composable
private fun AddTaskInputField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(
                text = placeholder,
                style = Subtitle_2,
                color = MaterialTheme.colors.textMuted.copy(alpha = 0.8f)
            )
        },
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            backgroundColor = if (MaterialTheme.colors.isLight) Color(0xFFF1F1F4) else MaterialTheme.colors.surfaceMuted,
            textColor = MaterialTheme.colors.FontColor,
            cursorColor = MaterialTheme.colors.primary
        )
    )
}

@Composable
private fun AddTaskProjectSuggestions(
    projects: List<ProjectEntity>,
    selectedProjectId: Long?,
    onSelect: (ProjectEntity) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        projects.forEach { project ->
            val selected = project.id == selectedProjectId
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        if (selected) MaterialTheme.colors.primary.copy(alpha = 0.12f) else MaterialTheme.colors.surfaceElevated
                    )
                    .clickable { onSelect(project) }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(
                            if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.textMuted.copy(alpha = 0.3f)
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = project.name,
                    style = Subtitle_2,
                    color = MaterialTheme.colors.FontColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun AddTaskDraftField(
    index: Int,
    value: String,
    canRemove: Boolean,
    onValueChange: (String) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        backgroundColor = MaterialTheme.colors.surfaceElevated,
        elevation = 6.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = if (index == 0) "Nama Tugas" else "Masukkan nama tugas",
                        style = Subtitle_2,
                        color = MaterialTheme.colors.textMuted.copy(alpha = 0.8f)
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    backgroundColor = Color.Transparent,
                    textColor = MaterialTheme.colors.FontColor,
                    cursorColor = MaterialTheme.colors.primary
                )
            )

            if (canRemove) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .border(
                            width = 1.4.dp,
                            color = MaterialTheme.colors.primary,
                            shape = CircleShape
                        )
                        .clickable(onClick = onRemove),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "-",
                        style = Subtitle_2,
                        color = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

private fun addTaskDayShortLabel(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "Sen"
        DayOfWeek.TUESDAY -> "Sel"
        DayOfWeek.WEDNESDAY -> "Rab"
        DayOfWeek.THURSDAY -> "Kam"
        DayOfWeek.FRIDAY -> "Jum"
        DayOfWeek.SATURDAY -> "Sab"
        DayOfWeek.SUNDAY -> "Min"
    }
}
