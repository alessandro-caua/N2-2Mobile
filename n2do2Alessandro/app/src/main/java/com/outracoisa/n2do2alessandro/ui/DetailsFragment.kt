package com.outracoisa.n2do2alessandro.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.outracoisa.n2do2alessandro.R
import com.outracoisa.n2do2alessandro.databinding.FragmentDetailsBinding
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.helper.GameConstants
import com.outracoisa.n2do2alessandro.viewmodel.DetailsViewModel

class DetailsFragment : Fragment() {
    
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: DetailsViewModel
    private var currentSession: GameSessionEntity? = null
    private var sessionId: Int = 0
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[DetailsViewModel::class.java]
        
        sessionId = arguments?.getInt(GameConstants.BUNDLE.SESSION_ID) ?: 0
        
        observeSession()
        setupButtons()
    }
    
    private fun observeSession() {
        viewModel.getSessionById(sessionId).observe(viewLifecycleOwner) { session ->
            currentSession = session
            displaySessionDetails(session)
        }
    }
    
    private fun displaySessionDetails(session: GameSessionEntity) {
        binding.apply {
            tvDetailName.text = session.gameName
            tvStatus.text = session.status
            tvDateTime.text = "${session.date} • ${session.time}"
            tvHost.text = session.hostName
            
            val confirmedCount = if (session.playersConfirmed.isBlank()) 0
                else session.playersConfirmed.split(",").size
            tvDetailPlayers.text = "$confirmedCount/${session.maxPlayers} jogadores"
            
            // Local (mostrar ou ocultar)
            if (session.location.isNotBlank()) {
                tvLocation.text = session.location
                layoutLocation.visibility = View.VISIBLE
                dividerLocation.visibility = View.VISIBLE
            } else {
                layoutLocation.visibility = View.GONE
                dividerLocation.visibility = View.GONE
            }
            
            // Observações
            if (session.notes.isNotBlank()) {
                tvDetailDescription.text = session.notes
                tvDetailDescription.visibility = View.VISIBLE
                dividerNotes.visibility = View.VISIBLE
            } else {
                tvDetailDescription.visibility = View.GONE
                dividerNotes.visibility = View.GONE
            }
        }
    }
    
    private fun setupButtons() {
        binding.btnFavorite.setOnCheckedChangeListener { _, isChecked ->
            // TODO: Implementar favoritos
        }
        
        binding.btnEdit.setOnClickListener {
            Toast.makeText(context, "Edição em desenvolvimento", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }
    
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Excluir Jogatina")
            .setMessage("Tem certeza que deseja excluir esta jogatina?")
            .setPositiveButton("Sim") { _, _ ->
                currentSession?.let { session ->
                    viewModel.delete(session)
                    Toast.makeText(context, "Jogo deletado!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
