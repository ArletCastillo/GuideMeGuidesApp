package com.example.guidemeguidesapp.views.reservationdetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.viewModels.ExperienceReservationViewModel
import com.example.guidemeguidesapp.viewModels.ProfileViewModel
import com.example.guidemeguidesapp.views.homescreen.DescriptionTags
import com.skydoves.landscapist.coil.CoilImage
import com.gowtham.ratingbar.RatingBar
import java.text.SimpleDateFormat

class ReservationDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeGuidesAppTheme {
                ReservationDetailsContent()
            }
        }
    }
}

@Composable
fun ReservationDetailsContent(reservationId: String = "",
                              profileViewModel: ProfileViewModel = viewModel(),
                              model: ExperienceReservationViewModel = viewModel()) {
    model.getReservation(reservationId, profileViewModel)

    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        content = {
            UserCard(profileViewModel = profileViewModel, imgSize = 100.dp, reservation = model.reservation)
            Divider(thickness = 4.dp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            AboutUser(description = profileViewModel.userData.data!!.aboutUser)
            Button(
                onClick = { /* TODO */ },
                modifier = Modifier.padding(top = 20.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                content = {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        Text(stringResource(id = R.string.send_message), color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    )
}

@Composable
fun UserCard(
    profileViewModel: ProfileViewModel,
    imgSize: Dp,
    reservation: ExperienceReservation) {
    val pattern = "MMM dd"
    val simpleDateFormat = SimpleDateFormat(pattern)
    val from: String = simpleDateFormat.format(reservation.fromDate)
    val to: String = simpleDateFormat.format(reservation.toDate)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
        content = {
            if(profileViewModel.userData.data!!.profilePhotoUrl.isEmpty()) {
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
                    CoilImage(imageModel = profileViewModel.userData.data!!.profilePhotoUrl,
                        contentDescription = "User profile photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.clip(CircleShape))
                }
            }
            Column(modifier = Modifier.padding(start = 20.dp).fillMaxWidth()) {
                Text(text = profileViewModel.userData.data!!.firstName, style = MaterialTheme.typography.h4, color = MaterialTheme.colors.onSecondary)
                Text(text = profileViewModel.userData.data!!.country, style = MaterialTheme.typography.h6, color = MaterialTheme.colors.onPrimary)
                Spacer(modifier = Modifier.height(10.dp))
                UserRating(userRating = 4.5f)
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = {
                        Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates", tint = MaterialTheme.colors.primary)
                        Text(modifier = Modifier.padding(start = 5.dp),
                            text = "$from - $to",
                            style = MaterialTheme.typography.subtitle2,
                            color = MaterialTheme.colors.onSecondary,)
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.spain),
                            contentDescription = "spanish",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 20.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.united_kingdom),
                            contentDescription = "english",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 20.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.germany),
                            contentDescription = "german",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(50.dp)
                                .padding(end = 20.dp))
                    }
                )
            }
        }
    )

}

@Composable
fun UserRating(userRating: Float = 0.0f) {
    RatingBar(value = userRating, size = 20.dp, isIndicator = true) { }
}

@Composable
fun AboutUser(description: String = "") {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){
        Text(
            text = stringResource(id = R.string.about_user),
            style = MaterialTheme.typography.h6,
            color = MaterialTheme.colors.onSecondary,
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = description,
        style = MaterialTheme.typography.subtitle2,
        color = MaterialTheme.colors.onSecondary,
    )
}

@Preview(showBackground = true)
@Composable
fun ReservationDetailsPreview() {
    GuideMeGuidesAppTheme {
        ReservationDetailsContent()
    }
}