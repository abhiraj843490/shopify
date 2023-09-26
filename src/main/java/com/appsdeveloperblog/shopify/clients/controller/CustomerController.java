package com.appsdeveloperblog.shopify.clients.controller;

import com.appsdeveloperblog.shopify.clients.entity.*;
import com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment.FulFillmentDto;
import com.appsdeveloperblog.shopify.clients.entity.createCustomerDto.Customer;
import com.appsdeveloperblog.shopify.clients.entity.createCustomerDto.CustomerDto;
import com.appsdeveloperblog.shopify.clients.entity.createOrder.CreateOrder;
import com.appsdeveloperblog.shopify.clients.entity.createOrder.CreateOrderDto;
import com.appsdeveloperblog.shopify.clients.entity.createOrder.CreateOrderList;
import com.appsdeveloperblog.shopify.clients.entity.createProductDto.Product;
import com.appsdeveloperblog.shopify.clients.entity.createProductDto.ProductDto;
import com.appsdeveloperblog.shopify.clients.entity.email.EmailDetails;
import com.appsdeveloperblog.shopify.clients.repository.CustomerRepository;
import com.appsdeveloperblog.shopify.clients.repository.OrderRepository;
import com.appsdeveloperblog.shopify.clients.repository.ProductRepository;
import com.appsdeveloperblog.shopify.clients.repository.UserRepository;
import com.appsdeveloperblog.shopify.clients.security.service.JwtService;
import com.appsdeveloperblog.shopify.clients.service.EmailService;
import com.appsdeveloperblog.shopify.clients.service.ShopifyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;

@Log4j2
@RestController
@CrossOrigin(origins = "http://localhost:4000")
public class CustomerController {
    @Value("${shopify.app.url}")
    private String shopifyUrl;
    @Value("${shopify.app.scope}")
    private String scopeName;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ShopifyService service;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    BCryptPasswordEncoder encoder;
    @Autowired
    private EmailService emailService;

    @Autowired
    JwtService jwtService;
    Gson gson = new Gson();

    //creating web hook
    @PostMapping("/products/update")
    public void products(@RequestBody Products data){
        System.out.println(data);
        productRepository.save(data);
    }
    @PostMapping("/products/delete")
    public void deleteProduct(@RequestBody Product product){
        System.out.println(product+" deleted");
        productRepository.deleteById(product.getId());
    }
    @PostMapping("/products/create")
    public void productCreate(@RequestBody Product product) {
        log.info(product+" created");
        Products products = new Products();
        BeanUtils.copyProperties(product, products);
        productRepository.save(products);
    }
    @PostMapping("/customer/delete")
    public void deleteCustomer(@RequestBody Customer customer){
        log.info(customer+" deleted");
        customerRepository.deleteById(customer.getId());
    }
    @PostMapping("/customer/update")
    public void updateCustomer(@RequestBody Customer customer){
        log.info(customer);
        Customers customers = new Customers();
        BeanUtils.copyProperties(customer, customers);
        customerRepository.save(customers);
    }
    @PostMapping("/customer/create")
    public void customerCreate(@RequestBody Customer customer) {

        String json = gson.toJson(customer);
        Customers customers = gson.fromJson(json, Customers.class);
        log.info("customers: "+customers);
        customerRepository.save(customers);
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//

    @GetMapping("/products")
    @CrossOrigin(origins = "http://localhost:4000")
    public ResponseEntity<?> getProducts(){
        List<Products> list = service.getAllProducts();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable (value = "id") Long id){
        log.info("product id "+id);
        Products products= service.getProductById(id);
        return ResponseEntity.ok(products);
    }
    @PutMapping("/product/update/{id}")
    public ProductDto updateProduct(@RequestBody Product product,
                                      @PathVariable(value = "id")Long id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        log.info("update product "+id);
        ProductDto list = service.updateProduct(product,id,shopName);
        return list;
    }


    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping("/customers")
    public ResponseEntity<?> getCustomers(){
        List<Customers>list = service.getAllCustomers();
        return ResponseEntity.ok(list);

    }
    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping("/customer/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable (value = "id") Long id){
        log.info("customer id "+id);
        Customers customers= service.getCustomerById(id);
        return ResponseEntity.ok(customers);
    }

    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping("/orders")
    public ResponseEntity<?> getOrders(){
        List<Orders>list = service.getAllOrders();
        return ResponseEntity.ok(list);
    }
    @GetMapping("/allOrders")
    public ResponseEntity<?> getAllOrders(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);

        UserEntity userEntity = userRepository.findByShopName(shopName).get();
        String token = userEntity.getShopifyToken();
        String url = "https://"+shopName+"/admin/api/2023-04/orders.json";
        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<OrderList> res = webClient.get()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .retrieve().bodyToMono(OrderList.class);
        OrderList list = res.block();

        for(Orders orders: list.getOrders())
            orderRepository.save(orders);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/allProducts")
    public ResponseEntity<?> getAllProducts(){
        UserEntity userEntity = userRepository.findByShopName("lucenttrainingstore.myshopify.com").get();
        String token = userEntity.getShopifyToken();
        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/products.json";
        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<ProductList> res = webClient.get()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .retrieve().bodyToMono(ProductList.class);
        ProductList list = res.block();
        log.info(list);
        for(Products products: list.getProducts())
            productRepository.save(products);
        return ResponseEntity.ok(list);
    }
    @CrossOrigin(origins = "http://localhost:4000")
    @DeleteMapping("/del/orders/{id}")
    public void deleteOrder(@PathVariable(value = "id")Long id,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);

        log.info("form order"+id);
        UserEntity userEntity = userRepository.findByShopName(shopName).get();
        String token = userEntity.getShopifyToken();
        WebClient webClient = WebClient.create();
        webClient.delete()
                .uri("https://lucenttrainingstore.myshopify.com/admin/api/2023-04/orders/"+id+".json")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Shopify-Access-Token", token)
                .exchange()
                .block();
    }
    @CrossOrigin(origins = "http://localhost:4000")
    @DeleteMapping("/del/customer/{id}")
    public void deleteCustomer(@PathVariable(value = "id")Long id,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);

        log.info("form customer :"+id);
        UserEntity userEntity = userRepository.findByShopName(shopName).get();
        String token = userEntity.getShopifyToken();
        WebClient webClient = WebClient.create();
        webClient.delete()
                .uri("https://lucenttrainingstore.myshopify.com/admin/api/2023-04/customers/"+id+".json")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Shopify-Access-Token", token)
                .exchange()
                .block();
    }
    @CrossOrigin(origins = "http://localhost:4000")
    @DeleteMapping("/del/product/{id}")
    public void deleteProduct(@PathVariable(value = "id")Long id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        log.info("form product :"+id);
        String url ="https://lucenttrainingstore.myshopify.com/admin/api/2023-04/products/";

        UserEntity userEntity = userRepository.findByShopName(shopName).get();
        String token = userEntity.getShopifyToken();
        WebClient webClient = WebClient.builder()
                .baseUrl(url).build();

        webClient.delete()
                .uri(id+".json")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Shopify-Access-Token", token)
                .exchange()
                .block();
    }

    @CrossOrigin(origins = "http://localhost:4000")
    @GetMapping("/success")
    public String getToken() {

        UserEntity user = userRepository.findByShopName("lucenttrainingstore.myshopify.com").get();
        String access_token = user.getShopifyToken();
        String shopName = user.getShopName();

        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-01/shop.json";
        WebClient webClient = WebClient
                .builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector()).build();
        Mono<String> res = webClient.get().uri("/")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-Shopify-Access-Token", access_token)
                .retrieve().bodyToMono(String.class);

        String shopId = "";
        String response = res.block();

        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response);
            JsonNode idNode = root.path("shop").path("id");
            shopId = idNode.asText();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("Shop ID: " + shopId);
        //store in database

        user.setShopId(encoder.encode(shopId));

        if (!userRepository.findByShopName(shopName).isPresent()) {
            userRepository.save(user);
        } else {
            userRepository.deleteAll();
            userRepository.save(user);
        }
        //Jwt Token
        Authentication authentication = authenticationManager
                    .authenticate(new
                            UsernamePasswordAuthenticationToken(shopName, shopId));
            if (authentication.isAuthenticated()) {
                log.info(jwtService.generateToken(user.getShopName()));

                return jwtService.generateToken(user.getShopName());

            } else {
                throw new UsernameNotFoundException("invalid user request");
        }
//        return token;
    }
    @CrossOrigin(origins = "http://localhost:4000")
    @PostMapping("/create/customer")
    public void   createCustomer(@RequestBody Customer customer,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        log.info(customer);
        service.createCustomer(customer, shopName);
    }
    @CrossOrigin(origins = "http://localhost:4000")
    @PutMapping("/update/customer/{id}")
    public CustomerDto updateCustomer(@RequestBody Customer customer,
                                    @PathVariable(value = "id")Long id,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        log.info("update customer "+id);
        return service.updateCustomer(customer,id,shopName);

    }
    @PostMapping("/create/product")
    public ProductDto createProduct(@RequestBody Product product, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        return service.createProduct(product,shopName);

    }
    @PostMapping("/create/order")
    public void createOrder(@RequestBody CreateOrderDto order, HttpServletRequest req){

        String header = req.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName= jwtService.extractUsername(jwt);
        service.createOrder(order,shopName);

    }
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails emailDetails, HttpServletRequest req){
        log.info(req.getPathInfo());
        return emailService.sendMail(emailDetails);

    }
    @GetMapping("/fulfillmentOrder/{id}")
    public FulFillmentDto fulFillOrder(@PathVariable(value = "id")Long id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);

        FulFillmentDto dto = service.fulFillOrder(id,shopName);
        log.info(dto);
        return dto;
    }
    //by 3rd party app
    @GetMapping("/request/{id}")
    public void requestFulfillment(@PathVariable(value = "id")Long id,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        log.info("order id "+id);
        service.requestFulfillment(id,shopName);
    }
    @PostMapping("/call_back/fulfillment_order_notification")
    public void acceptRequest(HttpServletRequest request){
        log.info("callback "+request.toString());
    }
    @GetMapping("/cus")
    public String customerDetails(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        return service.customerDetails(shopName);
    }
    @GetMapping("/prod")
    public String productDetails(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shopName = jwtService.extractUsername(jwt);
        return service.productDetails(shopName);
    }
    @PostMapping("cre/custm")
    public String createCustomerGql(@RequestBody Customer customerInput, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        log.info("customer " + customerInput.getEmail());
        String jwt = header.substring(7);
        String shop = jwtService.extractUsername(jwt);
        return service.createCustomerGql(shop, customerInput);
    }
    @PostMapping("del/custm/{id}")
    public String deleteCustomerGql(@PathVariable(value = "id")Long id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shop = jwtService.extractUsername(jwt);
        return service.deleteCustomerGql(shop, id);
    }
    @PostMapping("update/custm/{id}")
    public String updateCustomerGql(@PathVariable(value = "id")Long id,@RequestBody Customer customer ,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shop = jwtService.extractUsername(jwt);
        return service.updateCustomerGql(shop, id,customer);
    }
    @GetMapping("cre/prod")
    public String createProductGql(@RequestBody Product product, HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shop = jwtService.extractUsername(jwt);
        return service.createProductGql(shop, product);
    }
    @PostMapping("del/prod/{id}")
    public String deleteProductGql(@PathVariable(value = "id")Long id, HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shop = jwtService.extractUsername(jwt);
        return service.deleteProductGql(shop,id);
    }
    @PostMapping("update/prod/{id}")
    public String updateProductGql(@PathVariable(value = "id")Long id,@RequestBody Product product ,HttpServletRequest request){
        String header = request.getHeader("Authorization");
        String jwt = header.substring(7);
        String shop = jwtService.extractUsername(jwt);
        return service.updateProductGql(shop,product,id);
    }
}
