/**
 *Java基本泛型-两个泛型类型
 *20150804
 */
public class UserInfo<K,V> {
  private K key;
  private V value;
  public void put(K key,V value) {
    this.key = key;
    this.value = value;
  }

  public K getKey() {
    return key;
  }

  public V getValue() {
    return value;
  }

  public String toString() {
      return this.key.toString() + " | " + value.toString();
  }
}
