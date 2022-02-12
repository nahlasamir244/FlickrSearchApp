package com.nahlasamir244.flickrsearchapp.presentation.home.adapter

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

object AdBannerHandler : AdListener() {

    override fun onAdClosed() {
        super.onAdClosed()
    }

    override fun onAdFailedToLoad(p0: LoadAdError) {
        super.onAdFailedToLoad(p0)
    }

    override fun onAdOpened() {
        super.onAdOpened()
    }

    override fun onAdLoaded() {
        super.onAdLoaded()
    }

    override fun onAdClicked() {
        super.onAdClicked()
    }

    override fun onAdImpression() {
        super.onAdImpression()
    }
}