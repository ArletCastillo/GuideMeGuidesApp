package com.example.guidemeguidesapp.views.reservations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.viewModels.ExperienceReservationViewModel
import com.example.guidemeguidesapp.viewModels.ProfileViewModel
import com.example.guidemeguidesapp.views.homescreen.TouristAlert
import com.skydoves.landscapist.coil.CoilImage
import java.text.SimpleDateFormat

class ReservationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeGuidesAppTheme {
                ReservationsContent()
            }
        }
    }
}

@Composable
fun ReservationsContent(navController: NavHostController? = null,
                        model: ExperienceReservationViewModel = viewModel(),
                        profileViewModel: ProfileViewModel = viewModel()) {
    model.getGuideReservations(profileViewModel.profileData.data!!.id)

    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .fillMaxSize(),
        content = {
            itemsIndexed(model.guideReservations) { index: Int, item: ExperienceReservation ->
                ReservationCard(experienceReservation = item, navController = navController)
                Spacer(modifier = Modifier.padding(bottom = 10.dp))
            }
        }
    )
}

@Composable
fun ReservationCard(experienceReservation: ExperienceReservation, navController: NavHostController? = null) {
    val pattern = "MMM dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    val from: String = simpleDateFormat.format(experienceReservation.fromDate)
    val to: String = simpleDateFormat.format(experienceReservation.toDate)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = { navController?.navigate("details/${experienceReservation.id}") }
            ),
        elevation = 10.dp,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
                Column() {
                    Text(
                        text = "${experienceReservation.touristFirstName} ${experienceReservation.touristLastName}",
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates", tint = MaterialTheme.colors.secondary)
                            Text(modifier = Modifier.padding(start = 5.dp),
                                text = "$from - $to",
                                style = MaterialTheme.typography.subtitle2,
                                color = MaterialTheme.colors.onSecondary
                            )
                        }
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ReservationsPreview() {
    GuideMeGuidesAppTheme {
        ReservationsContent()
    }
}