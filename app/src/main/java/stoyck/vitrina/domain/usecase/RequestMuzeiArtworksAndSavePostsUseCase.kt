package stoyck.vitrina.domain.usecase

import com.google.android.apps.muzei.api.provider.Artwork
import stoyck.vitrina.persistence.data.toArtwork
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestMuzeiArtworksAndSavePostsUseCase @Inject constructor(
    private val savePostsUseCase: SavePostsUseCase,
    private val loadImagesUseCase: RetrieveLatestImagesUseCase
) {

    suspend operator fun invoke(): List<Artwork> {
        val posts = loadImagesUseCase()
        savePostsUseCase(posts)
        return posts.map{it.toArtwork()}
    }
}