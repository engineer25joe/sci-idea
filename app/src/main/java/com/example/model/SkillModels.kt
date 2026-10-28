package com.example.model

enum class SkillCategory(val displayName: String, val iconName: String) {
    DIGITAL_TECH("Technology", "computer"),
    VOCATIONAL_TRADES("Vocational & Trades", "build"),
    AGRIBUSINESS("Agriculture", "agriculture"),
    FOOD_HOSPITALITY("Food & Beverage", "restaurant"),
    SERVICES_LOGISTICS("Services", "local_shipping"),
    BEAUTY_FASHION("Beauty & Fashion", "face"),
    RETAIL_COMMERCE("Retail & Trade", "storefront"),
    MANUFACTURING("Manufacturing", "precision_manufacturing")
}

data class Skill(
    val id: String,
    val name: String,
    val category: SkillCategory,
    val description: String = ""
)

enum class InterestArea(val displayName: String, val shengLabel: String) {
    ONLINE_FREELANCING("Digital & Online", "Tech & Digital"),
    FOOD_BEVERAGE("Food & Catering", "Chakula na Snacks"),
    FARMING_LIVESTOCK("Agribusiness & Farming", "Ukilimo & Mifugo"),
    FASHION_BEAUTY("Fashion & Beauty Care", "Mitumba & Urembo"),
    LOGISTICS_TRANSPORT("Transport & Delivery", "Boda & Deliveries"),
    CRAFTS_FABRICATION("Handicrafts & Fabrication", "Fundi & Crafts"),
    CLEANING_MAINTENANCE("Cleaning & Maintenance", "Usafi & Repairs"),
    EVENTS_ENTERTAINMENT("Events & Media", "Entertainment & Content")
}

enum class CapitalTier(val label: String, val maxKes: Int, val description: String) {
    MICRO("Under KES 5K", 5000, "Bootstrap with minimal savings"),
    LOW("KES 5K - 20K", 20000, "Accessible with Hustler Fund or Chama"),
    MEDIUM("KES 20K - 50K", 50000, "Small commercial equipment/stock"),
    EXPANDED("KES 50K - 150K+", 150000, "Formal shop, greenhouse or vehicle lease")
}

enum class EnvironmentType(val label: String) {
    URBAN("Nairobi & Cities"),
    PERI_URBAN("Town Centers & Estates"),
    RURAL("Counties & Rural Towns"),
    ONLINE("Remote / Online Anywhere")
}

enum class BusinessModelType(val label: String) {
    ONLINE("Online Only"),
    OFFLINE("Physical / Field Only"),
    HYBRID("Hybrid (Online + Physical)")
}

enum class RiskLevel(val label: String) {
    LOW("Low Risk (Minimal Loss)"),
    MEDIUM("Medium Risk (Manageable)"),
    HIGH("High Risk (High Return)")
}

enum class TimeCommitment(val label: String) {
    PART_TIME("Part-Time (10-20 hrs/wk)"),
    FULL_TIME("Full-Time (40+ hrs/wk)"),
    FLEXIBLE("Flexible / Weekends")
}

enum class EquipmentResource(val id: String, val label: String) {
    SMARTPHONE("smartphone", "Smartphone (4G/5G)"),
    LAPTOP("laptop", "Laptop / Desktop PC"),
    MOTORBIKE("motorbike", "Motorbike / Bicycle"),
    KITCHEN("kitchen", "Kitchen & Basic Utensils"),
    HAND_TOOLS("hand_tools", "Basic Hand Tools"),
    SEWING_MACHINE("sewing_machine", "Sewing Machine"),
    LAND_SPACE("land_space", "Backyard / Farming Space")
}

enum class FeasibilityGroup(val label: String, val emoji: String, val badgeColorHex: Long) {
    READY_NOW("Ready Now", "🟢", 0xFF16A34A),
    PREPARATION_NEEDED("Preparation Needed", "🟡", 0xFFCA8A04),
    FUTURE_OPPORTUNITIES("Future Opportunities", "🔵", 0xFF0284C7)
}
