package server.services;

import context.Component;
import server.dto.AllProductsPayload;
import server.repositories.ProductRepository;

import java.util.Map;

public class ProductServiceImpl implements ProductService, Component {

    private ProductRepository repository;

    public ProductServiceImpl(ProductRepository repository) {
        this.repository = repository;
    }

    public ProductServiceImpl(){}


    @Override
    public AllProductsPayload sellProduct(String name, int count) {
        repository.addProduct(name, count);
        return listProducts();
    }

    @Override
    public AllProductsPayload listProducts() {
        Map<String, Integer> map = repository.listProduct();
        return AllProductsPayload.createPayload(map);
    }

    public ProductRepository getRepository() {
        return repository;
    }

    public void setRepository(ProductRepository repository) {
        this.repository = repository;
    }
}
