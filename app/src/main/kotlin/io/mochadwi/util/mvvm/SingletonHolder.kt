package io.mochadwi.util.mvvm

open class SingletonHolder<out T, in A, in B, in C>(creator: (A, B, C) -> T) {
    private var creator: ((A, B, C) -> T)? = creator
    @Volatile
    private var instance: T? = null

    fun getInstance(arg0: A, arg1: B, arg2: C): T {
        val i = instance
        if (i != null) {
            return i
        }

        return synchronized(this) {
            val i2 = instance
            if (i2 != null) {
                i2
            } else {
                val created = creator!!(arg0, arg1, arg2)
                instance = created
                creator = null
                created
            }
        }
    }
}