package cl.gestiona.app_07_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter adapter;
    private ListView list_device;
    private List<String> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list_device = (ListView) findViewById(R.id.list_device);
        data = new ArrayList<>();
        configuracionBT();
        cargaDispositivosAsociados();

        //evento del listview: envia la address del disp seleccionado
        list_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String info = data.get(position);
                String address = info.split("#")[1];
                Intent i = new Intent(MainActivity.this, Main2Activity.class);
                i.putExtra("address",address);
                startActivity(i);
            }
        });

    }


    public void cargaDispositivosAsociados(){
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for(BluetoothDevice device : devices){
            data.add(device.getName()+"#"+device.getAddress());
        }
        list_device.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,data));
    }

    public void  configuracionBT(){
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null){
            Toast.makeText(this, "Sin BT", Toast.LENGTH_SHORT).show();
        }else{
            if(!adapter.isEnabled()){
                Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(i, 1);
            }
        }
    }
    public void onBluetooth(View view){
        if (adapter != null){
            adapter.enable();
        }
    }
    public void offBluetooth(View view){
        if (adapter != null){
            adapter.disable();
        }
    }





}
