package DesignPatterns.CallBack.demo_3;

/**
 * @author Congpeixin
 * @version 1.0
 * @date 2021/12/24 4:03 下午
 * @describe
 */
public class Downloader extends Thread{
    private String url;
    private Thunder thunder;

    public void download(String url, Thunder thunder){
        this.url = url;
        this.thunder = thunder;
        this.start();
    }

    @Override
    public void run() {
        if (url == null) {
            return;
        }
        switch (url){
            case "z.zip":
                waitSleep(2000);
            case "b.jpg":
                waitSleep(3000);
            case "win10.iso":
                waitSleep(7000);
        }

        thunder.downloadDone(url);
    }

    private void waitSleep(int seconds){
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
