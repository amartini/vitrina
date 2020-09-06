package stoyck.vitrina.domain.usecase

import android.content.Context
import com.google.android.apps.muzei.api.provider.ProviderContract
import stoyck.vitrina.BuildConfig
import stoyck.vitrina.persistence.data.toArtwork
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class HideArtworkUseCase @Inject constructor(
    private val context: Context,
    private val savePosts: SavePostsUseCase,
    private val addHiddenPost: AddHiddenPostUseCase,
    private val loadPosts: LoadPostsUseCase
) {

    suspend operator fun invoke(id: String?) {
        val previousPosts = loadPosts()
        val posts = previousPosts.filter { it.token != id }
        savePosts(posts)
        addHiddenPost(id)
        val provider = ProviderContract
            .getProviderClient(context.applicationContext, BuildConfig.VITRINA_AUTHORITY)
        provider.setArtwork(posts.map{it.toArtwork()})
    }

}