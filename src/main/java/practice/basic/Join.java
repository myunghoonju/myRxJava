package practice.basic;

public class Join {

    public static void main(String[] args) {
        multiThreadTwo();
    }

    private static void multiThreadOne() {
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Thread 1 end");
            } catch (InterruptedException e) {
                // none to do
            }
        });

        thread.start();
        System.out.println("main is waiting");

        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error");
        }

        System.out.println("main restart");
    }

    private static void multiThreadTwo() {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(3000);
                System.out.println("Thread 1 end");
            } catch (InterruptedException e) {
                // none to do
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("Thread 2 end");
            } catch (InterruptedException e) {
                // none to do
            }
        });

        thread1.start();
        thread2.start();

        System.out.println("main is waiting");


        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error");
        }

        System.out.println("main restart");
    }
}
