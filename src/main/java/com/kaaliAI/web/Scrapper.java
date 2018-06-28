/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kaaliAI.web;

import com.kaaliAI.web.entity.Synonym;
import com.kaaliAI.web.entity.repository.SynonymRepository;
import com.kaaliAI.web.util.HttpClient;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Dell
 */
public class Scrapper {

    @Autowired
    static SynonymRepository sr;

    public static void main(String[] args) throws SQLException {
        try {
            String url = "http://www.thesaurus.com/browse/pen?s=ts";
            String param = "Keywords=java";
            String content = HttpClient.get(url);
            // String regex="<a class=\"job-item\"(.*?)href=\"(.*?)\"\\s>\\n(.*?)</a>\\n";
            // String regex="\"synonyms\":\\[(.*?)]";
            String regex = "<script>(.*?)\"synonyms\":\\[(.*?)],\"antonyms\":\\[(.*?)]";

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/kaali", "root", "");
            String sql = "insert into synonyms(synonym,root_word_id)values(?,?)";  //insert into synonyms(synonym,root_word_id)values('ok','1') // f
            String ant = "insert into antonyms(antonym,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word='pen'";

            String synsql = "insert into synonyms(synonym,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word=?;";

            String antsql = "insert into antonyms(antonym,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word=?;";
            String relatedWordSql = "insert into related_words(related_word,root_word_id)\n"
                    + "select ?, r.id\n"
                    + " from root_word r where key_word=?";
            
            String rootOneByOneSql="select * from root_word";

            PreparedStatement rootOneByOneSqlstmt = conn.prepareStatement(rootOneByOneSql);
            PreparedStatement stmt = conn.prepareStatement(synsql);
            PreparedStatement antstmt = conn.prepareStatement(antsql);
            PreparedStatement relatedWordSqlstmt = conn.prepareStatement(relatedWordSql);
            /*
            insert into synonyms(synonym,root_word_id)
            select 'papa', r.id
             from root_word r where key_word='pen';                       */
            /**
             * regex for synonyms and antonyms
             *
             * <script>(.*?)"synonyms":\[(.*?)],"antonyms":\[(.*?)]
             */
            /**
             *
             * regex for more common words
             * "definition":"yes","synonyms":\[(.*?)](.*?)"targetTerm":"(.*?)"(.*?)"synonyms":\[(.*?)]
             *
             * working
             * {"_id":(.*?)"targetTerm":"(.*?)",(.*?)"synonyms":\[(.*?)](.*?)
             * {"_id":(.*?)"targetTerm":"(.*?)",(.*?)"synonyms":\[(.*?)]
             * "_id":(.*?),"entry":"(.*?)","targetTerm":"(.*?)",(.*?)"synonyms":\[(.*?)]
             */
            /**
             *
             * regex for differenticiating the term vulgarity clear
             * {(.*?),"isInformal":"(.*?)","isVulgar":(.*?),"term":"(.*?)","targetTerm":"(.*?)","targetSlug":"(.*?)"}
             */
            //<script>(.*?)"synonyms":\[(.*?)],"antonyms":\[(.*?)]
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
                Matcher matcher1 = pattern.matcher(synonyms);
                //System.out.println(matcher1.group(0));
                while (matcher1.find()) {
                    String term = matcher1.group(1);
                    stmt.setString(1, term);
                    stmt.setString(2, "pen");
                    //stmt.setInt(2, 9729);
                    stmt.executeUpdate();
                    System.out.println("term=>" + term);
                    // System.out.println("Email:"+matcher1.group());
                }
                Matcher matcher2 = pattern.matcher(antonyms);
                //System.out.println(matcher1.group(0));

                while (matcher2.find()) {
                    String term = matcher2.group(1);
                    antstmt.setString(1, term);
                    antstmt.setString(2, "pen");
                    antstmt.executeUpdate();
                    System.out.println("term=>" + term);
                }

            }
            String regdata = "\"_id\":(.*?),\"entry\":\"(.*?)\",\"targetTerm\":\"(.*?)\",(.*?)\"synonyms\":\\[(.*?)]";
            Pattern pattern2 = Pattern.compile(regdata);
            Matcher matcher2 = pattern2.matcher(content);
                int i=1;
                List<String> dataCollect = new ArrayList<>();

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
                    dataCollect.add(term);
                     relatedWordSqlstmt.setString(1, term);
                      relatedWordSqlstmt.setString(2, "pen");
                     relatedWordSqlstmt.executeUpdate();
                    System.out.println("related word===entry1=>" + entry1);
                    System.out.println("term=>" + term);
                    System.out.println("targetTerm=>" + targetTerm);
                    // System.out.println("Email:"+matcher1.group()); 
                    i++;
                    System.out.println(i);
                }
                
            }
               // relatedWordSqlstmt.setObject(1, dataCollect);
               // relatedWordSqlstmt.setString(2, "pen");
               //System.out.println(relatedWordSqlstmt.executeUpdate()); ;
            System.out.println("finished");
            System.out.println("*****************************************************");
            ResultSet rootwords=rootOneByOneSqlstmt.executeQuery();
                  while(rootwords.next()){
                      String keyword=rootwords.getString("key_word");
                      System.out.println(rootwords.getString("key_word"));
                  }
            //  System.out.println(content);
        } catch (IOException e) {
            //  System.out.println(e.getMessage());
        }
    }
}
