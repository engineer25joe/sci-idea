package com.example.model

import org.json.JSONObject

data class SciBusinessPlanDraft(
    val planId: String,
    val sourceIdeaId: String,
    val businessName: String,
    val problem: String,
    val solution: String,
    val targetMarket: String,
    val competitors: String,
    val productsAndServices: String,
    val pricingStrategy: String,
    val marketingPlan: String,
    val operationsPlan: String,
    val startupCostsKes: Int,
    val monthlyRevenueKes: Int,
    val monthlyExpensesKes: Int,
    val keyRisks: String,
    val growthStrategy: String,
    val currency: String = "KES",
    val ecosystem: String = "SCI BASE COLLECTIONS",
    val destinationApp: String = "SCI BUSINESS PLAN",
    val createdTimestamp: Long = System.currentTimeMillis(),
    val lastModifiedTimestamp: Long = System.currentTimeMillis()
) {
    fun toJsonString(): String {
        return JSONObject().apply {
            put("schema_version", "1.0.0")
            put("ecosystem", ecosystem)
            put("destination_app", destinationApp)
            put("plan_id", planId)
            put("source_idea_id", sourceIdeaId)
            put("business_name", businessName)
            put("problem", problem)
            put("solution", solution)
            put("target_market", targetMarket)
            put("competitors", competitors)
            put("products_and_services", productsAndServices)
            put("pricing_strategy", pricingStrategy)
            put("marketing_plan", marketingPlan)
            put("operations_plan", operationsPlan)
            put("startup_costs", startupCostsKes)
            put("monthly_revenue", monthlyRevenueKes)
            put("monthly_expenses", monthlyExpensesKes)
            put("key_risks", keyRisks)
            put("growth_strategy", growthStrategy)
            put("currency", currency)
            put("created_timestamp", createdTimestamp)
            put("last_modified_timestamp", lastModifiedTimestamp)
        }.toString(2)
    }

    companion object {
        fun fromIdea(idea: BusinessIdea): SciBusinessPlanDraft {
            return SciBusinessPlanDraft(
                planId = "sci_plan_${idea.id}_${System.currentTimeMillis()}",
                sourceIdeaId = idea.id,
                businessName = idea.title,
                problem = idea.problemBeingSolved.ifBlank { "Unmet local demand in community estates." },
                solution = idea.proposedSolution.ifBlank { idea.summary },
                targetMarket = idea.targetCustomers.joinToString(", ").ifBlank { "Local estate residents, students & workers" },
                competitors = idea.competitorResearchGuidance.ifBlank { "Traditional street vendors and informal traders" },
                productsAndServices = "${idea.shengTitle}: Core services with reliable quality and doorstep convenience",
                pricingStrategy = idea.pricingConsiderations.ifBlank { "Competitive market pricing with M-PESA payment" },
                marketingPlan = idea.marketingStrategy.ifBlank { "WhatsApp groups, estate posters, TikTok reels and word of mouth" },
                operationsPlan = idea.launchSteps.take(3).joinToString("; "),
                startupCostsKes = idea.capitalMinKes,
                monthlyRevenueKes = idea.estimatedDailyRevenueKes.first * 26,
                monthlyExpensesKes = (idea.estimatedDailyRevenueKes.first * 26) - idea.estimatedMonthlyProfitKes.first,
                keyRisks = idea.risksAndMitigations.firstOrNull()?.let { "${it.first}: ${it.second}" } ?: "Cashflow management",
                growthStrategy = "Reinvest 50% profits, formalize on eCitizen, expand delivery radius"
            )
        }
    }
}
