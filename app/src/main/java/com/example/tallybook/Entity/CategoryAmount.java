package com.example.tallybook.Entity;
import com.github.mikephil.charting.data.PieEntry;
public class CategoryAmount {
    public String category;
    public float allAmount;

    public CategoryAmount(String category, float allAmount) {
        this.category = category;
        this.allAmount = allAmount;
    }

    public PieEntry toPieEntry() {
        return new PieEntry(allAmount, category);
    }
}
