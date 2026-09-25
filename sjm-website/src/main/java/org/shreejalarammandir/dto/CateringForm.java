package org.shreejalarammandir.dto;

import jakarta.validation.constraints.NotBlank;

public class CateringForm {

    @NotBlank
    private String mealSummary;

    public String getMealSummary() {
        return mealSummary;
    }

    public void setMealSummary(String mealSummary) {
        this.mealSummary = mealSummary;
    }
}
