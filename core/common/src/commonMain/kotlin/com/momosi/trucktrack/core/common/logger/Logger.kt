package com.momosi.trucktrack.core.common.logger

import co.touchlab.kermit.LogWriter
import co.touchlab.kermit.Severity
import co.touchlab.kermit.platformLogWriter
import co.touchlab.kermit.Logger as KermitLogger

object Logger {
    fun init(logToConsole: Boolean) {
        KermitLogger.setMinSeverity(Severity.Verbose)
        if (logToConsole) KermitLogger.addLogWriter(platformLogWriter())
    }

    fun addWriter(writer: LogWriter) = KermitLogger.addLogWriter(writer)

    fun v(tag: String, message: String) = KermitLogger.v(message, tag = tag)

    fun d(tag: String, message: String) = KermitLogger.d(message, tag = tag)

    fun i(tag: String, message: String) = KermitLogger.i(message, tag = tag)

    fun w(tag: String, message: String) = KermitLogger.w(message, tag = tag)

    fun w(
        tag: String,
        t: Throwable,
        message: String,
    ) = KermitLogger.w(message, t, tag)

    fun e(tag: String, message: String) = KermitLogger.e(message, tag = tag)

    fun e(
        tag: String,
        t: Throwable,
        message: String,
    ) = KermitLogger.e(message, t, tag)
}
