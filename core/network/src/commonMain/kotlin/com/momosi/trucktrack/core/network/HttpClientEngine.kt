package com.momosi.trucktrack.core.network

import io.ktor.client.engine.HttpClientEngineFactory

expect fun httpClientEngineFactory(): HttpClientEngineFactory<*>
