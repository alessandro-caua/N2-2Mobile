package com.outracoisa.n2do2alessandro.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.outracoisa.n2do2alessandro.R
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.ui.listener.OnGameSessionClickListener

class GameSessionAdapter(
    private val listener: OnGameSessionClickListener
) : RecyclerView.Adapter<GameSessionAdapter.SessionViewHolder>() {
    
    private var sessions = listOf<GameSessionEntity>()
    
    fun setSessions(newSessions: List<GameSessionEntity>) {
        sessions = newSessions
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_session, parent, false)
        return SessionViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        holder.bind(sessions[position])
    }
    
    override fun getItemCount() = sessions.size
    
    inner class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val colorBar: View = itemView.findViewById(R.id.colorBar)
        private val tvGameName: TextView = itemView.findViewById(R.id.tvGameName)
        private val tvHost: TextView = itemView.findViewById(R.id.tvHostName)
        private val chipStatus: Chip = itemView.findViewById(R.id.chipStatus)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val layoutLocation: View = itemView.findViewById(R.id.layoutLocation)
        private val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        private val tvPlayers: TextView = itemView.findViewById(R.id.tvPlayers)
        private val btnConfirm: Button = itemView.findViewById(R.id.btnConfirmPresence)
        
        fun bind(session: GameSessionEntity) {
            // Cor da barra
            try {
                colorBar.setBackgroundColor(Color.parseColor(session.color))
            } catch (e: Exception) {
                colorBar.setBackgroundColor(Color.parseColor("#6C63FF"))
            }
            
            // Informações básicas
            tvGameName.text = session.gameName
            tvHost.text = "Por ${session.hostName}"
            tvDate.text = session.date
            tvTime.text = session.time
            
            // Status chip
            chipStatus.text = session.status
            when (session.status) {
                "Agendado" -> chipStatus.setChipBackgroundColorResource(R.color.status_info)
                "Realizado" -> chipStatus.setChipBackgroundColorResource(R.color.status_success)
                "Cancelado" -> chipStatus.setChipBackgroundColorResource(R.color.status_error)
            }
            
            // Local (mostrar ou ocultar)
            if (session.location.isNotBlank()) {
                tvLocation.text = session.location
                layoutLocation.visibility = View.VISIBLE
            } else {
                layoutLocation.visibility = View.GONE
            }
            
            // Jogadores confirmados
            val confirmedCount = if (session.playersConfirmed.isBlank()) 0 
                else session.playersConfirmed.split(",").size
            tvPlayers.text = "$confirmedCount/${session.maxPlayers} jogadores"
            
            // Botão confirmar (só se agendado)
            if (session.status == "Agendado") {
                btnConfirm.visibility = View.VISIBLE
                btnConfirm.setOnClickListener {
                    // TODO: Implementar confirmação de presença
                }
            } else {
                btnConfirm.visibility = View.GONE
            }
            
            // Click na sessão
            itemView.setOnClickListener {
                listener.onSessionClick(session.id)
            }
        }
    }
}
