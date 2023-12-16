package com.spn.companyapplication.components

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.models.Lead
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@Composable
fun StatusUpdateDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onChange: () -> Unit,
    onCommentsChange: (String) -> Unit,
    onStatusUpdateDropdownOptionSelect: (String) -> Unit,
    hint: String,
    showStatusUpdateOptions: Boolean,
    setShowStatusUpdateOption: (Boolean) -> Unit,
    filterOptions: List<String>,
    commentForStatusUpdate: String,
    onSubmit: () -> Unit
) {
    val rotationState by animateFloatAsState(
        targetValue = if (showStatusUpdateOptions) 180f else 0f
    )
    if (showDialog) {
        Dialog(onDismissRequest = { onDismiss.invoke() }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        "Status Update", style = TextStyle(
                            fontFamily = FontFamily(Font(com.spn.companyapplication.R.font.outfit_bold)),
                            fontSize = 21.sp,
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Dropdown(
                        onDropdownMenuChange = { onChange.invoke() },
                        hint = hint,
                        rotationState = rotationState,
                        showOptions = showStatusUpdateOptions,
                        onDismiss = { setShowStatusUpdateOption(false) },
                        onSelected = { onStatusUpdateDropdownOptionSelect(it) },
                        options = filterOptions
                    )
                    TextInput(label = "Comments",
                        value = commentForStatusUpdate,
                        onChange = { onCommentsChange(it) })

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        text = "Update Status",
                        enabled = hint != "Select Status" && commentForStatusUpdate != "",
                        onClick = {
                            onSubmit.invoke()
                        },
                        color = Color(("#130b5c").toColorInt()),
                        uppercase = false,
                        padding = 2.dp
                    )
                }
            }
        }
    }
}