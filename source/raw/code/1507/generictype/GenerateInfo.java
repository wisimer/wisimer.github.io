/**
 *通过泛型方法传入泛型参数返回泛型实例
 *20150804
 */
public class GenerateInfo<T> {
    private T mInfo;
    public void setInfo(T info) {
        mInfo = info;
    }
    public T getInfo() {
        return mInfo;
    }
    public String toString() {
        System.out.println(this.mInfo.toString());
        return mInfo.toString();
    }
}
