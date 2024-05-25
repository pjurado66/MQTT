package com.pjurado.mqtt

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pjurado.mqtt.ui.theme.MQTTTheme

class MainActivity : ComponentActivity() {
    lateinit var mqttService: MqttService

    private lateinit var idClient: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getClientId()
        mqttService = MqttService(applicationContext, idClient)
        mqttService.connect()
        mqttService.subscribe("test")
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
    Column {


        Text(
            text = "Hello $name!",
            modifier = modifier
        )
        TextField(value = mensaje, onValueChange = { mensaje = it })
        Button(onClick = { mqttService.publish("test", mensaje) }) {
            Text("Enviar")
        }
    }

}

