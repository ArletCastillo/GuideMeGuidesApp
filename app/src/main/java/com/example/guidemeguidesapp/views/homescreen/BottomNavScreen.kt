package com.example.guidemeguidesapp.views.homescreen

import com.example.guidemeguidesapp.R


sealed class BottomNavScreen(val route: String, val label: String, val resId: Int) {
    object Map: BottomNavScreen("alerts", "Alerts", R.drawable.loudspeaker_outlined)
    object AudioGuide: BottomNavScreen("reservations", "Reservations", R.drawable.booking)
    object Chat: BottomNavScreen("chat", "Chat", R.drawable.chat_bubbles)
}
