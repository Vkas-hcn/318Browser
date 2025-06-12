package bigo.ad.zces

class HighOrderProcessor {
    companion object {
        fun processWithCallback(callback: () -> Unit): () -> Unit = {
            runCatching { callback() }
        }

        fun conditionalExecution(condition: () -> Boolean, action: () -> Unit) {
            if (condition()) action()
        }
    }
}