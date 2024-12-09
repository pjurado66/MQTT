package com.pjurado.mqtt

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pjurado.mqtt.ui.theme.MQTTTheme

class MainActivity : ComponentActivity() {
    lateinit var mqttService: MqttService

    private lateinit var idClient: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getClientId()
        mqttService = MqttService(applicationContext, idClient)
        mqttService.connect()

        //mqttService.publish("test", "Hello from Android")
        //mqttService.connectBroker()
        enableEdgeToEdge()
        setContent {
            MQTTTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = idClient,
                        modifier = Modifier.padding(innerPadding),
                        mqttService = mqttService
                    )
                }
            }
        }
    }

    fun getClientId() {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        idClient = "$manufacturer $model"
    }

    override fun onDestroy() {
        super.onDestroy()
        mqttService.disconnect()
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier, mqttService: MqttService) {
    var mensaje by rememberSaveable { mutableStateOf("") }
    var temperatura by rememberSaveable { mutableStateOf("0") }
    var humedad by rememberSaveable { mutableStateOf("0") }
    mqttService.subscribe("temperatura"){
        temperatura = it
    }
    mqttService.subscribe("humedad"){
        humedad = it
    }
    Column {


        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        TextField(value = mensaje, onValueChange = { mensaje = it })
        Button(onClick = { mqttService.publish("temperatura", mensaje) }) {
            Text("Enviar Temperatura")
        }

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround,

            )
        {
            Column(
                Modifier.weight(1f).background(Color.Magenta),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Temperatura", Modifier.padding(8.dp), fontSize = 24.sp)
                Text(temperatura, Modifier.padding(8.dp), fontSize = 24.sp)
            }
            Column(
                Modifier.weight(1f).background(Color.Cyan),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Humedad", Modifier.padding(8.dp), fontSize = 24.sp)
                Text(humedad, Modifier.padding(8.dp), fontSize = 24.sp)
            }
        }
    }

}

