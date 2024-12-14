package com.gcancino.levelingup.domain.entities

sealed class State {
    object Loading : State()
    object Success : State()
    object Idle : State()
    data class Error(val message: String) : State()

}