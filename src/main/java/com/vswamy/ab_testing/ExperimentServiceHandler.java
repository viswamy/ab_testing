package com.vswamy.ab_testing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.thrift.TException;

import redis.clients.jedis.Jedis;

import com.vswamy.ab_testing.ExperimentService;
import com.vswamy.ab_testing.exception.NullArgumentException;
import com.vswamy.ab_testing.exception.ZeroLengthArgumentException;

public enum ExperimentServiceHandler implements ExperimentService.Iface
{
    INSTANCE;
    Random rand = new Random();
    HashMap<String, ExperimentState[]> map = new HashMap<String, ExperimentState[]>();
    Jedis jedis = new Jedis(Constants.REDIS_SERVER_HOST, Constants.REDIS_SERVER_PORT);

    private void validateExperimentStates(ExperimentState[] experimentStates) throws NullArgumentException,
            ZeroLengthArgumentException
    {
        if (experimentStates == null)
            throw new NullArgumentException("The input argument cannot be null");

        if (experimentStates.length == 0)
            throw new ZeroLengthArgumentException("The input argument cannot be of size 0");

    }

    private ExperimentState getRandomExperimentState(ExperimentState[] experimentStates) throws NullArgumentException,
            ZeroLengthArgumentException
    {
        validateExperimentStates(experimentStates);
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

        return retExperimentState;
    }

    public boolean ping() throws TException
    {
        return true;
    }

    public ExperimentStateResponse getExperimentState(ExperimentStateRequest request)
            throws ExperimentNotFoundException
    {
        String experimentName = request.getExperimentName();
        boolean debugInfo = request.isDebugInfo();
        ExperimentStateResponse response = new ExperimentStateResponse();
        if (!map.containsKey(experimentName))
        {
            populate(experimentName);
        }
        response.setExperimentName(experimentName);
        response.setState(getRandomExperimentState(map.get(experimentName)));

        if (debugInfo)
        {
            response.setAllStates(Arrays.asList(map.get(experimentName)));
            response.setDebugInfo(true);
        }

        return response;
    }

    /**
     * Fetches data for a given experiment in redis and puts it in the HashMap
     * 
     * @param experimentName
     */
    private void populate(String experimentName)
    {
        Map<String, String> temp = jedis.hgetAll(experimentName);
        if (temp == null)
            new ExperimentNotFoundException("The given experiment was not found");

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
        map.put(experimentName, states);
        return;
    }
}
