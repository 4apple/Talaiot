package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.github.rybalkinsd.kohttp.dsl.httpPost
import okhttp3.RequestBody
import okhttp3.Response
import java.lang.Exception
import java.net.URL


class SimpleRequest(mode: LogTracker) : Request {
    override var logTracker = mode

    override fun send(url: String, content: String) {
        val urlSpec = URL(url)
        logTracker.log(url)
        var response: Response? = null
        try {
            val query = urlSpec.query.split("=")
            response = httpPost {
                host = urlSpec.host
                if (urlSpec.port != -1) {
                    port = urlSpec.port
                }
                scheme = urlSpec.protocol
                path = urlSpec.path
                param {
                    query[0] to query[1]
                }
                body {
                    RequestBody.create(null, content)
                }
            }
            logTracker.log(response.message())

        } catch (e: Exception) {
            logTracker.log(e.message ?: "error requesting $url")
        } finally {
            response?.body()?.close()
        }
    }
}