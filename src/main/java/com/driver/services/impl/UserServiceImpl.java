package com.driver.services.impl;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.model.User;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository3;
    @Autowired
    ServiceProviderRepository serviceProviderRepository3;
    @Autowired
    CountryRepository countryRepository3;

    @Override
    public User register(String username, String password, String countryName) throws Exception{
        try {
            System.out.println(countryName);
            CountryName name = CountryName.valueOf(countryName.toUpperCase());
            System.out.println(name);
            Country country = new Country(name, name.toCode());
            User user = new User(username, password, country.getCode(), null, false, country);
            System.out.println(user);
            user = userRepository3.save(user);
            System.out.println(user);
            user.setOriginalIp(String.format("%s.%s", country.getCode(), user.getId()));
            System.out.println(user);
            return userRepository3.save(user);
        } catch (Exception e) {
            System.out.println(e);
            throw new Exception("Country not found");
        }
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user = userRepository3.findById(userId).orElse(null);
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).orElse(null);

        if (user == null) {
            return null;
        }

        if (serviceProvider != null) {
            user.getServiceProviderList().add(serviceProvider);
        }

        return userRepository3.save(user);
    }
}
