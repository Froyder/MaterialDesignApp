package com.example.materialdesignapp.ui.view

import android.animation.Animator
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.example.materialdesignapp.R
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPref = getSharedPreferences("THEME", MODE_PRIVATE)
        setTheme(sharedPref.getInt("Theme", 1))

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splash_text.typeface = Typeface.createFromAsset(assets, "SpaceQuest-Xj4o.ttf")
        splash_text.animate()
            .alpha(1f)
            .scaleXBy(0.8f)
            .scaleYBy(0.8f)
            .duration = 2900

        splash_image.animate()
            .alpha(1f)
            .rotationBy(850f)
            .setInterpolator(AccelerateDecelerateInterpolator ())
            .setDuration(3000)
            .setListener(object : Animator.AnimatorListener {

                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
            })
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}