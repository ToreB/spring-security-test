package no.toreb;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    public boolean allowAccess(final String requestParameter) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        final boolean hasSecretRole = authentication.getAuthorities()
                                                    .stream()
                                                    .anyMatch(o -> o.getAuthority().equals("ROLE_SECRET"));

        return hasSecretRole && requestParameter.equals("secret");
    }

    private boolean hasRole(final String role) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities()
                             .stream()
                             .anyMatch(o -> o.getAuthority().equals(role));
    }

    public boolean allowAccess(final Event event) {
        if (event.getExtra() == null) {
            return hasRole("ROLE_WRITE");
        }

        return hasRole("ROLE_WRITE_EXTRA");
    }
}
