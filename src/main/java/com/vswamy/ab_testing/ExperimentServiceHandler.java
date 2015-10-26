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
        if (experimentName == null || experimentName.trim().length() == 0)
            throw new NullOrEmptyException("The input experiment name is either null or empty!...");

        return helper.getRandomExperimentState(experimentName);
    }

    public Map<String, String> getExperimentsState(List<String> experimentsName) throws NullOrEmptyException,
            TException
    {
        if (experimentsName == null || experimentsName.size() == 0)
            throw new NullOrEmptyException("The input experiment list is either null or empty!...");

        return helper.getExperimentsState(experimentsName);
    }

    public Experiment getExperiment(String experimentName) throws ExperimentNotFoundException, NullOrEmptyException,
            TException
    {
        if (experimentName == null || experimentName.trim().length() == 0)
            throw new NullOrEmptyException("The input experiment name is either null or empty!...");

        return helper.getExperiment(experimentName);
    }

    public boolean createExperiment(Experiment experiment) throws ExperimentAlreadyExistsException,
            NullOrEmptyException, TException
    {
        if(experiment == null)
            throw new NullOrEmptyException("The input experiment is either null or emtpy!...");
        
        if(experiment.getExperimentName().trim().length() == 0)
            throw new NullOrEmptyException("The input experiment name is either null or empty!...");
        
        return helper.createExperiment(experiment);
    }

    public boolean editExperiment(Experiment experiment) throws InvalidPasscodeException, NullOrEmptyException,
            ExperimentNotFoundException, TException
    {
        if(experiment == null)
            throw new NullOrEmptyException("The input experiment is either null or emtpy!...");
        
        if(experiment.getExperimentName().trim().length() == 0)
            throw new NullOrEmptyException("The input experiment name is either null or empty!...");
        
        return helper.editExperiment(experiment);
    }

}
