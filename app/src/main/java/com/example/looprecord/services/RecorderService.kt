package com.example.looprecord.services

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.util.Log
import java.io.IOException

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
private const val START_RECORDING = "START"
private const val STOP_RECORDING = "STOP"

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecorderService : IntentService("RecorderService") {

    private var recorder: MediaRecorder? = null
    private var fileName: String = ""

    override fun onHandleIntent(intent: Intent?) {
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        when (intent?.action) {
            START_RECORDING -> {
                startRecording()
            }
            STOP_RECORDING -> {
                stopRecording()
            }
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    companion object {
        /**
         * Starts this service to perform action Foo with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun startRecording(context: Context) {
            val intent = Intent(context, RecorderService::class.java).apply {
                action = START_RECORDING
            }
            context.startService(intent)
        }

        /**
         * Starts this service to perform action Baz with the given parameters. If
         * the service is already performing a task this action will be queued.
         *
         * @see IntentService
         */
        // TODO: Customize helper method
        @JvmStatic
        fun stopRecording(context: Context) {
            val intent = Intent(context, RecorderService::class.java).apply {
                action = STOP_RECORDING
            }
            context.startService(intent)
        }
    }

}
