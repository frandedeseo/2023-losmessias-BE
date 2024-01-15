package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.ConfirmationToken;
import com.losmessias.leherer.repository.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationToken getToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findByToken(token);
        if (confirmationToken == null){
            throw new IllegalStateException("token not found");
        }
        return confirmationToken;
    }

    public String generateConfirmationToken(AppUser appUser){
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                appUser
        );

        confirmationTokenRepository.save(confirmationToken);

        return token;
    }

    public ConfirmationToken validateToken(String token){
        ConfirmationToken confirmationToken = getToken(token);

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());

        confirmationTokenRepository.save(confirmationToken);

        return confirmationToken;
    }
}