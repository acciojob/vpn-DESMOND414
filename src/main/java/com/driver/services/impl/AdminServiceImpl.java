package com.driver.services.impl;

import com.driver.model.Admin;
import com.driver.model.Country;
import com.driver.model.CountryName;
import com.driver.model.ServiceProvider;
import com.driver.repository.AdminRepository;
import com.driver.repository.CountryRepository;
import com.driver.repository.ServiceProviderRepository;
import com.driver.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminRepository adminRepository1;

    @Autowired
    ServiceProviderRepository serviceProviderRepository1;

    @Autowired
    CountryRepository countryRepository1;

    @Override
    public Admin register(String username, String password) {
        return adminRepository1.save(new Admin(username, password));
    }

    @Override
    public Admin addServiceProvider(int adminId, String providerName) {
        Admin admin = adminRepository1.findById(adminId).orElse(null);
        if (admin == null) {
            return null;
        }
        ServiceProvider serviceProvider = new ServiceProvider(providerName, admin);
        admin.getServiceProviders().add(serviceProvider);
        return adminRepository1.save(admin);
    }

    @Override
    public ServiceProvider addCountry(int serviceProviderId, String countryName) throws Exception{
        try {
            CountryName name = CountryName.valueOf(countryName);
            Country country = new Country(name, name.toCode());
            ServiceProvider serviceProvider = serviceProviderRepository1.findById(serviceProviderId).orElse(null);
            if (serviceProvider != null) {
                serviceProvider.getCountryList().add(country);
            }
            return serviceProvider;
        } catch (Exception e) {
            throw new Exception("Country not found");
        }

    }
}
