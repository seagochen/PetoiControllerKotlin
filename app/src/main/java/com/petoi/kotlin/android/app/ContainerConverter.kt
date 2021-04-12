package com.petoi.kotlin.android.app

class ContainerConverter<T> {
    companion object {
        fun <T> toMutableList(set: MutableSet<T>) : List<T> {
            return set.toList()
        }
    }
}