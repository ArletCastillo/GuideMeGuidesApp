package com.example.guidemeguidesapp.views.editGuideExperience

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemeguidesapp.R
import com.example.guidemeguidesapp.viewModels.GuideExperienceViewModel
import com.example.guidemetravelersapp.helpers.commonComposables.DescriptionTags

@Composable
fun ManageGuideExperience(guideExperienceViewModel: GuideExperienceViewModel = viewModel()) {
    val description = remember { mutableStateOf(TextFieldValue(text = guideExperienceViewModel.editableGuideExperience.experienceDescription)) }
    val price = remember { mutableStateOf(TextFieldValue(text = guideExperienceViewModel.editableGuideExperience.experiencePrice.toString())) }
    val tag = remember { mutableStateOf(TextFieldValue(text = "")) }
    val tags = remember { mutableStateOf(guideExperienceViewModel.editableGuideExperience.experienceTags) }
    val focusManager = LocalFocusManager.current

    description.value = TextFieldValue(guideExperienceViewModel.editableGuideExperience.experienceDescription ?: "")
    price.value = TextFieldValue(guideExperienceViewModel.editableGuideExperience.experiencePrice.toString() ?: "")
    tags.value = guideExperienceViewModel.editableGuideExperience.experienceTags

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.your_guide_experience),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.experience_cost),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = price.value,
                    onValueChange = { value ->
                        price.value = value
                        if(price.value.text.isNotEmpty())
                            guideExperienceViewModel.editableGuideExperience.experiencePrice = value.text.toFloat()
                    },
                    label = { Text(text = stringResource(id = R.string.experience_cost)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    maxLines = 1
                )
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 25.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.describe_experience),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { value ->
                        description.value = value
                        guideExperienceViewModel.editableGuideExperience.experienceDescription = value.text
                    },
                    label = { Text(text = stringResource(id = R.string.describe_experience)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Default,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    maxLines = 10
                )
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 25.dp)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(id = R.string.experience_tags),
                    color = MaterialTheme.colors.onSecondary,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
            ) {
                for (tagItem in tags.value) {
                    DescriptionTags(tagName = tagItem)
                }
            }

            Row(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = tag.value,
                    onValueChange = { value ->
                        tag.value = value
                    },
                    label = { Text(text = stringResource(id = R.string.add_tag)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    maxLines = 1,
                    trailingIcon = {
                        TextButton(onClick = {
                            tags.value.add(tag.value.text)
                            tag.value = TextFieldValue("")
                            focusManager.clearFocus()
                        }) {
                            Text(
                                text = stringResource(id = R.string.add_tag),
                                color = MaterialTheme.colors.onSecondary,
                                style = MaterialTheme.typography.caption,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                )
            }

            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {guideExperienceViewModel.updateExperience(tags.value)},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp)
                        .fillMaxWidth(),
                    content = {
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                            Text(stringResource(id = R.string.save), color = Color.White, fontWeight = FontWeight.Bold)
                            if (guideExperienceViewModel.updateGuideExperienceStatus.inProgress) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(25.dp)
                                )
                            }
                            else if (!guideExperienceViewModel.updateGuideExperienceStatus.inProgress
                                && !guideExperienceViewModel.updateGuideExperienceStatus.hasError
                                && guideExperienceViewModel.updateGuideExperienceStatus.data!!) {
                                Icon(imageVector = Icons.Default.CheckCircleOutline, tint = Color.White, contentDescription = "" )
                            }
                        }
                    }
                )
            }

        }
    }
}