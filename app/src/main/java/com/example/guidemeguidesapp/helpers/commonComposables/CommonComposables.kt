package com.example.guidemetravelersapp.helpers.commonComposables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.guidemeguidesapp.ui.theme.AcceptGreen
import com.example.guidemeguidesapp.ui.theme.CancelRed
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun FullsizeImage(imageUrl: String) {
    Column(Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CoilImage(imageModel = imageUrl,
                contentDescription = "User profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(RectangleShape))
        }
    }
}

@Composable
fun LoadingBar() {
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
}

@Composable
fun LoadingSpinner() {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun SuccessCheckmark() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(
            imageVector = Icons.Default.CheckCircleOutline,
            contentDescription = "Success",
            tint = AcceptGreen,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun Failed() {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = "Error",
            tint = CancelRed,
            modifier = Modifier.size(20.dp)
        )
        Text(text = "Error", color = CancelRed)
    }
}

@Composable
fun DescriptionTags(tagName: String) {
    Box(modifier = Modifier.padding(end = 10.dp)) {
        Text(
            text = tagName,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.secondaryVariant,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            overflow = TextOverflow.Ellipsis,
            color = Color.White,
            style = MaterialTheme.typography.caption
        )
    }
}