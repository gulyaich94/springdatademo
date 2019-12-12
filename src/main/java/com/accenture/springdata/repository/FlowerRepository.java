package com.accenture.springdata.repository;

import com.accenture.springdata.entity.Flower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlowerRepository extends JpaRepository<Flower, Long>, CustomFlowerDao<Flower>, QuerydslPredicateExecutor<Flower> {

    // поиск цветов по цвету
    List<Flower> findByColor(String color);

    // поиск цветов по наименованию и цвету
    List<Flower> findByNameAndColor(String name, String color);

    // поиск цветов по цвету с сортировкой по имени по возрастанию
    List<Flower> findByColorOrderByNameAsc(String color);

    // поиск одного цветка по наименованию
    Optional<Flower> findFirstByName(String name);

    // получение трех цветков из базы по имени, упорядоченные по цене по возрастанию
    List<Flower> findTop3ByNameOrderByPriceAsc(String name);

    // получение уникальных записей из БД по наименованию цветка
    List<Flower> findDistinctByName(String name);

    @Query(nativeQuery = true, value = "SELECT * FROM FLOWER ORDER BY RAND() LIMIT ?#{[0]}")
    List<Flower> findCountFlowerRandom(int count);

    @Query(value="SELECT f FROM Flower f")
    List<Flower> findAllFlowers(Sort sort);

    @Transactional
    int deleteByName(String name);

    @Transactional
    @Modifying
    @Query(value = "delete from Flower f where f.name = :name", nativeQuery = true)
    int deleteFlowersByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from Flower where price = (select max(price) from Flower)")
    int deleteMaxPriceFlowers();

    Page<Flower> findAllByPrice(BigDecimal price, Pageable pageable);

    @Query("SELECT f FROM Flower f WHERE f.name = ?1 and f.color = ?2")
    List<Flower> findFlowersByNameAndColor(String name, String color);

    @Query(
        value = "SELECT * FROM Flower ORDER BY id",
        countQuery = "SELECT count(*) FROM Flower",
        nativeQuery = true)
    Page<Flower> findAllFlowersWithPagination(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Flower f SET f.count = f.count + 1 WHERE f.id = 4151")
    int updateFlowerCountNonNative();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Flower f SET f.count = f.count + 2 WHERE f.id = 4151", nativeQuery = true)
    int updateFlowerCountNative();

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT f FROM Flower f WHERE f.id = :flowerId")
    Flower findFlowerToOrder(@Param("flowerId") Long flowerId);
}
