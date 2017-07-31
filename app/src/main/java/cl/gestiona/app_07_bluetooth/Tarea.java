package cl.gestiona.app_07_bluetooth;




import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Created by roman on 21-07-17.
 */

public class Tarea extends AsyncTask<Void, String, Void> {


    private InputStream mmInStream;
    private OutputStream mmOutStream;

    private String address;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket socket;
    private BluetoothDevice bluetoothDevice;
    private Main2Activity activity;



    public Tarea(Main2Activity activity,String address){
        this.address = address;
        this.activity = activity;
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        establecerConexion();
    }

    @Override
    protected Void doInBackground(Void... params) {
        byte[] buffer = new byte[1024];

        System.out.println("RUN");
        // Keep looping to listen for received messages
        while (true) {
            try {
                System.out.println("---->"+mmInStream);
                mmInStream.read(buffer);
                String readMessage = Arrays.toString(buffer);
                System.out.println("Message desde  Hilo Secundario:"+readMessage);
                readMessage = (readMessage==null)?"":readMessage.split(",")[0].replace("[","");
                publishProgress(readMessage);
            } catch (IOException e) {
                System.out.println("---------------Error de Lectura------------");
                establecerConexion();
            }
            catch(NullPointerException e){
                establecerConexion();
                System.out.println("NULL POINTER");
            }

        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        activity.getText().setText(values[0]);
    }


    @Override
    protected void onPostExecute(Void re) {
        super.onPostExecute(re);
    }



    private  void establecerConexion(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter!=null) {
            bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            System.out.println("YES");
            //crear el socket a partir del bluetoothDevice
            try {
                socket = (BluetoothSocket) bluetoothDevice.getClass()
                        .getMethod("createRfcommSocket", new Class[]{int.class})
                        .invoke(bluetoothDevice, 1);

                socket.connect();


                System.out.println("YEsy");
                //SI HAY CONECCION ESTABLECEMOS LOS INPUT Y OUTPUT
                if (socket.isConnected()){
                    InputStream tmpIn = null;
                    OutputStream tmpOut = null;
                    try {
                        //Create I/O streams for connection
                        tmpIn = socket.getInputStream();
                        tmpOut = socket.getOutputStream();
                    } catch (IOException e) {
                        System.out.println("Constructor Hilo:");// e.printStackTrace();
                    }

                    mmInStream = tmpIn;
                    mmOutStream = tmpOut;
                    System.out.println("CONECTADO A SOCKET");
                }else{
                    System.out.println("NO CONECTADO A SOCKET");
                }


            } catch (Exception e) {
                System.out.println("CATCH SOCKET");
                try {
                    socket.close();
                } catch (Exception e1) {
                    //e1.printStackTrace();

                }
            }
        }else{
            System.out.println("Error Bluetooth Adapter");
        }
    }


    public void write(String input) {
        byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
        try {
            mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
        } catch (IOException e) {
            //if you cannot write, close the application
            //Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
            //finish();
            System.out.println("error de escritura");
        }
    }




}
