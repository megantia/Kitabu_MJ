package com.example.kitabu.domain

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val MAX_NAME_LENGTH = 40
private const val DAY_MILLIS = 24 * 60 * 60 * 1000L

fun formatDate(millis: Long): String =
    SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))

fun returnDateAfter(days: Int, nowMillis: Long = System.currentTimeMillis()): Long =
    nowMillis + days * DAY_MILLIS

fun sanitizeUserName(raw: String): String =
    raw.filterNot { it.isISOControl() }
        .replace(Regex("\\s+"), " ")
        .trim()
        .take(MAX_NAME_LENGTH)
