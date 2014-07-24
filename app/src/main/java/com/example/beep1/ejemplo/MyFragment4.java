package com.example.beep1.ejemplo;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by beep1 on 19/07/2014.
 */
public class MyFragment4 extends Fragment {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    private String datos_envio;
    private OutputStream outStream = null;
    private BluetoothSocket btSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "Conectar";
    private static String address = "98:D3:31:50:0D:AC";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button alante,atras,der,izq;
    public boolean adelante = false, atras2 = false,derecha = false ,izquierda = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ComprobarBt();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.e("Conectado", device.toString());
        Conectar();

        View view = inflater.inflate(R.layout.fragment_layout_four, container,
                false);

        alante =  (Button)view.findViewById(R.id.ad);
        alante.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN ) {

                    adelante = true;

                    if(adelante) {

                        datos_envio = "a";
                        writeData(datos_envio);
                    };



                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {


                    adelante = false;

                    if(adelante==false) {

                        datos_envio = "s";
                        writeData(datos_envio);
                    };


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
                    writeData(datos_envio);

                    atras2 = true;

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {

                    datos_envio = "s";
                    writeData(datos_envio);

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
                        writeData(datos_envio);
                    };

                    if((atras2)&&(derecha)) {

                        datos_envio = "e";
                        writeData(datos_envio);
                    };
                    

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP ) {

                    derecha = false;

                    if((adelante)&&(derecha==false)) {

                        datos_envio = "a";
                        writeData(datos_envio);
                    };

                    if((atras2)&&(derecha==false)) {

                        datos_envio = "d";
                        writeData(datos_envio);
                    };

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
                        writeData(datos_envio);
                    };

                    if((atras2)&&(izquierda)) {

                        datos_envio = "f";
                        writeData(datos_envio);
                    };

                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {

                    izquierda = false;

                    if((adelante)&&(izquierda==false)) {

                        datos_envio = "a";
                        writeData(datos_envio);
                    };

                    if((atras2)&&(izquierda==false)) {

                        datos_envio = "d";
                        writeData(datos_envio);
                    };

                    return true;
                }

                return false;

            }
        });

        Direccion();

        return view;
    }

    private void Direccion(){
        if((adelante)) {

            datos_envio = "a";
            writeData(datos_envio);

        };




    }

    private void writeData(String data){

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "Error antes de enviar informaci�n", e);
        }

        String message = data;
        byte[] msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            Log.d(TAG, "Error mientras se env�a la informaci�n", e);
        }
    }

    private void ComprobarBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getActivity().getApplicationContext(), "Bluetooth Disabled !", Toast.LENGTH_SHORT).show();
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(),"Bluetooth null !", Toast.LENGTH_SHORT).show();
        }
    }

    public void Conectar() {

        Log.d(TAG, address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.d(TAG, "Connecting to ... " + device);
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            Log.d(TAG, "Conexi�n realizada.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d(TAG, "No se pudo terminar la conexi�n");
            }
            Log.d(TAG, "Fallo al crear la conexi�n");
        }

    }

    private void Desconectar() {


        if (outStream != null) {
            try {outStream.close();} catch (Exception e) {}
            outStream = null;
        }

        if (btSocket != null) {
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