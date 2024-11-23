package model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Sale {
    private int id;
    private float totalCost;
    private String sellerUsername, date;

    public Sale(int id, float totalCost, String sellerUsername, String date) {
        this.id = id;
        this.totalCost = totalCost;
        this.sellerUsername = sellerUsername;
        this.date = date;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }
}
