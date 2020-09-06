package stoyck.vitrina.persistence.data

import android.net.Uri
import com.google.android.apps.muzei.api.provider.Artwork

/**
 * Don't change this if possible
 */
class PersistedPostData(
    val token: String,
    val title: String,
    val byline: String,
    val attribution: String,
    val persistentUri: String,
    var webUri: String
)

fun PersistedPostData.toArtwork(): Artwork = Artwork(
    token = token,
    title = title,
    byline = byline,
    attribution = attribution,
    persistentUri = Uri.parse(persistentUri),
    webUri = Uri.parse(webUri)
)
