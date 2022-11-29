package com.rmaproject.jetheroes.data

import com.rmaproject.jetheroes.model.HeroesData

class HeroRepository {
    fun getHeroes() = HeroesData.heroes
}