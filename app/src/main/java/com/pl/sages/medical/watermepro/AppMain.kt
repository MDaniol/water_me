package com.pl.sages.medical.watermepro

import android.app.Application

class AppMain: Application() {
    override fun onCreate() {
        super.onCreate()
        Container.initialize(this)
    }
}