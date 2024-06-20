package com.example.quizapp.ui.components.basic.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    type: String = "filled",
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable() (RowScope.() -> Unit)
) {
    if (type == "filled") {
        Button(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            colors = colors,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            content = content
        )
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            elevation = elevation,
            contentPadding = contentPadding,
            interactionSource = interactionSource,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
            content = content
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    CustomButton(onClick = {}, type = "outlined") {
        Text(text = "Logout", style = MaterialTheme.typography.titleSmall)
    }
}