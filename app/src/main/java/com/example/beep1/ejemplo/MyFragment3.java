package com.example.beep1.ejemplo;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by beep1 on 19/07/2014.
 */
public class MyFragment3 extends Fragment implements View.OnClickListener {

    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1;
    private String datos_envio;
    private OutputStream outStream = null;
    private BluetoothSocket btSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static final String TAG = "Conectar";
    private static String address = "98:D3:31:50:0D:AC";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    Button conectar,desconectar,voz;
    TextView adR,adD,adI,atR,atD,atI;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ComprobarBt();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.e("Conectado", device.toString());
        Conectar();
        View view = inflater.inflate(R.layout.fragment_layout_three, container,
                false);

        conectar =  (Button)view.findViewById(R.id.Conectar);
        conectar.setOnClickListener(this);
        desconectar =  (Button)view.findViewById(R.id.Desconectar);
        desconectar.setOnClickListener(this);
        voz =  (Button)view.findViewById(R.id.Voz);
        voz.setOnClickListener(this);

        adR = (TextView)view.findViewById(R.id.adRecto);
        adD = (TextView)view.findViewById(R.id.adDer);
        adI = (TextView)view.findViewById(R.id.adIzq);
        atR = (TextView)view.findViewById(R.id.atRecto);
        atD = (TextView)view.findViewById(R.id.atDer);
        atI = (TextView)view.findViewById(R.id.atIzq);

        return view;
    }


        public void onClick(View v)
        {
            switch(v.getId())
            {
                case R.id.Conectar :
                    Conectar();
                    break;
                case R.id.Desconectar :
                    Desconectar();

                    break;
                case R.id.Voz :

                    startVoiceRecognitionActivity();
                    datos_envio = "s";
                    writeData(datos_envio);
                    adR.setTextColor(Color.RED);
                    adI.setTextColor(Color.RED);
                    adD.setTextColor(Color.RED);
                    atR.setTextColor(Color.RED);
                    atD.setTextColor(Color.RED);
                    atI.setTextColor(Color.RED);

            }


        }



    private void startVoiceRecognitionActivity() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga, hacia donde quiere ir");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);

    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String[] palabras = matches.get(0).toString().split(" ");

            if ((palabras[0].equals("adelante")) && (palabras[1].equals("recto"))) {

                datos_envio = "a";
                writeData(datos_envio);
                adR.setTextColor(Color.GREEN);
                adI.setTextColor(Color.RED);
                adD.setTextColor(Color.RED);
                atR.setTextColor(Color.RED);
                atD.setTextColor(Color.RED);
                atI.setTextColor(Color.RED);

            }

            if ((palabras[0].equals("adelante")) && (palabras[1].equals("derecha"))) {

                datos_envio = "b";
                writeData(datos_envio);
                adR.setTextColor(Color.RED);
                adI.setTextColor(Color.RED);
                adD.setTextColor(Color.GREEN);
                atR.setTextColor(Color.RED);
                atD.setTextColor(Color.RED);
                atI.setTextColor(Color.RED);

            }

            if ((palabras[0].equals("adelante")) && (palabras[1].equals("izquierda"))) {

                datos_envio = "c";
                writeData(datos_envio);
                adR.setTextColor(Color.RED);
                adI.setTextColor(Color.GREEN);
                adD.setTextColor(Color.RED);
                atR.setTextColor(Color.RED);
                atD.setTextColor(Color.RED);
                atI.setTextColor(Color.RED);

            }

            if ((palabras[0].equals("parar")) && (palabras[1].equals("movimiento"))) {

                datos_envio = "s";
                writeData(datos_envio);
                adR.setTextColor(Color.RED);
                adI.setTextColor(Color.RED);
                adD.setTextColor(Color.RED);
                atR.setTextColor(Color.RED);
                atD.setTextColor(Color.RED);
                atI.setTextColor(Color.RED);

            }

            if ((palabras[0].equals("atrás")) && (palabras[1].equals("recto"))) {

                datos_envio = "d";
                writeData(datos_envio);
                adR.setTextColor(Color.RED);
                adI.setTextColor(Color.RED);
                adD.setTextColor(Color.RED);
                atR.setTextColor(Color.GREEN);
                atD.setTextColor(Color.RED);
                atI.setTextColor(Color.RED);

            }
            if ((palabras[0].equals("atrás")) && (palabras[1].equals("derecha"))) {

                datos_envio = "e";
                writeData(datos_envio);
                adR.setTextColor(Color.RED);
                adI.setTextColor(Color.RED);
                adD.setTextColor(Color.RED);
                atR.setTextColor(Color.RED);
                atD.setTextColor(Color.GREEN);
                atI.setTextColor(Color.RED);

            }

            if ((palabras[0].equals("atrás")) && (palabras[1].equals("izquierda"))) {

                datos_envio = "f";
                writeData(datos_envio);
                adR.setTextColor(Color.RED);
                adI.setTextColor(Color.RED);
                adD.setTextColor(Color.RED);
                atR.setTextColor(Color.RED);
                atD.setTextColor(Color.RED);
                atI.setTextColor(Color.GREEN);

            }
        }

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
