package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BusinessIdea
import com.example.model.HustleJourneyProgress
import com.example.model.JourneyStage
import com.example.model.JourneyTask
import com.example.ui.components.DeveloperFooter
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JourneySheet(
    idea: BusinessIdea,
    progress: HustleJourneyProgress,
    tasks: List<JourneyTask>,
    onToggleTask: (String) -> Unit,
    onSaveNotes: (String) -> Unit,
    onOpenBusinessPlan: () -> Unit,
    onDismiss: () -> Unit
) {
    var notesText by remember(progress.notes) { mutableStateOf(progress.notes) }
    val completionPercent = progress.getCompletionPercentage(tasks.size)
    val currentStage = progress.getCurrentStage(tasks)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = SurfaceCard,
        modifier = Modifier.testTag("journey_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "6-Stage Hustle Journey",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SciBluePrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = idea.title,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Summary Card
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SciBlueContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Current Stage: ${currentStage.displayName}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSciBlueContainer
                            )
                        )
                        Text(
                            text = "$completionPercent% Complete",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = SciBluePrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { completionPercent / 100f },
                        color = SciBluePrimary,
                        trackColor = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "${progress.completedTaskIds.size} of ${tasks.size} milestones fulfilled",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6 Stages
            JourneyStage.values().forEach { stage ->
                val stageTasks = tasks.filter { it.stage == stage }
                val isCurrentStage = currentStage == stage

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCurrentStage) Color(0xFFF0FDF4) else SurfaceVariantWarm,
                    border = if (isCurrentStage) androidx.compose.foundation.BorderStroke(1.dp, KenyaGreenPrimary) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = stage.displayName,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrentStage) KenyaGreenPrimary else TextPrimary
                                )
                            )
                            if (isCurrentStage) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = KenyaGreenPrimary
                                ) {
                                    Text(
                                        text = "ACTIVE",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = stage.description,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        stageTasks.forEach { task ->
                            val isChecked = progress.completedTaskIds.contains(task.id)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onToggleTask(task.id) }
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Checkbox(
                                    checked = isChecked,
                                    onCheckedChange = { onToggleTask(task.id) },
                                    colors = CheckboxDefaults.colors(checkedColor = SciBluePrimary)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isChecked) FontWeight.Normal else FontWeight.SemiBold,
                                            color = if (isChecked) TextMuted else TextPrimary
                                        )
                                    )
                                    Text(
                                        text = task.description,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notes Section
            Text(
                text = "Hustle Notes & Contacts:",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = notesText,
                onValueChange = {
                    notesText = it
                    onSaveNotes(it)
                },
                placeholder = { Text("Record supplier phone numbers, exact stall location, or target launch date...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Build Business Plan Action
            Button(
                onClick = onOpenBusinessPlan,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Description, contentDescription = "Plan")
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Build Business Plan (SCI BUSINESS PLAN)",
                    fontWeight = FontWeight.Bold
                )
            }

            DeveloperFooter()
        }
    }
}
