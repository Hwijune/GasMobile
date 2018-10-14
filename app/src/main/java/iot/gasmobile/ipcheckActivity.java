package iot.gasmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ipcheckActivity extends AppCompatActivity {

    public EditText et;
    public Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipcheck);

        et = (EditText)findViewById(R.id.ipaddr);
    }
//
    public void gomain(View view){
        final Intent intent3 = new Intent(this, MainActivity.class);
        String ipaddr = String.valueOf(et.getText());
        intent3.putExtra("ipaddr",ipaddr);
        startActivity(intent3);
        finish();
    }
}
