package com.example.guidemeguidesapp.views.homescreen

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.TouristAlert
import com.example.guidemeguidesapp.ui.theme.CancelRed
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.viewModels.ProfileViewModel
import com.example.guidemeguidesapp.viewModels.TouristAlertViewModel
import com.example.guidemeguidesapp.views.chatView.ChatList
import com.example.guidemeguidesapp.views.chatView.ChatView
import com.example.guidemeguidesapp.views.editGuideExperience.ManageGuideExperience
import com.example.guidemeguidesapp.views.reservationdetails.ReservationDetailsContent
import com.example.guidemeguidesapp.views.reservations.ReservationsContent
import com.example.guidemetravelersapp.helpers.commonComposables.Failed
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.example.guidemetravelersapp.helpers.commonComposables.SuccessCheckmark
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HomescreenActivity : ComponentActivity() {
    @ExperimentalPermissionsApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: TouristAlertViewModel by viewModels()
        setContent {
            GuideMeGuidesAppTheme {
                HomescreenContent(viewModel)
            }
        }
    }
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun HomescreenContent(viewModel: TouristAlertViewModel? = null, profileViewModel: ProfileViewModel = viewModel()) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(scaffoldState, scope) },
        content = { innerPadding -> Box(modifier = Modifier.padding(innerPadding)) {
            ScreenController(navController = navController, viewModel = viewModel!!, profileViewModel) } },
        drawerContent = { NavDrawer(scaffoldState, scope, navController, profileViewModel) },
        drawerShape = RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp),
        drawerGesturesEnabled = true,
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
                Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications")
            }
        },
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = MaterialTheme.colors.background
    )
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Composable
fun ScreenController(navController: NavHostController, viewModel: TouristAlertViewModel, profileViewModel: ProfileViewModel) {
    NavHost(
        navController = navController,
        startDestination = "alerts",
        builder = {
            composable(route = "alerts", content = { ScaffoldContent(navController = navController, viewModel = viewModel, profileViewModel = profileViewModel) })
            composable(route = "reservations", content = { ReservationsContent(navController = navController) })
            composable(route = "chat", content = { ChatList(navController = navController) })
            composable(route = "details/{reservationId}", content = { backStackEntry ->
                ReservationDetailsContent(backStackEntry.arguments?.getString("reservationId")!!, navController) })
            composable(route = "details", content = { ReservationDetailsContent() })
            composable(route = "manage_experience", content = { ManageGuideExperience() })
            composable(route = "chat_with/{sentTo_Id}", content = { backStackEntry ->
                ChatView(backStackEntry.arguments?.getString("sentTo_Id")!!)
            })
        }
    )
}

@ExperimentalPermissionsApi
@Composable
fun ScaffoldContent(navController: NavHostController, viewModel: TouristAlertViewModel, profileViewModel: ProfileViewModel) {
    val openDialog = remember { mutableStateOf(true) }
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val context = LocalContext.current
    PermissionRequired(
        permissionState = locationPermissionState,
        permissionNotGrantedContent = {
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = { openDialog.value = false },
                    title = { Text(text = stringResource(id = R.string.location_permission_title), fontWeight = FontWeight.Bold) },
                    text = { Text(stringResource(id = R.string.location_permission_content)) },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                                locationPermissionState.launchPermissionRequest()
                            }) {
                            Text(text = stringResource(id = R.string.confirm_permission), color = MaterialTheme.colors.primaryVariant)
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDialog.value = false
                            }
                        ) {
                            Text(text = stringResource(id = R.string.dismiss_permission), color = MaterialTheme.colors.primaryVariant)
                        }
                    },
                )
            }
        },
        permissionNotAvailableContent = {
            Column {
                Toast.makeText(context, stringResource(id = R.string.ondismiss_message), Toast.LENGTH_LONG).show()
                Text(text = stringResource(id = R.string.ondismiss_message),
                    modifier = Modifier.padding(bottom = 15.dp),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSecondary,
                    fontWeight = FontWeight.Bold)
            }
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .fillMaxSize(),
                content = {
                    viewModel.fetchTouristAlerts()
                    item {
                        Text(
                            text = "${stringResource(id = R.string.salutation)} ${profileViewModel.profileData.data!!.firstName}",
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(bottom = 20.dp))
                        Text(
                            text = "${stringResource(id = R.string.available_alerts)} ${viewModel.currentCityLocation}",
                            color = MaterialTheme.colors.onSecondary,
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(bottom = 20.dp))
                    }
                    if (viewModel.touristAlerts.inProgress) {
                        item {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LoadingSpinner()
                            }
                        }
                    }
                    else {
                        itemsIndexed(viewModel.touristAlerts.data!!) { _, item ->
                            TouristAlert(touristAlert = item, imgSize = 70.dp, touristAlertViewModel = viewModel)
                            Spacer(modifier = Modifier.padding(bottom = 10.dp))
                        }
                    }
                }
            )
        }
    )
}

@Composable
fun TouristAlert(touristAlert: TouristAlert, imgSize: Dp, touristAlertViewModel: TouristAlertViewModel) {
    val pattern = "MMM dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    val from: String = simpleDateFormat.format(touristAlert.fromDate)
    val to: String = simpleDateFormat.format(touristAlert.toDate)
    val openConfirmationDialog = remember { mutableStateOf(false) }

    if (openConfirmationDialog.value) {
        GuideOfferConfirmation(openDialog = openConfirmationDialog, touristAlert = touristAlert, touristAlertViewModel = touristAlertViewModel)
    }

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
                                    if(touristAlert.touristPhotoUrl.isEmpty()) {
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
                                            CoilImage(imageModel = touristAlert.touristPhotoUrl,
                                                contentDescription = "User profile photo",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.clip(CircleShape))
                                        }
                                    }
                                    Column(modifier = Modifier.padding(start = 10.dp)) {
                                        Text(
                                            text = touristAlert.touristFirstName,
                                            style = MaterialTheme.typography.subtitle1,
                                            color = MaterialTheme.colors.onSecondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = touristAlert.touristCountry,
                                            style = MaterialTheme.typography.subtitle2,
                                        )
                                    }
                                }
                            )
                            OutlinedButton(
                                onClick = { openConfirmationDialog.value = true },
                                border = BorderStroke(1.dp, color = MaterialTheme.colors.primary),
                                content = {
                                    Icon(imageVector = Icons.Default.Explore,
                                        contentDescription = "Guide",
                                        tint = MaterialTheme.colors.primary)
                                    Text(
                                        text = stringResource(id = R.string.guide_traveler),
                                        color = MaterialTheme.colors.primary,
                                        modifier = Modifier.padding(start = 5.dp),
                                        style = MaterialTheme.typography.caption
                                    )
                                }
                            )
                        }
                    )
                    Text(text = stringResource(id = R.string.destination) + ": ${touristAlert.touristDestination}",
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSecondary,)
                    Row(
                        modifier = Modifier.padding(top = 10.dp,bottom = 10.dp),
                        content = {
                            Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates")
                            Text(modifier = Modifier.padding(start = 5.dp), text = "$from - $to",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onSecondary)
                        }
                    )
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        content = {
                            Text(text = stringResource(id = R.string.languages) + ":")
                            Row(
                                modifier = Modifier.fillMaxWidth()) {
                                for (language in touristAlert.touristLanguages) {
                                    Text(modifier = Modifier.padding(start = 5.dp), text = language,
                                        style = MaterialTheme.typography.subtitle2,
                                        color = MaterialTheme.colors.onSecondary)
                                }
                            }
                        }
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth()) {
                        for (tag in touristAlert.experienceTags) {
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
                    color = MaterialTheme.colors.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
    }
}

@Composable
fun NavDrawer(scaffoldState: ScaffoldState,
              scope: CoroutineScope,
              navController: NavHostController,
              profileViewModel: ProfileViewModel
) {
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
                            name = profileViewModel.profileData.data!!.firstName,
                            lastname = profileViewModel.profileData.data!!.lastName,
                            username = profileViewModel.profileData.data!!.username,
                            imgSize = 60.dp,
                            navController = navController,
                            navRoute = "profile",
                            imageUrl = profileViewModel.profileData.data!!.profilePhotoUrl,
                            scaffoldState = scaffoldState,
                            scope = scope
                        )
//                        Icon(
//                            Icons.Default.MenuOpen,
//                            contentDescription = "Menu Open",
//                            modifier = Modifier.clickable(onClick = { scope.launch { scaffoldState.drawerState.close() } })
//                        )
                    }
                )
                Divider(thickness = 2.dp)
                NavOption(title = stringResource(id = R.string.my_experience), scaffoldState = scaffoldState, scope, navController, "manage_experience")
                NavOption(title = stringResource(id = R.string.guiding_offer), scaffoldState = scaffoldState, scope, navController, "")
            }
        }
        Row(modifier = Modifier.weight(1f)) {
            Column {
                Divider(thickness = 2.dp)
                Text(
                    text = stringResource(id = R.string.logout),
                    style = TextStyle(color = CancelRed, fontSize = 18.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple(color = MaterialTheme.colors.secondary),
                            onClick = { profileViewModel.signOutUser() }
                        )
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

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
                    navController.navigate(navRoute)
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

@Composable
fun GuideOfferConfirmation(openDialog: MutableState<Boolean>, touristAlert: TouristAlert, touristAlertViewModel: TouristAlertViewModel) {
    AlertDialog(
        onDismissRequest = { openDialog.value = false },
        title = {
            Text(text = stringResource(id = R.string.guideoffer_modal_title), fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(stringResource(id = R.string.guideoffer_modal_content) + " ${touristAlert.touristFirstName} ${touristAlert.touristLastName}?", Modifier.padding(bottom = 10.dp))
                if(touristAlertViewModel.newGuideOfferStatus.inProgress) {
                    LoadingSpinner()
                }
                else if (!touristAlertViewModel.newGuideOfferStatus.inProgress
                    && !touristAlertViewModel.newGuideOfferStatus.hasError
                    && touristAlertViewModel.newGuideOfferStatus.data!!) {
                    SuccessCheckmark()
                }
                else if (!touristAlertViewModel.newGuideOfferStatus.inProgress && touristAlertViewModel.newGuideOfferStatus.hasError) {
                    Failed()
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    touristAlertViewModel.sendGuideOffer(touristAlert)
                }
            ) {
                Text(text = stringResource(id = R.string.confirm_permission), color = MaterialTheme.colors.onSecondary)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }
            ) {
                Text(text = stringResource(id = R.string.dismiss_permission), color = MaterialTheme.colors.onError)
            }
        },
    )
}

@ExperimentalPermissionsApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GuideMeGuidesAppTheme {
        HomescreenContent()
    }
}