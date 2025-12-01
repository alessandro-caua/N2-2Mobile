package com.outracoisa.n2do2alessandro.ui.listener

interface OnGameSessionClickListener {
    fun onSessionClick(sessionId: Int)
    fun onFavoriteClick(sessionId: Int, isFavorite: Boolean)
}
