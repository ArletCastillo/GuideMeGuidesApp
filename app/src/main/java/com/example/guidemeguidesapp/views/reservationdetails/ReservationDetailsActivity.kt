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
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.views.homescreen.DescriptionTags
import com.skydoves.landscapist.coil.CoilImage
import com.gowtham.ratingbar.RatingBar

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
fun ReservationDetailsContent() {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        content = {
            UserCard(name = "Karen", country = "United States", imgSize = 100.dp)
            Divider(thickness = 4.dp, modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            AboutUser(description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.")
            Text(
                text = stringResource(id = R.string.category),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(top = 10.dp, bottom = 10.dp))
            DescriptionTags(tagName = "cultural")
            Row(
                modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                content = {
                    Text(
                        text = stringResource(id = R.string.group_quantity) + ":",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary)
                    Text(text = "2", modifier = Modifier.padding(start = 5.dp, top = 5.dp))
                }
            )
            Row(
                modifier = Modifier.padding(bottom = 20.dp),
                content = {
                    Icon(imageVector = Icons.Default.CheckCircleOutline, contentDescription = "success")
                    Text(text = stringResource(id = R.string.payment_successful), modifier = Modifier.padding(start = 5.dp))
                }
            )
            Button(
                onClick = { /* TODO */ },
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                content = {
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                        Text(stringResource(id = R.string.send_message), color = Color.White)
                    }
                }
            )
        }
    )
}

@Composable
fun UserCard(
    name: String,
    country: String,
    imgSize: Dp,
    imageUrl: String = "",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(bottom = 10.dp)
            .fillMaxWidth(),
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
                Text(text = name, style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold))
                Text(text = country, style = TextStyle(fontSize = 20.sp))
                UserRating(4.5f)
                Row(
                    content = {
                        Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates")
                        Text(modifier = Modifier.padding(start = 5.dp),text = "Oct 25 - Oct 27")
                    }
                )
            }
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
                    .size(80.dp)
                    .padding(end = 20.dp))
            Icon(
                painter = painterResource(id = R.drawable.united_kingdom),
                contentDescription = "english",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 20.dp))
            Icon(
                painter = painterResource(id = R.drawable.germany),
                contentDescription = "german",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 20.dp))
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
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = description,
        color = MaterialTheme.colors.onPrimary,
        fontSize = 17.sp
    )
}

@Preview(showBackground = true)
@Composable
fun ReservationDetailsPreview() {
    GuideMeGuidesAppTheme {
        ReservationDetailsContent()
    }
}