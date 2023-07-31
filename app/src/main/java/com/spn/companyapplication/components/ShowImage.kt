import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R

@Composable
fun ShowImage(showImage: Boolean, onDismiss: () -> Unit, bitmap: Bitmap) {
    if (showImage) {
        Dialog(onDismissRequest = onDismiss) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White),
            ) {
                Column(Modifier.padding(horizontal = 20.dp, vertical = 25.dp)) {
                    Image(bitmap = bitmap.asImageBitmap(), "", Modifier.size(300.dp).padding(top = 10.dp))
                    Button(
                        onClick = {
                            onDismiss.invoke()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = "Close", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                                fontSize = 17.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }
        }
    }
}