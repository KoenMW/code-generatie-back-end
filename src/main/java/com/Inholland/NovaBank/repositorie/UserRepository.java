package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.hasAccount = ?1 ORDER BY u.id")
    List<User> findAllUsersWithoutAccount(Pageable pageable, boolean active);

    @Query("SELECT u FROM User u ORDER BY u.id")
    List<User> getAll(Pageable pageable);
    User findUserByUsername(String username);

    @Query("SELECT u.dayLimit FROM User u WHERE u.id = ?1")
    int findUserDayLimitById(Long id);

    @Query("SELECT u.transactionLimit FROM User u WHERE u.id = ?1")
    int findUserTransactionLimitById(Long id);

}
