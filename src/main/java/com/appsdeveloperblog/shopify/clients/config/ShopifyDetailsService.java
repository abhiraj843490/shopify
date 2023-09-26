package com.appsdeveloperblog.shopify.clients.config;
import com.appsdeveloperblog.shopify.clients.entity.UserEntity;
import com.appsdeveloperblog.shopify.clients.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Component
@Service
public class ShopifyDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String shopName) throws UsernameNotFoundException {
        Optional<UserEntity>userEntity = userRepository.findByShopName(shopName);
        return userEntity.map(ShopifyUserDetails::new)
                .orElseThrow(()->new UsernameNotFoundException("shop not found "+shopName));
    }
}
