package com.rmaproject.jetnoteapp.data.kotpref

import com.chibatching.kotpref.KotprefModel

object FirstLaunchState : KotprefModel() {
    var isAppInitialised by booleanPref(true)
}