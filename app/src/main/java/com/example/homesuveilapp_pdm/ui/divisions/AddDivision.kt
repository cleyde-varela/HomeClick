package com.example.homesuveilapp_pdm.ui.divisions

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.FragmentAddDivisionBinding
import com.example.homesuveilapp_pdm.db.Division
import com.example.homesuveilapp_pdm.db.DivisionType


/**
 * Fragment responsible for adding a new division
 */
class AddDivision : Fragment() {

    private lateinit var binding : FragmentAddDivisionBinding
    private val viewModel by viewModels<DivisionsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddDivisionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createDeviceBtn.setOnClickListener {
            /* Creates a new division */
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_baseline_circle_24)

            val newDivision = Division(
                binding.divisionNameEditText.text.toString(),
                spinnerSelection(binding.divisionsTypeSpinner.selectedItem.toString()), "")

            viewModel.addDivision(newDivision)

            findNavController().popBackStack()
        }
    }

    /* Returns the DivisionType based on the user's spinner selection */
    private fun spinnerSelection(selection: String) : DivisionType {
        return when (selection) {
            "Room" -> DivisionType.ROOM
            "Bathroom" -> DivisionType.BATHROOM
            "Living room" -> DivisionType.LIVING_ROOM
            "Garage" -> DivisionType.GARAGE
            "Garden" -> DivisionType.GARDEN
            "Kitchen" -> DivisionType.KITCHEN
            else -> {
                DivisionType.ROOM
            }
        }
    }
}