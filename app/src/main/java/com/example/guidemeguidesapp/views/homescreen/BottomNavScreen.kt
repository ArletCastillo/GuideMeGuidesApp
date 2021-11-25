package com.example.guidemeguidesapp.views.homescreen

import com.example.guidemeguidesapp.R


sealed class BottomNavScreen(val route: String, val label: String, val resId: Int) {
    object Map: BottomNavScreen("alerts", "Alerts", R.drawable.megaphone)
    object AudioGuide: BottomNavScreen("reservations", "Reservations", R.drawable.booking_filled)
    object Chat: BottomNavScreen("chat", "Chat", R.drawable.chat_bubbles)
}
