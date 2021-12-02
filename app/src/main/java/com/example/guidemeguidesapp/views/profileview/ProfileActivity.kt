package com.example.guidemeguidesapp.views.profileview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.guidemeguidesapp.ui.theme.GuideMeGuidesAppTheme
import com.example.guidemeguidesapp.viewModels.ProfileViewModel
import com.example.guidemetravelersapp.helpers.commonComposables.LoadingBar
import com.gowtham.ratingbar.RatingBar
import com.skydoves.landscapist.coil.CoilImage
import java.net.URLEncoder
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.dataModels.Review
import com.example.guidemeguidesapp.dataModels.User

class ProfileActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeGuidesAppTheme {
                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxHeight()) {
                    UserProfileInformation()
                }
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun UserProfileInformation(profileViewModel: ProfileViewModel = viewModel(), navController: NavHostController? = null) {

    if(profileViewModel.profileData.inProgress) {
        Column(verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxWidth()) {
            LoadingBar()
        }
    }
    else {
        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()) {
            item {
                Spacer(modifier = Modifier.height(30.dp))

                UserInfo(profileViewModel.profileData.data!!, navController!!)

                //Rating stars for the guide
                Spacer(modifier = Modifier.height(10.dp))
                UserRating(profileViewModel.calculateUserRating())
                EditProfileButton(navController, profileViewModel)

                Spacer(modifier = Modifier.height(15.dp))
                Divider(color = MaterialTheme.colors.onPrimary, thickness = 1.dp)
                Spacer(modifier = Modifier.height(15.dp))

                AboutUser(profileViewModel.profileData.data!!.aboutUser)

                Spacer(modifier = Modifier.height(15.dp))

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = MaterialTheme.colors.onPrimary, thickness = 1.dp)
                Spacer(modifier = Modifier.height(15.dp))
            }

            stickyHeader {
                UserOverviewRating(profileViewModel.profileData.data!!.reviews.size)
            }

            if(profileViewModel.profileData.data!!.reviews.isNotEmpty()) {
                itemsIndexed(profileViewModel.profileData.data!!.reviews) { index, item ->
                    UserReview(item)
                }
            }
        }
    }
}

@Composable
fun UserRating(userRating: Float = 0.0f) {
    RatingBar(value = userRating, size = 20.dp, isIndicator = true) { }
}

@Composable
fun EditProfileButton(navController: NavHostController, profileViewModel: ProfileViewModel) {
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedButton(onClick = {
        profileViewModel.editableUser = profileViewModel.profileData.data!!
        navController.navigate("editProfile")
    },
        modifier = Modifier.wrapContentWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colors.primary)
    ) {
        Row(horizontalArrangement = Arrangement.Center){
            Icon(imageVector = Icons.Filled.Edit,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(20.dp).padding(end = 5.dp))
            Text(stringResource(id = R.string.edit_profile_button), color = MaterialTheme.colors.primary)
        }
    }
}

@Composable
fun UserInfo(user: User = User(), navController: NavHostController) {
    Box(modifier = Modifier
        .size(120.dp)
        .border(2.dp, MaterialTheme.colors.secondary, CircleShape)
        .clickable { navController.navigate("profile_photo/photo=${URLEncoder.encode(user.profilePhotoUrl, "utf-8")}") }) {
        if (user.profilePhotoUrl.isNotEmpty()) {
            CoilImage(imageModel = user.profilePhotoUrl,
                contentDescription = "User profile photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape))
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
    Text(
        text = "${user.firstName} ${user.lastName}",
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.onSecondary,
        fontSize = 25.sp
    )
}

@Composable
fun AboutUser(experienceDescription: String = "") {
    Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){
        Text(
            text = stringResource(id = R.string.about_user),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(start = 15.dp)
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    Text(text = experienceDescription,
        color = MaterialTheme.colors.onSecondary,
        modifier = Modifier.padding(horizontal = 15.dp),
        style = MaterialTheme.typography.body1
    )
}

@Composable
fun UserOverviewRating(reviewsCount: Int = 0) {
    Row(horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.reviews_text),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier.padding(start = 15.dp)
        )
        Text(
            text = "($reviewsCount)",
            fontSize = 10.sp,
            color = MaterialTheme.colors.onPrimary,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
fun UserReview(review: Review = Review()) {
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(15.dp)) {
        Box(modifier = Modifier.size(50.dp)) {
            if (review.profilePhotoUrl.isNotEmpty()) {
                CoilImage(imageModel = review.profilePhotoUrl,
                    contentDescription = "User profile photo",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape))
            } else {
                Image(
                    painter = painterResource(R.drawable.dummy_avatar),
                    contentDescription = "Temporal dummy avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }
        Column(modifier = Modifier.padding(start = 15.dp)) {
            Text(
                text = review.userName,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onSecondary,
                fontSize = 15.sp
            )
            RatingBar(value = review.ratingValue, size = 15.dp, isIndicator = true) {
            }
            Text(text = review.ratingComment,
                color = MaterialTheme.colors.onPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )
        }
    }
}

@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    GuideMeGuidesAppTheme {
        UserProfileInformation()
    }
}