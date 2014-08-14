package com.example.beep1.ejemplo;

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



public class ControlManual extends Fragment implements View.OnClickListener {

    private String datos_envio;
    private OutputStream outStream = null;
    private BluetoothSocket btSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "Conectar";
    private static String address = "98:D3:31:50:0D:AC";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button alante,atras,der,izq,conectar,desconectar;
    public boolean adelante = false, atras2 = false,derecha = false ,izquierda = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        comprobarBt();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.e("Conectado", device.toString());

        View view = inflater.inflate(R.layout.control_manual, container,
                false);

        conectar =  (Button)view.findViewById(R.id.Conectar);
        conectar.setOnClickListener(this);
        desconectar =  (Button)view.findViewById(R.id.Desconectar);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Conectar:
                conectarBt();
                break;
            case R.id.Desconectar:
                desconectarBt();

                break;

        }
    }


    private void escribirInf(String data){

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d(TAG, "Error antes de enviar información", e);
        }

        String message = data;
        byte[] msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            Log.d(TAG, "Error mientras se envíaa la información", e);
        }
    }

    private void comprobarBt() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(getActivity().getApplicationContext(), "Bluetooth Desactivado !", Toast.LENGTH_SHORT).show();
        }

        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity().getApplicationContext(),"No hay Bluetooth !", Toast.LENGTH_SHORT).show();
        }
    }

    public void conectarBt() {

        Log.d(TAG, address);
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.d(TAG, "Conectando a ... " + device);
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            Log.d(TAG, "Conexión realizada.");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                Log.d(TAG, "No se pudo terminar la conexión");
            }
            Log.d(TAG, "Fallo al crear la conexión");
        }

    }

    private void desconectarBt() {

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