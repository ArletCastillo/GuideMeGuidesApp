package com.example.guidemeguidesapp.views.reservations

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.ExperienceReservationRequest
import com.example.guidemeguidesapp.viewModels.ExperienceReservationViewModel
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun ReservationRequests(reservationViewModel: ExperienceReservationViewModel = viewModel()) {
    val isRefreshingReservationRequests by reservationViewModel.isRefreshingReservationRequests.collectAsState()
    reservationViewModel.getReservationRequestsForGuide()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshingReservationRequests),
        onRefresh = { reservationViewModel.refreshReservationRequests() },
        content = {
            LazyColumn(Modifier.fillMaxSize())  {
                item {
                    if(reservationViewModel.guideReservationRequests.data.isNullOrEmpty() && !reservationViewModel.guideReservationRequests.inProgress) {
                        Text(modifier = Modifier.padding(15.dp),text = stringResource(id = R.string.no_request))
                    }
                }
                if (reservationViewModel.guideReservationRequests.inProgress) {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingSpinner()
                        }
                    }
                } else {
                    if(!reservationViewModel.guideReservationRequests.data.isNullOrEmpty()) {
                        itemsIndexed(reservationViewModel.guideReservationRequests.data!!) { index, item ->
                            Card(
                                modifier = Modifier.padding(15.dp),
                                elevation = 15.dp,
                                content = {
                                    ReservationRequestCardContent(item, reservationViewModel)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationRequestCardContent(experienceReservationRequest: ExperienceReservationRequest, reservationViewModel: ExperienceReservationViewModel) {
    val from = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experienceReservationRequest.fromDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    val to = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(experienceReservationRequest.toDate.time).atZone(
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
                text = stringResource(id = R.string.tourist_name)+": ",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${experienceReservationRequest.touristFirstName} ${experienceReservationRequest.touristLastName}",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.destination)+": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = experienceReservationRequest.address.city,
                color = MaterialTheme.colors.onSecondary,
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.from_to)+": ${from.value.text} / ${to.value.text}",
                color = MaterialTheme.colors.onSecondary
            )

        }
        Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)) {
            Text(
                text = stringResource(id = R.string.price)+": ",
                color = MaterialTheme.colors.onSecondary
            )
            Text(
                text = experienceReservationRequest.price.toString(),
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle()
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
        Spacer(modifier = Modifier.height(10.dp))
        AcceptRejectRequest(
            reservationRequest = experienceReservationRequest,
            reservationViewModel = reservationViewModel
        )
    }
}

@Composable
fun AcceptRejectRequest(reservationRequest: ExperienceReservationRequest, reservationViewModel: ExperienceReservationViewModel) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp)
    ) {

        OutlinedButton(
            onClick = { reservationViewModel.rejectReservationRequest(reservationRequest) },
            enabled = !reservationViewModel.rejectReservationRequest.inProgress,
        ) {
            Text(
                stringResource(id = R.string.reject_request),
                color = MaterialTheme.colors.onError,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if(reservationViewModel.rejectReservationRequest.inProgress) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(22.dp)
                )
            }
        }

        Button(
            onClick = { reservationViewModel.acceptReservationRequest(reservationRequest) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.primary
            ),
            enabled = !reservationViewModel.acceptReservationRequest.inProgress,
        ) {
            Text(
                stringResource(id = R.string.accept_request),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 5.dp)
            )
            if(reservationViewModel.acceptReservationRequest.inProgress) {
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