package com.example.quizapp.constants

import com.example.quizapp.ui.components.basic.select.SelectOption

const val COUNTDOWN_TIME = 10

const val PUBLIC_ACCESS = 0
const val SHARE_WITH_FOLLOWER_ACCESS = 1
const val PRIVATE_ACCESS = 2

const val VIEW_ACCESS_LEVEL = 0
const val EDIT_ACCESS_LEVEL = 1
const val OWNER_ACCESS_LEVEL = 2
const val NONE_ACCESS_LEVEL = 3

val accessTypeOptions = listOf(
    SelectOption(
        PUBLIC_ACCESS, "Public"
    ),
    SelectOption(
        SHARE_WITH_FOLLOWER_ACCESS, "Share with followers"
    ),
    SelectOption(
        PRIVATE_ACCESS, "Private"
    )
)
