/*
 * Copyright 2020 The Matrix.org Foundation C.I.C.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.matrix.android.sdk.internal.util

import org.matrix.android.sdk.BuildConfig
import timber.log.Timber

internal suspend fun <T> logDuration(message: String,
                                     block: suspend () -> T): T {
    Timber.v("$message -- BEGIN")
    val start = System.currentTimeMillis()
    val result = logRamUsage(message) {
        block()
    }
    val duration = System.currentTimeMillis() - start
    Timber.v("$message -- END duration: $duration ms")

    return result
}

internal suspend fun <T> logRamUsage(message: String, block: suspend () -> T): T {
    return if (BuildConfig.DEBUG) {
        val runtime = Runtime.getRuntime()
        runtime.gc()
        val freeMemoryInMb = runtime.freeMemory() / 1048576L
        val usedMemInMBStart = runtime.totalMemory() / 1048576L - freeMemoryInMb
        Timber.v("$message -- BEGIN (free memory: $freeMemoryInMb MB)")
        val result = block()
        runtime.gc()
        val usedMemInMBEnd = (runtime.totalMemory() - runtime.freeMemory()) / 1048576L
        val usedMemInMBDiff = usedMemInMBEnd - usedMemInMBStart
        Timber.v("$message -- END RAM usage: $usedMemInMBDiff MB")
        result
    } else {
        block()
    }
}
