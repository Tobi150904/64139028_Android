package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SoDu")
public class SoDu {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private double amount;

    public SoDu(double amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
