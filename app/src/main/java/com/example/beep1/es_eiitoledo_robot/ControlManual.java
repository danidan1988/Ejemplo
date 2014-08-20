package com.example.beep1.es_eiitoledo_robot;

/* Librerías importadas */

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

//Declaramos la clase, extendemos el Fragment que la contiene e implementamos un listener que nos
//permitirá dar una función a un botón al clicarlo.

public class ControlManual extends Fragment implements View.OnClickListener {

    private String datos_envio; //Variable que almacena los datos de envío por Bluetooth
    private OutputStream outStream = null; //Declaración de constructor Bluetooth
    private BluetoothSocket btSocket = null; //Declaración de constructor Bluetooth
    private BluetoothAdapter mBluetoothAdapter = null; //Declaración de constructor Bluetooth
    private static final String TAG = "Conectar"; //Etiqueta
    private static String address = "98:D3:31:50:0D:AC"; //Dirección del módulo Bluetooth
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Declaración del UUID Bluetooth


    Button alante,atras,der,izq,conectar,desconectar; //Botones implementados en la interfaz
    public boolean adelante = false, atras2 = false,derecha = false ,izquierda = false; //Variables booleanas usadas para programar el movimiento

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        comprobarBt();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.e("Conectado", device.toString());

        View view = inflater.inflate(R.layout.control_manual, container,
                false);

        conectar = (Button)view.findViewById(R.id.Conectar);
        conectar.setOnClickListener(this);
        desconectar = (Button)view.findViewById(R.id.Desconectar);
        desconectar.setOnClickListener(this);

        alante =  (Button)view.findViewById(R.id.ad);
        alante.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {

                    adelante = true;

                    if(adelante) {

                        datos_envio = "a";
                        escribirInf(datos_envio);
                    }

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {

                    adelante = false;

                    if(adelante==false) {

                        datos_envio = "s";
                        escribirInf(datos_envio);
                    }

                    return true;
                }

                return false;

            }
        });

        atras =  (Button)view.findViewById(R.id.at);
        atras.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {

                    datos_envio = "d";
                    escribirInf(datos_envio);

                    atras2 = true;

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {

                    datos_envio = "s";
                    escribirInf(datos_envio);

                    atras2 = false;

                    return true;
                }

                return false;

            }
        });

        der =  (Button)view.findViewById(R.id.der);
        der.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {
                    derecha = true;

                    if((adelante)&&(derecha)) {

                        datos_envio = "b";
                        escribirInf(datos_envio);
                    }

                    if((atras2)&&(derecha)) {

                        datos_envio = "e";
                        escribirInf(datos_envio);
                    }
                    

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {

                    derecha = false;

                    if((adelante)&&(derecha==false)) {

                        datos_envio = "a";
                        escribirInf(datos_envio);
                    }

                    if((atras2)&&(derecha==false)) {

                        datos_envio = "d";
                        escribirInf(datos_envio);
                    }

                    return true;
                }

                return false;

            }
        });

        izq =  (Button)view.findViewById(R.id.izq);
        izq.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    izquierda = true;
                    if((adelante)&&(izquierda)) {

                        datos_envio = "c";
                        escribirInf(datos_envio);
                    }

                    if((atras2)&&(izquierda)) {

                        datos_envio = "f";
                        escribirInf(datos_envio);
                    }

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    izquierda = false;

                    if((adelante)&&(izquierda==false)) {

                        datos_envio = "a";
                        escribirInf(datos_envio);
                    }

                    if((atras2)&&(izquierda==false)) {

                        datos_envio = "d";
                        escribirInf(datos_envio);
                    }

                    return true;
                }

                return false;

            }
        });

        return view;
    }

    //Función usada para conectar y desconectar el Bluetooth mediante la pulsación de los botones Conectar y Desconectar

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Conectar: //Si se detecta que el botón pulsado ha sido el de conectar llamaremos a la función conectarBt

                conectarBt();

                break;
            case R.id.Desconectar:

                desconectarBt(); //Si se detecta que el botón pulsado ha sido el de conectar llamaremos a la función desconectarBt

                break;

        }
    }

    //Función que se encarga de enviar el string con los datos que indican el movmimento

    private void escribirInf(String data){

        try {
            outStream = btSocket.getOutputStream(); //Asigna a la variable outStream el flujo de salida del socket Bt
        } catch (IOException e) {
            Log.d(TAG, "Error antes de enviar información", e);
        }

        String mensaje = data; //Almacena el caracter escrito en la función en una variable llamada mensaje
        byte[] msgBuffer = mensaje.getBytes(); //Codifica la variable String mensaje en una secuencia de bytes y la escribe en msgBuffer para ser mandada posteriormente

        try {
            outStream.write(msgBuffer); //Escribe la información msgBuffer en el flujo de salida, produciéndose el envío
        } catch (IOException e) {
            Log.d(TAG, "Error mientras se envía la información", e);
        }
    }

    //Función que realiza la comprobación de que el dispositivo tenga Bluetooth y esté activado

    private void comprobarBt() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getActivity().getApplicationContext(), "Bluetooth Desactivado !", Toast.LENGTH_SHORT).show();
        } //En caso de que el Bluetooth no esté activado muestra un toast diciendo con el mensaje "Bluetooth Desactivado"

        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(),"No hay Bluetooth !", Toast.LENGTH_SHORT).show();
        } //En caso de que el Bluetooth no esté activado muestra un toast diciendo con el mensaje "Bluetooth Desactivado"
    }

    //Función que realiza la conexión entre el dispositivo y el módulo Bluetooth

    public void conectarBt() {

        Log.d(TAG, address);
        BluetoothDevice dispositivo = mBluetoothAdapter.getRemoteDevice(address); //Se crea un nuevo dispositivo y se le asigna la dirección del módulo Bt
        Log.d(TAG, "Conectando a ... " + dispositivo);
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket = dispositivo.createRfcommSocketToServiceRecord(MY_UUID); //Se abre un nuevo Socket Bt y se le asigna la UUID declarada anteriormente
            btSocket.connect(); //Se intenta establecer la conexión
            Log.d(TAG, "Conexión realizada.");
        } catch (IOException e) {
            try {
                btSocket.close(); //En caso de no poderse establecer la conexión se cierra el Socket
            } catch (IOException e2) {
                Log.d(TAG, "No se pudo terminar la conexión");
            }
            Log.d(TAG, "Fallo al crear la conexión");
        }

    }

    //Función que realiza la desconexión entre el dispositivo y el módulo Bluetooth

    private void desconectarBt() {

        if (outStream != null) { //Para el envío de mensajes
            try {outStream.close();} catch (Exception e) {}
            outStream = null;
        }

        if (btSocket != null) { //Cierra el socket de conexión
            try {btSocket.close();} catch (Exception e) {}
            btSocket = null;
        }

    }

    @Override
    public void onDestroy(){

        super.onDestroy();

        try {
            btSocket.close();
        } catch (IOException e) {
        }

    }

}