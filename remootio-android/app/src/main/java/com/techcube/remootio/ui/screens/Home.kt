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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techcube.remootio.R
import com.techcube.remootio.models.Action
import com.techcube.remootio.services.Repositories
import com.techcube.remootio.services.actions.ActionsRepository
import com.techcube.remootio.services.actions.ActionsRepositoryImpl

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
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.Top)
        ) {
            ActionButton(
                text = "Open / Close Doors",
                onClick = {
                    Repositories.actionRepository.sendAction(Action.OPEN)
                },
                color = Color(0xFF3A86FF),
                modifier = Modifier.weight(1f)
            )

            ActionButton(
                text = "Keep Doors Opened",
                onClick = {
                    Repositories.actionRepository.sendAction(Action.KEEP_OPENED)
                },
                color = Color(0xFF1DD3B0),
                modifier = Modifier.weight(1f)
            )
        }
    }

}