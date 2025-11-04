package com.communere.composeui_basics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.communere.composeui_basics.ui.theme.ComposeUIBasicsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeUIBasicsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfileScreen()
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    var isFollowing by remember { mutableStateOf(false) }

    val cardElevation by animateDpAsState(
        targetValue = if (isFollowing) 16.dp else 8.dp,
        label = "cardElevation"
    )

    val cardPadding by animateDpAsState(
        targetValue = if (isFollowing) 32.dp else 24.dp,
        label = "cardPadding"
    )

    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(), // smooth resizing
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = cardElevation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile picture",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Hessam Rastegari",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Android Developer",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            // Animate description fade/visibility
            AnimatedVisibility(visible = !isFollowing) {
                Text(
                    text = "\"Crafting modern mobile experiences.\"",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)
                        .fillMaxWidth(),
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FollowButton(
                    isFollowing = isFollowing,
                    onClick = { isFollowing = !isFollowing }
                )
                OutlinedButton(onClick = { /* TODO: Message */ }) {
                    Text("Message")
                }
            }
        }
    }
}

@Composable
fun FollowButton(
    isFollowing: Boolean,
    onClick: () -> Unit
) {
    val transition = updateTransition(targetState = isFollowing, label = "followTransition")

    val backgroundColor by transition.animateColor(label = "backgroundColor") { followed ->
        if (followed) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.primary
    }

    val textColor by transition.animateColor(label = "textColor") { followed ->
        if (followed) MaterialTheme.colorScheme.onPrimaryContainer
        else MaterialTheme.colorScheme.onPrimary
    }

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
    ) {
        Crossfade(targetState = isFollowing, label = "followText") { followed ->
            if (followed) {
                Text("Following ✓", color = textColor)
            } else {
                Text("Follow", color = textColor)
            }
        }
    }
}



// --- PREVIEWS ---------------------------------------------------------

@Preview(name = "Light Theme", showBackground = true)
@Composable
fun ProfilePreviewLight() {
    ComposeUIBasicsTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(name = "Dark Theme", showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProfilePreviewDark() {
    ComposeUIBasicsTheme(darkTheme = true) {
        ProfileScreen()
    }
}

@Preview(name = "Following State", showBackground = true)
@Composable
fun ProfilePreviewFollowing() {
    ComposeUIBasicsTheme {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Following ✓", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

