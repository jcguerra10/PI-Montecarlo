//
// Copyright (c) ZeroC, Inc. All rights reserved.
//

import com.zeroc.Ice.ObjectPrx;
import com.zeroc.demos.IceGrid.simple.Algorithm.Montecarlo;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloFactoryPrx;
import com.zeroc.demos.IceGrid.simple.Algorithm.MontecarloPrx;
import com.zeroc.demos.IceGrid.simple.Demo.*;

public class Client
{
    public static void main(String[] args)
    {
        int status = 0;
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        //
        // Try with resources block - communicator is automatically destroyed
        // at the end of this try block
        //
        try(com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "config.client", extraArgs))
        {
            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.IceGrid.simple");

            if(!extraArgs.isEmpty())
            {
                System.err.println("too many arguments");
                status = 1;
            }
            else
            {
                status = run(communicator);
            }
        }

        System.exit(status);
    }

    private static int run(com.zeroc.Ice.Communicator communicator)
    {
        //
        // First we try to connect to the object with the `hello'
        // identity. If it's not registered with the registry, we
        // search for an object with the ::Demo::Hello type.
        //
//        MontecarloPrx montecarlo = null;
//        try
//        {
//            montecarlo = MontecarloPrx.checkedCast(communicator.stringToProxy("Montecarlo"));
//        }
//        catch(com.zeroc.Ice.NotRegisteredException ex)
//        {
//            com.zeroc.IceGrid.QueryPrx query =
//                com.zeroc.IceGrid.QueryPrx.checkedCast(communicator.stringToProxy("DemoIceGrid/Query"));
//            montecarlo = MontecarloPrx.checkedCast(query.findObjectByType("::Algorithm::Montecarlo"));
//        }
//        if(montecarlo == null)
//        {
//            System.err.println("couldn't find a `::Algorithm::Montecarlo' object");
//            return 1;
//        }
//
//        menu();
//
//        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
//
//        String line = null;
//        do
//        {
//            try
//            {
//                System.out.print("==> ");
//                System.out.flush();
//                line = in.readLine();
//                if(line == null)
//                {
//                    break;
//                }
//                if(line.equals("s"))
//                {
//                    montecarlo.shutdown();
//                }
//                else if(line.equals("x"))
//                {
//                    // Nothing to do
//                }
//                else if(line.equals("?"))
//                {
//                    menu();
//                }
//                else
//                {
//                    montecarlo.algorithm(Integer.parseInt(line));
//                }
//            }
//            catch(java.io.IOException ex)
//            {
//                ex.printStackTrace();
//            }
//            catch(com.zeroc.Ice.LocalException ex)
//            {
//                ex.printStackTrace();
//            }
//            catch (NumberFormatException ex)
//            {
//                System.out.println("No es ni un numero, ni una opcion");
//            }
//        }
//        while(!line.equals("x"));


        ObjectPrx obj = communicator.stringToProxy("MontecarloFactory");
        MontecarloFactoryPrx fac = MontecarloFactoryPrx.checkedCast(obj);
        MontecarloPrx montecarlo  = fac.createMontecarlo();

        menu();

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));

        String line = null;
        do
        {
            try
            {
                System.out.print("==> ");
                System.out.flush();
                line = in.readLine();
                if(line == null)
                {
                    break;
                }
                if(line.equals("s"))
                {
                    montecarlo.shutdown();
                }
                else if(line.equals("x"))
                {
                    // Nothing to do
                }
                else if(line.equals("?"))
                {
                    menu();
                }
                else
                {
                    montecarlo.algorithm(Integer.parseInt(line));
                }
            }
            catch(java.io.IOException ex)
            {
                ex.printStackTrace();
            }
            catch(com.zeroc.Ice.LocalException ex)
            {
                ex.printStackTrace();
            }
            catch (NumberFormatException ex)
            {
                System.out.println("No es ni un numero, ni una opcion");
            }
        }
        while(!line.equals("x"));

        communicator.waitForShutdown();

        return 0;
    }

    private static void menu()
    {
        System.out.println(
            "usage:\n" +
            "t: send greeting\n" +
            "s: shutdown server\n" +
            "x: exit\n" +
            "?: help\n");
    }
}