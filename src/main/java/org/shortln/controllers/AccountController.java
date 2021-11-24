package org.shortln.controllers;

import lombok.Data;
import org.shortln.exceptions.BusinessException;
import org.shortln.models.Account;
import org.shortln.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Data
class AccountIn {
    private String username;
    private String nickname;
    private String password;
}

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    private AccountRepository ar;

    private String encryptPWD(String pwd) {
        return DigestUtils.md5DigestAsHex(pwd.getBytes());
    }

    @PostMapping("")
    public Account createAccount(
            @RequestBody AccountIn accountIn
    ) {
        var a = new Account();
        a.setUsername(accountIn.getUsername());
        a.setNickname(accountIn.getNickname());
        a.setPasswordHash(
                encryptPWD(accountIn.getPassword())
        );
        try {
            return ar.save(a);
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(HttpStatus.CREATED, "用户名已被注册。");
        }
    }

    @GetMapping("")
    public Pagination<Account> getAccounts(Pagination<Account>.Query query) {
        var newAc = Account.builder()
                .username(query.getStr())
                .nickname(query.getStr())
                .build();
        var matcher = ExampleMatcher.matching()
                .withMatcher("username", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("nickname", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIgnorePaths("password");
        return query.getPagination(ar, Example.of(newAc, matcher), page -> {
            page.forEach(item -> item.setPasswordHash(null));
            return page;
        });
    }

    @PostMapping("/{username}/login")
    public Account loginByUsername(
            @PathVariable String username,
            @RequestBody AccountIn accountIn
    ) {
        var ac = ar.getByUsername(username);
        if (ac == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "用户不存在。");
        }
        if (!Objects.equals(ac.getPasswordHash(), encryptPWD(accountIn.getPassword()))) {
            throw new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY, "密码错误。");
        }
        return ac;
    }
}
