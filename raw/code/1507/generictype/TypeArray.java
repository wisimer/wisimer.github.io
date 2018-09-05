/**
 *泛型数组
 *20150804
 */
public class TypeArray<T> {
    private T[] mArray;

    public void setArray(T[] array) {
        this.mArray = array;
    }

    public <T> T[] setArrayElements(T...array) {
        return array;
    }

    public T[] getArray() {
        return mArray;
    }
    public void printArray(T array[]) {
        StringBuilder sb = new StringBuilder();
        for(T e:array) {
            sb.append(e.toString());
        }
        System.out.println(sb.toString());
    }

    CalculateSumCallback mCalculateSumCallback = null;
    public void setCalculateSumCallback(CalculateSumCallback callback) {
        this.mCalculateSumCallback = callback;
    }
    public <T> void sumArray(T[] array) {
        mCalculateSumCallback.calculate(array);
    }
    public interface CalculateSumCallback<T> {
        public void calculate(T[] array);
    }
}
