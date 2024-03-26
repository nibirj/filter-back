package org.wisercat.test.assignment.database.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.wisercat.test.assignment.data.Filter;

public interface FilterRepository extends JpaRepository<Filter, Long> {

}
