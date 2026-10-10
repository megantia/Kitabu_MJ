package com.example.kitabu.domain

import kotlin.math.ceil

/** Whole days between now and [returnDeadlineMillis]; negative when overdue. */
fun daysRemaining(returnDeadlineMillis: Long, nowMillis: Long = System.currentTimeMillis()): Int {
    val diff = returnDeadlineMillis - nowMillis
    return ceil(diff / DAY_MILLIS.toDouble()).toInt()
}

fun isValidRentalDuration(days: Int): Boolean = days in 1..MAX_RENTAL_DAYS

const val MAX_RENTAL_DAYS = 30
private const val DAY_MILLIS = 24 * 60 * 60 * 1000L
