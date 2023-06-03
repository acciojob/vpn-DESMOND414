package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ConnectionRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    @Autowired
    UserRepository userRepository2;
    @Autowired
    ServiceProviderRepository serviceProviderRepository2;
    @Autowired
    ConnectionRepository connectionRepository2;

    @Override
    public User connect(int userId, String countryName) throws Exception {
        User user = userRepository2.findById(userId).orElse(null);
//        if (user == null) {
//            return null;
//        }
        if (user.getConnected()) {
            throw new Exception("Already connected");
        } else if (user.getOriginalCountry().getCountryName().equals(countryName)) {
            return user;
        } else {
            if (user.getServiceProviderList().isEmpty()) {
                throw new Exception("Unable to connect");
            }
            List<ServiceProvider> serviceProviders = user.getServiceProviderList().stream()
                    .filter(sp -> sp.getCountryList().stream().anyMatch(country -> country.getCountryName().equals(countryName)))
                    .sorted((sp1, sp2) -> {
                        if (sp1.getId() < sp2.getId()) {
                            return -1;
                        } else if (sp1.getId() == sp2.getId()) {
                            return 0;
                        } else {
                            return 1;
                        }
                    })
                    .collect(Collectors.toList());
            user.setConnected(true);
            user.getConnectionList().add(new Connection(user, serviceProviders.get(0)));
            user.setMaskedIp(String.format("%s.%s.%s", CountryName.valueOf(countryName.toUpperCase()).toCode(), serviceProviders.get(0), user.getId()));

            return userRepository2.save(user);
        }

    }

    @Override
    public User disconnect(int userId) throws Exception {
        User user = userRepository2.findById(userId).orElse(null);
//        if (user == null) {
//            return null;
//        }
        if(!user.getConnected()) {
            throw new Exception("Already disconnected");
        }
        user.setConnected(false);
        user.setMaskedIp(null);
        user.getConnectionList().stream().forEach(connection -> connectionRepository2.delete(connection));
        user.setConnectionList(null);

        return userRepository2.save(user);
    }

    @Override
    public User communicate(int senderId, int receiverId) throws Exception {
        User receiver = userRepository2.findById(receiverId).orElse(null);
        User sender = userRepository2.findById(senderId).orElse(null);

        CountryName receiverCountry = null;
        if (receiver.getConnected()) {
            receiverCountry = CountryName.valueOf(receiver.getMaskedIp().split(".")[0]);
        } else {
            receiverCountry = receiver.getOriginalCountry().getCountryName();
        }

        CountryName senderCountry = sender.getOriginalCountry().getCountryName();

        if (receiverCountry == senderCountry) {
            return sender;
        } else {
            try {
                return this.connect(senderId, receiverCountry.name());
            } catch(Exception e) {
                throw new Exception("Cannot establish communication");
            }
        }
    }
}
