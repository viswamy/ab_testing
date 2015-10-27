package com.vswamy.ab_testing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

public enum ExperimentServiceHelper
{
    INSTANCE;

    private final String passcode = "passcode";
    private final String authorName = "authorName";
    private final String authorEmailAddress = "authorEmailAddress";

    private Logger logger = LoggerFactory.getLogger(ExperimentServiceHelper.class);

    Random rand = new Random();
    Jedis jedis = new Jedis(Constants.REDIS_SERVER_HOST, Constants.REDIS_SERVER_PORT);
    HashMap<String, ExperimentInfo> map = new HashMap<String, ExperimentInfo>();

    public String getRandomExperimentState(String experimentName) throws ExperimentNotFoundException
    {
        if (!map.containsKey(experimentName))
            populate(experimentName);

        if (!map.containsKey(experimentName))
            throw new ExperimentNotFoundException("The experiment could not be found!...");

        ExperimentInfo info = map.get(experimentName);
        ExperimentState[] experimentStates = info.getStates();

        int totalWeightage = experimentStates[experimentStates.length - 1].getCumulativeWeightage();
        int randomNumber = rand.nextInt(totalWeightage);

        ExperimentState retExperimentState = null;
        /*
         * The assumption here is that number of states will be minimum. 
         * If the number of states increases, modified binary search algorithm can be applied here as 
         * it is known for a fact that cumulative weightage is non-decreasing in nature
        */
        for (int i = experimentStates.length - 1; i >= 0; i--)
        {
            if (randomNumber < experimentStates[i].getCumulativeWeightage())
                retExperimentState = experimentStates[i];
            else
                break;
        }

        return retExperimentState.getName();
    }

    /**
     * Fetches data for a given experiment in redis and puts it in the HashMap
     * 
     * @param experimentName
     */
    private void populate(String experimentName) throws ExperimentNotFoundException
    {
        Map<String, String> temp = jedis.hgetAll(experimentName);
        if (temp == null)
            new ExperimentNotFoundException("The given experiment was not found!...");

        ExperimentInfo info = new ExperimentInfo();
        info.setExperimentName(experimentName);
        info.setAuthorName(temp.get(this.authorName));
        info.setAuthorEmailAddress(temp.get(this.authorEmailAddress));
        info.setPasscode(null);

        temp.remove(this.authorName);
        temp.remove(this.authorEmailAddress);
        temp.remove(this.passcode);

        ExperimentState[] states = new ExperimentState[temp.size()];

        int weightage = 0;
        int cumulativeWeightage = 0;
        int iter = 0;

        for (Map.Entry<String, String> entry : temp.entrySet())
        {
            ExperimentState state = new ExperimentState();
            state.setName(entry.getKey());

            weightage = Integer.parseInt(entry.getValue());
            state.setWeightage(weightage);

            cumulativeWeightage += weightage;
            state.setCumulativeWeightage(cumulativeWeightage);

            states[iter++] = state;
        }
        info.setStates(states);
        map.put(experimentName, info);
        return;
    }

    public Map<String, String> getExperimentsState(List<String> experimentsName)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        for (String experimentName : experimentsName)
        {
            try
            {
                map.put(experimentName, this.getRandomExperimentState(experimentName));
            }
            catch (ExperimentNotFoundException exception)
            {
                // Log and consume the exception
                logger.debug("Experiment not found => {}", exception);
            }
        }
        return map;
    }

    public Experiment getExperiment(String experimentName) throws ExperimentNotFoundException
    {
        Experiment experiment = new Experiment();
        if (!map.containsKey(experimentName))
            populate(experimentName);

        if (!map.containsKey(experimentName))
            throw new ExperimentNotFoundException("The input experiment was not found!...");

        ExperimentInfo info = map.get(experimentName);
        experiment.setAuthorName(info.getAuthorName());
        experiment.setAuthorEmailAddress(info.getAuthorEmailAddress());
        experiment.setPasscode(info.getPasscode());
        experiment.setExperimentName(info.getExperimentName());
        HashMap<String, Integer> s = new HashMap<String, Integer>();
        for (ExperimentState state : info.getStates())
        {
            s.put(state.getName(), state.getWeightage());
        }
        experiment.setStateWeights(s);
        return experiment;
    }

    private void updateExperiment(Experiment experiment)
    {
        HashMap<String, String> temp = new HashMap<String, String>();
        temp.put(this.authorName, experiment.getAuthorName());
        temp.put(this.authorEmailAddress, experiment.getAuthorEmailAddress());
        temp.put(this.passcode, experiment.getPasscode());

        for (Entry<String, Integer> entry : experiment.getStateWeights().entrySet())
        {
            temp.put(entry.getKey(), entry.getValue().toString());
        }
        jedis.hmset(experiment.getExperimentName(), temp);
    }

    public boolean createExperiment(Experiment experiment) throws ExperimentAlreadyExistsException
    {
        Map<String, String> x = jedis.hgetAll(experiment.getExperimentName());

        if (x != null && !x.isEmpty())
            throw new ExperimentAlreadyExistsException("The experiment name already exists!...");

        this.updateExperiment(experiment);
        map.remove(experiment.getExperimentName());
        return true;
    }

    public boolean editExperiment(Experiment experiment) throws ExperimentNotFoundException
    {
        Map<String, String> x = jedis.hgetAll(experiment.getExperimentName());
        if (x == null || x.isEmpty())
            throw new ExperimentNotFoundException("The input experiment was not found!...");

        this.updateExperiment(experiment);
        map.remove(experiment.getExperimentName());
        return true;
    }
}
