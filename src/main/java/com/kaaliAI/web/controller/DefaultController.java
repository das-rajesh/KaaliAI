/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaaliAI.web.controller;

import com.kaaliAI.web.entity.Color;
import com.kaaliAI.web.entity.RootWord;
import com.kaaliAI.web.entity.Synonym;
import com.kaaliAI.web.entity.repository.ColorRepository;
import com.kaaliAI.web.entity.repository.RootWordRepository;
import com.kaaliAI.web.util.HttpClient;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Dell
 */
@Controller
@RequestMapping(value = "/home")
public class DefaultController {

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private RootWordRepository repository;

    @RequestMapping(method = RequestMethod.GET)
    public String index(Model model) {

        for (RootWord s : repository.findAll()) {
            System.out.println(s.getKeyWord());
        }

        // model.addAttribute("colors", colorRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/about", method = RequestMethod.GET)
    @ResponseBody
    public String about() {
        return "<h1> About us </h1>";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@ModelAttribute("Color") Color color) {
        colorRepository.save(color);
        return "redirect:/";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String edit(@PathVariable("id") long id, Model model) {

        //  model.addAttribute("color", colorRepository.findById(id).get());
        return "index";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") long id) {
        colorRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping(value = "/scrap", method = RequestMethod.GET)
    public void scrap() {

        try {
            String url = "http://www.thesaurus.com/browse/good?s=ts";
            String param = "Keywords=java";
            String content = HttpClient.get(url);
            String synsql = "insert into synonyms(synonym,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word=?;";

            String antsql = "insert into antonyms(antonym,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word=?;";
            String relatedWordSql = "insert into related_words(related_word,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word=?";
            String regex = "<script>(.*?)\"synonyms\":\\[(.*?)],\"antonyms\":\\[(.*?)]";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(content);
            Synonym synonym = new Synonym();

            while (matcher.find()) {
                String synonyms = matcher.group(2);
                String antonyms = matcher.group(3);
                // System.out.println("synonyms=>"+synonyms);
                //  System.out.println("antonyms=>"+antonyms);
                // String webContent=HttpClient.get(synonyms);
                pattern = Pattern.compile("\"term\":\"(.*?)\"");
                Matcher matcher1 = pattern.matcher(antonyms);
                //System.out.println(matcher1.group(0));
                while (matcher1.find()) {
                    String term = matcher1.group(1);
                    Synonym synonym1 = new Synonym();
                    synonym.setSynonym(term);
                    
                    

                  //  stmt.setString(1, term);
                    //stmt.setInt(2, 9729);
                    //  stmt.executeUpdate();
                    System.out.println("term=>" + term);
                    // System.out.println("Email:"+matcher1.group());
                }
                //   System.out.println("\n-----------------------------------------------------------------------------------------------------------------------\n");

            }
            String regdata = "\"_id\":(.*?),\"entry\":\"(.*?)\",\"targetTerm\":\"(.*?)\",(.*?)\"synonyms\":\\[(.*?)]";
            Pattern pattern2 = Pattern.compile(regdata);
            Matcher matcher2 = pattern2.matcher(content);

            while (matcher2.find()) {
                String data = matcher2.group(5);
                String entry1 = matcher2.group(2);
                String targetTerm = matcher2.group(3);

                pattern2 = Pattern.compile("(.*?),\"isInformal\":\"(.*?)\",\"isVulgar\":(.*?),\"term\":\"(.*?)\",\"targetTerm\":\"(.*?)\",\"targetSlug\":\"(.*?)\"");
                Matcher matcher1 = pattern2.matcher(data);
                //System.out.println(matcher1.group(0));
                while (matcher1.find()) {
                    String term = matcher1.group(4);
                    // String targetTerm = matcher1.group(5);
                    // String entry = matcher1.group(5);
                    System.out.println("entry1=>" + entry1);
                    System.out.println("term=>" + term);
                    System.out.println("targetTerm=>" + targetTerm);
                    // System.out.println("Email:"+matcher1.group());
                }
            }
            //  System.out.println(content);

        } catch (IOException e) {
            //  System.out.println(e.getMessage());
        }
    }

}
