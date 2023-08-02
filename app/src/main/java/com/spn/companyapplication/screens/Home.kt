package com.spn.companyapplication.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.R
import com.spn.companyapplication.components.Drawer
import com.spn.companyapplication.viewmodels.HomeViewModel
import kotlinx.coroutines.delay

class Home : ComponentActivity() {
    @OptIn(ExperimentalPagerApi::class)
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<HomeViewModel>()

        super.onCreate(savedInstanceState)
        setContent {
            val pagerState = rememberPagerState()
            val lifecycleObserver = rememberUpdatedState(ChangingImagesLifecycleObserver())
            LaunchedEffect(lifecycleObserver.value) {
                while (true) {
                    delay(5000L) // Change image and text every 5 seconds
                    viewModel.currentHomeScreenItemIndex =
                        (viewModel.currentHomeScreenItemIndex + 1) % viewModel.homeScreenItems.size
                }
            }
            LaunchedEffect(lifecycleObserver.value) {
                while (true) {
                    delay(3000L)
//                    viewModel.currentExpertiseItemIndex =
//                        (viewModel.currentExpertiseItemIndex + 1) % viewModel.expertiseItems.size
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                }
            }
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Drawer(
                        title = "Home",
                        context = this@Home,
                        activity = this@Home,
                        content = {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(ScrollState(0))
                            ) {
                                Box() {
                                    Crossfade(targetState = viewModel.currentHomeScreenItemIndex) { index ->
                                        Image(
                                            painter = painterResource(id = viewModel.homeScreenItems[index].icon),
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(500.dp)
//                                                .fillMaxSize()
                                                .alpha(0.6f),
                                        )
                                    }
                                    Column(
                                        Modifier
                                            .padding(16.dp)
                                            .offset(y = -(28).dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.logo),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .width(150.dp),
                                            tint = Color(("#130b5c").toColorInt())
                                        )
                                        Spacer(Modifier.height(30.dp))
                                        Crossfade(targetState = viewModel.currentHomeScreenItemIndex) { index ->
                                            Text(
                                                viewModel.homeScreenItems[index].title,
                                                style = TextStyle(
                                                    fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                                    fontSize = 18.sp,
                                                    color = androidx.compose.ui.graphics.Color.Black
                                                )
                                            )
                                        }

                                        Spacer(Modifier.height(20.dp))

                                        Crossfade(targetState = viewModel.currentHomeScreenItemIndex) { index ->
                                            Text(
                                                viewModel.homeScreenItems[index].text,
                                                style = TextStyle(
                                                    fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                                    fontSize = 50.sp,
                                                    color = androidx.compose.ui.graphics.Color.Black
                                                )
                                            )
                                        }
                                    }
                                }
                                Column(Modifier.padding(16.dp)) {
                                    Text(
                                        "Areas of Expertise", style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                                            fontSize = 15.sp,
                                            color = Color.Gray
                                        )
                                    )
                                    Text(
                                        "We're good at these areas to work", style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                                            fontSize = 23.sp,
                                            color = Color.Black
                                        )
                                    )

                                    Spacer(modifier = Modifier.height(10.dp))

                                    HorizontalPager(
                                        count = viewModel.expertiseItems.size,
                                        state = pagerState
                                    ) { page ->
                                        val expertiseItem =
                                            viewModel.expertiseItems[page]
                                        Card(
                                            Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(5.dp),
                                            elevation = 1.dp,
                                            border = BorderStroke(
                                                0.5.dp,
                                                Color.LightGray.copy(0.6f)
                                            )
                                        ) {
                                            Column(Modifier.padding(10.dp)) {
                                                Icon(
                                                    painter = painterResource(id = expertiseItem.icon),
                                                    contentDescription = null,
                                                    tint = Color(("#130b5c").toColorInt()),
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(RoundedCornerShape(8.dp)),
                                                )
                                                Text(
                                                    expertiseItem.title, style = TextStyle(
                                                        fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                                        fontSize = 18.sp,
                                                        color = Color.Black
                                                    )
                                                )
                                                Text(
                                                    expertiseItem.text, style = TextStyle(
                                                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                                                        fontSize = 17.sp,
                                                        color = Color.Black
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        })
                }
            }
        }
    }
}


class ChangingImagesLifecycleObserver : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        // This function is called when the app enters the foreground
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        // This function is called when the app enters the background
    }
}

class ScrollingCardsLifecycleObserver : LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        // This function is called when the app enters the foreground
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        // This function is called when the app enters the background
    }
}