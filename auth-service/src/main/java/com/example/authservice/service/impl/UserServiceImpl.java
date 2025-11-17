package com.example.authservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.primaryschoolmanagement.common.exception.ApiException;
import com.example.primaryschoolmanagement.dao.UserDao;
import com.example.primaryschoolmanagement.entity.AppUser;
import com.example.primaryschoolmanagement.entity.Role;
import com.example.authservice.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserDao, AppUser> implements UserService {

    @Resource
    private UserDao userRepo;

    @Resource
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Resource
    private CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = "users:profile", key = "#name", unless = "#result == null")
    public AppUser findByUserName(String name) {
        return userRepo.selectOne(new LambdaQueryWrapper<AppUser>()
                .eq(AppUser::getUserName, name));
    }

    @Override
    public int regUser(AppUser appUser) {
        return userRepo.insert(appUser);
    }

    @Override
    public Optional<AppUser> findByIdentifier(String identifier) {
        if (!StringUtils.hasText(identifier)) {
            log.warn("未找到用户");
            return Optional.empty();
        }
        String value = identifier.trim();
        LambdaQueryWrapper<AppUser> query = new LambdaQueryWrapper<>();
        query.eq(AppUser::getUserName, value)
                .or()
                .eq(AppUser::getEmail, value);
        AppUser user = userRepo.selectOne(query);
        return Optional.ofNullable(user);
    }

    @Override
    @CacheEvict(cacheNames = "users:profile", key = "#user.userName()")
    public void updatePassword(AppUser user, String rawPassword) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("用户信息不完整");
        }
        String encoded = passwordEncoder.encode(rawPassword);
        user.setPassword(encoded);
        LocalDateTime now = LocalDateTime.now();
        user.setUpdatedAt(now);
        int affected = userRepo.updateById(user);
        if (affected <= 0) {
            log.error("加载密码失败, userId={}", user.getId());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "加载密码失败");
        }
        log.info("密码更新成功, userId={}", user.getId());
    }

    @Override
    @CacheEvict(cacheNames = "users:roles", key = "#user.userName()")
    public Role selectRolesByUserId(Integer userId) {
        return userRepo.selectRolesByUserId(userId);
    }

    // 辅助方法：清除用户缓存
    private void evictUserCache(String userName) {
        if (userName == null) {
            return;
        }
        try {
            Cache cache = cacheManager.getCache("users:profile");
            if (cache != null) {
                cache.evict(userName);
                log.debug("已清除用户缓存：userName={}", userName);
            }
        } catch (Exception e) {
            log.warn("清除用户缓存失败：userName={}, error={}", userName, e.getMessage());
        }
    }

}
