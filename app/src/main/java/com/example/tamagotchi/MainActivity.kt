package com.example.tamagotchi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tamagotchi.ui.theme.TamagotchiTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamagotchiTheme {
                val tamagotchi = remember { Tamagotchi() }
                TamagotchiScreen(tamagotchi)
            }
        }
    }
}


@Composable
fun TamagotchiScreen(tamagotchi: Tamagotchi) {

    var currentImage by remember { mutableIntStateOf(R.drawable.feliz) }
    var showPelota by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = -50f, // Moverse 50 unidades para arriba
        targetValue = 50f,  // Moverse 50 unidades hacia abajo
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing), // Animación de 0.6s
            repeatMode = RepeatMode.Reverse // Repetir inversamnete
        ),
        label = "Vertical "
    )

    val offsetX by infiniteTransition.animateFloat(
        initialValue = -30f, // izquierda
        targetValue = 30f,   // derecha
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Horizontal"
    )

    LaunchedEffect1(Unit) {
        while (true) {
            delay(10000)
            tamagotchi.hambre = (tamagotchi.hambre - 10).coerceAtLeast(0)
            tamagotchi.felicidad = (tamagotchi.felicidad - 5).coerceAtLeast(0)
            tamagotchi.energia = (tamagotchi.energia - 5).coerceAtLeast(0)
        }
    }
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFF6650a4))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column (modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Hambre: ${tamagotchi.hambre}", color = Color.White)
                    Text(text = "Felicidad: ${tamagotchi.felicidad}", color = Color.White)
                    Text(text = "Energía: ${tamagotchi.energia}", color = Color.White)
                }
                if(showPelota) {
                    Image(
                        painter = painterResource(id = R.drawable.pelota),
                        contentDescription = "Imagen de uan pelota",
                        modifier = Modifier
                            .size(500.dp)
                            .padding(16.dp)
                            .offset(x = offsetX.dp, y = offsetY.dp)
                    )
                }else {
                    Image(
                        painter = painterResource(id = currentImage),
                        contentDescription = "Imagen del Tamagotchi",
                        modifier = Modifier
                            .size(500.dp)
                            .padding(16.dp)
                    )
                }


                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { tamagotchi.alimentar()
                        currentImage = R.drawable.comiendo
                        kotlinx.coroutines.MainScope().launch {
                            delay(2000)
                            currentImage = R.drawable.feliz
                        }
                    },
                        modifier = Modifier
                            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    ) {
                        Text("Alimentar")
                    }
                    Button(onClick = { tamagotchi.jugar()
                                         showPelota = true
                                        kotlinx.coroutines.MainScope().launch {
                                            delay(2000)
                                            showPelota = false
                                            }
                                     },
                        modifier = Modifier
                            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    ) {
                        Text("Jugar")
                    }
                    Button(onClick = { tamagotchi.dormir()
                                         currentImage = R.drawable.durmiendo
                                         kotlinx.coroutines.MainScope().launch{
                                             delay(2000)
                                             currentImage = R.drawable.feliz
                                         }
                                      },
                        modifier = Modifier
                            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
                    ){
                        Text("Dormir")
                    }
                }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun TamagotchiScreenPreview() {
    TamagotchiTheme {
        val tamagotchi = Tamagotchi()
        TamagotchiScreen(tamagotchi)
    }
}