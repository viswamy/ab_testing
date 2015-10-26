package com.vswamy.ab_testing;

import java.util.HashMap;

public class ExperimentInfo
{
    private String experimentName;

    private String authorName;
    private String authorEmailAddress;
    private String passcode;
    private ExperimentState[] states;

    public String getExperimentName()
    {
        return experimentName;
    }

    public void setExperimentName(String experimentName)
    {
        this.experimentName = experimentName;
    }

    public String getAuthorName()
    {
        return authorName;
    }

    public void setAuthorName(String authorName)
    {
        this.authorName = authorName;
    }

    public String getAuthorEmailAddress()
    {
        return authorEmailAddress;
    }

    public void setAuthorEmailAddress(String authorEmailAddress)
    {
        this.authorEmailAddress = authorEmailAddress;
    }

    public String getPasscode()
    {
        return passcode;
    }

    public void setPasscode(String passcode)
    {
        this.passcode = passcode;
    }

    public ExperimentState[] getStates()
    {
        return states;
    }

    public void setStates(ExperimentState[] states)
    {
        this.states = states;
    }

}
