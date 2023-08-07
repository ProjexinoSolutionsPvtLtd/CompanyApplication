package com.spn.companyapplication.components

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.spn.companyapplication.R
import com.spn.companyapplication.models.Lead
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@Composable
fun LeadCard(lead: Lead, context: Context, viewModel: ViewLeadViewModel, activity: Activity) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    if(viewModel.showUpdateDialog){
        StatusUpdateDialog(
            showDialog = viewModel.showUpdateDialog,
            onDismiss = { viewModel.showUpdateDialog = false },
            viewModel = viewModel,
            context = context,
            activity = activity
        )
    }

    Card(
        elevation = 5.dp, modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    lead.name, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                        fontSize = 19.sp,
                        color = Color.Black
                    )
                )
            }

            Spacer(Modifier.height(5.dp))

            Box(
                Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color(("#103b5c").toColorInt()).copy(0.5f))
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            viewModel.currentLeadId = lead.id
                            viewModel.showUpdateDialog = true
                        }
                    )
            ) {
                Text(
                    lead.status, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    ),
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }

            Spacer(Modifier.height(5.dp))

            iconWithText(icon = R.drawable.ic_number, text = lead.number)
            iconWithText(icon = R.drawable.ic_mail, text = lead.email)

            Spacer(Modifier.height(10.dp))

            if (isExpanded) {
                Divider(color = Color.LightGray.copy(0.7f))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    headingWithValue(heading = "Role", value = lead.role, weight = 0.5f)
                    headingWithValue(
                        heading = "Organization",
                        value = lead.organization,
                        weight = 0.5f
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    headingWithValue(
                        heading = "Requirement",
                        value = lead.requirement,
                        weight = 0.5f
                    )
                    headingWithValue(
                        heading = "Date/Time",
                        value = lead.dateTimeValue,
                        weight = 0.5f
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    headingWithValue(heading = "Address", value = lead.address, weight = 1f)
                }

                if (lead.documentUrl != "") {
                    Divider(
                        color = Color.LightGray.copy(0.7f),
                        modifier = Modifier.padding(vertical = 10.dp)
                    )

                    Column(modifier = Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                viewModel.openDocument(context, lead.documentUrl)
                            }
                        )) {
                        Text(
                            "Document", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        DocumentCard(lead.documentName, lead.documentType)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }

                //TODO: Implement fetching of image
//                if(lead.imageUrl != ""){
//                    if(lead.documentUrl == ""){
//                        Divider(color = Color.LightGray.copy(0.7f), modifier = Modifier.padding(vertical = 10.dp))
//                    }
//
//                    Text(lead.imageUrl)
//
//                    val image = rememberImagePainter(data = lead.imageUrl)
//
//                    Image(image, contentDescription = "")
//                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        onClick = {
                            isExpanded = !isExpanded
                        }
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "View Less" else "View More", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 16.sp,
                        color = Color(("#130b5c").toColorInt())
                    )
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    "",
                    tint = Color(("#130b5c").toColorInt()),
                    modifier = Modifier
                        .rotate(rotationState)
                        .clickable {
                            isExpanded = !isExpanded
                        }
                )
            }
        }
    }
}

@Composable
fun iconWithText(icon: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(id = icon), "")
        Spacer(Modifier.width(10.dp))
        Text(
            text, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 17.sp,
                color = Color.Black
            )
        )
    }
}

@Composable
fun RowScope.headingWithValue(heading: String, value: String, weight: Float) {
    Column(modifier = Modifier.weight(weight)) {
        Text(
            heading, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                fontSize = 14.sp,
                color = Color.Gray
            )
        )
        Text(
            value, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 17.sp,
                color = Color.Black
            )
        )
    }
}