package com.pipai.starsector.gri.utils

import java.util.Random

fun <T> MutableCollection<T>.removeRandom(rng: Random): T {
    val n = rng.nextInt(this.size)
    val elem = elementAt(n)
    remove(elem)
    return elem
}

fun <T> Collection<T>.chooseAmount(amount: Int, rng: Random): List<T> {
    val mut = this.toMutableList()
    val chosen: MutableList<T> = mutableListOf()
    while (chosen.size < amount && mut.isNotEmpty()) {
        chosen.add(mut.removeRandom(rng))
    }
    return chosen
}
