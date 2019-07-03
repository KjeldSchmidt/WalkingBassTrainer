package com.kjeldschmidt.walkingbasstrainer

import kotlin.math.pow

data class FrequencyRange(val lower: Double, val upper: Double)

class FrequencyMatcher {
	companion object {
		fun expectFrquency(root: Int, interval: Int): FrequencyRange {
			val calc_root = root - 1
			val calc_interval = interval - 1

			val factor = 2.0.pow((calc_root + calc_interval) / 12.0)
			val lower = 39 * factor
			val upper = 43 * factor

			return FrequencyRange(lower, upper)
		}
	}
}