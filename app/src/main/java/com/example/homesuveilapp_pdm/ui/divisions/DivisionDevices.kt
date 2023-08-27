package com.example.homesuveilapp_pdm.ui.divisions

import android.app.AlertDialog
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.FragmentDivisionDevicesBinding
import com.example.homesuveilapp_pdm.db.*
import com.example.homesuveilapp_pdm.ui.doors.DoorsViewModel
import com.example.homesuveilapp_pdm.ui.lights.LightsViewModel
import com.example.homesuveilapp_pdm.ui.thermostat.ThermostatViewModel
import com.example.homesuveilapp_pdm.ui.watering.WateringViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

/**
 * Division's devices fragment
 */
class DivisionDevices : Fragment(), MenuProvider {

    private lateinit var binding: FragmentDivisionDevicesBinding
    private val args: DivisionDevicesArgs by navArgs()
    private val lightsViewModel by viewModels<LightsViewModel>()
    private val doorsViewModel by viewModels<DoorsViewModel>()
    private val thermostatViewModel by viewModels<ThermostatViewModel>()
    private val wateringViewModel by viewModels<WateringViewModel>()
    private val divisionsViewModel by viewModels<DivisionsViewModel>()
    private val cameraViewModel by navGraphViewModels<CameraViewModel>(R.id.divisionDevices)
    private val devicesAdapter = DevicesAdapter(::onLightClicked, ::onLightLongClicked,
        ::onDoorClicked, ::onDoorLongClicked, ::onAreaClicked, ::onAreaLongClicked, ::onThermostatClicked, ::onThermoLongClicked)
    private var lights = listOf<Light>()
    private var doors = listOf<Door>()
    private var thermostat = listOf<Thermostat>()
    private var wateringAreas = listOf<Watering>()

    private fun onLightClicked(light: Light) {
        /* Turns the light on and off */
        lifecycleScope.launch {
            if (light.lightStatus == DeviceStatus.OFF) {
                lightsViewModel.setStatus(DeviceStatus.ON, light.id)
            } else {
                lightsViewModel.setStatus(DeviceStatus.OFF, light.id)
            }
            loadList()
        }
    }

    private fun onDoorClicked(door: Door) {
        /* Locks and unlocks the door */
        lifecycleScope.launch {
            if (door.doorStatus == DeviceStatus.OFF) {
                doorsViewModel.setStatus(DeviceStatus.ON, door.id)
            } else {
                doorsViewModel.setStatus(DeviceStatus.OFF, door.id)
            }
            loadList()
        }
    }

    private fun onThermostatClicked(thermostat: Thermostat) {
        /* Navigates to thermostat configuration fragment */
        findNavController().navigate(DivisionDevicesDirections.actionDivisionDevicesToThermostatConfig(thermostat.id, thermostat.thermostatName, thermostat.thermostatStatus, thermostat.thermostatMode, thermostat.temperature))
    }

    private fun onAreaClicked(watering: Watering){
        /* Navigates to watering configuration fragment */
        findNavController().navigate(DivisionDevicesDirections.actionDivisionDevicesToWateringConfig(watering.id, watering.areaName, watering.areaStatus, watering.areaMode, watering.intensity))
    }

    /* Delete onLongClick for each device type */
    private fun onLightLongClicked(light: Light) {
        deleteDeviceDialog("light", "Delete light", "Do you want to delete this light?",
                            light.id)
    }

    private fun onDoorLongClicked(door: Door) {
        deleteDeviceDialog("door", "Delete door", "Do you want to delete this door?",
            door.id)
    }

    private fun onThermoLongClicked(thermostat: Thermostat) {
        deleteDeviceDialog("thermostat", "Delete thermostat", "Do you want to delete this thermostat?",
            thermostat.id)
    }

    private fun onAreaLongClicked(watering: Watering) {
        deleteDeviceDialog("area", "Delete area", "Do you want to delete this area?",
            watering.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDivisionDevicesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.divisionsDeviceRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.divisionsDeviceRecyclerView.adapter = devicesAdapter

        loadList()
        requireActivity().addMenuProvider(this, viewLifecycleOwner)
    }

    /* Main function for this fragment */
    private fun loadList() {
        lifecycleScope.launchWhenStarted {
            /* Get data from database for each device type */
            lights = lightsViewModel.getLights(args.divisionID)
            doors = doorsViewModel.getDoors(args.divisionID)
            thermostat = thermostatViewModel.getAllThermostats(args.divisionID)
            wateringAreas = wateringViewModel.getAreas(args.divisionID)

            /* Organises the data collected and sets it to the devices' adapter */
            val devicesList = organiseDivisionDevicesList(lights, doors, thermostat, wateringAreas)
            devicesAdapter.items = devicesList

            /* Adds image */
            if (args.divisionImgUri != "") {
                setImg(args.divisionImgUri.toUri())
            } else {
                setPicture()
            }

            binding.divisionImage.setOnClickListener {
                pickSource()
            }

            /* Removes image */
            binding.divisionImage.setOnLongClickListener {
                deleteDeviceDialog("image", "Delete image", "Do you want to delete this image?", args.divisionID)
                true
            }
        }
    }

    /* Returns an ArrayList with the data collected from the database organised */
    private fun organiseDivisionDevicesList(
                lights: List<Light>,
                doors: List<Door>,
                thermostat: List<Thermostat>,
                wateringAreas: List<Watering>
    ): ArrayList<Any> {
        val devices = arrayListOf<Any>()
        devices.add(Title("Lights"))
        devices.addAll(lights)
        devices.add(Title("Doors"))
        devices.addAll(doors)
        devices.add(Title("Thermostat"))
        devices.addAll(thermostat)
        devices.add(Title("Watering"))
        devices.addAll(wateringAreas)
        return devices
    }

    /**
     * Shows dialog box to confirm delete action request and removes the device from its table in the db
     */
    private fun deleteDeviceDialog(type: String, titleText: String, msg: String, deviceID: Long) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(titleText)
        builder.setMessage(msg)
        builder.setIcon(R.drawable.dialog_delete_item)

        builder.setPositiveButton("Delete") { _, _ ->
            when (type.lowercase()) {
                "light" -> lifecycleScope.launch{ lightsViewModel.deleteLight(deviceID) }
                "door" -> lifecycleScope.launch { doorsViewModel.deleteDoor(deviceID) }
                "thermostat" -> lifecycleScope.launch { thermostatViewModel.deleteThermostat(deviceID) }
                "area" -> lifecycleScope.launch { wateringViewModel.deleteArea(deviceID) }
                "image" -> lifecycleScope.launch { divisionsViewModel.setImgURI("", args.divisionID) }
            }
            loadList()
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
                /* Navigates to the fragment responsible for adding a new device */
                findNavController().navigate(DivisionDevicesDirections.actionDivisionDevicesToAddDevice(args.divisionID))
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

    /* Shows dialog box for choosing image source */
    private fun pickSource(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Set image")
        builder.setMessage("Pick an image source")
        builder.setIcon(R.drawable.image)

        builder.setPositiveButton("Take picture") { _, _ ->
            Snackbar.make(requireView(), "Taking picture", Snackbar.LENGTH_SHORT).show()

            findNavController().navigate(DivisionDevicesDirections.actionDivisionDevicesToCamera(args.divisionID))
        }

        builder.setNegativeButton("Select from gallery") { _, _ ->
            Snackbar.make(requireView(), "Selecting from gallery", Snackbar.LENGTH_SHORT).show()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    /* Loads picture in ImageView as soon as it's taken */
    private fun setPicture() {
        lifecycleScope.launch {
            cameraViewModel.file?.let {
                binding.divisionImage.load(it.toUri())
            }
        }
    }

    /* Loads Image from db to ImageView */
    private fun setImg(uri: Uri) {
        binding.divisionImage.load(uri)
    }
}