package com.appsdeveloperblog.shopify.clients.service;

import com.appsdeveloperblog.shopify.clients.entity.*;
import com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment.FulFillMentLineItems;
import com.appsdeveloperblog.shopify.clients.entity.OrderFulFillment.FulFillmentDto;
import com.appsdeveloperblog.shopify.clients.entity.createCustomerDto.Customer;
import com.appsdeveloperblog.shopify.clients.entity.createCustomerDto.CustomerDto;
import com.appsdeveloperblog.shopify.clients.entity.createOrder.*;
import com.appsdeveloperblog.shopify.clients.entity.createProductDto.Product;
import com.appsdeveloperblog.shopify.clients.entity.createProductDto.ProductDto;
import com.appsdeveloperblog.shopify.clients.entity.externalApp.ExternalFulfillReq;
import com.appsdeveloperblog.shopify.clients.entity.externalApp.ExternalReqBodyApp;
import com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody.FulFillMentBody;
import com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody.FulFillMentBodyDto;
import com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody.FulfillmentOrderLineItems;
import com.appsdeveloperblog.shopify.clients.entity.fulfillmentReqBody.LineIitemsByFulfillmentOrder;
import com.appsdeveloperblog.shopify.clients.entity.fulfillmentResponse.FulfillmentResDto;
import com.appsdeveloperblog.shopify.clients.repository.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import java.util.*;

@Slf4j
@Service
public class ShopifyService {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    FulfillmentRepository fulfillmentRepository;
    @Autowired
    FulfillmentResRepository fulfillmentResRepository;

    public CustomerDto createCustomer(Customer customer,String shop) {
        try {
            UserEntity userEntity = userRepository.findByShopName(shop).get();
            String token = userEntity.getShopifyToken();

            CustomerDto customerDto = new CustomerDto();
            customerDto.setCustomer(customer);
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/customers.json";

            WebClient webClient = WebClient.builder()
                    .baseUrl(url)
                    .build();
            Mono<CustomerDto> res = webClient.post()
                    .header("Content-Type", "application/json")
                    .header("X-Shopify-Access-Token",token)
                    .body(Mono.just(customerDto), CustomerDto.class)
                    .retrieve().bodyToMono(CustomerDto.class);

            // Log the request body
            webClient.post()
                    .header("Content-Type", "application/json")
                    .header("X-Shopify-Access-Token",token)
                    .body(Mono.just(customerDto), CustomerDto.class)
                    .exchange()
                    .doOnNext(response -> {
                        System.out.println("Request body: " + customerDto);
                    });

            CustomerDto list = res.block();
            return list;
        } catch (WebClientResponseException e) {
            // Log the error response body
            log.error("Error creating customer: " + e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error creating customer", e);
            throw new RuntimeException("Error creating customer", e);
        }
    }


    public CustomerDto updateCustomer(Customer customer, Long id,String shop) {
        UserEntity userEntity = userRepository.findByShopName(shop).get();
        String token = userEntity.getShopifyToken();
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomer(customer);
        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/customers/"+id+".json";

        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<CustomerDto> res = webClient.put()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .bodyValue(customerDto)
                .retrieve().bodyToMono(CustomerDto.class);
        CustomerDto list = res.block();
        return list;
    }
    public ProductDto updateProduct(Product product, Long id,String shop) {
        UserEntity userEntity = userRepository.findByShopName(shop).get();
        String token = userEntity.getShopifyToken();

        ProductDto productDto = new ProductDto();
        productDto.setProduct(product);
        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/products/"+id+".json";

        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<ProductDto> res = webClient.put()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .bodyValue(productDto)
                .retrieve().bodyToMono(ProductDto.class);
        ProductDto list = res.block();
        return list;
    }
    public FulFillmentDto fulFillOrder(Long id,String shop) {
        UserEntity userEntity = userRepository.findByShopName(shop).get();
        String token = userEntity.getShopifyToken();

        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/orders/"+id+"/fulfillment_orders.json";
        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<FulFillmentDto> res = webClient.get()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .retrieve().bodyToMono(FulFillmentDto.class);
        FulFillmentDto list = res.block();
        System.out.println("fulfillment order "+list);
        fulfillmentRepository.saveAll(list.getFulfillment_orders());
        //fulfillment

        String fulfillUrl = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/fulfillments.json";

        FulFillMentBodyDto dto = new FulFillMentBodyDto();
        FulFillMentBody fulfillmentBody = new FulFillMentBody();
        LineIitemsByFulfillmentOrder lineItemsByFulfillmentOrder = new LineIitemsByFulfillmentOrder();
        FulfillmentOrderLineItems fulfillmentOrderLineItem = new FulfillmentOrderLineItems();

        //fulfillment order id
        lineItemsByFulfillmentOrder.setFulfillment_order_id(list.getFulfillment_orders().get(0).getId());
        //setFulfillment_order_line_items
        Gson gson = new Gson();
        String json = gson.toJson(list.getFulfillment_orders().get(0).getLine_items());

        System.out.println("json "+json);
        List<FulFillMentLineItems> lineItemsList = gson.fromJson(json, new TypeToken<List<FulFillMentLineItems>>(){}.getType());
//        System.out.println("lineItems "+lineItemsList);

        fulfillmentOrderLineItem.setId(lineItemsList.get(0).getId());
        fulfillmentOrderLineItem.setQuantity(lineItemsList.get(0).getQuantity());

        lineItemsByFulfillmentOrder.setFulfillment_order_line_items(Arrays.asList(fulfillmentOrderLineItem));
        System.out.println("lineItemsByFulfillmentOrder"+ lineItemsByFulfillmentOrder);

//        lineItemsByFulfillmentOrder.
        fulfillmentBody.setLine_items_by_fulfillment_order(Arrays.asList(lineItemsByFulfillmentOrder));

        dto.setFulfillment(fulfillmentBody);

//        lineItemsByFulfillmentOrder.setFulfillment_order_line_items();

        System.out.println("dto "+dto);

        WebClient web = WebClient.builder()
                .baseUrl(fulfillUrl).clientConnector(new ReactorClientHttpConnector())
                .build();


        Mono<FulfillmentResDto> result = web.post()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .bodyValue(dto)
                .retrieve().bodyToMono(FulfillmentResDto.class);
        FulfillmentResDto lis = result.block();
        fulfillmentResRepository.save(lis.getFulfillment());
        System.out.println("fulfillment "+lis);
        return list;
    }

    public ProductDto createProduct(Product product,String shop) {
        UserEntity userEntity = userRepository.findByShopName(shop).get();
        String token = userEntity.getShopifyToken();
        ProductDto productDto = new ProductDto();
        productDto.setProduct(product);

        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/products.json";

        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<ProductDto> res = webClient.post()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .bodyValue(productDto)
                .retrieve().bodyToMono(ProductDto.class);
        ProductDto list = res.block();

//        Product product1 = list.getProduct();
//        Products products = new Products();
//        BeanUtils.copyProperties(product1, products);
//        productRepository.save(products);
        return list;
    }


    public List<Products> getAllProducts() {
        List<Products> responseList  = new ArrayList<>();
        Iterable<Products>products = productRepository.findAll();
        for (Products student : products) {
            responseList.add(student);
        }
        return responseList;
    }


    public List<Customers> getAllCustomers() {
        List<Customers>responseList = new ArrayList<>();
        Iterable<Customers>customers = customerRepository.findAll();
        for (Customers customer : customers){
            responseList.add(customer);
        }
        return responseList;
    }

    public List<Orders> getAllOrders() {
        List<Orders>responseList = new ArrayList<>();
        Iterable<Orders>orders = orderRepository.findAll();
        for (Orders order : orders){
            responseList.add(order);
        }
        return responseList;
    }

    public Customers getCustomerById(Long id) {
        Customers customer = customerRepository.findById(id).get();
        return customer;
    }

    public Products getProductById(Long id) {
        Products products = productRepository.findById(id).get();
        return products;
    }

    public void requestFulfillment(Long id,String shop) {
        UserEntity userEntity = userRepository.findByShopName(shop).get();
        String token = userEntity.getShopifyToken();

        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/orders/"+id+"/fulfillment_orders.json";
        WebClient webClient = WebClient.builder()
                .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                .build();
        Mono<FulFillmentDto> res = webClient.get()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .retrieve().bodyToMono(FulFillmentDto.class);
        FulFillmentDto list = res.block();
//
        Long fulfillmentOrderId = list.getFulfillment_orders().get(0).getId();
        log.info("fulfillmentOrderId "+fulfillmentOrderId);

        ExternalReqBodyApp bodyApp = new ExternalReqBodyApp();
        ExternalFulfillReq req = new ExternalFulfillReq();
        bodyApp.setFulfillment_request(req);


        String reqUrl = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/fulfillment_orders/"+fulfillmentOrderId+"/fulfillment_request.json";

        WebClient client = WebClient.builder()
                .baseUrl(reqUrl).clientConnector(new ReactorClientHttpConnector())
                .build();

        Mono<String> response = client.post()
                .header("Content-Type", "application/json")
                .header("X-Shopify-Access-Token",token)
                .bodyValue(bodyApp)
                .retrieve().bodyToMono(String.class);

        String str = response.block();

    }

    public CreateOrderList createOrder(CreateOrderDto order, String shop) {
        String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/orders.json";
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);
        if(userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();

            CreateOrder crOrder = new CreateOrder();
            Transactions transactions = new Transactions();
            CreateOrderList orderList = new CreateOrderList();

            crOrder.setShipping_address(order.getCustomer().get(0).getAddresses().get(0));
            crOrder.setLine_items(order.getLine_items());
            Gson gson = new Gson();
            String json = gson.toJson(order.getCustomer().get(0));
            OrderCustomer customer = gson.fromJson(json, OrderCustomer.class);

            crOrder.setCustomer(customer);
            transactions.setAmount((order.getLine_items().get(0).getPrice()) * (order.getLine_items().get(0).getQuantity()));
            crOrder.setTransactions(Arrays.asList(transactions));
            orderList.setOrder(crOrder);

            WebClient webClient = WebClient.builder()
                    .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                    .build();
            Mono<Object> res = webClient.post()
                    .header("Content-Type", "application/json")
                    .header("X-Shopify-Access-Token", token)
                    .bodyValue(orderList)
                    .retrieve().bodyToMono(Object.class);
            Object list = res.block();
            log.info("list " + list);
            return orderList;
        }
        return null;
    }
//GraphQl
    public String customerDetails(String shop) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);
        if(userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();

            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            String query = "query { customers(first: 1) { edges { node { id email firstName lastName} } } }";
            WebClient webClient = WebClient.builder()
                    .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                    .build();
            Mono<String> res = webClient.post()
                    .header("Content-Type", "application/json")
                    .header("X-Shopify-Access-Token", token)
                    .body(BodyInserters.fromValue("{\"query\":\"" + query + "\"}"))
                    .retrieve().bodyToMono(String.class);
            String list = res.block();
            log.info(list);
            return list;
        }
        return null;
    }
    public String productDetails(String shop) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);
        if(userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();

            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            String query = "query { products(first: 1) { edges { node { id vendor title } } } }";
            WebClient webClient = WebClient.builder()
                    .baseUrl(url).clientConnector(new ReactorClientHttpConnector())
                    .build();
            Mono<String> res = webClient.post()
                    .header("Content-Type", "application/json")
                    .header("X-Shopify-Access-Token", token)
                    .body(BodyInserters.fromValue("{\"query\":\"" + query + "\"}"))
                    .retrieve().bodyToMono(String.class);
            String list = res.block();
            log.info(list);
            return list;
        }
        return null;
    }


    public String createCustomerGql(String shop, Customer customerInput) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);

        if (userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2022-07/graphql.json";
            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("X-Shopify-Access-Token", token)
                    .build();

            String requestBody = "{\r\n\"query\": \"mutation customerCreate($input: CustomerInput!) { customerCreate(input: $input) { userErrors { field message } customer { id email phone firstName lastName } } }\"," +
                    "\r\n \"variables\": {\r\n    \"input\": {\r\n      \"email\": \""+customerInput.getEmail()+"\",\r\n      \"firstName\": \""+customerInput.getFirstName()+"\",\r\n    \"lastName\": \""+customerInput.getLastName()+"\"\r\n    }\r\n  }\r\n}";
            Mono<String> responseMono = client.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class);
            return responseMono.block();
        }
        return null;
    }


    public String deleteCustomerGql(String shop,Long id) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);

        if (userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("X-Shopify-Access-Token", token)
                    .build();
            String reqBody = "{\n" +
                    "    \"query\": \"mutation customerDelete($id: ID!) { customerDelete(input: {id: $id}) { shop { id } userErrors { field message } deletedCustomerId } }\",\n" +
                    "    \"variables\": {\n" +
                    "        \"id\": \"gid://shopify/Customer/"+id.toString()+"\"\n" +
                    "    }\n" +
                    "}";
            Mono<String>res = client.post().bodyValue(reqBody).retrieve().bodyToMono(String.class);
            return res.block();
        }
        return null;
    }
    public String updateCustomerGql(String shop, Long id, Customer customer) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);

        if (userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("X-Shopify-Access-Token", token)
                    .build();
            String reqBody = "{\r\n\"query\": \"mutation customerUpdate($input: CustomerInput!) { customerUpdate(input: $input) { userErrors { field message } customer { id firstName lastName } } }\"," +
                    "\r\n \"variables\": {\r\n    \"input\": {\r\n      \"id\": \"gid://shopify/Customer/"+id.toString()+"\",\r\n      \"firstName\": \""+customer.getFirstName()+"\",\r\n      \"lastName\": \""+customer.getLastName()+"\",\r\n  \"email\": \""+customer.getEmail()+"\"\n" +
                    "    }\r\n  }\r\n}";
            Mono<String>res = client.post().bodyValue(reqBody).retrieve().bodyToMono(String.class);
            return res.block();
        }
        return null;
    }
    public String createProductGql(String shop, Product product) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);

        if (userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("X-Shopify-Access-Token", token)
                    .build();

            String requestBody = "{\r\n    \"query\": \"mutation { productCreate(input: {title: \"Sweet new product\", productType: \"Snowboard\", vendor: \"JadedPixel\"}) { product { id } } }\"\r\n}";

            Mono<String> responseMono = client.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class);

            return responseMono.block();
        }
        return null;
    }

    public String deleteProductGql(String shop,Long id) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);

        if (userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("X-Shopify-Access-Token", token)
                    .build();



            String reqBody = "{\n" +
                    "    \"query\": \"mutation productDelete($id: ID!) { productDelete(input: {id: $id}) { shop { id } userErrors { field message } deletedProductId } }\",\n" +
                    "    \"variables\": {\n" +
                    "        \"id\": \"gid://shopify/Product/"+id.toString()+"\"\n" +
                    "    }\n" +
                    "}";
            Mono<String> res = client.post().bodyValue(reqBody).retrieve().bodyToMono(String.class);
            return res.block();
        }
        return null;
    }

    public String updateProductGql(String shop, Product product,Long id) {
        Optional<UserEntity> userEntity = userRepository.findByShopName(shop);

        if (userEntity.isPresent()) {
            String token = userEntity.get().getShopifyToken();
            String url = "https://lucenttrainingstore.myshopify.com/admin/api/2023-04/graphql.json";
            WebClient client = WebClient.builder()
                    .baseUrl(url)
                    .defaultHeader("Content-Type", "application/json")
                    .defaultHeader("X-Shopify-Access-Token", token)
                    .build();
            String req = "{\r\n\"query\": \"mutation productUpdate($input: ProductInput!) { productUpdate(input: $input) { userErrors " +
                    "{ field message } product { id title vendor } } }\",\r\n \"variables\": {\r\n    \"input\": {\r\n      \"id\":" +
                    " \"gid://shopify/Product/"+id.toString()+"\",\r\n      \"title\": \""+product.getTitle()+"\",\r\n      \"vendor\": \""+product.getVendor()+"\"\r\n  " +
                    "  }\r\n  }\r\n}";

            Mono<String> res = client.post().bodyValue(req).retrieve().bodyToMono(String.class);
            return res.block();
        }
        return null;
    }
}
