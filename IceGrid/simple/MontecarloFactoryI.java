import com.zeroc.Ice.Current;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloFactory;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloPrx;

public class MontecarloFactoryI implements MontecarloFactory {
    @Override
    public MontecarloPrx createMontecarlo(Current current) {
        ObjectPrx prx = current.adapter.addWithUUID(new MontecarloI());
        String adapterId = current.adapter.getCommunicator().getProperties().
                getProperty(current.adapter.getName() + ".AdapterId");
        return MontecarloPrx.uncheckedCast(prx.ice_adapterId(adapterId));
    }
}