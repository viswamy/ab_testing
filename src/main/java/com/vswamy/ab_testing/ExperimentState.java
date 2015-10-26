package com.vswamy.ab_testing;

public class ExperimentState
{
    private String name;
    private int weightage;
    private int cumulativeWeightage;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getWeightage()
    {
        return weightage;
    }

    public void setWeightage(int weightage)
    {
        this.weightage = weightage;
    }

    public int getCumulativeWeightage()
    {
        return cumulativeWeightage;
    }

    public void setCumulativeWeightage(int cumulativeWeightage)
    {
        this.cumulativeWeightage = cumulativeWeightage;
    }

}
