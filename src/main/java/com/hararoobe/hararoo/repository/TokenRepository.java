package com.hararoobe.hararoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hararoobe.hararoo.entity.Token;

public interface TokenRepository extends JpaRepository<Token, Long>{

}
