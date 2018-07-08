/**
 *泛型方法
 *20150804
 */
public class DownloadMsg {
    public static <T> void download(T msg) {
        System.out.println("Downloading msg..."+msg.toString());
    }
}
