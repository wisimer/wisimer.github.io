/**
 *Java基本泛型
 *20150804
 */
public class MessageQueue<T> {
    private T mMsg = null;
    public T getMsg() {
        return mMsg;
    }
    public void addMsg(T msg) {
        this.mMsg = msg;
    }
    public void printMsg() {
        System.out.println(this.mMsg);
    }
}
