package cl.gestiona.app_07_bluetooth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity {

    private TextView txt;
    private Tarea task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        txt = (TextView) findViewById(R.id.txt);


        //captura la address seleccionada desde el listview
        String address = getIntent().getStringExtra("address");
        //llama a la tarea que se ejecuta en segundo plano
        task = new Tarea(this, address);
        task.execute();
    }


    public void encenderLED(View view){
        task.write("1");
    }

    public void apagarLED(View view){
        task.write("0");
    }

    public TextView getText(){
        return txt;
    }



}
