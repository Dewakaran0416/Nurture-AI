package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.ParentViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealsScreen(
    viewModel: ParentViewModel,
    onBack: () -> Unit
) {
    val feedingLogs by viewModel.feedingLogs.collectAsState()
    val isGeneratingPlan by viewModel.isGeneratingMealPlan.collectAsState()
    val mealPlanResult by viewModel.mealPlanResult.collectAsState()

    var activeTab by remember { mutableStateOf(0) } // 0 = Feeding logs, 1 = Smart AI Planner

    // Feeding Log inputs
    var feedType by remember { mutableStateOf("Formula Bottle") }
    var amountOrDuration by remember { mutableStateOf("") }
    var foodDetails by remember { mutableStateOf("") }
    var feedNote by remember { mutableStateOf("") }

    // Meal Plan inputs
    var plannerTarget by remember { mutableStateOf("Baby") } // "Baby" or "Parent"
    var babyAgeInMonths by remember { mutableStateOf("6") }
    var feedingPreference by remember { mutableStateOf("Spoon Purées First") }
    var allergiesText by remember { mutableStateOf("") }

    // Parent states
    var parentStage by remember { mutableStateOf("Early Postpartum Healing") }
    var parentRestriction by remember { mutableStateOf("None/Unrestricted") }
    var parentPrepPrefByText by remember { mutableStateOf("Quick 15-minute meals") }

    val feedTypes = listOf("Breast Feeding", "Formula Bottle", "Solid Foods")
    val defaultAgeOptions = listOf("4", "6", "8", "10", "12")
    val defaultPrefOptions = listOf("Spoon Purées First", "Baby-Led Weaning", "Mixed Baby Feeding Style")

    val parentStageOptions = listOf("Early Postpartum Healing", "Milk Supply Booster", "Energy Restoration", "General Postpartum Balance")
    val parentRestrictionOptions = listOf("None/Unrestricted", "Vegetarian", "Vegan", "Gluten-Free", "Dairy-Free")
    val parentPrepOptions = listOf("Quick 15-minute meals", "Warm soups & stews", "High protein meal prep", "Feminine iron focus")

    val sdfTime = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val sdfDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Infant Nutrition & Feeding", fontWeight = FontWeight.Bold, letterSpacing = (-0.5).sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
        ) {
            // Elegant Tab Selector
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = { Text("Log Feed Tracker", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = null) }
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = { Text("Smart Weaning AI", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Star, contentDescription = null) }
                )
            }

            if (activeTab == 0) {
                // FEED TRACKER TAB
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Log Baby's Latest Meal",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                // Feed type chips
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                                ) {
                                    feedTypes.forEach { type ->
                                        val selected = feedType == type
                                        FilterChip(
                                            selected = selected,
                                            onClick = { feedType = type },
                                            label = { Text(type) },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = amountOrDuration,
                                        onValueChange = { amountOrDuration = it },
                                        label = { Text("Amount / Duration") },
                                        placeholder = { Text("e.g. 150ml, 12 mins") },
                                        singleLine = true,
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("feed_amount_input")
                                    )

                                    if (feedType == "Solid Foods") {
                                        OutlinedTextField(
                                            value = foodDetails,
                                            onValueChange = { foodDetails = it },
                                            label = { Text("Solid Recipes info") },
                                            placeholder = { Text("e.g. Steamed squash") },
                                            singleLine = true,
                                            shape = RoundedCornerShape(12.dp),
                                            modifier = Modifier
                                                .weight(1f)
                                                .testTag("feed_food_input")
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = feedNote,
                                    onValueChange = { feedNote = it },
                                    label = { Text("Nutrition Diary Notes") },
                                    placeholder = { Text("e.g. Spat some first spoons, but finished bottle happily.") },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("feed_note_input")
                                )

                                Button(
                                    onClick = {
                                        if (amountOrDuration.isNotBlank()) {
                                            viewModel.addFeedingLog(
                                                type = feedType,
                                                amountOrDuration = amountOrDuration,
                                                foodDetails = foodDetails,
                                                note = feedNote
                                            )
                                            amountOrDuration = ""
                                            foodDetails = ""
                                            feedNote = ""
                                        }
                                    },
                                    enabled = amountOrDuration.isNotBlank(),
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .testTag("save_feed_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    Text("Record Feed log", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "Historical Feeding Logs",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (feedingLogs.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "🥣 No feeding records logged yet. Enter baby's meals above to track nourishment securely.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                )
                            }
                        }
                    } else {
                        items(feedingLogs) { log ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Food category emoji representation
                                    Text(
                                        text = when (log.type) {
                                            "Breast Feeding" -> "👩‍🍼"
                                            "Formula Bottle" -> "🍼"
                                            else -> "🥣"
                                        },
                                        fontSize = 28.sp,
                                        modifier = Modifier.padding(end = 16.dp)
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = log.type + " • " + log.amountOrDuration,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = sdfDate.format(Date(log.timestamp)) + " • " + sdfTime.format(Date(log.timestamp)),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                                        )
                                        if (log.foodDetails.isNotEmpty()) {
                                            Text(
                                                text = "Foods: " + log.foodDetails,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                                color = MaterialTheme.colorScheme.tertiary,
                                                modifier = Modifier.padding(top = 2.dp)
                                            )
                                        }
                                        if (log.note.isNotEmpty()) {
                                            Text(
                                                text = log.note,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                                                modifier = Modifier.padding(top = 4.dp)
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteFeedingLog(log) }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // SMART CUSTOM DIET & MEAL PLANNER TAB
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Target Selector
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Select Planning Domain",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Baby Choice
                                Button(
                                    onClick = { plannerTarget = "Baby" },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (plannerTarget == "Baby") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                        contentColor = if (plannerTarget == "Baby") Color.White else MaterialTheme.colorScheme.onBackground
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(44.dp).testTag("select_baby_target_btn")
                                ) {
                                    Icon(Icons.Default.Face, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("👶 Infant Solids", fontWeight = FontWeight.SemiBold)
                                }

                                // Parent Choice
                                Button(
                                    onClick = { plannerTarget = "Parent" },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (plannerTarget == "Parent") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                                        contentColor = if (plannerTarget == "Parent") Color.White else MaterialTheme.colorScheme.onBackground
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f).height(44.dp).testTag("select_parent_target_btn")
                                ) {
                                    Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("🤱 Postpartum Diet", fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    // Options Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            if (plannerTarget == "Baby") {
                                Text(
                                    text = "Plan Weaning With Pediatric Nutritionist AI",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                // Baby Age select chips
                                Text("Baby's age range (months):", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    defaultAgeOptions.forEach { age ->
                                        val isSel = babyAgeInMonths == age
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { babyAgeInMonths = age }
                                                .background(if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                                                .padding(vertical = 10.dp),
                                            color = Color.Transparent
                                        ) {
                                            Text(
                                                text = "$age m",
                                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                                textAlign = TextAlign.Center,
                                                color = if (isSel) Color.White else MaterialTheme.colorScheme.onBackground
                                            )
                                        }
                                    }
                                }

                                // Weaning Preference
                                Text("Solid Food style preference:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    defaultPrefOptions.forEach { opt ->
                                        val checked = feedingPreference == opt
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { feedingPreference = opt }
                                                .background(if (checked) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
                                                .padding(8.dp)
                                        ) {
                                            RadioButton(selected = checked, onClick = { feedingPreference = opt })
                                            Text(
                                                text = opt,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                }

                                // Allergies info
                                OutlinedTextField(
                                    value = allergiesText,
                                    onValueChange = { allergiesText = it },
                                    label = { Text("Allergies / safety list to avoid") },
                                    placeholder = { Text("e.g. Peanuts, soy, fish, or 'None'") },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("allergies_input")
                                )

                                Button(
                                    onClick = {
                                        val ageInt = babyAgeInMonths.toIntOrNull() ?: 6
                                        viewModel.generateMealPlanAction(
                                            ageInt,
                                            feedingPreference,
                                            allergiesText.ifEmpty { "None declared" }
                                        )
                                    },
                                    enabled = !isGeneratingPlan,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("generate_meal_plan_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    if (isGeneratingPlan) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Default.Star, contentDescription = null)
                                            Text("Synthesize Custom Nutrition Guide", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            } else {
                                // PARENT SPECIFIC CONFIG
                                Text(
                                    text = "Plan Nutritional Postpartum Recovery Guide",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text("Recovery stage & goal:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    parentStageOptions.forEach { stageOpt ->
                                        val isSel = parentStage == stageOpt
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .clickable { parentStage = stageOpt }
                                                .background(if (isSel) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent)
                                                .padding(6.dp)
                                        ) {
                                            RadioButton(selected = isSel, onClick = { parentStage = stageOpt })
                                            Text(
                                                text = stageOpt,
                                                style = MaterialTheme.typography.bodyMedium,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                }

                                Text("Dietary restrictions:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                                ) {
                                    parentRestrictionOptions.forEach { restOpt ->
                                        val isSel = parentRestriction == restOpt
                                        FilterChip(
                                            selected = isSel,
                                            onClick = { parentRestriction = restOpt },
                                            label = { Text(restOpt) },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }

                                Text("Meal preparation styles:", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState())
                                ) {
                                    parentPrepOptions.forEach { prepOpt ->
                                        val isSel = parentPrepPrefByText == prepOpt
                                        FilterChip(
                                            selected = isSel,
                                            onClick = { parentPrepPrefByText = prepOpt },
                                            label = { Text(prepOpt) },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                    }
                                }

                                OutlinedTextField(
                                    value = allergiesText,
                                    onValueChange = { allergiesText = it },
                                    label = { Text("Maternal Allergies or sensitivities") },
                                    placeholder = { Text("e.g. Nuts, eggs, shellfish, None") },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("parent_allergies_input")
                                )

                                Button(
                                    onClick = {
                                        viewModel.generateParentMealPlanAction(
                                            parentStage,
                                            parentRestriction,
                                            allergiesText.ifEmpty { "None declared" },
                                            parentPrepPrefByText
                                        )
                                    },
                                    enabled = !isGeneratingPlan,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .testTag("generate_parent_meal_plan_button"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                ) {
                                    if (isGeneratingPlan) {
                                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                    } else {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Icon(Icons.Default.Star, contentDescription = null)
                                            Text("Synthesize Postpartum Recovery Diet", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Display generation outcome
                    if (isGeneratingPlan || mealPlanResult != null) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.05f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f),
                                    RoundedCornerShape(16.dp)
                                )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary
                                    )
                                    Text(
                                        text = if (plannerTarget == "Baby") "AI Custom Infant Meal Blueprint" else "AI Postpartum Diet Blueprint",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.tertiary
                                    )
                                }

                                if (isGeneratingPlan) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
                                    }
                                } else {
                                    Text(
                                        text = mealPlanResult ?: "Empty plan loaded.",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground,
                                        lineHeight = 22.sp
                                    )

                                    Button(
                                        onClick = { viewModel.clearMealPlan() },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.align(Alignment.End)
                                    ) {
                                        Text("Reset / Customise new model")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
