import com.zeroc.Ice.Current;
import com.zeroc.demos.IceGrid.simple.Algorithm.Montecarlo;

public class MontecarloI implements Montecarlo {

    public MontecarloI() {
    }

    @Override
    public int algorithm(int n, Current current) {
        int i = n;
        int hits = 0;
        for (int j = 0; j <= i; j++) {

            double x = Math.abs(2*Math.random()-1);
            double y = Math.abs(2*Math.random()-1);
            double res = Math.pow(x, 2) + Math.pow(y, 2);

            if (res <= 1)
                hits++;
        }
        double relation = (double) hits / i;
        System.out.println("-------------------");
        System.out.println("i: " + i);
        System.out.println("hits: " + hits);
        System.out.println("relation: " + relation);
        relation *= 4;
        System.out.println("relation: " + relation);
        System.out.println("-------------------");
        return 0;
    }

    @Override
    public void shutdown(Current current) {

    }
}
