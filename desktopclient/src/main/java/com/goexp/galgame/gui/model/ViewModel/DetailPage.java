package com.goexp.galgame.gui.model.ViewModel;

public class DetailPage {

    public String url="";

    public String imgUrl="";
//    public StringProperty name;
//    public StringProperty ggbasesInfoUrl;


    @Override
    public String toString() {
        return "DetailPage{" +
                "ggbasesInfoUrl='" + url + '\'' +
                ", smallImg='" + imgUrl + '\'' +
                '}';
    }
}
