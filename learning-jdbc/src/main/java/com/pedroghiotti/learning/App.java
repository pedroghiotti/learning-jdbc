package com.pedroghiotti.learning;

import com.pedroghiotti.learning.data.dao.ServiceDao;
import com.pedroghiotti.learning.data.entity.Service;

import java.math.BigDecimal;
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

        Service newService = new Service();
        newService.setName("NewService@"+System.currentTimeMillis());
        newService.setPrice(new BigDecimal(0));
        newService = serviceDao.create(newService);
        System.out.println("\n===CREATE===\n" + newService);

        newService.setName("NewerService@"+System.currentTimeMillis());
        newService.setPrice(new BigDecimal(100));
        newService = serviceDao.update(newService);
        System.out.println("\n===UPDATE===\n" + newService);

        serviceDao.delete(newService.getServiceId());
        System.out.println("\n===DELETE===\n");
    }
}
