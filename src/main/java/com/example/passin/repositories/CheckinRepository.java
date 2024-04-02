package com.example.passin.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.passin.domain.checkin.Checkin;

public interface CheckinRepository extends JpaRepository<Checkin, Integer>{
  
}
