package io.bootify.simple5.service;

import io.bootify.simple5.domain.UserInfo;
import io.bootify.simple5.repos.UserInfoRepository;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


/**
 * Synchronize Keycloak user with the database after successful authentication.
 */
@Service
public class UserSynchronizationService {

    private static final Logger log = LoggerFactory.getLogger(UserSynchronizationService.class);

    private final UserInfoRepository userInfoRepository;

    public UserSynchronizationService(final UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    private void syncWithDatabase(final Map<String, Object> claims) {
        final String subject = ((String)claims.get("sub"));
        UserInfo userInfo = userInfoRepository.findByCredentials(subject);
        if (userInfo == null) {
            log.info("adding new user after successful authentication: {}", subject);
            userInfo = new UserInfo();
            userInfo.setCredentials(subject);
            // TODO provide data for new users
            userInfo.setIsActive(true);
        } else {
            log.debug("updating existing user after successful login: {}", subject);
        }
        userInfo.setEmail(((String)claims.get("email")));
        userInfo.setResetString(((String)claims.get("given_name")));
        userInfo.setUserRole(((String)claims.get("family_name")));
        userInfoRepository.save(userInfo);
    }

    @EventListener(AuthenticationSuccessEvent.class)
    public void onAuthenticationSuccessEvent(final AuthenticationSuccessEvent event) {
        if (event.getSource() instanceof JwtAuthenticationToken jwtToken && 
        "id1234".equals(jwtToken.getTokenAttributes().get("azp"))) {
            syncWithDatabase(jwtToken.getTokenAttributes());
        }
    }

}
