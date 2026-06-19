package com.example.DAR.Repository;

import com.example.DAR.Enums.HomeItemCategory;
import com.example.DAR.Enums.HomeItemStatus;
import com.example.DAR.Model.HomeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HomeItemRepository extends JpaRepository<HomeItem, Integer> {

    HomeItem findHomeItemById(Integer id);

    List<HomeItem> findHomeItemsByHomeId(Integer homeId);

    @Query("select h from HomeItem h where h.home.id = ?1 and h.category = ?2")
    List<HomeItem> findByCategory(Integer homeId, HomeItemCategory category);

    @Query("select h from HomeItem h where h.home.id = ?1 and h.status = ?2")
    List<HomeItem> findByStatus(Integer homeId, HomeItemStatus status);

    @Query("select h from HomeItem h where h.home.id = ?1 and h.nextServiceDate between ?2 and ?3")
    List<HomeItem> findUpcomingService(Integer homeId, LocalDate startDate, LocalDate endDate);

    @Query("select h from HomeItem h where h.home.id = ?1 and (lower(h.name) like lower(concat('%', ?2, '%')) or lower(h.brand) like lower(concat('%', ?2, '%')))")
    List<HomeItem> searchByNameOrBrand(Integer homeId, String keyword);
}
