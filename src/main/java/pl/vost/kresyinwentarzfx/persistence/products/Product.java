package pl.vost.kresyinwentarzfx.persistence.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;

@Entity
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
    private Long quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    public Product(String name, BigDecimal price, Warehouse warehouse, Invoice invoice){
        this.name = name;
        this.price = price;
        this.warehouse = warehouse;
        this.invoice = invoice;
        this.quantity = 0L;
        this.totalPrice = BigDecimal.valueOf(this.quantity).multiply(this.price);
    }


    public Product(Long id, String name, Long quantity, BigDecimal price, Warehouse warehouse, Invoice invoice){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.warehouse = warehouse;
        this.invoice = invoice;

        this.totalPrice = BigDecimal.valueOf(this.quantity).multiply(this.price);
    }

    public void recalculateTotalPrice(){
        this.totalPrice = BigDecimal.valueOf(this.quantity).multiply(this.price);
        if(this.totalPrice.compareTo(BigDecimal.ZERO) < 0){
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    public void addQuantity(Long quantity){
        this.quantity += quantity;
    }

    public void subtractQuantity(Long quantity){
        this.quantity -= quantity;
    }

    public Product(){
    }

    public BigDecimal getTotalPrice(){
        return totalPrice;
    }

    public static ProductBuilder builder(){
        return new ProductBuilder();
    }

    public Long getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public Long getQuantity(){
        return this.quantity;
    }

    public BigDecimal getPrice(){
        return this.price;
    }

    public Warehouse getWarehouse(){
        return this.warehouse;
    }

    public Invoice getInvoice(){
        return this.invoice;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setQuantity(Long quantity){
        this.quantity = quantity;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public void setWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    public void setInvoice(Invoice invoice){
        this.invoice = invoice;
    }

    public static class ProductBuilder{
        private Long id;
        private String name;
        private Long quantity;
        private BigDecimal price;
        private Warehouse warehouse;
        private Invoice invoice;

        public ProductBuilder(){
        }

        public ProductBuilder id(Long id){
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name){
            this.name = name;
            return this;
        }

        public ProductBuilder quantity(Long quantity){
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder price(BigDecimal price){
            this.price = price;
            return this;
        }

        public ProductBuilder warehouse(Warehouse warehouse){
            this.warehouse = warehouse;
            return this;
        }

        public ProductBuilder invoice(Invoice invoice){
            this.invoice = invoice;
            return this;
        }

        public Product build(){
            return new Product(this.id, this.name, this.quantity, this.price, this.warehouse, this.invoice);
        }

        public String toString(){
            return "Product.ProductBuilder(id=" + this.id + ", name=" + this.name + ", quantity=" + this.quantity + ", price=" + this.price + ", warehouse=" + this.warehouse + ", invoice=" + this.invoice + ")";
        }
    }
}
