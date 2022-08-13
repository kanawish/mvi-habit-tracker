@file:OptIn(ExperimentalMaterialApi::class)

package com.kanastruk.sample.habit.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 */
@Composable
fun DropDownPicker(
    label:String,
    initialPick: String,
    options: List<IconOption>,
    leadingIcon: ImageVector
) {
    // TODO: This should be built from 'permanent' model-side immutable type/enum.
    // NOTE: Would usually be localized, etc.

    // Expanded or not makes sense as internal state.
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(initialPick) }

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        expanded = false,
        onExpandedChange = { typeExpanded = !typeExpanded }
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            value = selectedOptionText,
            onValueChange = { },
            label = { Text(label) },
            leadingIcon = {
                // TODO change depending on pick?
                Icon(leadingIcon, null)
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = typeExpanded,
            onDismissRequest = { typeExpanded = false }
        ) {
            options.forEach { selectionOption ->
                val onClick = {
                    selectedOptionText = selectionOption.label
                    typeExpanded = false
                    selectionOption.pickHandler() // Trigger whatever parent wants.
                }
                DropdownMenuItem(onClick = onClick) {
                    selectionOption.icon?.let { IconFun ->
                        IconFun()
                        Spacer(Modifier.padding(end = 16.dp))
                    }
                    Text(text = selectionOption.label)
                }
            }
        }
    }
}