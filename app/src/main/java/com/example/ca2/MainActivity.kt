package com.example.ca2

import android.os.Bundle
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Review(
    val email: String,
    val daysStay: Int,
    val location: Float,
    val valueForMoney: Float,
    val staff: Float
) {
    fun calculateScore(): Float {
        return (location * 0.4f) + (valueForMoney * 0.3f) + (staff * 0.3f)
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFEFEFEF)
                ) {
                    HostelReviewScreen()
                }
            }
        }
    }
}

@Composable
fun HostelReviewScreen() {
    var email by remember { mutableStateOf("") }
    var daysStay by remember { mutableStateOf("1") }

    var location by remember { mutableFloatStateOf(5f) }
    var valueForMoney by remember { mutableFloatStateOf(5f) }
    var staff by remember { mutableFloatStateOf(5f) }

    var showDaysMenu by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }

    val dayOptions = (1..14).map { it.toString() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEFEFEF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
                .height(430.dp)
                .border(2.dp, Color.Gray)
                .background(Color(0xFF0D6784))
                .padding(22.dp)
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Email",
                        modifier = Modifier.width(120.dp),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = false
                        },
                        modifier = Modifier.width(180.dp),
                        singleLine = true,
                        isError = emailError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "No of Days Stay",
                        modifier = Modifier.width(120.dp),
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Box {
                        OutlinedTextField(
                            value = daysStay,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            modifier = Modifier.width(80.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        DropdownMenu(
                            expanded = showDaysMenu,
                            onDismissRequest = { showDaysMenu = false }
                        ) {
                            dayOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        daysStay = option
                                        showDaysMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                if (emailError) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Please enter a valid email",
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                RatingRow("Location", location) { location = it }
                Spacer(modifier = Modifier.height(24.dp))
                RatingRow("Value for Money", valueForMoney) { valueForMoney = it }
                Spacer(modifier = Modifier.height(24.dp))
                RatingRow("Staff", staff) { staff = it }
            }
        }

        Spacer(modifier = Modifier.height(60.dp))

        Button(
            onClick = {
                val validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches()

                if (!validEmail) {
                    emailError = true
                    resultText = ""
                } else {
                    val review = Review(
                        email = email,
                        daysStay = daysStay.toInt(),
                        location = location,
                        valueForMoney = valueForMoney,
                        staff = staff
                    )

                    resultText = "Review score: %.2f".format(review.calculateScore())

                    email = ""
                    daysStay = "1"
                    location = 5f
                    valueForMoney = 5f
                    staff = 5f
                    emailError = false
                }
            },
            modifier = Modifier
                .width(175.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF56B63A)
            )
        ) {
            Text(
                text = "Send Review",
                color = Color.Black,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (resultText.isNotEmpty()) {
            Text(
                text = resultText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}

@Composable
fun RatingRow(
    label: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = 0f..10f,
                steps = 9,
                modifier = Modifier.width(190.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF3A7BD5),
                    activeTrackColor = Color(0xFF3A7BD5),
                    inactiveTrackColor = Color(0xFFD9D9D9)
                )
            )
        }

        Spacer(modifier = Modifier.width(22.dp))

        Text(
            text = label,
            color = Color.Black,
            fontSize = 17.sp
        )
    }
}