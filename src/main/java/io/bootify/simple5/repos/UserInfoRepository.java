package io.bootify.simple5.repos;

import io.bootify.simple5.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByCredentials(String credentials);

    boolean existsByEmailIgnoreCase(String email);

}
