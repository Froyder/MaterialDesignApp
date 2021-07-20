package com.example.materialdesignapp.ui.view.bottom_navigation_view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import coil.api.loadAny
import com.example.materialdesignapp.viewmodel.PictureOfTheDayData
import com.example.materialdesignapp.viewmodel.PictureOfTheDayViewModel
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.PictureOfTheDayFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main_navigation_view.*

class PoDFragment : Fragment() {
    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var _binding: PictureOfTheDayFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private fun setBottomSheetBehavior(bottomSheet: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setFragmentResultListener("DATE_REQUEST") { requestKey, bundle ->
            val dateOfPicture = (bundle.getString("date"))
            if (dateOfPicture != null) {
                viewModel.getData(dateOfPicture).observe(viewLifecycleOwner, { renderData(it) })
            }
        }
        _binding = PictureOfTheDayFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_app_bar, menu)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData("").observe(viewLifecycleOwner, { renderData(it) })
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    binding.bottomSheet.bottomSheetDescription.text = getString(R.string.empty_link)
                } else {
                    showSuccess(url, data)
                }
            }
            is PictureOfTheDayData.Loading -> {
                binding.bottomSheet.bottomSheetDescriptionHeader.text = getString(R.string.loading)
            }
            is PictureOfTheDayData.Error -> {
                Toast.makeText(context, R.string.empty_link, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSuccess(url: String, serverData: PictureOfTheDayData.Success) {

        if (serverData.serverResponseData.mediaType == "image"){
            binding.imageView.load(url) {
                lifecycle(viewLifecycleOwner)
                error(R.drawable.ic_no_photo_vector)
                placeholder(R.drawable.ic_no_photo_vector)
            }

            binding.contentHint.text = ""
            binding.showDescription.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

        } else if (serverData.serverResponseData.mediaType == "video"){
            binding.imageView.loadAny(url){
                lifecycle(viewLifecycleOwner)
                error(R.drawable.ic_baseline_videocam_24)
            }
            binding.contentHint.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(serverData.serverResponseData.url.toString())
                })
            }
            binding.contentHint.text = getString(R.string.click_video)

            binding.showDescription.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

            binding.imageTitle.text = serverData.serverResponseData.title
            binding.bottomSheet.bottomSheetDescriptionHeader.text = serverData.serverResponseData.title
            binding.bottomSheet.bottomSheetDescription.text = serverData.serverResponseData.explanation
        }
}