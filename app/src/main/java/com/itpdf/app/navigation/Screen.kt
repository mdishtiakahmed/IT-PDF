package com.itpdf.app.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Tools : Screen("tools")
    object Editor : Screen("editor")
    object CvBuilder : Screen("cv_builder")
    object AiText : Screen("ai_text")
    object MyPdfs : Screen("my_pdfs")
    object Settings : Screen("settings")
    object ProUpgrade : Screen("pro_upgrade")
}