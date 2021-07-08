package com.example.materialdesignapp.ui.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.materialdesignapp.MainActivity
import com.example.materialdesignapp.viewmodel.PictureOfTheDayData
import com.example.materialdesignapp.viewmodel.PictureOfTheDayViewModel
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.MainFragmentBinding
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior

class PictureOfTheDayFragment : Fragment() {
    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    companion object {
        private var isMain = true
        private var isDateSettings = false
        fun newInstance() = PictureOfTheDayFragment()
    }

    private var _binding: MainFragmentBinding? = null
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
                viewModel.getData(dateOfPicture).observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
                isDateSettings = false
            }
        }

        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomSheetBehavior(binding.bottomSheet.bottomSheetContainer)
        setBottomAppBar(binding.bottomAppBar)
        binding.inputLayout.setEndIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.let {
                    BottomNavigationDrawerFragment().show(it.supportFragmentManager, "tag")
                }
            }
            R.id.app_bar_fav -> Toast.makeText(context, "Favourite", Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> Toast.makeText(context, "Settings", Toast.LENGTH_SHORT).show()
            R.id.app_bar_date -> if (isDateSettings) {
                Toast.makeText(context, getString(R.string.menu_is_on), Toast.LENGTH_SHORT).show()
                isDateSettings = false
            } else {
                isDateSettings = true
                activity?.supportFragmentManager?.beginTransaction()?.add(
                        R.id.container, ChipsFragment())?.addToBackStack(null)?.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                        ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                binding.fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.getData("").observe(viewLifecycleOwner, Observer<PictureOfTheDayData> { renderData(it) })
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

    private fun showSuccess(url: String, data: PictureOfTheDayData.Success) {
            binding.imageView.load(url) {
                lifecycle(viewLifecycleOwner)
                error(R.drawable.ic_no_photo_vector)
                placeholder(R.drawable.ic_no_photo_vector)
            }

//        binding.imageView.setOnClickListener(){
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }

        binding.bottomSheet.bottomSheetDescriptionHeader.text = data.serverResponseData.title
        binding.bottomSheet.bottomSheetDescription.text = data.serverResponseData.explanation
    }

}