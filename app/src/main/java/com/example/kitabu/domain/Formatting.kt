package com.example.kitabu.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val MAX_NAME_LENGTH = 40
private const val DAY_MILLIS = 24 * 60 * 60 * 1000L

fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))

/** Return date for a rental that starts now and runs for [days]. */
fun returnDateAfter(days: Int, nowMillis: Long = System.currentTimeMillis()): Long =
    nowMillis + days * DAY_MILLIS

/** Collapses repeated whitespace, strips control characters, and caps the length. */
fun sanitizeUserName(raw: String): String =
    raw.filterNot { it.isISOControl() }
        .replace(Regex("\\s+"), " ")
        .trim()
        .take(MAX_NAME_LENGTH)
