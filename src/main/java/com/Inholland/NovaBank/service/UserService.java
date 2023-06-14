package com.Inholland.NovaBank.service;

import com.Inholland.NovaBank.Jwt.JwtTokenProvider;
import com.Inholland.NovaBank.model.*;
import com.Inholland.NovaBank.model.DTO.*;
import com.Inholland.NovaBank.repositorie.AccountRepository;
import com.Inholland.NovaBank.repositorie.TransactionRepository;
import com.Inholland.NovaBank.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService extends BaseService{

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    AccountRepository accountRepository;

    public returnUserDTO getByIdDataSeeder(long id){
        return transformUser(userRepository.findById(id).orElse(null));
    }
    public returnUserDTO getById(long id){
        if(authUser(id)) {
            returnUserDTO user = transformUser(userRepository.findById(id).orElse(null));
            if(user == null){
                throw new IllegalArgumentException("User does not exist");
            }
            else{
                return user;
            }
        } else {
            throw new IllegalArgumentException("Not authorized");
        }

    }
    public boolean authUser(long id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentPrincipalName = authentication.getName();
        returnUserDTO user = getUserByUsername(currentPrincipalName);
        if(user.getRole().toString().equals("ROLE_ADMIN")){
            return true;
        } else return user.getId() == id;
    }

    public returnUserDTO transformUser(User user) {
        return new returnUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getDayLimit(), user.getTransactionLimit(), user.isHasAccount());
    }

    public returnUserDTO getUserByUsername(String username){
        return transformUser(userRepository.findUserByUsername(username));
    }
    public List<returnUserDTO> getAll(boolean isActive, Long limit, Long offset){
        if (limit == null) {
            limit = 1000L;
        }
        if (offset == null) {
            offset = 0L;
        }
        if (isActive) {
            return AllUsersWithoutAccount(limit, offset, false);
        } else {
            return getAll(limit, offset);
        }
    }
    public List<returnUserDTO> getAll(Long limit, Long offset){
        List <User> users = (List <User>) userRepository.getAll(getPageable(limit, offset));
        return transformUsers(users);
    }
    public List<returnUserDTO> AllUsersWithoutAccount(Long limit, Long offset, boolean active){
        return transformUsers(userRepository.findAllUsersWithoutAccount(getPageable(limit, offset), active));
    }
    public Pageable getPageable(Long limit, Long offset) {
        return new OffsetBasedPageRequest(offset.intValue(), limit.intValue());
    }
    public List<returnUserDTO> transformUsers (List<User> users){
        List <returnUserDTO> userDTOList = new ArrayList<>();
        for (User user: users) {
             userDTOList.add(new returnUserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail(), user.getRole(), user.getDayLimit(), user.getTransactionLimit(), user.isHasAccount()));
        }
        return userDTOList;
    }

    public returnUserDTO addUser(newUserDTO user) {
        if (checkUsername(user.getUsername()) && checkUsernameLength(user.getUsername())) {
            if(!checkIfNotNull(user)){
                throw new IllegalArgumentException("Not all fields are filled in");}
            if(user.getPassword().length() < 7){
                throw new IllegalArgumentException("Password needs to be at least 7 characters long");}
            if(!validEmail(user.getEmail())){
                throw new IllegalArgumentException("Email is not valid");}
            if(!checkUsername(user.getUsername())){
                throw new IllegalArgumentException("Username is already taken");}
            if(!checkUsernameLength(user.getUsername())){
                throw new IllegalArgumentException("Username needs to be between 4 and 20 characters long");}
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User newUser = createUser(user);
            User savedUser = userRepository.save(newUser);
            return new returnUserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole(), savedUser.getDayLimit(), savedUser.getTransactionLimit(), savedUser.isHasAccount());
        }
        throw new IllegalArgumentException("Username is not valid");
    }

    public boolean validEmail(String emailAddress) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return emailAddress.matches(regexPattern);
    }

    public boolean checkIfNotNull(newUserDTO user){
        return !Objects.equals(user.getFirstName(), "") && !Objects.equals(user.getLastName(), "") && !Objects.equals(user.getUsername(), "") && !Objects.equals(user.getPassword(), "") && !Objects.equals(user.getEmail(), "");
    }
    public boolean checkUsername(String username){
        return userRepository.findUserByUsername(username) == null;
    }
    public boolean checkUsernameLength(String username){
        return username.length() >= 4 && username.length() <= 20;
    }

    public User createUser(newUserDTO user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setRole(Role.ROLE_USER);
        newUser.setDayLimit(5000);
        newUser.setTransactionLimit(2000);
        newUser.setHasAccount(false);
        return newUser;
    }

    public returnUserDTO update(patchUserDTO user) {
        User userFromRepo = userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("Username not found"));
        if (userFromRepo != null) {
            switch (user.getKey()) {
                case "firstName" -> userFromRepo.setFirstName(user.getValue());
                case "lastName" -> userFromRepo.setLastName(user.getValue());
                case "username" -> {updateUsernameCheck(user);
                    userFromRepo.setUsername(user.getValue());}
                case "email" -> { updateEmailCheck(user);
                        userFromRepo.setEmail(user.getValue()); }
                case "role" -> userFromRepo.setRole((Role) Role.valueOf(user.getValue()));
                case "dayLimit" -> { updateDayLimitCheck(user);
                        userFromRepo.setDayLimit(Integer.parseInt(user.getValue()));}
                case "transactionLimit" -> { updateTransactionLimitCheck(user);
                        userFromRepo.setTransactionLimit(Integer.parseInt(user.getValue()));}
                case "hasAccount" -> userFromRepo.setHasAccount(Boolean.parseBoolean(user.getValue()));
                default -> {
                    return null; }
            }
            User savedUser = userRepository.save(userFromRepo);
            return new returnUserDTO(savedUser.getId(), savedUser.getFirstName(), savedUser.getLastName(), savedUser.getUsername(), savedUser.getEmail(), savedUser.getRole(), savedUser.getDayLimit(), savedUser.getTransactionLimit(), savedUser.isHasAccount());
        }
        throw new IllegalArgumentException("Username not found");
    }
    //exceptions for update
    public void updateUsernameCheck(patchUserDTO user){
        if (!checkUsernameLength(user.getValue()))
            throw new IllegalArgumentException("Username needs to be between 4 and 20 characters longs");
        else if (!checkUsername(user.getValue()))
            throw new IllegalArgumentException("Username is already taken");
    }
    public void updateEmailCheck(patchUserDTO user){
        if (!validEmail(user.getValue()))
            throw new IllegalArgumentException("Email is not valid");
    }
    public void updateDayLimitCheck(patchUserDTO user){
        if (!checkLimit(Float.parseFloat(user.getValue())))
            throw new IllegalArgumentException("Invalid limit, must be greater or equal to 0 and less than 1000000");
    }
    public void updateTransactionLimitCheck(patchUserDTO user){
        if (!checkLimit(Float.parseFloat(user.getValue())))
            throw new IllegalArgumentException("Invalid limit, must be greater or equal to 0 and less than 1000000");
    }
    //Limit check
    public boolean checkLimit(float limit){
        return limit >= 0 && limit < 1000000;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findUserByUsername(loginRequestDTO.getUsername());

        if(bCryptPasswordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRole(), user.getId());
            LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
            loginResponseDTO.setToken(token);
            return loginResponseDTO;
        } else {
            throw new IllegalArgumentException("Password is incorrect");
        }
    }

    //Transactions
    public double GetSumOfAllTransactionsFromAccountOfLast24Hours(long userId){
        double sum = 0;
        String checkingIban = accountRepository.findCheckingIbanByUserReferenceIdAndAccountType(userId, AccountType.CHECKING);
        List<String> ibans = accountRepository.findAllIbansByUserReferenceId(userId);
        sum = transactionRepository.findSumOfTransactionsFromAccount(checkingIban, ibans, LocalDateTime.now().minusDays(1));
        return sum;
    }

    public double getRemainingDailyLimit(long id) {
        long dailyLimit = userRepository.findUserDayLimitById(id);
        System.out.println(dailyLimit);
        System.out.println(GetSumOfAllTransactionsFromAccountOfLast24Hours(id));
        return (dailyLimit - GetSumOfAllTransactionsFromAccountOfLast24Hours(id));
    }
}
