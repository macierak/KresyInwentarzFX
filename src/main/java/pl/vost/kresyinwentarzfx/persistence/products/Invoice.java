package pl.vost.kresyinwentarzfx.persistence.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Entity
public class Invoice{

    @Id
    @GeneratedValue(generator = "ID_GENERATOR")
    @Column(name = "id", nullable = false)
    private Long id;

    private String number;
    private LocalDate date;

    @OneToMany(mappedBy = "invoice")
    private List<Product> products;

    public Invoice(Long id, String number, LocalDate date, List<Product> products){
        this.id = id;
        this.number = number;
        this.date = date;
        this.products = products;
    }

    public Invoice(){
    }

    public static InvoiceBuilder builder(){
        return new InvoiceBuilder();
    }

    public Long getId(){
        return this.id;
    }

    public String getNumber(){
        return this.number;
    }

    public LocalDate getDate(){
        return this.date;
    }

    public List<Product> getProducts(){
        return this.products;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }

    public static class InvoiceBuilder{
        private Long id;
        private String number;
        private LocalDate date;
        private List<Product> products;

        InvoiceBuilder(){
        }

        public InvoiceBuilder id(Long id){
            this.id = id;
            return this;
        }

        public InvoiceBuilder number(String number){
            this.number = number;
            return this;
        }

        public InvoiceBuilder date(LocalDate date){
            this.date = date;
            return this;
        }

        public InvoiceBuilder products(List<Product> products){
            this.products = products;
            return this;
        }

        public Invoice build(){
            return new Invoice(this.id, this.number, this.date, this.products);
        }

        public String toString(){
            return "Invoice.InvoiceBuilder(id=" + this.id + ", number=" + this.number + ", date=" + this.date + ", products=" + this.products + ")";
        }
    }
}
