package com.example.caloriescreen

import android.icu.text.CaseMap.Title
import android.icu.text.CaseMap.toTitle
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.support.v4.os.IResultReceiver.Default
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import com.example.caloriescreen.ui.theme.CalorieScreenTheme
import java.io.DataInput
import java.io.ObjectOutputStream.PutField

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalorieScreenTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalorieScreen()
                }

                }
            }
        }
    }

@Composable
fun CalorieScreen() {
    var weightInput by remember { mutableStateOf("") }
    var weight = weightInput.toIntOrNull() ?: 0
    var male by remember { mutableStateOf(true)}
    var intensity by remember { mutableStateOf(1.3f) }
    var result = remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
        )  {
            Heading(title = stringResource(R.string.calories))
            WeightField(weightInput = weightInput, onValueChange = {weightInput = it})
            GenderChoices(male, setGenderMale = { male = it })
            IntensityList(onClick = {intensity = it})
            Text (text = result.toString(), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
            Calculation(male = male, weight = weight, intensity = intensity, setResult = {result = it})
        }
    }



@Composable
fun WeightField(weightInput:String,onValueChange:(String) -> Unit) {
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter weight") },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

// Tähän heding
@Composable
fun Heading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )

}
    // GenderCoi.
    @Composable
    fun  GenderChoices(male: Boolean,setGenderMale:(Boolean)-> Unit) {
        Column(Modifier.selectableGroup()) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = male,
                    onClick = {setGenderMale(true)}
                )
                Text(text = "Male")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = !male,
                    onClick = {setGenderMale(false)}
                )
                Text(text = "Female")
            }
        }
    }

// intensity..
@Composable
fun IntensityList(onClick: (Float) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very hard")

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown
    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        textFieldSize = coordinates.size.toSize()
                    },
            label = { Text(text = "Select intensity") },
            trailingIcon = {
                Icon(
                    icon, contentDescription =  "contentDescription",
                Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(textFieldSize.width.toDp())

        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    val intensity: Float = when (label) {
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2f
                        "Very Hard" -> 2.2f
                        else -> 0.0f
                    }
                    onClick(intensity)
                    expanded = false
                }) {

                    Text(text = label)
                }
            }
       }
    }
}
//CALCULATIO
@Composable
fun Calculation(male: Boolean,weight: Int, intensity: Float, setResult:(Int) -> Unit) {
    Button(
        onClick = {
            if (male) {
                setResult(((879 + 10.2 * weight) * intensity).toInt())
            } else {
                setResult(((795 + 7.18 * weight) * intensity).toInt())
            }
        },
        modifier = Modifier.fillMaxWidth()
    ){
        Text(text = "CALCULATE")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalorieScreenTheme {


        CalorieScreen()

    }
}

