package pl.vost.kresyinwentarzfx.controller.element;

import pl.vost.kresyinwentarzfx.persistence.products.Product;

import java.math.BigDecimal;

public class ViewProduct{

    private Long id;
    private String name;
    private Integer quantity;
    private Double price;
    private Double totalPrice;

    public ViewProduct(Long id, String name, Long quantity, Double price, Double totalPrice){
        this.id = id;
        this.name = name;
        this.quantity = quantity.intValue();
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public ViewProduct(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.quantity = product.getQuantity().intValue();
        this.price = product.getPrice().doubleValue();
        this.totalPrice = product.getTotalPrice().doubleValue();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public void setQuantity(Integer quantity){
        this.quantity = quantity;
    }

    public Double getPrice(){
        return price;
    }

    public void setPrice(Double price){
        this.price = price;
    }

    public Double getTotalPrice(){
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice){
        this.totalPrice = totalPrice;
    }
}
