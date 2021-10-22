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
import androidx.navigation.NavHostController
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.skydoves.landscapist.coil.CoilImage

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
fun ReservationsContent(navController: NavHostController? = null) {
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
                text = "Your reservations",
                color = MaterialTheme.colors.onSecondary,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 20.dp)
            )
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            ReservationCard(name = "Juanito", lastname = "Perez", imgSize = 70.dp, navController = navController)
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            ReservationCard(name = "Juanito", lastname = "Perez", imgSize = 70.dp, navController = navController)
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            ReservationCard(name = "Juanito", lastname = "Perez", imgSize = 70.dp, navController = navController)
            Spacer(modifier = Modifier.padding(bottom = 10.dp))
            ReservationCard(name = "Juanito", lastname = "Perez", imgSize = 70.dp, navController = navController)
        } }
    )
}

@Composable
fun ReservationCard(name: String, lastname: String, imageUrl: String = "", imgSize: Dp, navController: NavHostController? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = MaterialTheme.colors.secondary),
                onClick = { navController?.navigate("details") }
            ),
        elevation = 10.dp,
        content = {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {
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
                Column(modifier = Modifier.padding(start = 20.dp)) {
                    Text(
                        text = "$name $lastname",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                    Row(
                        content = {
                            Icon(imageVector = Icons.Default.EventAvailable, contentDescription = "Dates")
                            Text(modifier = Modifier.padding(start = 5.dp),text = "Oct 25 - Oct 27")
                        }
                    )
                    Row(
                        modifier = Modifier.padding(top = 10.dp),
                        content = {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Dates",
                                tint = MaterialTheme.colors.secondary
                            )
                            Text(
                                modifier = Modifier.padding(start = 5.dp).clickable(onClick = { /* todo */ }),
                                text = stringResource(id = R.string.send_message),
                                style = TextStyle(color = MaterialTheme.colors.secondary, fontSize = 20.sp)
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