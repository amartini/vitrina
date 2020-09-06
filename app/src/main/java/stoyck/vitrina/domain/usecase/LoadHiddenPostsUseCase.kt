package stoyck.vitrina.domain.usecase

import stoyck.vitrina.persistence.VitrinaPersistence
import stoyck.vitrina.persistence.data.PersistedPostData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoadHiddenPostsUseCase @Inject constructor(
    private val persistence: VitrinaPersistence
) {

    operator fun invoke(): List<String?> = persistence.hiddenPosts

}