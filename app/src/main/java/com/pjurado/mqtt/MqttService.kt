package com.pjurado.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


class MqttService(val context: Context, val idClient: String) {
    //mqtt://pjurado:qFJwgVfiLS52jgK2@pjurado.cloud.shiftr.io
    private val MQTT_SERVER_URI = "tcp://pjurado.cloud.shiftr.io:1883"
    private val MQTT_USER = "pjurado"
    private val MQTT_PASS = "qFJwgVfiLS52jgK2"
    private val TAG = "MqttService"
    private lateinit var client: MqttClient

    fun connect() {
        try {
            // Set up the persistence layer
            val persistence = MemoryPersistence();

            // Initialize the MQTT client
            client = MqttClient(MQTT_SERVER_URI, idClient, persistence);

            // Set up the connection options
            val connectOptions = MqttConnectOptions()
            connectOptions.setCleanSession(true)
            connectOptions.userName = MQTT_USER
            connectOptions.password = MQTT_PASS.toCharArray()


            // Connect to the broker
            val token = client.connect(connectOptions);
            try {

                client.setCallback(object : MqttCallback {
                    override fun messageArrived(topic: String?, message: MqttMessage?) {
                        Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
                    }

                    override fun connectionLost(cause: Throwable?) {
                        Log.d(TAG, "Connection lost ${cause.toString()}")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {

                    }
                })

            } catch (e: MqttException) {
                e.printStackTrace()
            }
        } catch (e : MqttException ) {
            e.printStackTrace();
        }
    }


    fun disconnect() {
        try {
            client.disconnect()
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, message: String) {
        try {
            val mqttMessage = MqttMessage(message.toByteArray())
            client.publish(topic, mqttMessage);
        } catch (e: MqttException) {
            e.printStackTrace();
        }
    }

    fun subscribe(topic: String, qos: Int = 1) {
        try {
            client.subscribe(topic, qos, object : IMqttMessageListener {


                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.d(TAG, "Receive message al conectar: ${message.toString()} from topic: $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }


    //Funciona
/*    fun subscribe(topic: String) {
        try {
            client.subscribe(topic);

        } catch (e: MqttException) {
            e.printStackTrace();
        }
    }*/


    fun connectBroker(){
        val mqttClient = MqttAndroidClient(context, MQTT_SERVER_URI, idClient)
        val mqttConnectOptions = MqttConnectOptions()



        try {
            val iMqttToken = mqttClient.connect(mqttConnectOptions)
            /*iMqttToken.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    Log.d(TAG, "Connection success")
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    Log.d(TAG, "Connection failure")
                }
            }*/


        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }



    /*private lateinit var mqttClient: MqttAndroidClient
    // TAG
    companion object {
        const val TAG = "AndroidMqttClient"
    }

    fun connect(context: Context) {
        val serverURI = "tcp://broker.emqx.io:1883"
        mqttClient = MqttAndroidClient(context, serverURI, "kotlin_client")
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }
*/
}