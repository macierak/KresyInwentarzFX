package pl.vost.kresyinwentarzfx.persistence.products;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Warehouse{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String code;
    private String name;

    @OneToMany(mappedBy = "warehouse")
    private List<Product> products;


    public Warehouse(Long id, String code, String name, List<Product> products){
        this.id = id;
        this.code = code;
        this.name = name;
        this.products = products;
    }

    public Warehouse(){
    }

    public static WarehouseBuilder builder(){
        return new WarehouseBuilder();
    }

    public Long getId(){
        return this.id;
    }

    public String getCode(){
        return this.code;
    }

    public String getName(){
        return this.name;
    }

    public List<Product> getProducts(){
        return this.products;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setCode(String code){
        this.code = code;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }

    public static class WarehouseBuilder{
        private Long id;
        private String code;
        private String name;
        private List<Product> products;

        WarehouseBuilder(){
        }

        public WarehouseBuilder id(Long id){
            this.id = id;
            return this;
        }

        public WarehouseBuilder code(String code){
            this.code = code;
            return this;
        }

        public WarehouseBuilder name(String name){
            this.name = name;
            return this;
        }

        public WarehouseBuilder products(List<Product> products){
            this.products = products;
            return this;
        }

        public Warehouse build(){
            return new Warehouse(this.id, this.code, this.name, this.products);
        }

        public String toString(){
            return "Warehouse.WarehouseBuilder(id=" + this.id + ", code=" + this.code + ", name=" + this.name + ", products=" + this.products + ")";
        }
    }
}
