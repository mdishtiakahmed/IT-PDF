package com.itpdf.app

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.itpdf.app.ui.screens.*
import com.itpdf.app.ui.theme.ITPDFTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ITPDFTheme {
                // অ্যাপের নেভিগেশন সিস্টেম চালু করা হলো
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "home") {

        // ১. হোম স্ক্রিন (Home Screen)
        composable("home") {
            HomeScreen(
                onNavigateToTools = { navController.navigate("tools") },
                onNavigateToEditor = { navController.navigate("editor") },
                onNavigateToCv = { 
                     // CV ফিচারটি পরে যোগ করা হবে, আপাতত টোস্ট দেখানো হচ্ছে
                     Toast.makeText(context, "CV Maker Coming Soon!", Toast.LENGTH_SHORT).show()
                },
                onNavigateToMyPdfs = { navController.navigate("files") },
                onNavigateToSettings = { navController.navigate("settings") },
                onNavigateToPro = { 
                     Toast.makeText(context, "Premium Feature!", Toast.LENGTH_SHORT).show() 
                },
                onNavigateToAi = { 
                     Toast.makeText(context, "AI Generator Coming Soon!", Toast.LENGTH_SHORT).show() 
                }
            )
        }

        // ২. টুলস স্ক্রিন (Tools Screen)
        composable("tools") {
            // আপনার ToolsScreen এ যদি প্যারামিটার ভিন্ন থাকে তবে জানাবেন
            ToolsScreen(
                onBack = { navController.popBackStack() } 
            )
        }

        // ৩. ফাইলস স্ক্রিন (My Files Screen)
        composable("files") {
            MyFilesScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ৪. এডিটর স্ক্রিন (Editor Screen)
        composable("editor") {
            EditorScreen(onBack = { navController.popBackStack() })
        }

        // ৫. সেটিংস স্ক্রিন (Settings Screen)
        composable("settings") {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
    }
}
