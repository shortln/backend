package org.shortln.repositories;

import org.shortln.models.Link;
import org.shortln.models.LinksGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
}
