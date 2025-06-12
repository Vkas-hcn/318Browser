package bigo.ad.zces

import android.app.Service

class ForegroundController(private val service: Service) {
    private val handlers = mutableMapOf<String, () -> Unit>()

    init {
        registerHandlers()
    }

    private fun registerHandlers() {
        handlers["channel"] = { ChannelProcessor(service).process() }
        handlers["notification"] = { NotificationProcessor(service).process() }
        handlers["foreground"] = { ForegroundProcessor(service).process() }
    }

    fun executeChain() {
        handlers.keys.sorted().forEach { key ->
            handlers[key]?.invoke()
        }
    }
}