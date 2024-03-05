package practice.basic;

public class Join {

    public static void main(String[] args) {
        interrupt();
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

    private static void interrupt() {
        Thread main = Thread.currentThread();

        Thread longRunning = new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Thread 1 running");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("longRunning interrupted");
                main.interrupt();
            }
        });

        longRunning.start();

        Thread interrupted = new Thread(() -> {
            try {
                System.out.println("after 3sec this will interrupt longRunning thread");
                Thread.sleep(3000);
                longRunning.interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        interrupted.start();

        try {
            System.out.println("main is waiting");
            longRunning.join();
            System.out.println("main competed");
        } catch (InterruptedException e) {
            System.out.println("main interrupted");
            throw new RuntimeException(e);
        }
    }
}
