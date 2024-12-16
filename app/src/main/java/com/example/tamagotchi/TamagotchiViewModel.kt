package com.example.tamagotchi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TamagotchiViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application

    private val _hambre = MutableStateFlow(100)
    val hambre: StateFlow<Int> = _hambre

    private val _felicidad = MutableStateFlow(100)
    val felicidad: StateFlow<Int> = _felicidad

    private val _energia = MutableStateFlow(100)
    val energia: StateFlow<Int> = _energia

    init {
        loadData()
    }

    // Cargar los valores almacenados
    private fun loadData() {
        viewModelScope.launch {
            _hambre.value = TamagotchiPreferences.getHunger(context).first()
            _felicidad.value = TamagotchiPreferences.getHappiness(context).first()
            _energia.value = TamagotchiPreferences.getEnergy(context).first()
        }
    }

    // Guardar los valores actualizados
    private fun saveData() {
        viewModelScope.launch {
            TamagotchiPreferences.saveHunger(context, _hambre.value)
            TamagotchiPreferences.saveHappiness(context, _felicidad.value)
            TamagotchiPreferences.saveEnergy(context, _energia.value)
        }
    }

    fun alimentar() {
        _hambre.value = (_hambre.value + 20).coerceAtMost(100)
        _felicidad.value = (_felicidad.value + 10).coerceAtMost(100)
        _energia.value = (_energia.value + 10).coerceAtMost(100)
        saveData()
    }

    fun jugar() {
        _felicidad.value = (_felicidad.value + 20).coerceAtMost(100)
        _energia.value = (_energia.value - 10).coerceAtLeast(0)
        _hambre.value = (_hambre.value - 10).coerceAtLeast(0)
        saveData()
    }

    fun dormir() {
        _energia.value = (_energia.value + 20).coerceAtMost(100)
        _hambre.value = (_hambre.value - 10).coerceAtLeast(0)
        _felicidad.value = (_felicidad.value + 10).coerceAtMost(100)
        saveData()
    }
    fun updateBars() {
        _hambre.value = (_hambre.value - 10).coerceAtLeast(0)
        _felicidad.value = (_felicidad.value - 5).coerceAtLeast(0)
        _energia.value = (_energia.value - 7).coerceAtLeast(0)
        saveData()
    }




}