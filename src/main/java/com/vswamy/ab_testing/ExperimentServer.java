package com.vswamy.ab_testing;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TServer.Args;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExperimentServer
{
    public static ExperimentServiceHandler handler;
    public static ExperimentService.Processor processor;
    private static Logger logger = LoggerFactory.getLogger(ExperimentServer.class);

    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        String propertiesFileName = "ab_test.properties";
        if (args.length > 0)
            propertiesFileName = args[0];

        // Load the configurations
        Constants.setConstants(propertiesFileName);
        
        handler = ExperimentServiceHandler.INSTANCE;
        processor = new ExperimentService.Processor(handler);

        TServerTransport serverTransport;
        try
        {
            serverTransport = new TServerSocket(Constants.AB_TEST_SERVER_PORT);
            TServer server = new TSimpleServer(new Args(serverTransport).processor(processor));
            logger.info("Starting the simple server on port {}", Constants.AB_TEST_SERVER_PORT);
            
            server.serve();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            logger.info("Stopping the server...");
        }
    }
}
