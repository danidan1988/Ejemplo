package com.example.beep1.ejemplo;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * Created by beep1 on 18/07/2014.
 */
public class MyFragment2 extends Fragment implements SensorEventListener {

    public static final String IMAGE_RESOURCE_ID = "iconResourceID";
    public static final String ITEM_NAME = "itemName";
    private String datos_envio;
    private OutputStream outStream = null;
    private BluetoothSocket btSocket = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    Handler handler = new Handler();
    private static final String TAG = "Conectado";
    byte delimiter = 10;
    boolean stopWorker = false;
    int readBufferPosition = 0;
    byte[] readBuffer = new byte[1024];
    private static String address = "98:D3:31:50:0D:AC";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    TextView x,y,z;
    TextView adR,adD,adI,atR,atD,atI;
    ImageView flecha;
    StringBuilder builder = new StringBuilder();
    private Sensor mAccelerometer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        ComprobarBt();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        Log.e("Conectado", device.toString());
        Conectar();


        View view = inflater.inflate(R.layout.fragment_layout_two, container,
                false);







        x = (TextView)view.findViewById(R.id.xID);
        y = (TextView)view.findViewById(R.id.yID);
        z = (TextView)view.findViewById(R.id.zID);

        adR = (TextView)view.findViewById(R.id.adRecto);
        adD = (TextView)view.findViewById(R.id.adDer);
        adI = (TextView)view.findViewById(R.id.adIzq);
        atR = (TextView)view.findViewById(R.id.atRecto);
        atD = (TextView)view.findViewById(R.id.atDer);
        atI = (TextView)view.findViewById(R.id.atIzq);

        flecha = (ImageView)view.findViewById(R.id.Flecha);

        return view;
    }





    public void onSensorChanged(SensorEvent event) {
        float valx,valy;

        valx = event.values[0];
        valy = event.values[1];

        if(valy<-2){

            if((valx<2)&&(valx>-2)){

                datos_envio = "a";
                writeData(datos_envio);
                flecha.setImageResource(R.drawable.adr);

            }

            if(valx<-2){

                datos_envio = "b";
                writeData(datos_envio);
                flecha.setImageResource(R.drawable.adder);

            }

            if(valx>2){

                datos_envio = "c";
                writeData(datos_envio);
                flecha.setImageResource(R.drawable.adizq);

            }

        }

        if(valy>2){

            if((valx<2)&&(valx>-2)){

                datos_envio = "d";
                writeData(datos_envio);
                flecha.setImageResource(R.drawable.atr);

            }

            if(valx<-2){

                datos_envio = "e";
                writeData(datos_envio);
                flecha.setImageResource(R.drawable.atder);

            }

            if(valx>2){

                datos_envio = "f";
                writeData(datos_envio);
                flecha.setImageResource(R.drawable.atizq);

            }

        }

        if((valy<2)&&(valy>-2)){

            datos_envio = "s";
            writeData(datos_envio);
            flecha.setImageResource(R.drawable.stop);


        }


        x.setText("X = "+event.values[SensorManager.DATA_X]);
        y.setText("Y = "+event.values[SensorManager.DATA_Y]);
        z.setText("Z = "+event.values[SensorManager.DATA_Z]);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void onResume(){

        super.onResume();
        SensorManager sm = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0)
        {
            sm.registerListener(this, sensors.get(0), SensorManager.SENSOR_DELAY_GAME);
        }

    }
    public void onPause(){

        SensorManager mSensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onPause();

    }
    public void onStop(){

        SensorManager mSensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.unregisterListener(this, mAccelerometer);
        super.onStop();

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
