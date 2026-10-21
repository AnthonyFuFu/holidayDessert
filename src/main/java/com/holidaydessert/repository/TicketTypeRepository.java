package com.holidaydessert.repository;

import com.holidaydessert.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Integer> {

    @Query("SELECT tt FROM TicketType tt JOIN FETCH tt.ticket t WHERE t.ticketName = :ticketName ORDER BY tt.typeSort, tt.typeId")
    List<TicketType> findByTicketName(@Param("ticketName") String ticketName);

    @Modifying
    @Transactional
    @Query(value =
        "UPDATE ticket_type tt " +
        "INNER JOIN ticket t ON tt.TICKET_ID = t.TICKET_ID " +
        "SET tt.TYPE_REMAINING = tt.TYPE_QUANTITY " +
        "WHERE t.TICKET_NAME = :ticketName",
        nativeQuery = true)   // ← 加這個
    void resetRemainingByTicketName(@Param("ticketName") String ticketName);

    @Query("SELECT tt FROM TicketType tt JOIN FETCH tt.ticket t WHERE t.ticketId = :ticketId ORDER BY tt.typeSort, tt.typeId")
    List<TicketType> findByTicketId(@Param("ticketId") Integer ticketId);

    @Query("SELECT COALESCE(MAX(tt.typeSort), 0) FROM TicketType tt WHERE tt.ticket.ticketId = :ticketId")
    int findMaxSort(@Param("ticketId") Integer ticketId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE holiday_dessert.ticket_type SET TYPE_REMAINING = TYPE_QUANTITY WHERE TICKET_ID = :ticketId",
           nativeQuery = true)
    int resetRemainingByTicketId(@Param("ticketId") Integer ticketId);

    @Modifying
    @Transactional
    @Query("UPDATE TicketType t SET t.typeRemaining = t.typeRemaining - :quantity " +
           "WHERE t.typeId = :typeId AND t.typeRemaining >= :quantity")
    int decrementRemaining(@Param("typeId") Integer typeId, @Param("quantity") Integer quantity);
}