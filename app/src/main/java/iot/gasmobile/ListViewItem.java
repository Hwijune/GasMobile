package iot.gasmobile;

import android.graphics.drawable.Drawable;

/**
 * Created by 황도현 on 2018-05-18.
 */

public class ListViewItem {
    private Drawable iconDrawable ;
    private String titleStr ;
    private String descStr ;
    private String perStr;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }
    public void setPer(String per){
        perStr = per;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
    public String getPer(){
        return this.perStr;
    }
}
