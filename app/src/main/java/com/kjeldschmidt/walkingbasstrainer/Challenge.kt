package com.kjeldschmidt.walkingbasstrainer

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor

class Challenge : AppCompatActivity() {

	var bpm = 120
	var repetitions = 1
	var startTime: Long = 0
	var millisBetweenBeats: Long = 0
	lateinit var relativeExpectation: List<Int>
	lateinit var chords: List<Int>


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_challenge)

		relativeExpectation = intent.getIntegerArrayListExtra(Util.IntentPrefix + "relativeExpectation")
		chords = intent.getIntegerArrayListExtra(Util.IntentPrefix + "chords")

		startTime = System.currentTimeMillis()
		millisBetweenBeats = 1000L * 60 / bpm

		val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

		val pdh = PitchDetectionHandler { result, _ ->
			val pitchInHz = result.pitch
			val millisSinceStart = System.currentTimeMillis() - startTime
			val beatIndex = millisSinceStart / millisBetweenBeats

			val currentRoot = chords.elementAt(((beatIndex / relativeExpectation.size) % chords.size).toInt())
			val currentInterval = relativeExpectation.elementAt((beatIndex % relativeExpectation.size).toInt())

			val frequencyRange = FrequencyMatcher.expectFrquency(currentRoot, currentInterval)

			val indicatorColor: Int;
			if (frequencyRange.lower < pitchInHz && pitchInHz < frequencyRange.upper) {
				indicatorColor = Color.parseColor("#11DD22");
			} else {
				indicatorColor = Color.parseColor("#DD1122");
			}

			runOnUiThread {
				val freqView = findViewById<View>(R.id.frequencyView) as TextView
				freqView.text =
					"Current: $pitchInHz, Range: ${(frequencyRange.lower * 100).toInt() / 100}-${(frequencyRange.upper * 100).toInt() / 100}"

				val cmdView = findViewById<View>(R.id.commandView) as TextView
				cmdView.text = "$beatIndex, $currentRoot, $currentInterval"

				val successIndicator = findViewById<View>(R.id.successIndicator)
				successIndicator.setBackgroundColor(indicatorColor)
			}
		}
		val p = PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh)
		dispatcher.addAudioProcessor(p)
		Thread(dispatcher, "Audio Dispatcher").start()
	}


}
