package com.petoi.kotlin.android.app.bluetooth

open class BluetoothMessageQueue {
    // petoi设备返回的字符串信息全部存储到这个列表中
    private val petoiFeedbacks = mutableListOf<String>()

    // 计时
    private var lastTime = System.currentTimeMillis()

    // 超时设置
    private val timeout = 200

    // 包含线程锁的数据获取
    @Synchronized
    fun get() : String {
        if  (petoiFeedbacks.size > 0) {
            val feedback = petoiFeedbacks.joinToString(separator = "")
            clear()
            return feedback
        }

        // 返回空白无数据
        return ""
    }

    // 数据是否准备完毕
    @Synchronized
    fun isReady(): Boolean {
        return (System.currentTimeMillis() - lastTime > timeout)
    }

    // 把数据放入缓存中
    @Synchronized
    fun put(str: String) {
        petoiFeedbacks.add(str)

        // 更新时间戳
        lastTime = System.currentTimeMillis()
    }

    // clean
    @Synchronized
    fun clear() {
        petoiFeedbacks.clear()
    }

}