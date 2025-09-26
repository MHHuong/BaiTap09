package vn.host.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.host.entity.User;

public interface UserRepo extends JpaRepository<User, Long> { }