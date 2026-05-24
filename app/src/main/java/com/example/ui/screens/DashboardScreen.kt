package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ParentViewModel
import com.example.ui.theme.*

data class DashboardModule(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badgeValue: String,
    val tintColor: Color,
    val backgroundColor: Color,
    val borderColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: ParentViewModel,
    onNavigate: (String) -> Unit,
    onLogout: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val sleepLogs by viewModel.sleepLogs.collectAsState()
    val vaccineLogs by viewModel.vaccineLogs.collectAsState()
    val cryingLogs by viewModel.cryingLogs.collectAsState()
    val feedingLogs by viewModel.feedingLogs.collectAsState()
    val milestones by viewModel.milestoneLogs.collectAsState()
    val memories by viewModel.memoryLogs.collectAsState()

    val userName = currentUser?.displayName ?: "Caregiver"
    val isDark = isSystemInDarkTheme()

    // Dynamic stats computation to show on cards! Highly polished.
    val completedVaccines = vaccineLogs.count { it.status == "COMPLETED" }
    val totalVaccines = vaccineLogs.size
    val vaccineStat = "$completedVaccines/$totalVaccines Complete"

    val completedMilestones = milestones.count { it.status == "COMPLETED" }
    val milestoneStat = "$completedMilestones Achieved"

    val totalSleeps = sleepLogs.size
    val sleepStat = "$totalSleeps Naps tracked"

    val totalFeeds = feedingLogs.size
    val feedStat = "$totalFeeds Feedings logged"

    val cryLogSize = cryingLogs.size
    val cryStat = "$cryLogSize Reports"

    val memorySize = memories.size
    val memoryStat = "$memorySize Timeline Moment" + (if (memorySize != 1) "s" else "")

    val modules = listOf(
        DashboardModule(
            id = "sleep",
            title = "Sleep Log",
            description = sleepStat,
            icon = Icons.Default.CheckCircle,
            badgeValue = "🌙",
            tintColor = if (isDark) MinimalDarkTertiary else SleepLogAccent,
            backgroundColor = if (isDark) MaterialTheme.colorScheme.surface else SleepLogBg,
            borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else SleepLogBorder
        ),
        DashboardModule(
            id = "crying",
            title = "AI Cry Analyst",
            description = cryStat,
            icon = Icons.Default.Info,
            badgeValue = "🛡️",
            tintColor = if (isDark) MinimalDarkTertiary else CryAnalystAccent,
            backgroundColor = if (isDark) MaterialTheme.colorScheme.surface else CryAnalystBg,
            borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else CryAnalystBorder
        ),
        DashboardModule(
            id = "meals",
            title = "Meals & Feeding",
            description = feedStat,
            icon = Icons.Default.DateRange,
            badgeValue = "🍼",
            tintColor = if (isDark) MinimalDarkTertiary else MealsAccent,
            backgroundColor = if (isDark) MaterialTheme.colorScheme.surface else MealsBg,
            borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else MealsBorder
        ),
        DashboardModule(
            id = "vaccines",
            title = "Immunizations",
            description = vaccineStat,
            icon = Icons.Default.Check,
            badgeValue = "🛡️",
            tintColor = if (isDark) MinimalDarkTertiary else ImmunizationAccent,
            backgroundColor = if (isDark) MaterialTheme.colorScheme.surface else ImmunizationBg,
            borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else ImmunizationBorder
        ),
        DashboardModule(
            id = "milestones",
            title = "Milestone Progress",
            description = milestoneStat,
            icon = Icons.Default.Star,
            badgeValue = "🌟",
            tintColor = if (isDark) MinimalDarkTertiary else MilestoneAccent,
            backgroundColor = if (isDark) MaterialTheme.colorScheme.surface else MilestoneBg,
            borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else MilestoneBorder
        ),
        DashboardModule(
            id = "timeline",
            title = "Memory Timeline",
            description = memoryStat,
            icon = Icons.Default.Face,
            badgeValue = "📸",
            tintColor = if (isDark) MinimalDarkTertiary else TimelineAccent,
            backgroundColor = if (isDark) MaterialTheme.colorScheme.surface else TimelineBg,
            borderColor = if (isDark) MaterialTheme.colorScheme.outlineVariant else TimelineBorder
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Hi, $userName",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = (-0.5).sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "SECURE LOCAL STORAGE ACTIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                ),
                                color = Color(0xFF059669)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.testTag("logout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Log Out",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reassuring privacy banner header styled exactly like Clean Minimalism
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🔒",
                        fontSize = 24.sp
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Privacy Shield Enabled",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        Text(
                            text = "All trackers, baby logs, and profiles are encrypted locally. Zero cloud exposure danger.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            Text(
                text = "Trackers & AI Services",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )

            // Grid of highly beautiful, modern minimalist service modules!
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(modules) { module ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = module.backgroundColor),
                        border = BorderStroke(1.dp, module.borderColor),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(145.dp)
                            .testTag("dashboard_module_${module.id}")
                            .clickable { onNavigate(module.id) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(module.tintColor.copy(alpha = 0.08f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = module.badgeValue, fontSize = 20.sp)
                                }

                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    tint = module.tintColor.copy(alpha = 0.5f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = module.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = (-0.2).sp
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1
                                )
                                Text(
                                    text = module.description,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = module.tintColor,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // encouraging parent quote footer in a beautiful minimal container
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "🌱 \"A stressed parent holds oceans of love. Breathe deeply, you are doing a magnificent job.\"",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                )
            }
        }
    }
}
