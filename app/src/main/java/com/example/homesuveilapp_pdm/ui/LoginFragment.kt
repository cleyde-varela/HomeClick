package com.example.homesuveilapp_pdm.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

/**
 * Login fragment
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginPin = "9119" // App's login pin just for demonstration purposes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* Checks pin provided */
        binding.loginPinButton.setOnClickListener {
            if (binding.loginPinEditText.text.toString() == loginPin) {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToDivisions())
            } else {
                binding.loginPinEditText.text.clear()
                val snackError = Snackbar.make(requireView(), getString(R.string.wrong_pin_msg), Snackbar.LENGTH_SHORT)
                snackError.setBackgroundTint(Color.parseColor("#FF2255"))
                snackError.setTextColor(Color.parseColor("#FFFFFF"))
                snackError.show()
            }
        }

        /* Click listeners for demonstration purposes only */
        binding.loginHelpLabel.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.help_msg), Snackbar.LENGTH_SHORT).show()
        }

        binding.loginGetPinLabel.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.pin_recovery_msg), Snackbar.LENGTH_SHORT).show()
        }

        binding.loginLogo.setOnClickListener {
            val snackBar = Snackbar.make(requireView(), "Home is where the heart is!", Snackbar.LENGTH_SHORT)
            snackBar.setBackgroundTint(Color.parseColor("#34495E"))
            snackBar.setTextColor(Color.parseColor("#FFFFFF"))
            snackBar.show()
        }
    }
}