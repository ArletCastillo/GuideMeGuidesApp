package com.example.guidemeguidesapp.views.reservations

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.ExperienceReservation
import com.example.guidemeguidesapp.ui.theme.AcceptGreen
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.viewModels.ExperienceReservationViewModel
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ExperienceHistoryActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeGuidesAppTheme {
                ShowPastExperiences()
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun ShowPastExperiences(reservationViewModel: ExperienceReservationViewModel = viewModel()) {
    reservationViewModel.getPastExperiences()
    LazyColumn(modifier = Modifier.fillMaxSize())  {
        item {
            if(reservationViewModel.pastExperienceReservations.data.isNullOrEmpty() && !reservationViewModel.pastExperienceReservations.inProgress) {
                Text(modifier = Modifier.padding(15.dp), text = stringResource(id = R.string.no_past_trips))
            }
        }
        if (reservationViewModel.pastExperienceReservations.inProgress) {
            item {
                Box(modifier = Modifier.fillMaxSize()) {
                    LoadingSpinner()
                }
            }
        } else {
            if(!reservationViewModel.pastExperienceReservations.data.isNullOrEmpty()) {
                itemsIndexed(reservationViewModel.pastExperienceReservations.data!!) { index, item ->
                    Card(
                        modifier = Modifier.padding(15.dp),
                        elevation = 15.dp,
                        content = {
                            PastExperienceCardContent(item, reservationViewModel)
                        }
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastExperienceCardContent(experiencieReservation: ExperienceReservation, reservationViewModel: ExperienceReservationViewModel) {
    val from = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experiencieReservation.fromDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    val to = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experiencieReservation.toDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.exp_with) +": ",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${experiencieReservation.touristFirstName} ${experiencieReservation.touristLastName}",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.destination) +": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = experiencieReservation.address.city,
                color = MaterialTheme.colors.onSecondary,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.from_to) +": ${from.value.text} / ${to.value.text}",
                color = MaterialTheme.colors.onSecondary
            )

        }
        Spacer(modifier = Modifier.height(20.dp))
        if(experiencieReservation.ratingForTourist.ratingValue <= 0)
            RateExperience(experienceReservation = experiencieReservation, reservationViewModel)
        else
            AlreadyRated()
    }
}

@Composable
fun RateExperience(experienceReservation: ExperienceReservation, reservationViewModel: ExperienceReservationViewModel) {
    val rating = remember { mutableStateOf(0.0f) }
    val comment = remember { mutableStateOf(TextFieldValue(text = "")) }
    val focusManager = LocalFocusManager.current
    val maxChar = 150

    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
        Spacer(modifier = Modifier.height(10.dp))
        RatingBar(value = rating.value, size = 25.dp, isIndicator = false, ratingBarStyle = RatingBarStyle.HighLighted) {
            experienceReservation.ratingForTourist.ratingValue = it
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = comment.value,
            onValueChange = { value ->
                if (value.text.length <= maxChar) {
                    comment.value = value
                    experienceReservation.ratingForTourist.ratingComment = value.text
                }
            },
            label = { Text(text = stringResource(id = R.string.write_comment)) },
            textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            maxLines = 2,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            reservationViewModel.rateExperience(experienceReservation)
        },
            modifier = Modifier.wrapContentWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colors.secondary)
        ) {
            Row(horizontalArrangement = Arrangement.Center){
                Icon(imageVector = Icons.Filled.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(end = 5.dp))
                Text(stringResource(id = R.string.submit_rating), color = Color.White)
                if(reservationViewModel.rateReservationRequestStatus.inProgress) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .size(22.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AlreadyRated() {
    Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
    Spacer(modifier = Modifier.height(10.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(imageVector = Icons.Filled.CheckCircleOutline,
            contentDescription = null,
            tint = AcceptGreen)
        Text(
            text = stringResource(id = R.string.experience_rated),
            color = AcceptGreen
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileHistoryPreview() {
    GuideMeGuidesAppTheme {
        ShowPastExperiences()
    }
}