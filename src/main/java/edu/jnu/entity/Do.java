package edu.jnu.entity;
public class Do {
    private String Car_name;
    private double Min_price;
    private double Max_price;
    private float rating;
    private String image;

    // 构造函数
    public Do(String Car_name, double Min_price, double Max_price, float rating, String image) {
        this.Car_name = Car_name;
        this.Min_price = Min_price;
        this.Max_price = Max_price;
        this.rating = rating;
        this.image = image;
    }

    // Getter 和 Setter 方法（可选）
    public String getCar_name() {
        return Car_name;
    }

    public void setCar_name(String Car_name) {
        this.Car_name = Car_name;
    }

    public double getMin_price() {
        return Min_price;
    }

    public void setMin_price(double Min_price) {
        this.Min_price = Min_price;
    }

    public double getMax_price() {
        return Max_price;
    }

    public void setMax_price(double Max_price) {
        this.Max_price = Max_price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}