package com.newstudio.instagramreelcounter.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.newstudio.instagramreelcounter.AppUsageInfo

class UsageViewModel : ViewModel() {

    private val _usageList = MutableStateFlow<List<AppUsageInfo>>(emptyList())

    val usageList: StateFlow<List<AppUsageInfo>> get() = _usageList

    fun updateUsage(newEntry: AppUsageInfo) {
        val updatedList = _usageList.value.toMutableList()
        val existing = updatedList.find { it.packageName == newEntry.packageName }

        if (existing != null) {
            existing.totalTimeMillis += newEntry.totalTimeMillis
        } else {
            updatedList.add(newEntry)
        }

        _usageList.value = updatedList
    }

    fun clearUsageData() {
        _usageList.value = emptyList()
    }
}
