public class NonAtomic {
    static int x;

    public static void main(String[] args) {
        NonAtomic na = new NonAtomic();
        for (int i = 0; i < 10; i++) {
            na.new Runner().start();
        }
    }

    class Runner extends Thread {
        private int validCounts = 0;
        private int invalidCounts = 0;

        public void run() {
            for (int i = 0; i < 10; i++) {
//                synchronized (NonAtomic.class) {
                    int reference = (int) (Math.random() * 100);
                    x = reference;

                    // either yielding or doing something intensive
                    // should cause the problem to manifest.
                    yield();
//                  for (int y = 0; y < 10000; y++) {
//                      Math.tan(200);
//                  }

                    if (x == reference) {
                        validCounts++;
                    } else {
                        invalidCounts++;
                    }
                }
//            }

            System.out.println(getName()
                               + " valid: " + validCounts
                               + " invalid: " + invalidCounts);
        }
    }
}
