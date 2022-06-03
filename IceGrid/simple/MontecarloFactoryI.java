import com.zeroc.Ice.Current;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.demos.IceGrid.simple.Algorithm.Montecarlo;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloFactory;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloPrx;

public class MontecarloFactoryI implements MontecarloFactory {

    private MontecarloPrx  montecarloPrx;

    @Override
    public MontecarloPrx createMontecarlo(Current current) {
        ObjectPrx prx = current.adapter.addWithUUID(new MontecarloI());
        String adapterId = current.adapter.getCommunicator().getProperties().
                getProperty(current.adapter.getName() + ".AdapterId");
        montecarloPrx = MontecarloPrx.uncheckedCast(prx.ice_adapterId(adapterId));
        return montecarloPrx;
    }

    @Override
    public int doAlgorithm(int n, Current current) {
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

    public MontecarloPrx getMontecarloPrx() {
        return montecarloPrx;
    }

    public void setMontecarloPrx(MontecarloPrx montecarloPrx) {
        this.montecarloPrx = montecarloPrx;
    }
}