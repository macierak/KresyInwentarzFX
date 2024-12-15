package pl.vost.kresyinwentarzfx.domain;

import pl.vost.kresyinwentarzfx.persistence.BaseRepositoryImpl;
import pl.vost.kresyinwentarzfx.persistence.products.Warehouse;
import pl.vost.kresyinwentarzfx.protocol.request.CreateWarehouseRequest;

import java.util.List;

public class WarehouseService{

    private final BaseRepositoryImpl<Warehouse> repository;

    public WarehouseService(){
        this.repository = new BaseRepositoryImpl<>(Warehouse.class);
    }

    public void createWarehouse(CreateWarehouseRequest request){
        final var warehouse = Warehouse.builder()
                .name(request.name())
                .code(request.code())
                .build();

        repository.save(warehouse);
    }

    public List<Warehouse> getAllWarehouses(){
        return repository.findAll();
    }

    public Warehouse getWarehouseById(Long id){
        return repository.findById(id);
    }
}
