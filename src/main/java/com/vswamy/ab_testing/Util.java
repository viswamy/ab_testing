package com.vswamy.ab_testing;

import java.util.Random;

import com.vswamy.ab_testing.exception.NullArgumentException;
import com.vswamy.ab_testing.exception.ZeroLengthArgumentException;

public enum Util
{
    INSTANCE;
    Random rand = new Random();

    public void validateExperimentStates(ExperimentState[] experimentStates)
    {
        if (experimentStates == null)
            throw new NullArgumentException("The input argument cannot be null");

        if (experimentStates.length == 0)
            throw new ZeroLengthArgumentException(
                    "The input argument cannot be of size 0");

    }

    public ExperimentState getRandomExperimentState(
            ExperimentState[] experimentStates)
    {
        validateExperimentStates(experimentStates);
        int totalWeightage = experimentStates[experimentStates.length - 1]
                .getCumulativeWeightage();
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
}
