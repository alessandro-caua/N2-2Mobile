package com.outracoisa.n2do2alessandro.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.outracoisa.n2do2alessandro.R
import com.outracoisa.n2do2alessandro.databinding.FragmentHomeBinding
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.helper.GameConstants
import com.outracoisa.n2do2alessandro.helper.LolBlocker
import com.outracoisa.n2do2alessandro.ui.adapter.GameSessionAdapter
import com.outracoisa.n2do2alessandro.ui.listener.OnGameSessionClickListener
import com.outracoisa.n2do2alessandro.viewmodel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment(), OnGameSessionClickListener {
    
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: GameSessionAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        
        setupRecyclerView()
        setupFab()
        observeGames()
    }
    
    private fun setupRecyclerView() {
        adapter = GameSessionAdapter(this)
        binding.recyclerViewGames.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewGames.adapter = adapter
    }
    
    private fun setupFab() {
        binding.fabAddGame.setOnClickListener {
            // Check if user is blocked
            if (LolBlocker.isUserBlocked(requireContext())) {
                showBlockedDialog()
            } else {
                // Navigate to add game fragment
                findNavController().navigate(R.id.action_homeFragment_to_addGameFragment)
            }
        }
    }
    
    private fun showBlockedDialog() {
        val timeRemaining = LolBlocker.getBlockTimeRemaining(requireContext())
        val timeFormatted = LolBlocker.formatBlockTime(timeRemaining)
        
        AlertDialog.Builder(requireContext())
            .setTitle("🚫 App Bloqueado")
            .setMessage("Você está bloqueado por tentar adicionar League of Legends!\n\n" +
                    "Tempo restante: $timeFormatted\n\n" +
                    "Aprenda a jogar jogos de verdade! 😤")
            .setPositiveButton("Ok... 😔", null)
            .show()
    }
    
    private fun observeGames() {
        viewModel.allSessions.observe(viewLifecycleOwner) { sessions ->
            if (sessions.isEmpty()) {
                binding.layoutEmptyState.visibility = View.VISIBLE
                binding.recyclerViewGames.visibility = View.GONE
            } else {
                binding.layoutEmptyState.visibility = View.GONE
                binding.recyclerViewGames.visibility = View.VISIBLE
                adapter.setSessions(sessions)
            }
        }
    }
    
    private fun addSampleGame() {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 3)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        
        val sampleSessions = listOf(
            GameSessionEntity(
                gameName = "Catan 🎲",
                date = dateFormat.format(calendar.time),
                time = "19:00",
                hostName = "Alessandro",
                maxPlayers = 4,
                location = "Casa do João",
                notes = "Trazer bebidas! Jogo de tabuleiro clássico",
                status = "Agendado",
                color = "#2196F3"
            ),
            GameSessionEntity(
                gameName = "Valorant 🎮",
                date = dateFormat.format(calendar.time),
                time = "20:00",
                hostName = "Carlos",
                maxPlayers = 5,
                location = "Online - Discord",
                notes = "Competitivo em grupo, FPS tático",
                status = "Agendado",
                color = "#9C27B0"
            ),
            GameSessionEntity(
                gameName = "Uno 🃏",
                date = dateFormat.format(calendar.time),
                time = "18:30",
                hostName = "Maria",
                maxPlayers = 6,
                location = "República",
                notes = "Jogo de cartas rápido antes do jantar",
                status = "Agendado",
                color = "#FF9800"
            ),
            GameSessionEntity(
                gameName = "D&D - Campanha de Ravenloft 🎭",
                date = dateFormat.format(calendar.time),
                time = "14:00",
                hostName = "Pedro",
                maxPlayers = 6,
                location = "Casa do Pedro",
                notes = "RPG de mesa, sessão 5. Trazer fichas!",
                status = "Agendado",
                color = "#E91E63"
            )
        )
        
        viewModel.insert(sampleSessions.random())
        Toast.makeText(context, "Jogatina adicionada!", Toast.LENGTH_SHORT).show()
    }
    
    override fun onSessionClick(sessionId: Int) {
        val bundle = Bundle().apply {
            putInt(GameConstants.BUNDLE.SESSION_ID, sessionId)
        }
        findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
    }
    
    override fun onFavoriteClick(sessionId: Int, isFavorite: Boolean) {
        // Não usado mais - removida funcionalidade de favoritos
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
