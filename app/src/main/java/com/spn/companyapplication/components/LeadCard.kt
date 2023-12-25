package com.spn.companyapplication.components

import ShowImage
import android.app.Activity
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import coil.compose.rememberImagePainter
import com.spn.companyapplication.R
import com.spn.companyapplication.models.Lead
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@Composable
fun LeadCard(lead: Lead, context: Context, viewModel: ViewLeadViewModel, activity: Activity) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    if (viewModel.showImage) {
        ShowImage(
            showImage = viewModel.showImage,
            onDismiss = { viewModel.showImage = false },
            url = viewModel.currentImageUrl
        )
    }

    if (viewModel.showUpdateDialog) {
        StatusUpdateDialog(
            showDialog = viewModel.showUpdateDialog,
            onDismiss = { viewModel.showUpdateDialog = false },
            onChange = { viewModel.showStatusUpdateOptions =  !viewModel.showStatusUpdateOptions},
            onCommentsChange = { viewModel.commentForStatusUpdateChange(it) },
            onStatusUpdateDropdownOptionSelect = { viewModel.onStatusUpdateDropdownOptionSelect(it) },
            hint = viewModel.selectedStatusForUpdate,
            showStatusUpdateOptions = viewModel.showStatusUpdateOptions,
            setShowStatusUpdateOption = viewModel.setShowStatusUpdateOptions,
            filterOptions = viewModel.filterOptions,
            commentForStatusUpdate = viewModel.commentForStatusUpdate,
            onSubmit = {
                viewModel.updateStatus(viewModel.currentLeadId, context)
                viewModel.fetchLeads(activity)
                viewModel.clearDialogDetails()
            }
        )
    }

    Card(
        elevation = 5.dp, modifier = Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                InitialsIconLead(
                    name = lead.name,
                    backgroundColor = Color.LightGray,
                    contentColor = Color.DarkGray
                )

                Spacer(modifier = Modifier.width(15.dp))

                Column {
                    Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            lead.name, style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                                fontSize = 19.sp,
                                color = Color.Black
                            )
                        )

                        Box(
                            Modifier
                                .size(25.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(color = Color.Red)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                    onClick = {
                                        viewModel.deleteLead(lead.id)
                                    }
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete),
                                contentDescription = "delete",
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.Center).size(18.dp)
                            )
                        }
                    }
//            }

                    Spacer(Modifier.height(5.dp))

                    Box(
                        Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(viewModel.getStatusColor(lead.status))
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

                    iconWithTextLead(icon = R.drawable.ic_number, text = lead.number)

                    Spacer(Modifier.height(5.dp))
                }


            }

            if (isExpanded) {
                Spacer(Modifier.height(10.dp))
                Divider(color = Color.LightGray.copy(0.7f))
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    headingWithValueLead(heading = "Email", value = lead.email, weight = 1f)
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    headingWithValueLead(heading = "Role", value = lead.role, weight = 0.5f)
                    headingWithValueLead(
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
                    headingWithValueLead(
                        heading = "Requirement",
                        value = lead.requirement,
                        weight = 0.5f
                    )
                    headingWithValueLead(
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
                    headingWithValueLead(heading = "Address", value = lead.address, weight = 1f)
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

                if (lead.imageUrl != "") {
                    Spacer(modifier = Modifier.height(10.dp))
                    if (lead.documentUrl == "") {
                        Divider(
                            color = Color.LightGray.copy(0.7f),
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }

                    Text(
                        "Image", style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    )

                    Image(
                        rememberImagePainter(data = lead.imageUrl),
                        contentDescription = "",
                        modifier = Modifier
                            .size(200.dp)
                            .clickable {
                                viewModel.currentImageUrl = lead.imageUrl
                                viewModel.showImage = true
                            },
                        contentScale = ContentScale.Crop
                    )
                }
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
fun iconWithTextLead(icon: Int, text: String) {
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
fun InitialsIconLead(
    name: String,
    backgroundColor: Color,
    contentColor: Color,
    size: Int = 80,
    icon: ImageVector? = null,
) {
    Surface(
        modifier = Modifier.size(size.dp),
        shape = CircleShape,
        color = backgroundColor,
        contentColor = contentColor,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                )
            } else {
                Text(
                    text = getInitialsLead(name),
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 40.sp,
                        color = contentColor
                    )
                )
            }
        }
    }
}

@Composable
fun getInitialsLead(name: String): String {
    val initials = name.split(" ").joinToString("") { it.take(1) }
    return initials.take(2).toUpperCase()
}

@Composable
fun RowScope.headingWithValueLead(heading: String, value: String, weight: Float) {
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