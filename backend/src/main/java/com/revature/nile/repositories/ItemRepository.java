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


    // We can call the function in the database to update the stock of an item, and return the new stock,
    // with either a query or a procedure.
    @Query(value = "select update_stock(?1, ?2)", nativeQuery = true)
    Integer queryGetStockByItemId(int itemId, int quantity);

    @Procedure(procedureName = "update_stock")
    Integer procGetStockByItemId(int itemId, int quantity);


//    The stored procedure below is to be stored in the database (I think).

//    CREATE OR REPLACE PROCEDURE public.update_stock(id integer, quantity integer, out remainingstock integer)
//    returns integer
//    LANGUAGE plpgsql
//    AS $procedure$
//    BEGIN
//    update items SET stock = stock - quantity WHERE id = itemId;
//    SELECT stock FROM items WHERE id = itemId into remainingstock;
//    RETURN remainingstock;
//
//    commit;
//    END;
//    $procedure$
}
