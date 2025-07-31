package com.example.janitiriandroidinternassignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.janitiriandroidinternassignment.data.VitalsDatabase
import com.example.janitiriandroidinternassignment.screen.MainScreen
import com.example.janitiriandroidinternassignment.viewmodel.VitalsViewModel
import com.example.janitiriandroidinternassignment.viewmodel.VitalsViewModelFactory
import com.example.janitiriandroidinternassignment.work.VitalsReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //enableEdgeToEdge()
        val showDialog = intent?.getBooleanExtra("SHOW_LOG_DIALOG", false) ?: false


        val workRequest = PeriodicWorkRequestBuilder<VitalsReminderWorker>(5, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "VitalsReminder",
            androidx.work.ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )




        setContent {
            val context = LocalContext.current

            val db = remember { VitalsDatabase.getDatabase(context) }
            val dao = remember { db.vitalsDao() }

            val viewModel: VitalsViewModel = viewModel(
                factory = VitalsViewModelFactory(dao)
            )

            var forceShowDialog by remember { mutableStateOf(showDialog) }
            MainScreen(viewModel, forceShowDialog)

        }
    }
}

