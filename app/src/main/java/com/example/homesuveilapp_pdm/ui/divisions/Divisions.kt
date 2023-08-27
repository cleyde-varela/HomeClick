package com.example.homesuveilapp_pdm.ui.divisions

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.EachDivisionBinding
import com.example.homesuveilapp_pdm.databinding.FragmentDivisionsBinding
import com.example.homesuveilapp_pdm.db.Division
import com.example.homesuveilapp_pdm.db.DivisionType
import com.google.android.material.snackbar.Snackbar

/**
 * Divisions' menu fragment
 */
class Divisions : Fragment(), MenuProvider {

    private lateinit var binding: FragmentDivisionsBinding
    private val viewModel by viewModels<DivisionsViewModel>()
    private val divisionsAdapter = DivisionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDivisionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.divisionsRecyclerView.adapter = divisionsAdapter

        /* Gets data from database and sets it into adapter */
        lifecycleScope.launchWhenStarted {
            viewModel.getDivisionsFlow().collect {
                divisionsAdapter.setData(it)
            }
        }

        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    inner class DivisionsViewHolder(private val binding: EachDivisionBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var division : Division

        init {
            binding.root.setOnClickListener {
                /* Navigates to the selected division devices menu*/
                findNavController().navigate(DivisionsDirections.actionDivisionsToDivisionDevices(division.id, division.divisionName, division.imgURI))
            }

            binding.root.setOnLongClickListener {
                /* Removes a division */
                deleteDivisionDialog(division.id, bindingAdapterPosition)
                true
            }
        }

        fun bind(division : Division) {
            this.division = division
            binding.divisionNameTextView.text = division.divisionName

            /* Sets division image according to division type */
            when (division.divisionType) {
                DivisionType.ROOM -> binding.divisionImage.setImageResource(R.drawable.bedroom_division_icon)
                DivisionType.BATHROOM -> binding.divisionImage.setImageResource(R.drawable.bathroom_division_icon)
                DivisionType.LIVING_ROOM -> binding.divisionImage.setImageResource(R.drawable.living_room_division_icon)
                DivisionType.GARAGE -> binding.divisionImage.setImageResource(R.drawable.garage_division_icon)
                DivisionType.GARDEN -> binding.divisionImage.setImageResource(R.drawable.garden_division_icon)
                DivisionType.KITCHEN -> binding.divisionImage.setImageResource(R.drawable.kitchen_division_icon)
            }
        }
    }

    inner class DivisionsAdapter : RecyclerView.Adapter<DivisionsViewHolder>() {

        private val data: MutableList<Division> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivisionsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = EachDivisionBinding.inflate(layoutInflater, parent, false)
            return DivisionsViewHolder(binding)
        }

        override fun onBindViewHolder(holder: DivisionsViewHolder, position: Int) {
            val d = data[position]
            holder.bind(d)
        }

        override fun getItemCount() = data.size

        fun setData(divisions: List<Division>) {
            this.data.clear()
            this.data.addAll(divisions)
            notifyDataSetChanged()
        }

        fun delete(position: Int) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    /**
     * Shows dialog box to confirm delete action request
     * https://www.geeksforgeeks.org/how-to-create-a-custom-yes-no-dialog-in-android-with-kotlin/
     */
    private fun deleteDivisionDialog( divisionID: Long, bindingAdapterPosition: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Delete division")
        builder.setMessage("Delete this division?")
        builder.setIcon(R.drawable.dialog_delete_item)

        builder.setPositiveButton("Delete") { _, _ ->
            val removed = viewModel.deleteDivision(divisionID)
            divisionsAdapter.delete(bindingAdapterPosition)
        }

        builder.setNegativeButton("Cancel") { _, _ ->
            Snackbar.make(requireView(), "Delete canceled!", Snackbar.LENGTH_SHORT).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setBackgroundColor(Color.parseColor("#9CE489"))
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(Color.parseColor("#FF2255"))
    }

    /* Menu provider methods */
    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.addIcon -> {
                /* Navigates to the fragment responsible for adding a new division */
                findNavController().navigate(DivisionsDirections.actionDivisionsToAddDivision())
                true
            }
            R.id.threeDots -> {
                Snackbar.make(requireView(), "Dots aren't available yet!", Snackbar.LENGTH_SHORT).show()
                true
            }
            else -> {
                false
            }
        }
    }
}