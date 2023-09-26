package com.appsdeveloperblog.shopify.clients.controller;

import com.appsdeveloperblog.shopify.clients.entity.AuthRequest;
import com.appsdeveloperblog.shopify.clients.entity.UserEntity;
import com.appsdeveloperblog.shopify.clients.repository.UserRepository;
import com.appsdeveloperblog.shopify.clients.security.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.StandardBeanInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller("/")
@Slf4j
public class MyController {
    @Value("${shopify.app.secret}")
    private String appSecret;
    @Value("${shopify.app.key}")
    private String key;
    //    @Value("${shopify.app.shop}")
//    private String shopName;
    @Value("${shopify.app.scope}")
    private String scopeName;
    @Value("${shopify.app.redirect_uri}")
    private String redirect_uri;
    @Value("${shopify.app.state}")
    private String stateName;
    @Value("${shopify.app.access_mode}")
    private String access;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    @GetMapping("/")

    public RedirectView handleRequest(@RequestParam(name = "shopname") String shopname) {
        System.out.println("shopName "+shopname);
//        shopName = request.getHeader("Authorization");
//                                @RequestParam String hmac,
//                                @RequestParam String shop,
//                                @RequestParam String timestamp,
//                                @RequestParam String host) throws NoSuchAlgorithmException, InvalidKeyException {
//        System.out.println("Query string: " + request.getQueryString());
//
//        // Remove the hmac parameter from the query string
//        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//        queryParams.add("shop", shop);
//        queryParams.add("timestamp", timestamp);
//        queryParams.add("host", host);
//
//        //queryParams.remove("hmac");
//        Map<String, List<String>> sortedMap = new TreeMap<>(queryParams);

          //sort the map with Respect to keys
//        UriComponentsBuilder builder = UriComponentsBuilder.newInstance();
//        for (Map.Entry<String, List<String>> entry : sortedMap.entrySet()) {
//            String key = entry.getKey();
//            List<String> values = entry.getValue();
//            for (String value : values) {
//                builder.queryParam(key, value);
//            }
//        }
//
//        String newQueryString = builder.build(false).getQuery();
//
//        System.out.println("new Query " + newQueryString);
//
//        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
//        SecretKeySpec secretKey = new SecretKeySpec(appSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//        sha256Hmac.init(secretKey);
//        byte[] hashBytes = sha256Hmac.doFinal(newQueryString.getBytes(StandardCharsets.UTF_8));
//        String hash = Hex.encodeHexString(hashBytes);
//
//        System.out.println("expectedHmac " + hash);
//        //https://lucenttraining.myshopify.com/admin/oauth/authorize?client_id=b5f68bd776d22ca969eecff7ca9a4abb&scope=write_products,read_products,write_orders,read_orders,write_customers,read_customers&redirect_uri=http://localhost:3000/api/auth&state=test123&access_mode=per-user
//
//        System.out.println(shopName + " " + redirect_uri + " " + stateName);
//        if(shopName.equals("lucenttraining")) {
//        shopName = shopname;
//        System.out.println(shopName);
        String redirectUrl = "https://" + shopname + "/admin/oauth/authorize?" +
                "client_id=" + key + "&scope=" + scopeName + "&redirect_uri=" + redirect_uri
                + "&state=" + stateName + "&grant_options[]=" + access;

        return new RedirectView(redirectUrl);
        //return new RedirectView("https://lucenttraining.myshopify.com/admin/oauth/authorize?client_id=b5f68bd776d22ca969eecff7ca9a4abb&scope=write_products,read_products,write_orders,read_orders,write_customers,read_customers&redirect_uri=http://localhost:3000/api/auth&state=test123&grant_options[]=per-user");
//        }
//        return new RedirectView("https://lucenttraining.myshopify.com/admin/oauth/authorize?client_id=b5f68bd776d22ca969eecff7ca9a4abb&scope=write_products,read_products,write_orders,read_orders,write_customers,read_customers&redirect_uri=http://localhost:3000/api/auth&state=test123&grant_options[]=per-user");
    }

    @GetMapping("/api/auth")
    public RedirectView redirect(@RequestParam Map<String, String> params,
                                 @RequestHeader Map<String, String> headers) {
//        log.info("Params = {}, headers = {}",params,headers);

        if (params.get("state").equals("test1234")) {
            RestTemplate rest = new RestTemplate();
            Map<String, String> map = new HashMap<>();
            map.put("client_id", key);
            map.put("client_secret", appSecret);
            map.put("code", params.get("code"));
            String shopName = params.get("shop"); //
            Object obj = rest.postForObject("https://" + shopName+"/admin/oauth/access_token", map, Object.class);
//            log.info("Object = {}",obj);

            String[] keyValuePairs = obj.toString().substring(1, obj.toString().length() - 1).split(", ");
            log.info(keyValuePairs.toString());
            LinkedHashMap<String, String> m = new LinkedHashMap<>();
            for (String pair : keyValuePairs) {
                String[] entry = pair.split("=");
                m.put(entry[0], entry[1]);
            }
            String access_token = m.get("access_token");

            UserEntity user = new UserEntity();
            user.setShopifyToken(access_token);
            user.setShopName(shopName);
            if (!userRepository.findByShopName(shopName).isPresent()) {
                userRepository.save(user);
            } else {
                userRepository.deleteAll();
                userRepository.save(user);
            }

            return new RedirectView("http://localhost:4000/result");
        } else
            return null;
    }
//    public Mono<Object>redirect(@RequestParam Map<String,String>params){
//        if (params.get("state").equals("test123") ) {
//            WebClient client = WebClient.builder().build();
//            Map<String,String>map = new HashMap<>();
//            map.put("client_id", key);
//            map.put("client_secret", appSecret);
//            map.put("code", params.get("code"));
//            return client.post()
//                    .uri("https://" + shop + ".myshopify.com/admin/oauth/access_token")
//                    .body(BodyInserters.fromValue(map))
//                    .retrieve()
//                    .bodyToMono(Object.class);
//        }
//        else {
//            return Mono.just("Security failed");
//        }
//    }


    //request from frontend side
//    @GetMapping("/success")

}










