/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaaliAI.web.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Dell
 */
@Entity
@Table(name = "root_word")
public class RootWord implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "key_word")
    private String KeyWord;

    @OneToMany(mappedBy = "rootWord")
    private List<Synonym> synonyms;
    
    @OneToMany(mappedBy = "rootWord")
    private List<Antonym> antonyms;
    
    @OneToMany(mappedBy = "rootWord")
    private List<RelatedWord> relatedWords;
    

    public RootWord() {
    }

    public RootWord(long id, String KeyWord) {
        this.id = id;
        this.KeyWord = KeyWord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String KeyWord) {
        this.KeyWord = KeyWord;
    }
}
