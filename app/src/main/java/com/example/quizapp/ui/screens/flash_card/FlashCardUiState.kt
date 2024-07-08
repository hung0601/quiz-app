package com.example.quizapp.ui.screens.flash_card

import com.example.quizapp.model.Term

data class FlashCardUiState(
    val isOpen: Boolean = false,
    var terms: MutableList<Term> = mutableListOf(),
    val iterator: MutableListIterator<Term> = terms.listIterator(),
    var currentTerm: Term = iterator.next(),
    val step: Int = 0,
)