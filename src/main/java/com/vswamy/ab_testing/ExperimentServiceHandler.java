package com.vswamy.ab_testing;

import java.util.List;
import java.util.Map;

import org.apache.thrift.TException;

import com.vswamy.ab_testing.ExperimentService;

public enum ExperimentServiceHandler implements ExperimentService.Iface
{
    INSTANCE;
    ExperimentServiceHelper helper = ExperimentServiceHelper.INSTANCE;
    
    public boolean ping() throws TException
    {
        return true;
    }

    public String getExperimentState(String experimentName) throws ExperimentNotFoundException, NullOrEmptyException,
            TException
    {
        if(experimentName == null || experimentName.trim().length() == 0)
            throw new NullOrEmptyException("The input experiment name is either null or empty!...");
        
        return helper.getRandomExperimentState(experimentName);
    }

    public Map<String, String> getExperimentsState(List<String> experimentsName) throws NullOrEmptyException,
            TException
    {
        if(experimentsName == null || experimentsName.size() == 0)
            throw new NullOrEmptyException("The input experiment list is either null or empty!...");
        
        return null;
    }

    public Experiment getExperiment(String experimentName) throws ExperimentNotFoundException, NullOrEmptyException,
            TException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean createExperiment(Experiment experiment) throws ExperimentAlreadyExistsException,
            NullOrEmptyException, TException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean editExperiment(Experiment experiment) throws InvalidPasscodeException, NullOrEmptyException,
            ExperimentNotFoundException, TException
    {
        // TODO Auto-generated method stub
        return false;
    }

}
