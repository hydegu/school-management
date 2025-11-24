package com.example.authservice.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.example.authservice.entity.AppUser;
import com.example.authservice.entity.Role;


import java.util.List;
import java.util.Optional;

public interface UserService extends IService<AppUser> {

    List<Role> selectRolesByUserId(Long userId);

    AppUser findByUserName(String name);

    int regUser(AppUser appUser);

    Optional<AppUser> findByIdentifier(String identifier);

    void updatePassword(AppUser user, String rawPassword);

}
