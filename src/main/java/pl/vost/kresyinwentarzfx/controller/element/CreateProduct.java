package pl.vost.kresyinwentarzfx.controller.element;

import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class CreateProduct{

    private String name = "";
    private Integer quantity = 0;
    private Warehouse warehouse = new Warehouse();
    private BigDecimal price = BigDecimal.ZERO;

    public CreateProduct(){
    }

    public Warehouse getWarehouse(){
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
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

    public BigDecimal getPrice(){
        return price;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }
}
