package com.appsdeveloperblog.shopify.clients.controller;

import com.appsdeveloperblog.shopify.clients.entity.*;
import com.appsdeveloperblog.shopify.clients.repository.CustomerRepository;
import com.appsdeveloperblog.shopify.clients.repository.ProductRepository;
import com.appsdeveloperblog.shopify.clients.repository.VariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CustomerController {
    @Value("${shopify.app.url}")
    private String shopifyUrl;
    @Value("${shopify.app.token}")
    private String token;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    VariantRepository variantRepository;

    @GetMapping("/products")
    public Mono<ProductList> getProducts(){

        String url = shopifyUrl+"products.json";
//        String url = "https://lucenttraining.myshopify.com/admin/api/2023-01/products.json";
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .clientConnector(new ReactorClientHttpConnector())
                .build();
         Mono<ProductList>res = webClient.get()
                .uri("/")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Shopify-Access-Token", token)
                .retrieve()
                .bodyToMono(ProductList.class);
         ProductList list = res.block();//make it synchronized
         List<Products> arrayList = list.getProducts();
//        List<Products> arrayList = new ArrayList<>();
//        for(int i=0;i<list.getProducts().size();i++){
//            Products products = new Products();
//            products.setId(list.getProducts().get(i).getId());
//            products.setTitle(list.getProducts().get(i).getTitle());
//            products.setVendor(list.getProducts().get(i).getVendor());
//            arrayList.add(products);
//        }


        productRepository.saveAll(arrayList);

//        variantRepository.saveAll(response);
//        System.out.println(list.getProducts().get(0).getVariants().get(0).getInventory_policy());
         return res;
    }
    @GetMapping("/customers")
    public Mono<CustomerList>getCustomers(){
        String url = shopifyUrl+"customers.json";
        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<CustomerList> res = webClient.get()
                .uri("/")
                .header("Content-Type",MediaType.APPLICATION_JSON_VALUE)
                .header("X-Shopify-Access-Token",token)
                .retrieve().bodyToMono(CustomerList.class);
        CustomerList list = res.block();
//        System.out.println("Name = "+list.getCustomers().get(1).getFirst_name());
//        System.out.println("length "+list.getCustomers().size());

        List<Customers> arrayList = list.getCustomers();
        customerRepository.saveAll(arrayList);
        return res;
    }

}
