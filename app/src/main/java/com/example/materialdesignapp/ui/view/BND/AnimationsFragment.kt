package com.example.materialdesignapp.ui.view.BND

import android.graphics.Rect
import android.os.Bundle
import android.transition.*
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.materialdesignapp.R
import kotlinx.android.synthetic.main.animations_fragment.*

class AnimationsFragment : Fragment(){

    private var textIsVisible = false
    private var isExpanded = false
    private var toRightAnimation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.animations_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = Adapter()

        button.setOnClickListener {
            TransitionManager.beginDelayedTransition(transitions_container, Slide(Gravity.START))
            textIsVisible = !textIsVisible
            text.visibility = if (textIsVisible) View.VISIBLE else View.GONE
        }

        image_view.setOnClickListener {
            isExpanded = !isExpanded
            TransitionManager.beginDelayedTransition(
                container, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = image_view.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            image_view.layoutParams = params
            image_view.scaleType =
                if (isExpanded) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }

        button_two.setOnClickListener {
            val changeBounds = ChangeBounds()
            changeBounds.setPathMotion(ArcMotion())
            changeBounds.duration = 500
            TransitionManager.beginDelayedTransition(
                transitions_container,
                changeBounds
            )

            toRightAnimation = !toRightAnimation
            val params = button_two.layoutParams as LinearLayout.LayoutParams
            params.gravity =
                if (toRightAnimation) Gravity.END or Gravity.BOTTOM else Gravity.START or Gravity.TOP
            button_two.layoutParams = params
        }

        val titles: MutableList<String> = ArrayList()
        for (i in 0..4) {
            titles.add(String.format("Item %d", i + 1))
        }
        createViews(transitions_container_two, titles)
        button_three.setOnClickListener {
            TransitionManager.beginDelayedTransition(transitions_container_two, ChangeBounds())
            titles.shuffle()
            createViews(transitions_container_two, titles)
        }

    }

    private fun createViews(layout: ViewGroup, titles: List<String>) {
        layout.removeAllViews()
        for (title in titles) {
            val textView = TextView(context)
            textView.text = title
            textView.gravity = Gravity.CENTER_HORIZONTAL
            ViewCompat.setTransitionName(textView, title)
            layout.addView(textView)
        }
    }

    private fun explode(clickedView: View) {
        val viewRect = Rect()
        clickedView.getGlobalVisibleRect(viewRect)
        val explode = Explode()
        explode.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return viewRect
            }
        }
        explode.excludeTarget(clickedView, true)
        val set = TransitionSet()
            .addTransition(explode)
            .addTransition(Fade().addTarget(clickedView))
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    transition.removeListener(this)
                    //onBackPressed()
                }
            })
        TransitionManager.beginDelayedTransition(recycler_view, set)
        recycler_view.adapter = null
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.activity_animations_explode_recycle_view_item,
                    parent,
                    false
                ) as View
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                explode(it)
            }
        }

        override fun getItemCount(): Int {
            return 8
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

}