package edu.jnu.dao;

import edu.jnu.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserAuth, Long> {
    Optional<UserAuth> findByUserName(String userName);
}
