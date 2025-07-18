package com.egabi.university.service.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * UserService interface that extends UserDetailsService.
 * This service is responsible for loading user-specific data.
 * It can be used to retrieve user details for authentication and authorization purposes.
 */
public interface UserService extends UserDetailsService {
//TODO add CRUD methods for User entity
}
