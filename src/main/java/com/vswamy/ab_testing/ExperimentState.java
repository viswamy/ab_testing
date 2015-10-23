package com.vswamy.ab_testing;

class ExperimentState
{
    private String stateName;
    private Integer weightage;
    private Integer cumulativeWeightage;
    
    ExperimentState(String stateName, Integer weightage)
    {
        this.stateName = stateName;
        this.weightage = weightage;
    }
    
    public Integer getCumulativeWeightage()
    {
        return cumulativeWeightage;
    }

    public void setCumulativeWeightage(Integer cumulativeWeightage)
    {
        this.cumulativeWeightage = cumulativeWeightage;
    }

    public String getStateName()
    {
        return stateName;
    }

    public void setStateName(String stateName)
    {
        this.stateName = stateName;
    }

    public Integer getWeightage()
    {
        return weightage;
    }

    public void setWeightage(Integer weightage)
    {
        this.weightage = weightage;
    }
}
