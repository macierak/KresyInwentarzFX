package pl.vost.kresyinwentarzfx.persistence;

import pl.vost.kresyinwentarzfx.persistence.products.Product;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;

import java.util.List;

public class ProductRepository extends BaseRepositoryImpl<Product>{

    public ProductRepository(){
        super(Product.class);
    }

    public Product save(Product product){
        product.recalculateTotalPrice();
        return super.save(product);
    }

    public Product update(Product product){
        product.recalculateTotalPrice();
        return super.update(product);
    }

    public boolean hasProductByName(String name){
        return !entityManager.createQuery("SELECT p FROM Product p WHERE p.name = :name", Product.class)
                .setParameter("name", name)
                .getResultList().isEmpty();
    }

    public List<Product> getProductsByWarehouse(Warehouse warehouse){
        return entityManager.createQuery("SELECT p FROM Product p WHERE p.warehouse = :warehouse", Product.class)
                .setParameter("warehouse", warehouse)
                .getResultList();
    }

    public Product getProductByNameAndMag(String name, Warehouse warehouse){
        final var list = entityManager.createQuery("SELECT p FROM Product p WHERE p.name = :name and p.warehouse = " +
                        ":warehouse", Product.class)
                .setParameter("name", name)
                .setParameter("warehouse", warehouse)
                .getResultList();
        return list.isEmpty() ? null : list.getFirst();
    }

}
