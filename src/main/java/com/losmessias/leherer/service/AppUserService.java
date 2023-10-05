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

    public void validateEmailNotTaken(String email){

        if (getAppUser(email) != null) {
            throw new IllegalStateException("email already taken");
        }
    }

    public void signUpUser(AppUser appUser) {

        validateEmailNotTaken(appUser.getEmail());

        String encodedPassword = encodePassword(appUser.getPassword());
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }

    public String encodePassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    public void enableAppUser(String email) {
        AppUser appUser = getAppUser(email);
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }

    public void changePassword(String email, String password) {
        AppUser appUser = getAppUser(email);

        String encodedPassword = encodePassword(password);
        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }
}