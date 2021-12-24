package DesignPatterns.CallBack.demo_3;

import java.util.Date;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 4:03 下午
 * @describe
 */
public class Thunder {
    public void download() {
        new Downloader().download("a.zip", this);
        new Downloader().download("b.jpg", this);
        new Downloader().download("win10.iso", this);
    }

    public void downloadDone(String url) {
        System.out.println(new Date() + url + " download is sucess!!");
    }

    public static void main(String[] args) {
        new Thunder().download();
    }
}
