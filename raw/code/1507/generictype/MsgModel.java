/**
 *Java泛型通配符
 *20150804
 */
public class MsgModel<T> {
    private T mMsg = null;
    public T getMsg() {
      return mMsg;
    }
    public void addMsg(T msg) {
      this.mMsg = msg;
    }
    @Override
    public String toString() {
      return this.mMsg.toString();
    }
}
