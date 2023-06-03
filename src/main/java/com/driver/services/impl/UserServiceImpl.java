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
            CountryName name = CountryName.valueOf(countryName.toUpperCase());
            Country country = new Country(name, name.toCode());
            User user = new User(username, password, country.getCode() +".0", null, false, country);
            user = userRepository3.save(user);
            user.setOriginalIp(String.format("%s.%s", country.getCode(), user.getId()));
            return userRepository3.save(user);
        } catch (Exception e) {
            throw new Exception("Country not found");
        }
    }

    @Override
    public User subscribe(Integer userId, Integer serviceProviderId) {
        User user = userRepository3.findById(userId).orElse(null);
        ServiceProvider serviceProvider = serviceProviderRepository3.findById(serviceProviderId).orElse(null);
        user.getServiceProviderList().add(serviceProvider);

        return userRepository3.save(user);
    }
}
