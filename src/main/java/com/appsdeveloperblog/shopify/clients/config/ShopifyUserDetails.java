package com.appsdeveloperblog.shopify.clients.config;

import com.appsdeveloperblog.shopify.clients.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;

public class ShopifyUserDetails implements UserDetails {
    private String shopName;
    private String shopId;
    private List<GrantedAuthority>authorities;
    public ShopifyUserDetails(UserEntity user)
    {
        shopName = user.getShopName();
        shopId = user.getShopId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return shopId;
    }

    @Override
    public String getUsername() {
        return shopName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
}
