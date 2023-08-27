package com.example.homesuveilapp_pdm.ui.divisions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.homesuveilapp_pdm.R
import com.example.homesuveilapp_pdm.databinding.FragmentCameraBinding
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.PictureResult
import java.io.File
import kotlin.random.Random

/**
 * The camera fragment
 */
class Camera : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private val args: CameraArgs by navArgs()
    private val viewModel by navGraphViewModels<CameraViewModel>(R.id.divisionDevices)
    private val divisionsViewModel by viewModels<DivisionsViewModel>()
    private var i = Random.nextInt(0, 1000)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraView.setLifecycleOwner(viewLifecycleOwner)

        binding.cameraView.addCameraListener(object: CameraListener() {

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)

                val picName = "picture$i.jpg"
                val filesDir = requireContext().filesDir
                val file = File(filesDir, picName)

                result.toFile(file) {
                    it?.run {
                        viewModel.file = it
                        divisionsViewModel.setImgURI(it.toUri().toString(), args.divisionID)
                        findNavController().popBackStack()
                    }
                }
            }
        })

        binding.cameraBtn.setOnClickListener {
            binding.cameraView.takePicture()
        }
    }
}