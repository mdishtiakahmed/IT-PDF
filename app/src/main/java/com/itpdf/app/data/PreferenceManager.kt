package com.itpdf.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class PreferenceManager(private val context: Context) {
    companion object {
        private val IS_PRO_KEY = booleanPreferencesKey("is_pro_user")
    }

    val isProUser: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[IS_PRO_KEY] ?: false
    }

    suspend fun setProStatus(isPro: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[IS_PRO_KEY] = isPro
        }
    }
}