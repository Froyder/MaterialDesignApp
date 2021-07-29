package com.example.materialdesignapp.ui.view.fragments

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.QuoteSpan
import android.transition.*
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import coil.api.loadAny
import com.example.materialdesignapp.viewmodel.PictureOfTheDayData
import com.example.materialdesignapp.viewmodel.PictureOfTheDayViewModel
import com.example.materialdesignapp.R
import com.example.materialdesignapp.databinding.PictureOfTheDayFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_mars.*
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.*
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.constraint_container
import kotlinx.android.synthetic.main.picture_of_the_day_fragment.view.*

class PoDFragment : Fragment() {
    //Ленивая инициализация модели
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProviders.of(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var show = true
    private var isExpanded = false

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
        binding.imageView.setOnClickListener { if (show) hideComponents() else showComponents() }
        setDescriptionText()
        binding.showDescription.typeface = Typeface.createFromAsset(context?.assets, "SpaceQuest-Xj4o.ttf")
        //info_group.visibility = View.GONE
    }

    private fun setDescriptionText() {
        binding.showDescription.typeface = Typeface.createFromAsset(context?.assets, "SpaceQuest-Xj4o.ttf")

        val spannable = SpannableString(getString(R.string.show_description))
        spannable.setSpan(
            QuoteSpan(Color.GREEN, 20, 40),
            0, spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        spannable.setSpan(
            ForegroundColorSpan(Color.RED),
            5,9,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE)

        binding.showDescription.setText(spannable, TextView.BufferType.SPANNABLE)

    }

    private fun hideComponents() {
        show = false

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.picture_of_the_day_fragment_end)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        image_view.scaleType = ImageView.ScaleType.CENTER_CROP
        TransitionManager.beginDelayedTransition(constraint_container, transition)
        constraintSet.applyTo(constraint_container)
    }

    private fun showComponents() {
        show = true

        val constraintSet = ConstraintSet()
        constraintSet.clone(context, R.layout.picture_of_the_day_fragment)

        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        image_view.scaleType = ImageView.ScaleType.CENTER_CROP
        TransitionManager.beginDelayedTransition(constraint_container, transition)
        constraintSet.applyTo(constraint_container)
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

            binding.contentHint.visibility = View.GONE
            binding.showDescription.setOnClickListener {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }

        } else if (serverData.serverResponseData.mediaType == "video"){
            binding.imageView.loadAny(url){
                lifecycle(viewLifecycleOwner)
                error(R.drawable.ic_baseline_videocam_24)
            }

            binding.contentHint.visibility = View.VISIBLE
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