package com.example.guidemeguidesapp.views.homescreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.views.chatView.ChatList
import com.example.guidemeguidesapp.views.chatView.ChatView

class HomescreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeGuidesAppTheme {
                HomescreenContent()
            }
        }
    }
}

@Composable
fun HomescreenContent() {
    val navController = rememberNavController()

    ScreenController(navController = navController)
}

@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "chat",
        builder = {
            composable(route = "chat", content = { ChatList(navController = navController) })
            composable(route = "chat_with/{sentTo_Id}", content = { backStackEntry ->
                ChatView(backStackEntry.arguments?.getString("sentTo_Id")!!)
            })
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    GuideMeGuidesAppTheme {
        HomescreenContent()
    }
}