package io.mochadwi.util.ext

import org.joda.time.DateTime

/**
 *
 * In syaa Allah created or modified by @mochadwi
 * On 15/05/19 for social-app
 */

fun String.toDays() = DateTime(this).dayOfWeek().asText