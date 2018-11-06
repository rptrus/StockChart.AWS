package com.rohan.stockapp.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rohan.stockapp.entity.Holding;


@Repository
@Transactional
public interface HoldingRepository extends JpaRepository<Holding,Long>{

	Holding findByCode(String code);
}
