package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.DeveloperFooter
import com.example.ui.components.KesTag
import com.example.ui.theme.*
import com.example.viewmodel.BiasharaUiState
import com.example.viewmodel.BiasharaViewModel
import kotlin.math.roundToInt

@Composable
fun CalculatorScreen(
    uiState: BiasharaUiState,
    viewModel: BiasharaViewModel,
    modifier: Modifier = Modifier
) {
    var scenarioMode by remember { mutableStateOf(1) } // 0 = Low (0.7x), 1 = Moderate (1.0x), 2 = High (1.4x)
    val multiplier = when (scenarioMode) {
        0 -> 0.7f
        2 -> 1.4f
        else -> 1.0f
    }

    var workingDaysPerMonth by remember { mutableStateOf(26) }

    // Inputs
    var dailyUnitsInput by remember(uiState.calcDailyUnits) {
        mutableStateOf(uiState.calcDailyUnits.toString())
    }
    var unitPriceInput by remember(uiState.calcUnitPriceKes) {
        mutableStateOf(uiState.calcUnitPriceKes.toString())
    }
    var unitCostInput by remember(uiState.calcUnitCostKes) {
        mutableStateOf(uiState.calcUnitCostKes.toString())
    }
    var dailyOverheadInput by remember(uiState.calcDailyOverheadKes) {
        mutableStateOf(uiState.calcDailyOverheadKes.toString())
    }

    // Itemized Startup Capital Breakdown
    var equipCost by remember { mutableStateOf("6000") }
    var stockCost by remember { mutableStateOf("4500") }
    var rentSpaceCost by remember { mutableStateOf("2000") }
    var permitsCost by remember { mutableStateOf("1500") }
    var transportCost by remember { mutableStateOf("1000") }

    val totalStartupCapital = (equipCost.toIntOrNull() ?: 0) +
            (stockCost.toIntOrNull() ?: 0) +
            (rentSpaceCost.toIntOrNull() ?: 0) +
            (permitsCost.toIntOrNull() ?: 0) +
            (transportCost.toIntOrNull() ?: 0)

    val baseUnits = dailyUnitsInput.toIntOrNull() ?: 30
    val effectiveUnits = (baseUnits * multiplier).roundToInt()
    val price = unitPriceInput.toIntOrNull() ?: 120
    val cost = unitCostInput.toIntOrNull() ?: 60
    val dailyOverhead = dailyOverheadInput.toIntOrNull() ?: 400

    // Calculations
    val dailyRevenue = effectiveUnits * price
    val dailyDirectCosts = effectiveUnits * cost
    val dailyGrossProfit = (dailyRevenue - dailyDirectCosts).coerceAtLeast(0)
    val dailyOperatingProfit = (dailyGrossProfit - dailyOverhead).coerceAtLeast(0)
    val dailyNetProfit = dailyOperatingProfit

    val monthlyRevenue = dailyRevenue * workingDaysPerMonth
    val monthlyGrossProfit = dailyGrossProfit * workingDaysPerMonth
    val monthlyOperatingProfit = dailyOperatingProfit * workingDaysPerMonth

    val paybackDays = if (dailyOperatingProfit > 0) {
        (totalStartupCapital.toFloat() / dailyOperatingProfit).roundToInt().coerceAtLeast(1)
    } else 999

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("calculator_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header with SCI BASE Ecosystem Accent
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(SciBluePrimary, SciBlueDark)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 22.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = SciRedAccent,
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "SCI BASE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "Kikokotoo cha Biashara",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = KenyaGold,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Kenyan Profit & Unit Economics",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Simulate sales volumes, direct product costs, and monthly cashflow in KSh to identify your break-even point before spending a shilling.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.White.copy(alpha = 0.9f),
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Scenario Selector (Low, Moderate, High)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Illustrative Sales Scenario:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            Triple(0, "Low Volume", "70% baseline"),
                            Triple(1, "Moderate", "100% baseline"),
                            Triple(2, "Peak / High", "140% baseline")
                        ).forEach { (idx, label, sub) ->
                            val isSelected = scenarioMode == idx
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) SciBluePrimary else SurfaceVariantWarm,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { scenarioMode = idx }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else TextPrimary
                                        )
                                    )
                                    Text(
                                        text = sub,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            color = if (isSelected) Color.White.copy(alpha = 0.8f) else TextSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Financial Projection Summary Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SciBlueContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Financial Projections ($workingDaysPerMonth Days/Mo)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSciBlueContainer
                            )
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = KenyaGreenPrimary
                        ) {
                            Text(
                                text = "KSh CURRENCY",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KesTag(
                            amountText = "KSh ${"%,d".format(dailyOperatingProfit)}",
                            label = "Daily Operating Profit",
                            color = KenyaGreenPrimary,
                            modifier = Modifier.weight(1f)
                        )

                        KesTag(
                            amountText = "KSh ${"%,d".format(monthlyOperatingProfit)}",
                            label = "Monthly Operating Net",
                            color = SciBluePrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        KesTag(
                            amountText = if (paybackDays > 365) "Over 1 year" else "$paybackDays Days",
                            label = "Capital Break-Even",
                            color = KenyaGoldDark,
                            modifier = Modifier.weight(1f)
                        )

                        KesTag(
                            amountText = "KSh ${"%,d".format(monthlyRevenue)}",
                            label = "Monthly Turnover",
                            color = Color(0xFF1E293B),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "*Revenue = ${effectiveUnits} units × KSh ${price} = KSh ${"%,d".format(dailyRevenue)}/day | Direct Costs = KSh ${"%,d".format(dailyDirectCosts)}/day | Daily Overhead = KSh ${"%,d".format(dailyOverhead)}/day",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 10.sp)
                    )
                }
            }
        }

        // Daily Operating Variables Input Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Unit Sales & Price Assumptions",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = dailyUnitsInput,
                            onValueChange = {
                                dailyUnitsInput = it
                                it.toIntOrNull()?.let { v -> viewModel.updateCalculator(units = v) }
                            },
                            label = { Text("Base Units / Day") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_units_input")
                        )

                        OutlinedTextField(
                            value = unitPriceInput,
                            onValueChange = {
                                unitPriceInput = it
                                it.toIntOrNull()?.let { v -> viewModel.updateCalculator(unitPrice = v) }
                            },
                            label = { Text("Selling Price (KSh)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_price_input")
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = unitCostInput,
                            onValueChange = {
                                unitCostInput = it
                                it.toIntOrNull()?.let { v -> viewModel.updateCalculator(unitCost = v) }
                            },
                            label = { Text("Direct Cost/Unit (KSh)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_cost_input")
                        )

                        OutlinedTextField(
                            value = dailyOverheadInput,
                            onValueChange = {
                                dailyOverheadInput = it
                                it.toIntOrNull()?.let { v -> viewModel.updateCalculator(overhead = v) }
                            },
                            label = { Text("Daily Overhead (KSh)") },
                            placeholder = { Text("Transport, airtime") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_overhead_input")
                        )
                    }
                }
            }
        }

        // Editable Startup Capital Itemization Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Itemized Startup Costs",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Total: KSh ${"%,d".format(totalStartupCapital)}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = SciBluePrimary
                            )
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = equipCost,
                            onValueChange = { equipCost = it },
                            label = { Text("Equipment (KSh)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = stockCost,
                            onValueChange = { stockCost = it },
                            label = { Text("Opening Stock (KSh)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = rentSpaceCost,
                            onValueChange = { rentSpaceCost = it },
                            label = { Text("Rent/Space (KSh)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        OutlinedTextField(
                            value = permitsCost,
                            onValueChange = { permitsCost = it },
                            label = { Text("Permits/Health (KSh)") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    OutlinedTextField(
                        value = transportCost,
                        onValueChange = { transportCost = it },
                        label = { Text("Initial Transport & Packaging (KSh)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }
        }

        // Transparency Disclaimer Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceVariantWarm)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "⚠️ Financial Model Disclaimer:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = KenyaGoldDark
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "All projections and profit figures above are mathematical models based on user-entered assumptions. They do not constitute a guarantee of actual business performance, sales volume, or customer retention. Business success in Kenya depends on execution, customer relations, market demand, and consistency.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }

        // Developer Footer
        item {
            DeveloperFooter()
        }
    }
}
