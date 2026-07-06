package com.xyz.pomotrack.presentation.ui.screen.Setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.xyz.pomotrack.R
import com.xyz.pomotrack.presentation.ui.theme.FontColor
import com.xyz.pomotrack.presentation.ui.theme.Ket_1
import com.xyz.pomotrack.presentation.ui.theme.Subtitle_1
import com.xyz.pomotrack.presentation.ui.theme.backgroundBotton
import com.xyz.pomotrack.presentation.ui.theme.focusAccent
import com.xyz.pomotrack.presentation.ui.theme.surfaceElevated
import com.xyz.pomotrack.presentation.ui.theme.surfaceMuted
import com.xyz.pomotrack.presentation.ui.theme.textMuted

@Composable
fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        Text(
            text = title,
            style = Ket_1,
            color = MaterialTheme.colors.textMuted,
            modifier = Modifier.padding(start = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            backgroundColor = MaterialTheme.colors.surfaceElevated,
            elevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsActionItem(
    icon: Int,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    SettingsRow(
        icon = icon,
        title = title,
        subtitle = subtitle,
        modifier = Modifier.clickable(onClick = onClick),
        trailing = {
            IconButton(onClick = onClick, modifier = Modifier.size(40.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow),
                    contentDescription = title,
                    tint = MaterialTheme.colors.textMuted
                )
            }
        }
    )
}

@Composable
fun SettingsSwitchItem(
    icon: Int,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    SettingsRow(
        icon = icon,
        title = title,
        subtitle = subtitle,
        modifier = Modifier.clickable { onCheckedChange(!checked) },
        trailing = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}

@Composable
private fun SettingsRow(
    icon: Int,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colors.surfaceMuted),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = icon),
                contentDescription = title,
                tint = MaterialTheme.colors.focusAccent,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(text = title, style = Subtitle_1, color = MaterialTheme.colors.FontColor)
            Text(text = subtitle, style = Ket_1, color = MaterialTheme.colors.textMuted)
        }

        Spacer(modifier = Modifier.width(8.dp))
        trailing()
    }
}
