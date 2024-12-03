// SoDu.java
package vn.ngoviethoang.duancuoiky.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SoDu {
    @PrimaryKey(autoGenerate = true)
    private int id = 1;
    private int balance;

    public SoDu() {
    }

    public SoDu(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}