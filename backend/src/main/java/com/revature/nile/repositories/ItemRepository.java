package com.revature.nile.repositories;

import com.revature.nile.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<List<Item>> findAllByUserUserId(int userId);
}
