package com.newstudio.instagramreelcounter

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore("reel_counts")
        val INSTAGRAM_REEL_KEY = longPreferencesKey("instagram_reel")
        val YOUTUBE_REEL_KEY = longPreferencesKey("youtube_reel")
        val SNAPCHAT_REEL_KEY = longPreferencesKey("snapchat_reel")
        val FACEBOOK_REEL_KEY = longPreferencesKey("facebook_reel")
    }

    val instagramReelFlow: Flow<Long> = context.dataStore.data
        .map { preferences -> preferences[INSTAGRAM_REEL_KEY] ?: 0L }

    val youtubeReelFlow: Flow<Long> = context.dataStore.data
        .map { preferences -> preferences[YOUTUBE_REEL_KEY] ?: 0L }

    val snapchatReelFlow: Flow<Long> = context.dataStore.data
        .map { preferences -> preferences[SNAPCHAT_REEL_KEY] ?: 0L }

    val facebookReelFlow: Flow<Long> = context.dataStore.data
        .map { preferences -> preferences[FACEBOOK_REEL_KEY] ?: 0L }

    suspend fun saveInstagramReel(count: Long) {
        context.dataStore.edit { preferences ->
            preferences[INSTAGRAM_REEL_KEY] = count
        }
    }

    suspend fun saveYoutubeReel(count: Long) {
        context.dataStore.edit { preferences ->
            preferences[YOUTUBE_REEL_KEY] = count
        }
    }

    suspend fun saveSnapchatReel(count: Long) {
        context.dataStore.edit { preferences ->
            preferences[SNAPCHAT_REEL_KEY] = count
        }
    }

    suspend fun saveFacebookReel(count: Long) {
        context.dataStore.edit { preferences ->
            preferences[FACEBOOK_REEL_KEY] = count
        }
    }


    suspend fun saveAppLimit(packageName: String, hours: Int, minutes: Int) {
        val key = stringPreferencesKey("${packageName}_limit")
        context.dataStore.edit { preferences ->
            preferences[key] = "$hours:$minutes"
        }
    }

    suspend fun saveReelLimit(appName : String, reelLimit : Int){
        val key = stringPreferencesKey("${appName}_limit")
        context.dataStore.edit { preferences ->
            preferences[key] = "${reelLimit}"
        }
    }

    fun getReelLimit(appName: String): Flow<Int> {
        val key = stringPreferencesKey("${appName}_limit")
        return context.dataStore.data.map { preferences ->
            preferences[key]?.toIntOrNull() ?: 0
        }
    }


    fun getAppLimit(packageName: String): Flow<Pair<Int, Int>> {
        val key = stringPreferencesKey("${packageName}_limit")
        return context.dataStore.data.map { preferences ->
            preferences[key]?.let { saved ->
                val (h, m) = saved.split(":").map { it.toIntOrNull() ?: 0 }
                h to m
            } ?: (0 to 0)
        }
    }

}
