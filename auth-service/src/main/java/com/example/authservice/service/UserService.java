package com.example.authservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.primaryschoolmanagement.entity.AppUser;
import com.example.primaryschoolmanagement.entity.Role;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public interface UserService extends IService<AppUser> {

    Role selectRolesByUserId(Integer userId);

    AppUser findByUserName(String name);

    int regUser(AppUser appUser);

    Optional<AppUser> findByIdentifier(String identifier);

    void updatePassword(AppUser user, String rawPassword);

}
