package com.example.finmanagerbackend.account;

import com.example.finmanagerbackend.application_user.ApplicationUser;
import com.example.finmanagerbackend.application_user.ApplicationUserRepository;
import com.example.finmanagerbackend.income_expense.IncomeExpenseDTO;
import com.example.finmanagerbackend.income_expense.IncomeExpenseService;
import com.example.finmanagerbackend.limit.LimitService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {
    private final IncomeExpenseService incomeExpenseService;
    private final LimitService limitService;
    private final ApplicationUserRepository applicationUserRepository;

    public AccountService(IncomeExpenseService incomeExpenseService,
                          LimitService limitService,
                          ApplicationUserRepository applicationUserRepository) {

        this.incomeExpenseService = incomeExpenseService;
        this.limitService = limitService;
        this.applicationUserRepository = applicationUserRepository;
    }

    // musi być account, na którym operujemy
    // account musi być wzięty z appUser chyba
    public void addNewOperation(String userId, IncomeExpenseDTO incomeExpenseDTO) {
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByEmail(userId);
        Account account;

        if (userOptional.isPresent()) {
            account = userOptional.get().getAccount();;
        }
    }
}
