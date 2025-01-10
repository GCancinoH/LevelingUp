
package com.gcancino.levelingup

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import com.gcancino.levelingup.ui.theme.LevelingUpTheme
import com.gcancino.levelingup.utils.Navigation
import com.google.firebase.FirebaseApp

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = applicationContext
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Setting app orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


        enableEdgeToEdge()
        setContent {
            LevelingUpTheme {
                Navigation(context)
            }
        }
    }
}