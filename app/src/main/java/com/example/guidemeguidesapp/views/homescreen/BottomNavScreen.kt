package com.example.guidemeguidesapp.views.homescreen

import com.example.guidemeguidesapp.R


sealed class BottomNavScreen(val route: String, val label: String, val resId: Int) {
    object Map: BottomNavScreen("guides", "Guides", R.drawable.tour_guide)
    object AudioGuide: BottomNavScreen("map", "Map", R.drawable.placeholder)
    object Chat: BottomNavScreen("chat", "Chat", R.drawable.chat_bubbles)
}
