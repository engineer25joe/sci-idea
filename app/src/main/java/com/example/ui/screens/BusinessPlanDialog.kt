package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SciBusinessPlanDraft
import com.example.ui.components.DeveloperFooter
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusinessPlanDialog(
    draft: SciBusinessPlanDraft,
    onSaveDraft: (SciBusinessPlanDraft) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var businessName by remember { mutableStateOf(draft.businessName) }
    var problem by remember { mutableStateOf(draft.problem) }
    var solution by remember { mutableStateOf(draft.solution) }
    var targetMarket by remember { mutableStateOf(draft.targetMarket) }
    var competitors by remember { mutableStateOf(draft.competitors) }
    var productsAndServices by remember { mutableStateOf(draft.productsAndServices) }
    var pricingStrategy by remember { mutableStateOf(draft.pricingStrategy) }
    var marketingPlan by remember { mutableStateOf(draft.marketingPlan) }
    var operationsPlan by remember { mutableStateOf(draft.operationsPlan) }
    var startupCosts by remember { mutableStateOf(draft.startupCostsKes.toString()) }
    var monthlyRevenue by remember { mutableStateOf(draft.monthlyRevenueKes.toString()) }
    var monthlyExpenses by remember { mutableStateOf(draft.monthlyExpensesKes.toString()) }
    var keyRisks by remember { mutableStateOf(draft.keyRisks) }
    var growthStrategy by remember { mutableStateOf(draft.growthStrategy) }

    fun currentDraft(): SciBusinessPlanDraft {
        return draft.copy(
            businessName = businessName,
            problem = problem,
            solution = solution,
            targetMarket = targetMarket,
            competitors = competitors,
            productsAndServices = productsAndServices,
            pricingStrategy = pricingStrategy,
            marketingPlan = marketingPlan,
            operationsPlan = operationsPlan,
            startupCostsKes = startupCosts.toIntOrNull() ?: draft.startupCostsKes,
            monthlyRevenueKes = monthlyRevenue.toIntOrNull() ?: draft.monthlyRevenueKes,
            monthlyExpensesKes = monthlyExpenses.toIntOrNull() ?: draft.monthlyExpensesKes,
            keyRisks = keyRisks,
            growthStrategy = growthStrategy,
            lastModifiedTimestamp = System.currentTimeMillis()
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        containerColor = SurfaceCard,
        modifier = Modifier.testTag("business_plan_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 36.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SCI BASE COLLECTIONS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = KenyaGoldDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Build Business Plan Draft",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = SciBluePrimary
                        )
                    )
                    Text(
                        text = "Pre-configured format for import into SCI BUSINESS PLAN",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = SciBlueContainer),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = "Sync", tint = SciBluePrimary)
                    Text(
                        text = "All fields are pre-populated from your selected Kenyan hustle and can be customized below.",
                        style = MaterialTheme.typography.bodySmall.copy(color = OnSciBlueContainer, fontSize = 11.sp)
                    )
                }
            }

            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text("Business Venture Name") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = problem,
                onValueChange = { problem = it },
                label = { Text("Problem Being Solved") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = solution,
                onValueChange = { solution = it },
                label = { Text("Proposed Solution & Product") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = targetMarket,
                onValueChange = { targetMarket = it },
                label = { Text("Target Customers in Kenya") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = competitors,
                onValueChange = { competitors = it },
                label = { Text("Competitors & Advantage") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = startupCosts,
                    onValueChange = { startupCosts = it },
                    label = { Text("Startup Costs (KSh)") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = monthlyRevenue,
                    onValueChange = { monthlyRevenue = it },
                    label = { Text("Est. Revenue (KSh)") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = pricingStrategy,
                onValueChange = { pricingStrategy = it },
                label = { Text("Pricing Strategy") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = marketingPlan,
                onValueChange = { marketingPlan = it },
                label = { Text("Marketing & Outreach Strategy") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = operationsPlan,
                onValueChange = { operationsPlan = it },
                label = { Text("Operations & Sourcing") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = keyRisks,
                onValueChange = { keyRisks = it },
                label = { Text("Key Risks & Mitigations") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = growthStrategy,
                onValueChange = { growthStrategy = it },
                label = { Text("Growth & Reinvestment Strategy") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        val updated = currentDraft()
                        onSaveDraft(updated)
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("SCI Business Plan JSON", updated.toJsonString())
                        clipboard.setPrimaryClip(clip)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = "Copy")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy JSON", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val updated = currentDraft()
                        onSaveDraft(updated)
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, updated.toJsonString())
                            type = "application/json"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "Export to SCI BUSINESS PLAN"))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SciBluePrimary),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Export")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export Draft", fontSize = 12.sp)
                }
            }

            DeveloperFooter()
        }
    }
}
