package com.itpdf.app.domain

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiHelper {
    // এখানে আপনার API Key বসান
    private const val API_KEY = "AIzaSyAg8971YAPbeIOjMbkZj5-lv81PD-H8pyE"

    suspend fun generateContent(prompt: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val generativeModel = GenerativeModel(
                    // modelName = "gemini-pro" বা "gemini-1.5-flash" ব্যবহার করতে পারেন
                    modelName = "gemini-1.5-flash",
                    apiKey = API_KEY
                )

                val response = generativeModel.generateContent(prompt)
                response.text ?: "AI কোনো উত্তর দিতে পারেনি।"
            } catch (e: Exception) {
                "Error: ${e.localizedMessage} - ইন্টারনেট কানেকশন চেক করুন।"
            }
        }
    }
}
