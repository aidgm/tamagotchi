package com.example.tamagotchi

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class Tamagotchi {
    var hambre by mutableIntStateOf(100)
    var felicidad by mutableIntStateOf(100)
    var energia by mutableIntStateOf(100)

    fun alimentar(){
        hambre = (hambre + 20).coerceAtMost(100)
        felicidad = (felicidad +10).coerceAtMost(100)
        energia = (energia + 10).coerceAtMost(100)
    }

    fun jugar(){
        felicidad = (felicidad+20).coerceAtMost(100)
        energia = (energia - 10).coerceAtLeast(0)
        hambre = (hambre -10).coerceAtLeast(0)
    }

    fun dormir(){
        energia = (energia + 20).coerceAtMost(100)
        hambre = (hambre - 10).coerceAtLeast(0)
        felicidad = (felicidad +10).coerceAtMost(100)
    }
}