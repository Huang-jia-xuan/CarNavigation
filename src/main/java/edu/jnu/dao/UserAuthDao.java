package edu.jnu.dao;

import edu.jnu.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthDao extends JpaRepository<UserAuth, Integer> {
    UserAuth findByUserNameAndPassword(String userName, String password);
    UserAuth findByUserName(String userName);
}
