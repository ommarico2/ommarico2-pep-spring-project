package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account persistAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account registerAccount(Account account) {

        Account regAccount = accountRepository.save(account);

        return regAccount;

    }

    public Account loginAccount(Account account) {
        Optional<Account> loginAccount = accountRepository.findAccountByUsernameAndPassword(account.getUsername(),
                account.getPassword());
        if (loginAccount.isPresent()) {
            return loginAccount.get();
        } else {
            return null;
        }

    }

    public boolean isThereMatchingAccountUsername(String username) {
        Optional<Account> optAcc = accountRepository.findAccountByUsername(username);
        if (optAcc.isPresent()) {
            return true;
        } else {
            return false;
        }
    }

}