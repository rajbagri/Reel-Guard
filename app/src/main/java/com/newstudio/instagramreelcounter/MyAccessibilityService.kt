package com.newstudio.instagramreelcounter

import android.accessibilityservice.AccessibilityService
import android.os.SystemClock
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class MyAccessibilityService : AccessibilityService() {

    private lateinit var dataStoreManager: DataStoreManager
    private var lastScrollTime: Long = 0
    private var instagramReelCount: Long = 0
    private var youtubeShortsCount: Long = 0
    private var isInInstagramReel = false
    private var isInYoutubeShorts = false

    private val serviceScope = CoroutineScope(Job() + Dispatchers.IO)

    override fun onServiceConnected() {
        super.onServiceConnected()
        dataStoreManager = DataStoreManager(applicationContext)

        serviceScope.launch {
            instagramReelCount = dataStoreManager.instagramReelFlow.firstOrNull() ?: 0L
            youtubeShortsCount = dataStoreManager.youtubeReelFlow.firstOrNull() ?: 0L
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        val dataStore = DataStoreManager(applicationContext)

        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_SCROLLED -> {
                val rootNode = rootInActiveWindow
                if (rootNode != null) {
                    when (event.packageName) {
                        "com.instagram.android" -> {
                            isInInstagramReel = isInInstagramReelsTab(rootNode)
                        }
                        "com.google.android.youtube" -> {
                            isInYoutubeShorts = isInYoutubeShortsTab(rootNode)
                            Log.d("bool", isInYoutubeShorts.toString())
                        }
                    }
                    rootNode.recycle()
                }

                val currentTime = SystemClock.uptimeMillis()

                if (isInInstagramReel && currentTime - lastScrollTime > 700) {
                    instagramReelCount++
                    serviceScope.launch {
                        dataStoreManager.saveInstagramReel(instagramReelCount)
                    }
                    lastScrollTime = currentTime
                    Log.d("INSTAGRAM_REELS", "Reel count: $instagramReelCount")
                }



            }

            AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED -> {
                val rootNode = rootInActiveWindow
                if (rootNode != null) {
                    when (event.packageName) {
                        "com.google.android.youtube" -> {
                            isInYoutubeShorts = isInYoutubeShortsTab(rootNode)
                        }
                    }
                    rootNode.recycle()
                }

                val currentTime = SystemClock.uptimeMillis()

                if (isInYoutubeShorts && currentTime - lastScrollTime > 500) {
                    youtubeShortsCount++
                    serviceScope.launch {
                        dataStoreManager.saveYoutubeReel(youtubeShortsCount)
                    }
                    lastScrollTime = currentTime
                    Log.d("YOUTUBE_SHORTS", "Shorts count: $youtubeShortsCount")
                }
            }


            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED -> {
                Log.d("WINDOW_CHANGE", "Changed: ${event.packageName} - ${event.className}")
            }
        }
    }

    private fun isInInstagramReelsTab(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""
        val text = node.text?.toString()?.lowercase() ?: ""

        val likeNode = node.findAccessibilityNodeInfosByText("Like").firstOrNull()
        val commentNode = node.findAccessibilityNodeInfosByText("Comment").firstOrNull()
        val sendNode = node.findAccessibilityNodeInfosByText("Send").firstOrNull()
        val isValid = likeNode != null && commentNode != null && sendNode != null
        Log.d("sdf", isValid.toString())
        Log.d("tabName", contentDesc)

        if (!isValid && contentDesc.contains("reel") && !contentDesc.contains("suggested") && !contentDesc.contains("reels")){
            CoroutineScope(Dispatchers.IO).launch {
                val limit = dataStoreManager.getReelLimit("Instagram").first()
                Log.d("instagramCount", instagramReelCount.toString())
                Log.d("limit", limit.toString())
                if (instagramReelCount > limit && limit > 0) {
                    Log.d("we", "wearehere")
                    performGlobalAction(GLOBAL_ACTION_BACK) // ⬅️ Automatically press back
                }
            }
            return true
        }


        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            val found = isInInstagramReelsTab(child)
            child?.recycle()
            if (found) return true
        }

        return false
    }

    private fun isInYoutubeShortsTab(node: AccessibilityNodeInfo?): Boolean {
        if (node == null) return false

        // Look for nodes that are selected and represent "Shorts" tab
        val contentDesc = node.contentDescription?.toString()?.lowercase() ?: ""
        val text = node.text?.toString()?.lowercase() ?: ""
        val className = node.className?.toString()?.lowercase() ?: ""

        if ((contentDesc.contains("shorts") || text.contains("shorts")) && node.isSelected) {
            Log.d("YoutubeTab", "Selected Shorts tab via: $contentDesc $text")
            return true
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            val found = isInYoutubeShortsTab(child)
            child?.recycle()
            if (found) return true
        }

        return false
    }

    override fun onInterrupt() {}
}
