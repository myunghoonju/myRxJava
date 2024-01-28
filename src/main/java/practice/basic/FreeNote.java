package practice.basic;

public class FreeNote {

    public static void main(String[] args) {
        ReverseOrder reverseOrder = new ReverseOrder();
        //reverseOrder.reverseOrder();

        MyRecord myRecord = new MyRecord("1", "myunghoon", "developer");
        System.out.println(myRecord.id());
        System.out.println(myRecord.name());
        System.out.println(myRecord.occupation());
        System.out.println(myRecord);
    }
}
