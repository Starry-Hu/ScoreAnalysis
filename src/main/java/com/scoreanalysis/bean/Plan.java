package com.scoreanalysis.bean;

public class Plan {
    private String planId;

    private String planName;

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Plan plan = (Plan) o;

        if (planId != null ? !planId.equals(plan.planId) : plan.planId != null) return false;
        return planName != null ? planName.equals(plan.planName) : plan.planName == null;
    }

    @Override
    public int hashCode() {
        int result = planId != null ? planId.hashCode() : 0;
        result = 31 * result + (planName != null ? planName.hashCode() : 0);
        return result;
    }
}