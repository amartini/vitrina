package stoyck.vitrina.domain.usecase

import stoyck.vitrina.persistence.VitrinaPersistence
import stoyck.vitrina.persistence.data.PersistedPostData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddHiddenPostUseCase @Inject constructor(
    private val persistence: VitrinaPersistence
) {

    operator fun invoke(post: String?) {
        persistence.hiddenPosts += post
    }

}