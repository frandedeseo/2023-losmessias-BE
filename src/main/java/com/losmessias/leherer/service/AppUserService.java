package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByEmail(email);
        if (user!=null){
            return user;
        }else{
            throw new UsernameNotFoundException("Invalid username or password.");
        }
    }

    public AppUser getAppUser(String email){
        return appUserRepository.findByEmail(email);
    }

    public AppUser getAppUserById(Long id) {
        return appUserRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("app user not found"));
    }

    public void validateEmailNotTaken(String email){

        if (getAppUser(email) != null) {
            throw new IllegalStateException("email already taken");
        }
    }

    public void enableAppUser(String email) {
        AppUser appUser = getAppUser(email);
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }

    public String changePassword(String email, String password) {
        AppUser appUser = getAppUser(email);

        String encodedPassword = bCryptPasswordEncoder.encode(password);
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
        return "Password changed successfully";
    }

    public AppUser update(Long id, AppUser appUser) {
        AppUser appUserToUpdate = getAppUserById(id);
        appUserToUpdate.setFirstName(appUser.getFirstName() != null ? appUser.getFirstName() : appUserToUpdate.getFirstName());
        appUserToUpdate.setLastName(appUser.getLastName() != null ? appUser.getLastName() : appUserToUpdate.getLastName());
        appUserToUpdate.setEmail(appUser.getEmail() != null ? appUser.getEmail() : appUserToUpdate.getEmail());
        appUserToUpdate.setLocation(appUser.getLocation() != null ? appUser.getLocation() : appUserToUpdate.getLocation());
        appUserToUpdate.setPhone(appUser.getPhone() != null ? appUser.getPhone() : appUserToUpdate.getPhone());
        appUserToUpdate.setClassReservations(appUser.getClassReservations() != null ? appUser.getClassReservations() : appUserToUpdate.getClassReservations());
        return appUserRepository.save(appUserToUpdate);
    }
}