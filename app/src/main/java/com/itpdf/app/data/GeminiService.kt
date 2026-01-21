package com.itpdf.app.data

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GeminiService {
    // NOTE: Hardcoded for demo. In production, use BuildConfig.
    private const val API_KEY = "AIzaSyClB3oy5L_gkjJI0s6_ky12QjDBrnPcCmY" 
    
    private val model = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = API_KEY
    )

    suspend fun generateText(prompt: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = model.generateContent(prompt)
            Result.success(response.text ?: "No response generated.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun improveText(input: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = "Improve the grammar, tone, and professionalism of this text. Maintain the original language (Bangla or English): \n\n$input"
            val response = model.generateContent(prompt)
            Result.success(response.text ?: input)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun generateCvSection(role: String, section: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val prompt = "Write a professional CV section: '$section' for the role of '$role'. Use bullet points. Be concise and impactful."
            val response = model.generateContent(prompt)
            Result.success(response.text ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}