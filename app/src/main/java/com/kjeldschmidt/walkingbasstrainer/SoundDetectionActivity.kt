package com.kjeldschmidt.walkingbasstrainer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import android.view.View
import android.widget.TextView
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import java.util.ArrayList


class SoundDetectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sound_detection)

        checkRecordPermission()

        val intent = Intent(this, Challenge::class.java).apply {
            putIntegerArrayListExtra(
                Util.IntentPrefix + "chords",
                arrayListOf(1, 1, 1, 1, 4, 4, 1, 1, 5, 4, 1, 1 )
            )

            putIntegerArrayListExtra(
                Util.IntentPrefix + "relativeExpectation",
                arrayListOf( 1, 3, 5, 1 )
            )
        }

        startActivity(intent)
    }

    fun checkRecordPermission() {

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), 123);
        }
    }
}
