package com.example.looprecord.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.looprecord.MainActivity
import com.example.looprecord.R
import java.io.IOException

/**
 * An [IntentService] subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */

private const val LOG_TAG = "AudioRecordTest"
private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class RecorderService : IntentService("RecorderService") {

    private var recorder: MediaRecorder? = null
    private var fileName: String = ""
    private val CHANNEL = "RECORDER"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent: Intent?) {
        Log.d(LOG_TAG,"in service")
        Log.d(LOG_TAG, fileName)
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        var notification: Notification = Notification.Builder(this, CHANNEL)
            .setContentTitle("Recorder Service")
            .setContentText("Body")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
        startRecording()
        Thread.sleep(5_000)
        stopRecording()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL, "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private fun startRecording() {
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"
        Log.d(LOG_TAG, "actual startRecording")
        Log.d(LOG_TAG, fileName)
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
        Log.d(LOG_TAG, "actual stopRecording")

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
        @RequiresApi(Build.VERSION_CODES.O)
        @JvmStatic
        fun startRecording(context: Context) {
            Log.d(LOG_TAG, "in companion start")
            val intent = Intent(context, RecorderService::class.java)
            context.startForegroundService(intent)
        }
    }

}
