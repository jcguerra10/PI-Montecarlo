//
// Copyright (c) ZeroC, Inc. All rights reserved.
//

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloFactoryPrx;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client {
    private static int contadorTareas = 1;

    public static void main(String[] args) {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        //
        // Try with resources block - communicator is automatically destroyed
        // at the end of this try block
        //
        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client", extraArgs)) {
            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");

            if (!extraArgs.isEmpty()) {
                System.err.println("too many arguments");
                status = 1;
            } else {
                status = run(communicator);
            }
        }

        System.exit(status);
    }

    private static int run(com.zeroc.Ice.Communicator communicator) {

        menu();

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do {
            try {
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();


//                MontecarloFactoryPrx fac = getProxy(communicator);
//                System.out.println("-- " + fac);
//                System.out.println("-- " + sizeOfNodes(communicator));

                if (line == null) {
                    break;
                }
                if (line.equals("s")) {
                    //montecarlo.shutdown();
                } else if (line.equals("x")) {
                    // Nothing to do
                } else if (line.equals("?")) {
                    menu();
                } else {
                    //fac.doAlgorithm(Integer.parseInt(line));


                    String finalLine = line;

                    List<Callable<Double>> tareas=
                            Stream.generate(() -> getTareaSleepUnSegundo(communicator, Integer.parseInt(finalLine)))
                                    .limit(5)
                                    .collect(Collectors.toList());

                    ExecutorService executor = Executors.newFixedThreadPool(2);

                    List<Future<Double>> futures = executor.invokeAll(tareas);
                    for (Future<Double> future : futures) {
                        Double resultado = future.get();
                        System.out.println(resultado);
                    }

                }
            } catch (java.io.IOException ex) {
                ex.printStackTrace();
            } catch (com.zeroc.Ice.LocalException ex) {
                ex.printStackTrace();
            } catch (NumberFormatException ex) {
                System.out.println("No es ni un numero, ni una opcion");
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (!line.equals("x"));

        communicator.waitForShutdown();

        return 0;
    }

    private static Callable<Double> getTareaSleepUnSegundo(Communicator communicator, int n) {
        MontecarloFactoryPrx fac = getProxy(communicator);
        System.out.println("-- " + fac);
        Callable<Double> tarea = () -> {
            try {
                Double i = fac.doAlgorithm(n);
                return i;
            }catch (Exception e){

            }
            return null;
        };
        return tarea;
    }

    private static MontecarloFactoryPrx getProxy(com.zeroc.Ice.Communicator communicator) {
        System.out.println("getP");
        MontecarloFactoryPrx hello;

        try {
            hello = MontecarloFactoryPrx.checkedCast(communicator.stringToProxy("MontecarloFactory").ice_locatorCacheTimeout(0));
        } catch (com.zeroc.Ice.NotRegisteredException ex) {
            com.zeroc.IceGrid.QueryPrx query =
                    com.zeroc.IceGrid.QueryPrx.checkedCast(communicator.stringToProxy("IceGrid/Query"));
            hello = MontecarloFactoryPrx.checkedCast(query.findObjectByType("::Algorithm::MontecarloFactory"));
        }
        if (hello == null) {
            System.err.println("couldn't find a `::Algorithm::MontecarloFactory' object");
            return null;
        }
        return hello;
    }

    private static int sizeOfNodes(com.zeroc.Ice.Communicator communicator) {

        com.zeroc.IceGrid.QueryPrx query =
                com.zeroc.IceGrid.QueryPrx.checkedCast(communicator.stringToProxy("IceGrid/Query"));
        ObjectPrx[] hello = query.findAllObjectsByType("::Algorithm::MontecarloFactory");

        return hello.length;
    }

    private static void menu() {
        System.out.println(
                "usage:\n" +
                        "t: send greeting\n" +
                        "s: shutdown server\n" +
                        "x: exit\n" +
                        "?: help\n");
    }
}
