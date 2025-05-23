package com.newstudio.instagramreelcounter.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.newstudio.instagramreelcounter.DataStoreManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReelCardViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application.applicationContext)

    val instagramReel: StateFlow<Long> = dataStoreManager.instagramReelFlow.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        0L
    )

    val youtubeReel: StateFlow<Long> = dataStoreManager.youtubeReelFlow.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        0L
    )

    val snapchatReel: StateFlow<Long> = dataStoreManager.snapchatReelFlow.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        0L
    )

    val facebookReel: StateFlow<Long> = dataStoreManager.facebookReelFlow.stateIn(
        viewModelScope,
        SharingStarted.Companion.WhileSubscribed(5000),
        0L
    )

    // Functions to update counts
    fun updateInstagramReel(newCount: Long) {
        viewModelScope.launch {
            dataStoreManager.saveInstagramReel(newCount)
        }
    }

    fun updateYoutubeReel(newCount: Long) {
        viewModelScope.launch {
            dataStoreManager.saveYoutubeReel(newCount)
        }
    }

    fun updateSnapchatReel(newCount: Long) {
        viewModelScope.launch {
            dataStoreManager.saveSnapchatReel(newCount)
        }
    }

    fun updateFacebookReel(newCount: Long) {
        viewModelScope.launch {
            dataStoreManager.saveFacebookReel(newCount)
        }
    }
}