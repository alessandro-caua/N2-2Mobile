package com.outracoisa.n2do2alessandro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.outracoisa.n2do2alessandro.R
import com.outracoisa.n2do2alessandro.databinding.FragmentFavoriteBinding
import com.outracoisa.n2do2alessandro.helper.GameConstants
import com.outracoisa.n2do2alessandro.ui.adapter.GameSessionAdapter
import com.outracoisa.n2do2alessandro.ui.listener.OnGameSessionClickListener
import com.outracoisa.n2do2alessandro.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment(), OnGameSessionClickListener {
    
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: GameSessionAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]
        
        setupRecyclerView()
        observeFavoriteGames()
    }
    
    private fun setupRecyclerView() {
        adapter = GameSessionAdapter(this)
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFavorites.adapter = adapter
    }
    
    private fun observeFavoriteGames() {
        viewModel.upcomingSessions.observe(viewLifecycleOwner) { sessions ->
            if (sessions.isEmpty()) {
                binding.tvEmptyFavorites.visibility = View.VISIBLE
                binding.recyclerViewFavorites.visibility = View.GONE
            } else {
                binding.tvEmptyFavorites.visibility = View.GONE
                binding.recyclerViewFavorites.visibility = View.VISIBLE
                adapter.setSessions(sessions)
            }
        }
    }
    
    override fun onSessionClick(sessionId: Int) {
        val bundle = Bundle().apply {
            putInt(GameConstants.BUNDLE.SESSION_ID, sessionId)
        }
        findNavController().navigate(R.id.action_favoriteFragment_to_detailsFragment, bundle)
    }
    
    override fun onFavoriteClick(sessionId: Int, isFavorite: Boolean) {
        // Não usado - favoritos removidos do sistema
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
