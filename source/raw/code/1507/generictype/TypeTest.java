public class TypeTest {
    public static void main(String[] args) {
        System.out.println("= = = = = = = = MessageQueue | 基本泛型 = = = = = = = = = ");
        MessageQueue<String> mq= new MessageQueue<String>();
        mq.addMsg("MQ");
        System.out.println(mq.getMsg());
        mq.printMsg();
        MessageQueue<Integer> mqInt= new MessageQueue<Integer>();
        mqInt.addMsg(1);
        System.out.println(mqInt.getMsg());
        mqInt.printMsg();

        System.out.println("= = = = = = = = UserInfo | 两个泛型参数 = = = = = = = = = ");
        UserInfo<Integer,String> userInfo = new UserInfo<Integer,String>();
        userInfo.put(2,"wxp");
        System.out.println(userInfo.toString());

        System.out.println("= = = = = = = = MsgModel | 通配符 = = = = = = = = = ");
        MsgModel<String> msgModel = new MsgModel<String>();
        msgModel.addMsg("www");
        printStringMsgModel(msgModel);
        MsgModel<Integer> msgModelInt = new MsgModel<Integer>();
        msgModelInt.addMsg(3);
        printMsgModel(msgModelInt);
        UserInfo<Integer,String> userInfo1 = new UserInfo<Integer,String>();
        userInfo1.put(3,"wxp");
        MsgModel<UserInfo> msgModelUserInfo = new MsgModel<UserInfo>();
        msgModelUserInfo.addMsg(userInfo1);
        printUserInfoMsgModel(msgModelUserInfo);

        System.out.println("= = = = = = = = IUploadMsg | 泛型接口 = = = = = = = = = ");
        UploadAction<String> uploadAction = new UploadAction<String>();
        uploadAction.addMsg("xxx");
        
        UploadIntAction uploadIntAction = new UploadIntAction();
        uploadIntAction.addMsg(4);

        System.out.println("= = = = = = = = DownloadMsg | 泛型方法 = = = = = = = = = ");
        DownloadMsg.download("ppp");

        System.out.println("= = = = = = = = GenerateInfo | 泛型方法返回泛型实例 = = = = = = = = = ");
        GenerateInfo<String> info = genInfo("wwwxxxppp");
        info.toString();

        UserInfo<String,Integer> userInfo2 = genUserInfo("wxp",5);
        System.out.println(userInfo2.toString());

        System.out.println("= = = = = = = = TypeArray | 泛型数组 = = = = = = = = = ");
        TypeArray<String> stringArray = new TypeArray<String>();
        stringArray.setArray(new String[]{"W","X","P"});
        stringArray.printArray(stringArray.getArray());
        stringArray.setCalculateSumCallback(new TypeArray.CalculateSumCallback<String> () {
            @Override
            public void calculate(String[] array) {
                String sum = "";
                for(int i = 0 ; i < array.length; i++) {
                    sum+= array[i];
                }
                System.out.println("sum = "+sum);
            }
        });
        stringArray.sumArray(new String[]{"w","x","p"});

        TypeArray<Integer> integerArray = new TypeArray<Integer>();
        integerArray.printArray(integerArray.setArrayElements(1,2,3));
        integerArray.setCalculateSumCallback(new TypeArray.CalculateSumCallback<Integer> () {
            @Override
            public void calculate(Integer[] array) {
                int sum = 0;
                for(int i = 0 ; i < array.length; i++) {
                    sum+= array[i];
                }
                System.out.println("sum = "+sum);
            }
        });

        integerArray.sumArray(new Integer[]{1,2,3});
        Integer i[] = fun1(1,2,3,4,5,6); // 返回泛型数组
        fun2(i);

    }
    //泛型数组
    public static <T> T[] fun1(T...arg) { // 接收可变参数
        return arg ;   // 返回泛型数组
    }
    public static <T> void fun2(T param[]) { // 输出
        System.out.print("接收泛型数组：") ;
        for(T t:param){
            System.out.print(t + "、");
        }
        System.out.println();
    }

    //泛型方法返回泛型实例
    public static <T> GenerateInfo<T> genInfo(T info) {
        GenerateInfo<T> temp = new GenerateInfo<T>();
        temp.setInfo(info);
        return temp;
    }

    //泛型方法返回泛型实例
    public static <K,V> UserInfo<K,V> genUserInfo(K key,V value) {
        UserInfo<K,V> temp = new UserInfo<K,V>();
        temp.put(key,value);
        return temp;
    }

    //泛型接口
    public static class UploadAction<T> implements IUploadMsg<T> {
        private T mMsg;
        public void addMsg(T msg) {
            this.mMsg = msg;
            upload(msg);
        }

        @Override
        public void upload(T msg) {
            System.out.println("uploading msg..."+msg.toString());
        }
    }

    //泛型接口
    public static class UploadIntAction implements IUploadMsg<Integer> {
        private Integer mMsg;
        public void addMsg(Integer msg) {
            this.mMsg = msg;
            upload(msg);
        }

        @Override
        public void upload(Integer msg) {
            System.out.println("uploading msg..."+msg);
        }
    }

    //受限通配符，限制泛型参数只能接收String或Object类型的泛型
    public static void printStringMsgModel(MsgModel<? extends String> model) {
        System.out.println("model = "+model.toString());
    }
    //受限通配符，限制泛型参数只能接收UserInfo或子类类型的泛型
    public static void printUserInfoMsgModel(MsgModel<? extends UserInfo> model) {
        System.out.println("model = "+model.getMsg().getValue());
    }
    //一般通配符
    public static void printMsgModel(MsgModel<? extends Number> model) {
        System.out.println("model = "+model.toString());
    }
}
