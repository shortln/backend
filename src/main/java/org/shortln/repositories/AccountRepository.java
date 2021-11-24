package org.shortln.repositories;

import org.shortln.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    public Account getByUsername(String username);
}
