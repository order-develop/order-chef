package com.example.chef.model;

import java.util.Date;

public class PackageDetail {
    private Integer foodPackageId;
    private Integer foodId;
    private Date cTime;
    private Date mTime;

    public Integer getFoodPackageId() {
        return foodPackageId;
    }

    public void setFoodPackageId(Integer foodPackageId) {
        this.foodPackageId = foodPackageId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getmTime() {
        return mTime;
    }

    public void setmTime(Date mTime) {
        this.mTime = mTime;
    }
}