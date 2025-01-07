package pl.vost.kresyinwentarzfx.domain;

import pl.vost.kresyinwentarzfx.controller.element.ViewProduct;
import pl.vost.kresyinwentarzfx.persistence.BaseRepositoryImpl;
import pl.vost.kresyinwentarzfx.persistence.ProductRepository;
import pl.vost.kresyinwentarzfx.persistence.products.Product;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;
import pl.vost.kresyinwentarzfx.protocol.request.CreateProductRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


public class ProductService{

    private final ProductRepository productRepository;
    private final WarehouseService warehouseService;
    private final InvoiceService invoiceService;

    public ProductService(){
        this.productRepository = new ProductRepository();
        this.warehouseService = new WarehouseService();
        this.invoiceService = new InvoiceService();
    }

    public ViewProduct decrementProduct(ViewProduct viewProduct){
        final var product = productRepository.findById(viewProduct.getId());
        product.subtractQuantity(1L);
        final var saved = productRepository.update(product);
        return new ViewProduct(saved);
    }

    public ViewProduct updateProduct(ViewProduct viewProduct){
        final var product = productRepository.findById(viewProduct.getId());
        product.setName(viewProduct.getName());
        product.setPrice(BigDecimal.valueOf(viewProduct.getPrice()));
        product.setQuantity(viewProduct.getQuantity().longValue());
        final var saved = productRepository.update(product);
        return new ViewProduct(saved);
    }

    public Map<Warehouse, List<Product>> getAllProductsMap(){
        return productRepository.findAll().stream()
                .collect(Collectors.groupingBy(Product::getWarehouse));
    }

    public void saveProducts(List<Product> products){
        products.forEach(this::saveProduct);
    }

    public void saveProduct(Product product){
        if(Objects.isNull(product.getWarehouse().getId())){
            return;
        }
        final var warehouse = warehouseService.getWarehouseById(product.getWarehouse().getId());
        final var productInRepo = productRepository.getProductByNameAndMag(product.getName(), warehouse);

        if(Objects.isNull(productInRepo)){
            productRepository.save(product);
            return;
        }

        productInRepo.addQuantity(product.getQuantity());
        productRepository.update(productInRepo);
    }

    public void createProduct(CreateProductRequest request){
        final var warehouse = warehouseService.getWarehouseById(request.warehouseId());
        final var invoice = invoiceService.getInvoiceById(request.invoiceId());

        final var product = Product.builder()
                .quantity(0L)
                .invoice(invoice)
                .warehouse(warehouse)
                .name(request.name())
                .price(request.price())
                .build();

        saveProduct(product);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id);
    }

    public List<Product> getAllProductsForWarehouse(Warehouse warehouse){
        return productRepository.getProductsByWarehouse(warehouse);
    }

    public List<Product> getAllProductsByInvoiceId(Long invoiceId){
        final var invoice = invoiceService.getInvoiceById(invoiceId);
        return invoice.getProducts();
    }
}
