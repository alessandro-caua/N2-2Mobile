package com.outracoisa.n2do2alessandro.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.outracoisa.n2do2alessandro.R
import com.outracoisa.n2do2alessandro.databinding.FragmentAddGameBinding
import com.outracoisa.n2do2alessandro.entity.GameSessionEntity
import com.outracoisa.n2do2alessandro.helper.GameConstants
import com.outracoisa.n2do2alessandro.helper.LolBlocker
import com.outracoisa.n2do2alessandro.viewmodel.AddGameViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddGameFragment : Fragment() {
    private var _binding: FragmentAddGameBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: AddGameViewModel
    
    private var selectedColor: String = "#2196F3" // Default blue
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddGameBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        viewModel = ViewModelProvider(this)[AddGameViewModel::class.java]
        
        // Check if user is blocked
        if (LolBlocker.isUserBlocked(requireContext())) {
            showBlockedDialog()
            return
        }
        
        setupColorSelector()
        setupDateTimePickers()
        setupButtons()
    }
    
    private fun showBlockedDialog() {
        val timeRemaining = LolBlocker.getBlockTimeRemaining(requireContext())
        val timeFormatted = LolBlocker.formatBlockTime(timeRemaining)
        
        AlertDialog.Builder(requireContext())
            .setTitle("🚫 App Bloqueado")
            .setMessage("Você tentou adicionar League of Legends ao app.\n\n" +
                    "Como punição, o app está bloqueado por 24 horas.\n\n" +
                    "Tempo restante: $timeFormatted\n\n" +
                    "Da próxima vez, escolha um jogo de verdade! 😤")
            .setCancelable(false)
            .setPositiveButton("Entendi... 😔") { _, _ ->
                findNavController().navigateUp()
            }
            .show()
    }
    
    private fun setupColorSelector() {
        val colorViews = listOf(
            binding.colorBlue to "#2196F3",
            binding.colorPurple to "#9C27B0",
            binding.colorOrange to "#FF9800",
            binding.colorGreen to "#4CAF50",
            binding.colorRed to "#F44336",
            binding.colorTeal to "#009688",
            binding.colorPink to "#E91E63"
        )
        
        colorViews.forEach { (view, color) ->
            view.setOnClickListener {
                selectedColor = color
                colorViews.forEach { (v, _) ->
                    v.alpha = 0.3f
                    v.scaleX = 1.0f
                    v.scaleY = 1.0f
                }
                view.alpha = 1.0f
                view.scaleX = 1.2f
                view.scaleY = 1.2f
            }
        }
        
        // Set default selection
        binding.colorBlue.alpha = 1.0f
        binding.colorBlue.scaleX = 1.2f
        binding.colorBlue.scaleY = 1.2f
    }
    
    private fun setupDateTimePickers() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        
        binding.etDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = dateFormat.format(calendar.time)
                    binding.etDate.setText(selectedDate)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        
        binding.etTime.setOnClickListener {
            TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    selectedTime = timeFormat.format(calendar.time)
                    binding.etTime.setText(selectedTime)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }
    }
    
    private fun setupButtons() {
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        
        binding.btnSave.setOnClickListener {
            saveGameSession()
        }
    }
    
    private fun saveGameSession() {
        val gameName = binding.etGameName.text.toString().trim()
        val hostName = binding.etHostName.text.toString().trim()
        val location = binding.etLocation.text.toString().trim()
        val maxPlayersStr = binding.etMaxPlayers.text.toString().trim()
        val notes = binding.etNotes.text.toString().trim()
        
        // Validation
        if (gameName.isEmpty()) {
            Toast.makeText(requireContext(), "Digite o nome do jogo", Toast.LENGTH_SHORT).show()
            return
        }
        
        // CHECK FOR LOL - EASTER EGG!
        if (LolBlocker.isGameNameSuspicious(gameName)) {
            LolBlocker.blockUser(requireContext())
            showLolPunishment()
            return
        }
        
        if (hostName.isEmpty()) {
            Toast.makeText(requireContext(), "Digite o nome do organizador", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (selectedDate.isEmpty()) {
            Toast.makeText(requireContext(), "Selecione uma data", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (selectedTime.isEmpty()) {
            Toast.makeText(requireContext(), "Selecione um horário", Toast.LENGTH_SHORT).show()
            return
        }
        
        val maxPlayers = if (maxPlayersStr.isEmpty()) 0 else {
            maxPlayersStr.toIntOrNull() ?: 0
        }
        
        if (maxPlayers <= 0) {
            Toast.makeText(requireContext(), "Digite um número válido de jogadores", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Create entity
        val gameSession = GameSessionEntity(
            gameName = gameName,
            date = selectedDate,
            time = selectedTime,
            hostName = hostName,
            playersConfirmed = "", // Empty initially
            maxPlayers = maxPlayers,
            location = location.ifEmpty { "Não informado" },
            notes = notes.ifEmpty { "Sem observações" },
            status = GameConstants.STATUS.SCHEDULED,
            color = selectedColor
        )
        
        // Save to database
        viewModel.insertGameSession(
            gameSession = gameSession,
            onSuccess = {
                Toast.makeText(requireContext(), "Jogatina criada com sucesso! 🎮", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            },
            onError = { error ->
                Snackbar.make(binding.root, "Erro: $error", Snackbar.LENGTH_LONG).show()
            }
        )
    }
    
    private fun showLolPunishment() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("🚨 ALERTA DE SEGURANÇA 🚨")
            .setMessage("VOCÊ TENTOU ADICIONAR LEAGUE OF LEGENDS!\n\n" +
                    "Isso é INACEITÁVEL! 😡\n\n" +
                    "Como punição, você está BLOQUEADO do app por 24 HORAS!\n\n" +
                    "Use esse tempo para refletir sobre suas escolhas de vida...\n\n" +
                    "E da próxima vez, jogue algo de verdade! 🎮")
            .setCancelable(false)
            .setPositiveButton("Me desculpe... 😭") { _, _ ->
                findNavController().navigateUp()
            }
            .create()
        
        dialog.show()
        
        // Make the dialog more dramatic
        dialog.window?.decorView?.postDelayed({
            Toast.makeText(requireContext(), "Você foi BANIDO! ☠️", Toast.LENGTH_LONG).show()
        }, 500)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
