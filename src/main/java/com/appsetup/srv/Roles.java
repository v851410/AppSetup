package com.appsetup.srv;

import com.appsetup.type.UserRole;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 *
 * @author Vitaliy
 */
public class Roles {

    public boolean hasRoles(Collection<? extends GrantedAuthority> authorities, UserRole... roles) {

        for (UserRole it : roles) {
            if (authorities.contains(new SimpleGrantedAuthority(it.name()))) {
                return true;
            }
        }
        return false;
    }
    
}
