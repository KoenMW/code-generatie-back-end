package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    int findUserDailyLimitById(Long id);

    int findUserTransactionLimitById(Long id);

}
