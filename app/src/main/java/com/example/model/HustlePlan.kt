package com.example.model

enum class JourneyStage(val stepNumber: Int, val displayName: String, val description: String) {
    DISCOVER(1, "1. Discover", "Identify fit and review launch blueprint"),
    EVALUATE(2, "2. Evaluate", "Test unit economics and interview 3 customers"),
    RESEARCH(3, "3. Research", "Scout wholesale suppliers and compare prices"),
    PREPARE(4, "4. Prepare", "Gather capital, permits, and equipment"),
    LAUNCH(5, "5. Launch", "Setup M-PESA Till and fulfill first 10 orders"),
    GROW(6, "6. Grow", "Record cashflow and reinvest 50% profits")
}

data class JourneyTask(
    val id: String,
    val title: String,
    val description: String,
    val stage: JourneyStage,
    val isMandatory: Boolean = false
)

data class HustleJourneyProgress(
    val ideaId: String,
    val completedTaskIds: Set<String> = emptySet(),
    val notes: String = "",
    val targetCapitalKes: Int = 0,
    val savedCapitalKes: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
) {
    fun getCompletionPercentage(totalTasks: Int): Int {
        if (totalTasks == 0) return 0
        return ((completedTaskIds.size.toFloat() / totalTasks) * 100).toInt().coerceIn(0, 100)
    }

    fun getCurrentStage(allTasks: List<JourneyTask>): JourneyStage {
        val stages = JourneyStage.values()
        for (st in stages) {
            val stageTasks = allTasks.filter { it.stage == st }
            val allDone = stageTasks.isNotEmpty() && stageTasks.all { completedTaskIds.contains(it.id) }
            if (!allDone) return st
        }
        return JourneyStage.GROW
    }
}
