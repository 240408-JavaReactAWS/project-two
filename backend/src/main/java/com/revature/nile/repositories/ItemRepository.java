package com.revature.nile.repositories;

import com.revature.nile.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<List<Item>> findAllByUserUserId(int userId);


    @Query(value = "select getStockByItemId(?1, ?2)", nativeQuery = true)
    Integer queryGetStockByItemId(int itemId, int quantity, int stock);

    @Procedure(procedureName = "getStockByItemId")
    Integer procGetStockByItemId(int itemId, int quantity, int stock);
}
