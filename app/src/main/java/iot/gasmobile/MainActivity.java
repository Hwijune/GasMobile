package iot.gasmobile;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {

    //서버주소
    public static  String sIP = "123123123";
    //사용포트
    public static final int sPORT = 8888;
    //클래스객체
    public Sendmsg sendclass = null;
    //MainActivity 컨텍스트
    public static Context mcontext;

    //화면구성
    public ImageButton showdata = null;
    public TextView data = null;
    ListView listview;
    ListViewAdapter adapter;
    public TextView sumdata = null;

    //임시숫자
    public int avearr[] = new int[20];
    public int imgarr[] = new int[20];
    public String receivedata = "1,2,3,4,5,";
    public String[] word;
    public String[] word2;
    public String[] word3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        @SuppressLint("MissingPermission") String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
//        System.out.println(ip);
        Intent intent = getIntent();
        sIP = intent.getStringExtra("ipaddr");

        //화면구성
        showdata = (ImageButton)findViewById(R.id.showdata);
        //data = (TextView)findViewById(R.id.data);
        sumdata = (TextView)findViewById(R.id.sumdata);
        adapter = new ListViewAdapter();
        listview = (ListView)findViewById(R.id.list);
        listview.setAdapter(adapter);
        //컨텍스트 지정
        mcontext = this;


        showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SendData 클래스 생성
                sendclass = new Sendmsg("showdata");
                //보내기 시작
                sendclass.start();
                try{
                    sendclass.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                savedata();
                Changeitem();
            }
        });

        //초기세팅
        sendclass = new Sendmsg("showdata");
        sendclass.start();
        try{
            sendclass.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        savedata();
        Changeitem();

        //informationactivity로 이동
        final Intent intent2 = new Intent(this, InformationActivity.class);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                System.out.println(position);
                ListViewItem item = (ListViewItem) parent.getItemAtPosition(position);
                String titleStr = item.getTitle();
                String descStr = item.getDesc();
                Drawable iconDrawable = item.getIcon();
                int aaa = Integer.parseInt(descStr);
                int bbb = Integer.parseInt(titleStr);
                intent2.putExtra("data",aaa);
                intent2.putExtra("name",bbb);
                intent2.putExtra("data2",Integer.parseInt(word3[position]));
                startActivity(intent2);
            }
        });

    }

    //뒤로가기 두번 종료 변수
    private final long FINISH_INTERVAL_TIME = 2000;
    private  long backPressedTime = 0;
    //뒤로가기 두번 종료 메서드
    public void onBackPressed(){
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            super.onBackPressed();
        }
        else{
            backPressedTime = tempTime;
            Toast.makeText(this, "\'뒤로\'버튼 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //리스트뷰에 아이템 추가
    public void Changeitem(){
        adapter.listViewItemList.clear();
        adapter.notifyDataSetChanged();
        for(int i = 0; i<7; i++){
            adapter.addItem(ContextCompat.getDrawable(this, imgarr[i]),Integer.toString(i+101),word[i],Integer.toString(avearr[i]));
        }
        for(int i = 0; i<6; i++){
            adapter.addItem(ContextCompat.getDrawable(this, imgarr[i+7]),Integer.toString(i+201),word[i+7],Integer.toString(avearr[i+7]));
        }
        for(int i = 0; i<7; i++){
            adapter.addItem(ContextCompat.getDrawable(this, imgarr[i+13]),Integer.toString(i+301),word[i+13],Integer.toString(avearr[i+13]));
        }
        sumdata.setText(Integer.toString(sum));
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
                //데이터 수신 대기
                byte[] buf2 = new byte[1024];
                DatagramPacket packet2 = new DatagramPacket(buf2, buf2.length, serverAddr, sPORT);
                socket.receive(packet2);
                //데이터 수신되었다면 문자열로 변환
                receivedata = new String(packet2.getData(), 0, packet2.getLength());
                //임시로 표시
                //data.setText(receivedata);
            } catch (Exception e) {

            }
        }
    }

    //받은데이터 저장
    public int sum = 0;
    public void savedata(){
        sum = 0;
        word2 = receivedata.split("a");
        word = word2[0].split(",");
        word3 = word2[1].split(",");
        System.out.println(word2[1]);
        for(int i = 0; i<20; i++){
            sum += Integer.parseInt(word[i]);
        }
        for(int i = 0; i<20; i++){
            avearr[i] = (int)(((double)Integer.parseInt(word[i])/sum)*100);
        }
        for(int i = 0; i<20; i++){
            if(Integer.parseInt(word[i])>=50)
                imgarr[i] = R.drawable.fuck;
            else if(Integer.parseInt(word[i])>=40)
                imgarr[i] = R.drawable.bad;
            else if(Integer.parseInt(word[i])>=30)
                imgarr[i] = R.drawable.normal;
            else if(Integer.parseInt(word[i])>=20)
                imgarr[i] = R.drawable.nice;
            else
                imgarr[i] = R.drawable.lovely;
        }
    }

    //새로고침
    public void refresh(){
        //SendData 클래스 생성
        sendclass = new Sendmsg("showdata");
        //보내기 시작
        sendclass.start();
        try{
            sendclass.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        savedata();
        Changeitem();
    }

    //튜토리얼 띄우기
    public void opentutorial(View view){
        Intent intent = new Intent(this, PopupActivity.class);
        startActivity(intent);
    }

    //알림 띄우기
    public void opennoti(View view){
        Intent intent = new Intent(this, NotiActivity.class);
        startActivity(intent);
    }
}