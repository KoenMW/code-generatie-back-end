package com.Inholland.NovaBank.repositorie;

import com.Inholland.NovaBank.model.Account;
import com.Inholland.NovaBank.model.DTO.returnUserDTO;
import com.Inholland.NovaBank.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {


    @Query("SELECT u FROM User u WHERE u.hasAccount = ?1 ORDER BY u.id")
    List<Account> findAllUsersWithoutAccount(Pageable pageable, boolean active);


    User findUserByUsername(String username);

    @Query("SELECT u.dayLimit FROM User u WHERE u.id = ?1")
    int findUserDayLimitById(Long id);

    @Query("SELECT u.transactionLimit FROM User u WHERE u.id = ?1")
    int findUserTransactionLimitById(Long id);

}
