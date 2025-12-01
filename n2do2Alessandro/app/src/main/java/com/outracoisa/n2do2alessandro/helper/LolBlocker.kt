package com.outracoisa.n2do2alessandro.helper

import android.content.Context
import android.content.SharedPreferences

object LolBlocker {
    private const val PREFS_NAME = "JogatinasPrefs"
    private const val KEY_BLOCK_UNTIL = "lol_block_until"
    private const val BLOCK_DURATION_MS = 24 * 60 * 60 * 1000L // 24 horas
    
    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    fun isGameNameSuspicious(gameName: String): Boolean {
        val lowerName = gameName.lowercase().trim()
        return lowerName.contains("league") || 
               lowerName.contains("lol") ||
               lowerName.contains("league of legends") ||
               lowerName.contains("riot")
    }
    
    fun blockUser(context: Context) {
        val blockUntil = System.currentTimeMillis() + BLOCK_DURATION_MS
        getPrefs(context).edit()
            .putLong(KEY_BLOCK_UNTIL, blockUntil)
            .apply()
    }
    
    fun isUserBlocked(context: Context): Boolean {
        val blockUntil = getPrefs(context).getLong(KEY_BLOCK_UNTIL, 0L)
        return System.currentTimeMillis() < blockUntil
    }
    
    fun getBlockTimeRemaining(context: Context): Long {
        val blockUntil = getPrefs(context).getLong(KEY_BLOCK_UNTIL, 0L)
        val remaining = blockUntil - System.currentTimeMillis()
        return if (remaining > 0) remaining else 0L
    }
    
    fun formatBlockTime(timeMs: Long): String {
        val hours = timeMs / (1000 * 60 * 60)
        val minutes = (timeMs % (1000 * 60 * 60)) / (1000 * 60)
        return "${hours}h ${minutes}min"
    }
}
