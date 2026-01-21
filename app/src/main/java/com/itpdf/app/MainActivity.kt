package com.itpdf.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itpdf.app.navigation.Screen
import com.itpdf.app.ui.screens.*
import com.itpdf.app.ui.theme.ITPDFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITPDFTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ITPdfApp()
                }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun ITPdfApp() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) { 
            HomeScreen(
                onNavigateToTools = { navController.navigate(Screen.Tools.route) },
                onNavigateToEditor = { navController.navigate(Screen.Editor.route) },
                onNavigateToCv = { navController.navigate(Screen.CvBuilder.route) },
                onNavigateToMyPdfs = { navController.navigate(Screen.MyPdfs.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToPro = { navController.navigate(Screen.ProUpgrade.route) },
                onNavigateToAi = { navController.navigate(Screen.AiText.route) }
            ) 
        }
        composable(Screen.Tools.route) { 
            ToolsScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.Editor.route) { 
            EditorScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.CvBuilder.route) { 
            CvBuilderScreen(onBack = { navController.popBackStack() }) 
        }
        composable(Screen.AiText.route) {
            AiTextScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.MyPdfs.route) {
            MyPdfsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.ProUpgrade.route) {
            ProUpgradeScreen(onBack = { navController.popBackStack() })
        }
    }
}