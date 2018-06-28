/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaaliAI.web.entity.repository;

import com.kaaliAI.web.entity.RelatedWord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Dell
 */
@Repository
public interface RelatedRepository  extends 
        JpaRepository<RelatedWord, Long>{
    
}
