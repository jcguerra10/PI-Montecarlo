#pragma once

["java:package:com.zeroc.demos.IceGrid.simple"]
module Algorithm
{
    interface Montecarlo {
        int algorithm(int n);
        void shutdown();
    };

    interface MontecarloFactory {
        Montecarlo* createMontecarlo();
        double doAlgorithm(int n);
    };
};