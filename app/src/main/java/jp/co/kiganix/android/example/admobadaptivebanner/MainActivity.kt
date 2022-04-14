package jp.co.kiganix.android.example.admobadaptivebanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import jp.co.kiganix.android.example.admobadaptivebanner.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val binding: MainActivityBinding by lazy { MainActivityBinding.inflate(layoutInflater) }

    private val density: Float by lazy {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.density
    }

    private var ad: AdView? = null

    private var initialLayoutComplete: Boolean = false

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MobileAds.initialize(this)
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("ABCDEF012345"))
                .build()
        )

        setContentView(binding.root)

        binding.container.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true

                val containerWidth = binding.container.measuredWidth
                val divided = containerWidth/density
                val adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(
                    this@MainActivity,
                    divided.toInt()
                )

                binding.text.text = "containerWidth: $containerWidth, density: $density, divided: $divided, adSize: $adSize"

                ad = AdView(this).apply {
                    this.adSize = adSize
                    adUnitId = "ca-app-pub-3940256099942544/9214589741"
                    adListener = this@MainActivity.adListener
                    loadAd(AdRequest.Builder().build())
                }.also {
                    binding.container.addView(it)
                }
            }
        }

    }

    private val adListener = object : AdListener() {

        override fun onAdLoaded() {
            super.onAdLoaded()
            println("onAdLoaded")
        }

        override fun onAdFailedToLoad(p0: LoadAdError) {
            super.onAdFailedToLoad(p0)
            println(p0)
        }

    }

    override fun onPause() {
        super.onPause()
        ad?.pause()
    }

    override fun onResume() {
        super.onResume()
        ad?.resume()
    }

    override fun onDestroy() {
        ad?.destroy()
        super.onDestroy()
    }

}
