package com.pedroghiotti.learning;

import com.pedroghiotti.learning.data.dao.ServiceDao;

public class App {
    public static void main(String[] args) {
        ServiceDao serviceDao = new ServiceDao();

        System.out.println("\n===GET=ALL===");
        serviceDao.getAll().forEach(System.out::println);


    }
}
