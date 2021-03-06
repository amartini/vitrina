package stoyck.vitrina

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import stoyck.vitrina.di.TestVitrinaApplication
import stoyck.vitrina.network.RedditService
import stoyck.vitrina.persistence.data.PersistedSubredditData
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class NetworkCallTests {

    @Inject
    lateinit var redditService: RedditService

    @Before
    fun setup() {
        val app =
            InstrumentationRegistry.getInstrumentation()
                .targetContext
                .applicationContext
                    as TestVitrinaApplication
        app.appComponent.inject(this)
    }

    @Test
    fun retrieveAccessToken() {
        val token = runBlocking {
            redditService.retrieveAccessToken()
        }

        // Testing the constant values
        Assert.assertEquals("bearer", token.tokenType)
        Assert.assertEquals("DO_NOT_TRACK_THIS_DEVICE", token.deviceId)
        Assert.assertEquals(3600, token.expiresIn)
        Assert.assertEquals("*", token.scope)
    }

    @Test
    fun subredditRetrievalWorks() {
        val subreddit = runBlocking {
            redditService.retrieveSubreddit("earthporn")
        }

        // Capitalization is fixed
        Assert.assertEquals("EarthPorn", subreddit.displayName)
    }

    @Test
    fun mutlipleSubredditRetrievalWorks() {
        val subreddits = listOf(
            "earthporn",
            "cityporn",
            "spaceporn"
        )

        val subreddit = runBlocking {
            redditService.retrievePosts(subreddits)
        }

        // Capitalization is fixed
        Assert.assertEquals(subreddit.size, 100)

        val sources = subreddit.map { it.subreddit.toLowerCase() }.toSet()
        Assert.assertTrue(
            "Should container more than 1 type of subreddit",
            sources.size > 1 && sources.size <= subreddits.size
        )
    }

    @Test
    fun subredditRecommendationRetrievalWorks() {
        val query = "earthporn"

        val subreddits = runBlocking {
            redditService.retrieveHints(query = query)
        }

        Assert.assertTrue(subreddits.isNotEmpty())

        subreddits.forEach {
            Assert.assertTrue("Subreddit $it", it.name.startsWith(query, ignoreCase = true))
        }
    }

//    @Test
//    fun initialDataCreation() {
//        val data = listOf(
//            "earthporn",
//            "cityporn",
//            "spaceporn"
//        )
//
//        runBlocking {
//            val values = data.map {
//                redditService.retrieveSubreddit(it)
//            }
//            val persisted = values.map { PersistedSubredditData.fromAbout(it) }
//
//            println(values)
//        }
//    }
}