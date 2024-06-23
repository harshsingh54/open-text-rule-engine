package com.example.debricked.rule_engine.services;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DebrickedServiceTest {

    @Autowired
    private DebrickedService service;

    @Test
    public void test(){
        List<Integer> idList = new ArrayList<>();
        idList.add(7);
        idList.add(9);

        service.processFiles(idList);
    }

}
