package stoyck.vitrina.domain.preferences

data class PreferencesData(
    val isOver18: Boolean,
    val shuffle: Boolean
) {

    companion object {
        val DEFAULT = PreferencesData(isOver18 = false, shuffle = true)
    }

}
