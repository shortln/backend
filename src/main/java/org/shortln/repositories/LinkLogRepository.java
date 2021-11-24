package org.shortln.repositories;

import org.shortln.models.LinkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkLogRepository extends JpaRepository<LinkLog, Long> {
}
