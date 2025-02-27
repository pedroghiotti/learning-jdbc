package com.pedroghiotti.learning;

import com.pedroghiotti.learning.data.dao.ServiceDao;
import com.pedroghiotti.learning.data.entity.Service;

import java.util.List;
import java.util.Optional;

public class App {
    public static void main(String[] args) {
        ServiceDao serviceDao = new ServiceDao();

        System.out.println("\n====SERVICES====");

        List<Service> services = serviceDao.getAll();
        System.out.println("\n===GET_ALL===");
        services.forEach(System.out::println);

        Optional<Service> serviceById = serviceDao.getById(services.get(0).getServiceId());
        System.out.println("\n===GET_BY_ID===\n" + serviceById.get());

    }
}
