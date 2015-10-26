package com.vswamy.ab_testing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        ExperimentState[] experimentStates = info.states;

        int totalWeightage = experimentStates[experimentStates.length - 1].getCumulativeWeightage();
        int randomNumber = rand.nextInt(totalWeightage);

        ExperimentState retExperimentState = null;

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
                //Log and consume the exception
                logger.debug("Experiment not found => {}", exception);
            }
        }
        return map;
    }
}
