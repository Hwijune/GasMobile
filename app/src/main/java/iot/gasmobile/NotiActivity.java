package iot.gasmobile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import static iot.gasmobile.MainActivity.sIP;
import static iot.gasmobile.MainActivity.sPORT;

/**
 * Created by 황도현 on 2018-07-05.
 */

public class NotiActivity extends Activity {

    //리스트에 들어갈 원소를 초기화
    ArrayList<String> listItems=new ArrayList<String>();
    //리스트의 데이터를 다루는 어댑터 선언
    ArrayAdapter<String> adapter;

    public String receivedata = "1,2,3,4,5,";
    public String[] word;
    ListView notili;
    public Sendmsg sendclass = null;

    private static Toast sToast;

    private final long FINISH_INTERVAL_TIME = 1000;
    private  long backPressedTime = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_noti);

        notili = (ListView)findViewById(R.id.notilist);

//        sendclass = new Sendmsg("showreq");
//        sendclass.start();
//        try{
//            sendclass.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        adapter.clear();
//        adapter.notifyDataSetChanged();

        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        notili.setAdapter(adapter);
        setlist();

        notili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backPressedTime;
                if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
                    sendclass = new Sendmsg(word[position]+",1");
                    sendclass.start();
                    try{
                        sendclass.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    backPressedTime = 0;
                    setlist();
                }
                else{
                    backPressedTime = tempTime;
                    showToast(NotiActivity.this,"한번 더 누르시면 삭제됩니다.");
                    //Toast.makeText(NotiActivity.this, "한번 더 누르시면 삭제됩니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }) ;
    }

    //토스트중복방지
    public static void showToast(Context context, String message) {
        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
        }
        sToast.show();
    }

    public void setlist(){
        sendclass = new Sendmsg("showreq");
        sendclass.start();
        try{
            sendclass.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        word = receivedata.split(",");
        listItems.clear();
        adapter.notifyDataSetChanged();
        for(int i = 0; i<word.length; i++){
            listItems.add(word[i]+"호 청소 부탁드려요");
        }
    }

    public void close(View view){
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
                if(text.equals("showreq")){
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
                    //데이터 수신 대기
                    byte[] buf2 = new byte[1024];
                    DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length, serverAddr, sPORT);
                    socket.receive(packet2);
                    //데이터 수신되었다면 문자열로 변환
                    receivedata = new String(packet2.getData(), 0, packet2.getLength());
                    //임시로 표시
                    //data.setText(receivedata);
                }
                else{
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
                }
            } catch (Exception e) {

            }
        }
    }
}
