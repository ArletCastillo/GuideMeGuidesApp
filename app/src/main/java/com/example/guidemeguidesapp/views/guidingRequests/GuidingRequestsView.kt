package com.example.guidemeguidesapp.views.guidingRequests

import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.GuidingOffer
import com.example.guidemeguidesapp.dataModels.ReservationStatus
import com.example.guidemeguidesapp.ui.theme.AcceptGreen
import com.example.guidemeguidesapp.ui.theme.CancelRed
import com.example.guidemeguidesapp.ui.theme.WarningOrange
import com.example.guidemeguidesapp.viewModels.TouristAlertViewModel
import com.example.guidemeguidesapp.views.reservations.AcceptRejectRequest
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingSpinner
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalFoundationApi
@Composable
fun GuidingOffers(touristAlertViewModel: TouristAlertViewModel = viewModel()) {
    val isRefreshingGuidingRequests by touristAlertViewModel.isRefreshingGuidingRequests.collectAsState()
    touristAlertViewModel.getGuideOffers()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshingGuidingRequests),
        onRefresh = { touristAlertViewModel.isRefreshingGuidingRequests },
        content = {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp))  {
                item {
                    Text(
                        text = stringResource(id = R.string.guiding_offer),
                        color = MaterialTheme.colors.onSecondary,
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(bottom = 20.dp))
                }
                if (touristAlertViewModel.guideOffers.inProgress) {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            LoadingSpinner()
                        }
                    }
                } else {
                    if(!touristAlertViewModel.guideOffers.data.isNullOrEmpty()) {
                        itemsIndexed(touristAlertViewModel.guideOffers.data!!) { index, item ->
                            Card(
                                modifier = Modifier.padding(15.dp),
                                elevation = 15.dp,
                                content = {
                                    GuideOfferCardContent(item)
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
fun GuideOfferCardContent(guideOffer: GuidingOffer) {
    val from = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(guideOffer.fromDate.time).atZone(
                ZoneId.systemDefault()).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd-MM-yyyy")
            ))
    ) }
    val to = remember { mutableStateOf(
        TextFieldValue(
            Instant.ofEpochMilli(guideOffer.toDate.time).atZone(
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
                text = stringResource(id = R.string.tourist_name) +": ",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${guideOffer.touristFirstName} ${guideOffer.touristLastName}",
                color = MaterialTheme.colors.primary,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
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

        Status(status = ReservationStatus.values()[guideOffer.reservationStatus])
    }
}

@Composable
fun Status(status: ReservationStatus) {
    Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
    Spacer(modifier = Modifier.height(10.dp))
    when(status) {
        ReservationStatus.PENDING -> GuidingOfferStatus(
            color = WarningOrange,
            icon = Icons.Default.Warning,
            text = stringResource(id = R.string.pending)
        )
        ReservationStatus.ACCEPTED -> GuidingOfferStatus(
            color = AcceptGreen,
            icon = Icons.Default.CheckCircleOutline,
            text = stringResource(id = R.string.accepted)
        )
        ReservationStatus.REJECTED -> GuidingOfferStatus(
            color = CancelRed,
            icon = Icons.Default.DoNotDisturb,
            text = stringResource(id = R.string.rejected)
        )
    }
}

@Composable
fun GuidingOfferStatus(color: Color, icon: ImageVector, text: String) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(imageVector = icon,
            contentDescription = null,
            tint = color)
        Text(
            text = text,
            color = color,
            modifier = Modifier.padding(start = 10.dp)
        )
    }
}