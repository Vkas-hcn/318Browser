package bigo.ad.zces

object ReflectionHelper {
    fun <T : Any> invokeMethod(instance: T, methodName: String, vararg args: Any?): Any? {
        return try {
            val method = instance::class.java.getDeclaredMethod(methodName, *args.map { it?.javaClass }.toTypedArray())
            method.isAccessible = true
            method.invoke(instance, *args)
        } catch (e: Exception) {
            null
        }
    }
}