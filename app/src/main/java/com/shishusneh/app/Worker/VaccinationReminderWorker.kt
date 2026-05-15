package com.shishusneh.app.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.*
import java.util.concurrent.TimeUnit

// ─────────────────────────────────────────────────────────────────────────────
//  Vaccination reminder worker  (runs via WorkManager)
// ─────────────────────────────────────────────────────────────────────────────

class VaccinationReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val vaccineName = inputData.getString(KEY_VACCINE_NAME) ?: "Vaccination"
        val disease     = inputData.getString(KEY_DISEASE)      ?: ""

        createChannel()
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("💉 Vaccination Reminder")
            .setContentText("$vaccineName is due today! Prevents: $disease")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText("$vaccineName is due today.\nPrevents: $disease\nVisit your nearest clinic."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        nm.notify(System.currentTimeMillis().toInt(), notif)
        return Result.success()
    }

    private fun createChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID, "Vaccination Reminders", NotificationManager.IMPORTANCE_HIGH
        ).apply { description = "Reminders for upcoming baby vaccinations" }
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID        = "vaccination_reminders"
        const val KEY_VACCINE_NAME  = "vaccine_name"
        const val KEY_DISEASE       = "disease"

        /**
         * Schedule a one-time reminder [delayDays] days from now.
         * Tag = "vax_<vaccineId>" so it can be cancelled individually.
         */
        fun schedule(context: Context, vaccineId: Int, vaccineName: String, disease: String, delayDays: Long) {
            val data = workDataOf(
                KEY_VACCINE_NAME to vaccineName,
                KEY_DISEASE to disease
            )
            val request = OneTimeWorkRequestBuilder<VaccinationReminderWorker>()
                .setInputData(data)
                .setInitialDelay(delayDays, TimeUnit.DAYS)
                .addTag("vax_$vaccineId")
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork("vax_$vaccineId", ExistingWorkPolicy.REPLACE, request)
        }

        fun cancel(context: Context, vaccineId: Int) {
            WorkManager.getInstance(context).cancelAllWorkByTag("vax_$vaccineId")
        }
    }
}
