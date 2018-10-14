package iot.gasmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static iot.gasmobile.MainActivity.sIP;
import static iot.gasmobile.MainActivity.sPORT;


/**
 * Created by 황도현 on 2018-05-11.
 */

public class InformationActivity extends Activity {

    //화면구성
    public ImageView img = null;
    public ImageView img2 = null;
    public TextView tv = null;
    public TextView tv2 = null;

    public Sendmsg sendclass = null;

    int bid;
    int bname;
    int bid2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_information);

        //화면구성
        img = (ImageView)findViewById(R.id.img);
        img2 = (ImageView)findViewById(R.id.img2);
        tv = (TextView)findViewById(R.id.information);
        tv2 = (TextView)findViewById(R.id.information2);

        //mainactivity에서 값받기
        Intent intent = getIntent();
        bid = intent.getIntExtra("data",9999);
        bname = intent.getIntExtra("name",9999);
        bid2 = intent.getIntExtra("data2",9999);


        //웹이미지띄우기
        Picasso.with(this).load("http://surk0130.dothome.co.kr/image/"+bname+"-1.jpg").into(img);
        Picasso.with(this).load("http://surk0130.dothome.co.kr/image/"+bname+"-2.jpg").into(img2);

        tv.setText(String.valueOf(bid));
        tv2.setText(String.valueOf(bid2));

    }
    //액티비티 종료
    public void close2(View view){
        finish();
    }
    //삭제메세지 전송
    public void deletedata(View view){
        //SendData 클래스 생성
        sendclass = new Sendmsg(String.valueOf(bname)+",1");
        sendclass.start();
        Toast.makeText(this, "데이터가 초기화됐어요!", Toast.LENGTH_SHORT).show();
        ((MainActivity)MainActivity.mcontext).refresh();
        finish();
    }

    //메세지 전송 클래스
    class Sendmsg extends Thread {

        String text;

        public Sendmsg(String text) {
            this.text = text;
        }

        public void run() {
            try {
                //UDP 통신용 소켓 생성
                DatagramSocket socket = new DatagramSocket();
                //서버 주소 변수
                InetAddress serverAddr = InetAddress.getByName(sIP);
                //보낼 데이터 생성
                byte[] buf = (text).getBytes();
                //패킷으로 변경
                DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, sPORT);
                //패킷 전송
                socket.send(packet);
            } catch (Exception e) {

            }
        }
    }
}
