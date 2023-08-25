package com.spn.companyapplication.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.screens.Register
import com.spn.companyapplication.viewmodels.RegisterViewModel
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@Composable
fun Dropdown(
    onDropdownMenuChange: () -> Unit,
//    onClickActions: Map<String, () -> Unit>,
    showOptions: Boolean,
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit,
    options: List<String>,
    hint: String,
    rotationState: Float
) {
    Card(
        elevation = 0.dp,
        border = BorderStroke(
            1.dp, Color(("#9f9f9f").toColorInt())
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 55.dp)
            .clickable {
                onDropdownMenuChange.invoke()
            }) {
        Column(
            Modifier
                .padding(
                    horizontal = 13.dp,
                    vertical = 8.dp
                ), verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    hint,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_down),
                    contentDescription = "",
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(rotationState)
                )
            }
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd){
                DropdownMenu(
                    showOptions = showOptions,
                    onDismiss = { onDismiss.invoke() },
                    options = options,
                    onSelected = onSelected,
                    onClickMap = mapOf(),
//                    getSelectedOption = {  }
                )
            }
        }
    }
}