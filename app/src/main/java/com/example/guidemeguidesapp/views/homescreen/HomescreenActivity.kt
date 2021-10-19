package com.example.guidemeguidesapp.views.homescreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.ui.theme.CancelRed
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.views.chatView.ChatList
import com.example.guidemeguidesapp.views.chatView.ChatView
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scaffoldState, scope) },
        content = { innerPadding -> Box(modifier = Modifier.padding(innerPadding)) {
            ScreenController(navController = navController) } },
        drawerContent = { NavDrawer(scaffoldState, scope, navController) },
        drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
        drawerGesturesEnabled = false,
        bottomBar = { BottomBar(navController) },
        backgroundColor = Color.Unspecified
    )
}

@Composable
fun AppBar(scaffoldState: ScaffoldState, scope: CoroutineScope) {
    TopAppBar(
        title = { Icon(painter = painterResource(id = R.drawable.logo_transparent), contentDescription = "Guide Me logo", modifier = Modifier.fillMaxWidth() ) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Drawer menu")
            }
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Language, contentDescription = "Translate")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
            }
        },
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.background
    )
}

@Composable
fun ScreenController(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "alerts",
        builder = {
            composable(route = "alerts", content = { ScaffoldContent(navController = navController) })
            composable(route = "chat", content = { ChatList(navController = navController) })
            composable(route = "chat_with/{sentTo_Id}", content = { backStackEntry ->
                ChatView(backStackEntry.arguments?.getString("sentTo_Id")!!)
            })
        }
    )
}

@Composable
fun ScaffoldContent(navController: NavHostController) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .fillMaxSize(),
        content = { item {
            Text(
                text = "Hi There, Arlet",
                color = MaterialTheme.colors.onSecondary,
                fontSize = 30.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Text(
                text = "Available alerts in Santo Domingo",
                color = MaterialTheme.colors.onSecondary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            TouristAlert(
                name = "Alicia",
                country = "Dominican Republic",
                imageUrl = "",
                imgSize = 50.dp,
                destination = "Puerto Plata",
                tags = listOf("cultural", "culinary")
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            TouristAlert(
                name = "John",
                country = "United States",
                imageUrl = "",
                imgSize = 50.dp,
                destination = "Samana",
                tags = listOf("cultural", "culinary")
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            TouristAlert(
                name = "Sabrina",
                country = "Mexico",
                imageUrl = "",
                imgSize = 50.dp,
                destination = "Punta Cana",
                tags = listOf("culinary")
            )
        } }
    )
}

@Composable
fun TouristAlert(name: String, country: String, imageUrl: String = "", imgSize: Dp, destination: String, tags: List<String>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 10.dp,
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                content = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                content = {
                                    if(imageUrl.isEmpty()) {
                                        Image(
                                            painter = painterResource(R.drawable.dummy_avatar),
                                            contentDescription = "Temporal dummy avatar",
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .height(imgSize)
                                        )
                                    } else {
                                        Box(modifier = Modifier
                                            .size(imgSize)
                                            .border(
                                                2.dp,
                                                MaterialTheme.colors.secondary,
                                                CircleShape
                                            )) {
                                            CoilImage(imageModel = imageUrl,
                                                contentDescription = "User profile photo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.clip(CircleShape))
                                        }
                                    }
                                    Column(modifier = Modifier.padding(start = 10.dp)) {
                                        Text(
                                            text = name,
                                            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                        )
                                        Text(text = country)
                                    }
                                }
                            )
                            OutlinedButton(
                                onClick = { /*TODO*/ },
                                border = BorderStroke(1.dp, color = MaterialTheme.colors.secondary),
                                content = {
                                    Icon(imageVector = Icons.Default.Explore, contentDescription = "Guide", tint = MaterialTheme.colors.secondary)
                                    Text(text = stringResource(id = R.string.guide_traveler), color = MaterialTheme.colors.secondary, modifier = Modifier.padding(start = 5.dp))
                                }
                            )
                        }
                    )
                    Text(text = stringResource(id = R.string.destination) + ": $destination")
                    Row(
                        modifier = Modifier.padding(top = 10.dp,bottom = 10.dp),
                        content = {
                            Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates")
                            Text(modifier = Modifier.padding(start = 5.dp),text = "Oct 25 - Oct 27")
                        }
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        content = {
                            Text(text = stringResource(id = R.string.languages) + ": ES, ENG, DE")
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth()) {
                        for (tag in tags) {
                            DescriptionTags(tagName = tag)
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun DescriptionTags(tagName: String) {
    Box(modifier = Modifier.padding(end = 10.dp)) {
        Text(
            text = tagName,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.secondary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            overflow = TextOverflow.Ellipsis,
            color = Color.White
        )
    }
}

@Composable
fun NavDrawer(scaffoldState: ScaffoldState, scope: CoroutineScope, navController: NavHostController) {
    Column(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        Row(modifier = Modifier.weight(4f)) {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    content = {
                        UserCard(
                            name = "Arlet",
                            lastname = "Castillo",
                            username = "hola123",
                            imgSize = 60.dp,
                            navController = navController,
                            navRoute = "profile",
                            imageUrl = "",
                            scaffoldState = scaffoldState,
                            scope = scope
                        )
                        Icon(
                            Icons.Default.MenuOpen,
                            contentDescription = "Menu Open",
                            modifier = Modifier.clickable(onClick = { scope.launch { scaffoldState.drawerState.close() } })
                        )
                    }
                )
                Divider(thickness = 2.dp)
                NavOption(title = "History", scaffoldState = scaffoldState, scope, navController, "")
                NavOption(title = "Be a tourist", scaffoldState = scaffoldState, scope, navController, "")
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            Column {
                Divider(thickness = 2.dp)
                Text(
                    text = "Logout",
                    style = TextStyle(color = CancelRed, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = MaterialTheme.colors.secondary),
                            onClick = { scope.launch { scaffoldState.drawerState.close() } }
                        )
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

/* User card with standard information */
@Composable
fun UserCard(
    name: String,
    lastname: String,
    username: String,
    imgSize: Dp,
    navController: NavHostController,
    navRoute: String = "",
    imageUrl: String = "",
    scaffoldState: ScaffoldState,
    scope: CoroutineScope
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 20.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = {
                    scope.launch { scaffoldState.drawerState.close() }
//                    navController.navigate(navRoute)
                }
            ),
        content = {
            if(imageUrl.isEmpty()) {
                Image(
                    painter = painterResource(R.drawable.dummy_avatar),
                    contentDescription = "Temporal dummy avatar",
                    modifier = Modifier
                        .clip(CircleShape)
                        .height(imgSize)
                )
            } else {
                Box(modifier = Modifier
                    .size(imgSize)
                    .border(2.dp, MaterialTheme.colors.secondary, CircleShape)) {
                    CoilImage(imageModel = imageUrl,
                        contentDescription = "User profile photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(CircleShape))
                }
            }
            Column(modifier = Modifier.padding(start = 20.dp)) {
                Text(
                    text = "$name $lastname",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Text(text = username)
            }
        }
    )
}

@Composable
fun NavOption(title: String, scaffoldState: ScaffoldState, scope: CoroutineScope, navController: NavHostController, navRoute: String = "") {
    Text(
        text = title,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = {
                    scope.launch { scaffoldState.drawerState.close() }
//                    navController.navigate(navRoute)
                }
            )
            .padding(16.dp)
            .fillMaxWidth()
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val items = listOf(BottomNavScreen.Map, BottomNavScreen.AudioGuide, BottomNavScreen.Chat)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomNavigation(
        content = { items.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(
                    painter = painterResource(id = screen.resId),
                    contentDescription = screen.label,
                    modifier = Modifier.height(25.dp)
                ) },
                selected = currentRoute == screen.route,
                selectedContentColor = MaterialTheme.colors.primaryVariant,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                })
        } }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeGuidesAppTheme {
        HomescreenContent()
    }
}