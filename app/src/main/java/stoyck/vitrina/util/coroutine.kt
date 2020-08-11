package stoyck.vitrina.util

import android.content.Context
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


suspend fun uiThread(action: suspend () -> Unit) {
    withContext(Dispatchers.Main) {
        action()
    }
}


suspend fun showToast(context: Context, text: String) {
    uiThread {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }
}


suspend fun showToast(context: Context, stringRes: Int) {
    uiThread {
        Toast.makeText(
            context,
            stringRes,
            Toast.LENGTH_LONG
        ).show()
    }
}