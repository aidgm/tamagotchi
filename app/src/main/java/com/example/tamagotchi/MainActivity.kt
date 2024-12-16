package com.example.tamagotchi

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tamagotchi.ui.theme.TamagotchiTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect as LaunchedEffect1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TamagotchiTheme {
                val viewModel: TamagotchiViewModel = viewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}

// alternar pantallas
@Composable
fun MainScreen(viewModel: TamagotchiViewModel) {
    var isGameScreen by remember { mutableStateOf(false) }

    if (isGameScreen) {
        GameScreen(onBackClicked = { isGameScreen = false })
    } else {
        TamagotchiScreen(
            viewModel = viewModel,
            onPlayClicked = { isGameScreen = true }
        )
    }
}

//barras
@Composable
fun CustomProgressBar(progress: Float, color: Color, text:String) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 0.dp) // sin margen
    ){
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 1.dp)
        )

        //contenedor externo de la barra
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = Color.Transparent) // Fondo de la barra
            ) {
            //contnedor interno de barras
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress) // Ajusta el ancho según el progreso
                    .background(color = color, shape = RoundedCornerShape(50.dp)) // Color de progreso
            )
        }
    }

}

//se muestran barras y botones
@Composable
fun TamagotchiScreen(viewModel: TamagotchiViewModel, onPlayClicked: () -> Unit) {
    val hambre by viewModel.hambre.collectAsState()
    val felicidad by viewModel.felicidad.collectAsState()
    val energia by viewModel.energia.collectAsState()

    var currentImage by remember { mutableStateOf(R.drawable.feliz) }
    // Efecto para reducir las barras con el tiempo
    LaunchedEffect1(Unit) {
        while (true) {
            delay(10000) // Actualiza cada 10 segundos
            viewModel.updateBars()

            if (hambre == 0 || felicidad == 0 || energia == 0) {
                currentImage = R.drawable.llorar
            } else if (hambre <= 40 || felicidad <= 40 || energia <= 40) {
                currentImage = R.drawable.enfadado
            } else {
                currentImage = R.drawable.feliz
            }
        }
    }

    //estructura general pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFD8C3E5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        //barras
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CustomProgressBar(progress = hambre / 100f, color = Color(0xFFFFC1C1), text = "Hambre")
            CustomProgressBar(progress = felicidad / 100f, color = Color(0xFFB2DFDB), text = "Felicidad")
            CustomProgressBar(progress = energia / 100f, color = Color(0xFFAEC6CF), text = "Energía")

        }
        Image(
            painter = painterResource(currentImage),
            contentDescription = "Imagen del Tamagotchi",
            modifier = Modifier.size(200.dp)
        )
        //botones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.alimentar()
                    currentImage = R.drawable.comiendo
                    //volver a la imagen original
                    MainScope().launch {
                        delay(5000) // Espera 5 segundos
                        currentImage = R.drawable.feliz // Vuelve a la imagen original
                    }
                }
            ) {
                 Text("Alimentar")
                }

            Button(
                onClick = {
                    viewModel.jugar()
                    MainScope().launch {
                        delay(5000)
                        currentImage = R.drawable.feliz
                    }
                    onPlayClicked()
                }
            ) {
                Text("Jugar")
            }

            Button(
                onClick = {
                    currentImage = R.drawable.durmiendo
                    viewModel.dormir()
                    MainScope().launch {
                        delay(5000)
                        currentImage = R.drawable.feliz
                    }
                }
            ) {
                Text("Dormir")
            }
        }
    }
}

//pantalla del juego
@Composable
fun GameScreen(onBackClicked: () -> Unit) {
    var ballOffsetY by remember { mutableStateOf(0f) } // posición inicla pelota
    val infiniteTransition = rememberInfiniteTransition()
    val isGoal = ballOffsetY < -200f //determina si hay gol
    //animación de rebote
    val animatedOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Nueva pantalla al presionar "Jugar"
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Agrega la imagen como fondo
        Image(
            painter = painterResource(id = R.drawable.porteria), // Cambia "fondo_porteria" al nombre de tu archivo
            contentDescription = "Fondo de portería",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painterResource(id = R.drawable.pelota1), // Imagen de la pelota
            contentDescription = "Pelota",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.BottomCenter) // Posición inicial centrada en la parte inferior
                .offset(y = (ballOffsetY + animatedOffsetY-50).dp) // aplica la animación de rebote
                .clickable {
                    ballOffsetY -= 250f // hacia arriba
                    // vuelve la pelota a posicion inciial
                    MainScope().launch {
                    delay(1000) // Espera 1 segundo
                        ballOffsetY = 0f // Vuelve a la posición inicial
                    }
                }
        )

        // Mensaje de gol
        if (isGoal) {
            Text(
                text = "¡Gol!",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center),
                style = androidx.compose.ui.text.TextStyle(fontSize = 32.sp)
            )
        }

        Button(
            onClick = onBackClicked,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        ) {
            Text("Volver")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TamagotchiScreenPreview() {
    TamagotchiTheme {
        TamagotchiScreen(
            viewModel = TamagotchiViewModel(Application()),
            onPlayClicked = {})
    }
}


