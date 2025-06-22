import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.techcube.remootio.models.Action
import com.techcube.remootio.models.ActionStatus
import com.techcube.remootio.services.Repositories

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top)
    ) {
        Text(
            text = "Remootio",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top)
        ) {
            ActionButton(
                text = "Open / Close Doors",
                onClick = {
                    Repositories.actionRepository.sendAction(
                        Action(
                            status = ActionStatus.OPEN,
                            timestamp = Timestamp.now().toDate().getTime()
                        )
                    )
                },
                color = Color(0xFF3A86FF),
                modifier = Modifier.weight(1f)
            )

            ActionButton(
                text = "Keep Doors Opened",
                onClick = {
                    Repositories.actionRepository.sendAction(
                        Action(
                            status = ActionStatus.KEEP_OPENED,
                            timestamp = Timestamp.now().toDate().getTime()
                        )
                    )
                },
                color = Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
        }
    }

}