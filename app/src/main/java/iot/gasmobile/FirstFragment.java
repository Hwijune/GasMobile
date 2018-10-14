package iot.gasmobile;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by 황도현 on 2018-05-05.
 */

public class FirstFragment extends Fragment{
    public FirstFragment(){

    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        return layout;
    }
}
