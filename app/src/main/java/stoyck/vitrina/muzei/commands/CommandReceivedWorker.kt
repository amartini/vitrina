package stoyck.vitrina.muzei.commands

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.work.*
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import stoyck.vitrina.BuildConfig
import stoyck.vitrina.R
import stoyck.vitrina.VitrinaApplication
import stoyck.vitrina.domain.usecase.HideArtworkUseCase
import stoyck.vitrina.domain.usecase.SaveArtworkOnDiskUseCase
import stoyck.vitrina.util.showToast
import java.io.File
import java.util.concurrent.Executors
import javax.inject.Inject

class CommandReceivedWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    data class Params(
        val command: String,
        val existingFile: File,
        val uri: Uri,
        val title: String,
        val byLine: String,
        val attribution: String,
        val id: String?
    )

    companion object {
        private const val KEY_PARAMS = "KEY_PARAMS"

        private val SINGLE_THREAD_CONTEXT by lazy {
            Executors.newSingleThreadExecutor { target -> Thread(target, "VitrinaArt") }
                .asCoroutineDispatcher()
        }

        internal fun enqueueLoad(
            context: Context,
            gson: Gson,
            params: Params
        ) {
            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(
                OneTimeWorkRequestBuilder<CommandReceivedWorker>()
                    .setInputData(
                        Data.Builder()
                            .putString(KEY_PARAMS, gson.toJson(params))
                            .build()
                    )
                    .build()
            )
        }
    }

    @Inject
    lateinit var saveArtworkOnDisk: SaveArtworkOnDiskUseCase

    @Inject
    lateinit var hideArtwork: HideArtworkUseCase

    @Inject
    lateinit var gson: Gson

    init {
        (context.applicationContext as VitrinaApplication)
            .appComponent
            .inject(this)
    }

    override suspend fun doWork(): Result = withContext(SINGLE_THREAD_CONTEXT) {
        if (BuildConfig.DEBUG) {
            showToast(
                applicationContext,
                "Debug: " + applicationContext.getString(R.string.action_save_worker_start),
                duration = Toast.LENGTH_SHORT
            )
        }

        val paramsRaw = inputData.getString(KEY_PARAMS)

        if (paramsRaw == null) {
            FirebaseCrashlytics.getInstance()
                .recordException(RuntimeException("No params received"))
            return@withContext Result.failure()
        }

        val params =
            gson.fromJson(paramsRaw, Params::class.java)

        when(params.command) {
            VitrinaProtocolConstants.COMMAND_SAVE_KEY -> saveArtworkOnDisk(params)
            VitrinaProtocolConstants.COMMAND_HIDE_KEY -> hideArtwork(params.id)
        }

        Result.success()
    }
}