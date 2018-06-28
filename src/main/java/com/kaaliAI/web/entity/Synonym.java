/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaaliAI.web.entity;

/**
 *
 * @author Dell
 */
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "synonyms")
public class Synonym implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "synonym")
    private String synonym;

    @JoinColumn(name = "root_word_id")
    @ManyToOne
    private RootWord rootWord;

    public Synonym() {
    }

    public Synonym(long id, String synonym, RootWord rootWord) {
        this.id = id;
        this.synonym = synonym;
        this.rootWord = rootWord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public RootWord getRootWord() {
        return rootWord;
    }

    public void setRootWord(RootWord rootWord) {
        this.rootWord = rootWord;
    }

   
}
