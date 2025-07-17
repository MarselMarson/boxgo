package com.szd.boxgo.service.user;

import com.szd.boxgo.dto.user.UserDto;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final UserRepoService repoService;

    public UserDto getUserById(Long id) {
        User user = repoService.getUserById(id);
        return userMapper.toDto(user);
    }

    public UserDetailsService userDetailsService() {
        return repoService::getUserByEmail;
    }
}
