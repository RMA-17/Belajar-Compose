package com.rmaproject.jetheroes.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmaproject.jetheroes.model.Hero
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JetHeroesViewModel(
    private val repository: HeroRepository
) : ViewModel() {
    private val _groupedHeroes = MutableStateFlow(
        repository.getHeroes()
            .sortedBy { it.name }
            .groupBy { it.name[0] }
    )

    val groupedHeroes : StateFlow<Map<Char, List<Hero>>> get() = _groupedHeroes

    /**
     * data disimpan sebagai StateFlow supaya data dapat ter-update ketika terjadi perubahan
     */
}

class ViewModelFactory(private val repository: HeroRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JetHeroesViewModel::class.java)) {
            return JetHeroesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}