package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.HustleRepository
import com.example.domain.RecommendationEngine
import com.example.domain.UserProfileQuery
import com.example.model.*
import com.example.util.CurrencyFormatter
import com.example.util.SupportedCurrency
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    private lateinit var context: Context
    private lateinit var repository: HustleRepository

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext<Context>()
        repository = HustleRepository(context)
    }

    @Test
    fun `read string from context matches BiasharaHub`() {
        val appName = context.getString(R.string.app_name)
        assertEquals("BiasharaHub", appName)
    }

    @Test
    fun `currency formatter accurately formats KES and USD`() {
        val kesFormatted = CurrencyFormatter.format(15000, SupportedCurrency.KES)
        assertEquals("KSh 15,000", kesFormatted)

        val usdFormatted = CurrencyFormatter.format(120, SupportedCurrency.USD)
        assertEquals("$ 120", usdFormatted)

        val rangeFormatted = CurrencyFormatter.formatRange(5000, 12000, SupportedCurrency.KES)
        assertEquals("KSh 5,000 - 12,000", rangeFormatted)
    }

    @Test
    fun `matchBusinessIdeas returns evaluated ideas with explainability`() {
        val query = UserProfileQuery(
            exactCapitalKes = 10000,
            skillIds = setOf("cooking_baking", "beverages"),
            interestAreas = setOf(InterestArea.FOOD_BEVERAGE)
        )

        val matches = repository.matchBusinessIdeas(query)
        assertTrue("Expected matches to be populated", matches.isNotEmpty())

        val topMatch = matches.first()
        assertTrue("Match score must be positive", topMatch.matchScore > 40)
        assertNotNull("Match explanation should be generated", topMatch.explanation)
        assertNotNull("Feasibility group must be assigned", topMatch.feasibilityGroup)
    }

    @Test
    fun `feasibility classification correctly marks ready now when capital and skill match`() {
        val smokieIdea = repository.businessIdeas.first { it.id == "smokie_pasua" }
        val querySufficient = UserProfileQuery(
            exactCapitalKes = 12000, // min is 5000
            skillIds = setOf("cooking_baking")
        )

        val evaluatedReady = RecommendationEngine.evaluate(smokieIdea, querySufficient, repository.defaultSkills)
        assertEquals(FeasibilityGroup.READY_NOW, evaluatedReady.feasibilityGroup)

        val querySevereDeficit = UserProfileQuery(
            exactCapitalKes = 1000, // min is 5000, severe gap (< 40%)
            skillIds = setOf("cooking_baking")
        )
        val evaluatedFuture = RecommendationEngine.evaluate(smokieIdea, querySevereDeficit, repository.defaultSkills)
        assertEquals(FeasibilityGroup.FUTURE_OPPORTUNITIES, evaluatedFuture.feasibilityGroup)
    }

    @Test
    fun `sci business plan draft serializes to valid JSON`() {
        val idea = repository.businessIdeas.first { it.id == "mitumba_thrift" }
        val draft = SciBusinessPlanDraft.fromIdea(idea)

        val jsonString = draft.toJsonString()
        val json = JSONObject(jsonString)

        assertEquals("SCI BASE COLLECTIONS", json.getString("ecosystem"))
        assertEquals("SCI BUSINESS PLAN", json.getString("destination_app"))
        assertEquals(idea.title, json.getString("business_name"))
        assertTrue(json.getInt("startup_costs") > 0)
    }

    @Test
    fun `6-stage journey tracks progress accurately`() {
        val ideaId = "test_journey_idea"
        val tasks = repository.defaultJourneyTasks
        val initialProgress = HustleJourneyProgress(
            ideaId = ideaId,
            completedTaskIds = setOf("disc_1", "disc_2")
        )

        val percentage = initialProgress.getCompletionPercentage(tasks.size)
        assertTrue("Percentage should be positive", percentage > 0)

        repository.saveJourneyProgress(initialProgress)
        val retrieved = repository.getJourneyProgress(ideaId)

        assertEquals(2, retrieved.completedTaskIds.size)
        assertTrue(retrieved.completedTaskIds.contains("disc_1"))
    }

    @Test
    fun `document library toggles local offline download status`() {
        val docs = repository.getDocumentLibrary()
        assertTrue("Document library must have prepared guides", docs.isNotEmpty())

        val targetDocId = docs.first().id
        val wasDownloaded = repository.toggleDocumentDownload(targetDocId)

        val updatedDocs = repository.getDocumentLibrary()
        val target = updatedDocs.find { it.id == targetDocId }
        assertNotNull(target)
        if (wasDownloaded) {
            assertEquals(DownloadStatus.DOWNLOADED, target!!.downloadStatus)
            assertNotNull(target.localFilePath)
        }
    }

    @Test
    fun `success stories contains verification status and citations`() {
        val stories = repository.successStories
        assertTrue(stories.size >= 3)
        stories.forEach { profile ->
            assertNotNull(profile.name)
            assertNotNull(profile.verificationStatus)
            assertTrue(profile.citationSource.isNotBlank())
            assertTrue(profile.lastVerifiedDate.isNotBlank())
            assertTrue(profile.mentorshipDisclaimer.isNotBlank())
        }
    }

    @Test
    fun `mentorship request persistence saves and retrieves requests`() {
        val request = MentorshipRequest(
            id = "test_req_1",
            mentorName = "Brian Mwangi",
            applicantName = "Kamau Otieno",
            phoneOrEmail = "0712345678",
            selectedSkillOrVenture = "Graphic Design",
            questionOrGoal = "How do I price logo designs for Nairobi businesses?",
            preferredMode = "WhatsApp Call"
        )

        repository.saveMentorshipRequest(request)
        val saved = repository.getMentorshipRequests()

        assertTrue(saved.any { it.id == "test_req_1" && it.applicantName == "Kamau Otieno" })
    }

    @Test
    fun `toggle compare business idea limits to 3`() {
        val idea1 = "mitumba_thrift"
        val idea2 = "smokie_pasua"
        val idea3 = "graphics_sme"
        val idea4 = "mobile_carwash"

        repository.toggleCompareIdea(idea1)
        repository.toggleCompareIdea(idea2)
        repository.toggleCompareIdea(idea3)
        repository.toggleCompareIdea(idea4)

        val compared = repository.getComparedIdeaIds()
        assertTrue("Compare list should not exceed 3 items", compared.size <= 3)
    }
}
