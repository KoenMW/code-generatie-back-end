package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUsername(String username);


    @Query("SELECT u.dayLimit FROM User u WHERE u.id = ?1")
    int findUserDayLimitById(Long id);

    @Query("SELECT u.transactionLimit FROM User u WHERE u.id = ?1")
    int findUserTransactionLimitById(Long id);

}
