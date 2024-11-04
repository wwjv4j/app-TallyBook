package com.example.tallybook.Entity;
import com.github.mikephil.charting.data.PieEntry;
public class CategoryAmount {
    public String category;
    public float amount;

    public CategoryAmount(String category, float amount) {
        this.category = category;
        this.amount = amount;
    }

    public PieEntry toPieEntry() {
        return new PieEntry(amount, category);
    }
}
