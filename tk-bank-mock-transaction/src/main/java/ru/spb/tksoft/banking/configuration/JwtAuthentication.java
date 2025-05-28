package ru.spb.tksoft.banking.configuration;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.spb.tksoft.banking.controller.JwtUser;

/**
 * Auth with JWT.
 * 
 * @author Konstantin Terskikh, kostus.online.1974@yandex.ru, 2025
 */
public class JwtAuthentication implements Authentication {

    private final JwtUser user;
    private boolean authenticated = true;

    /**
     * Constructor.
     */
    public JwtAuthentication(JwtUser user) {
        this.user = user;
    }

    /**
     * @inheritDoc
     * @return Empty list.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    /**
     * @inheritDoc
     * @return Null.
     */
    @Override
    public Object getCredentials() {
        return null;
    }

    /**
     * @inheritDoc
     * @return Map "USER_ID" -> user id.
     */
    @Override
    public Object getDetails() {
        return Collections.singletonMap("USER_ID", user.userId());
    }

    /**
     * @inheritDoc
     * @return User itself.
     */
    @Override
    public Object getPrincipal() {
        return user;
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    /**
     * @inheritDoc
     * @return User's email as name.
     */
    @Override
    public String getName() {
        return user.email();
    }
}
