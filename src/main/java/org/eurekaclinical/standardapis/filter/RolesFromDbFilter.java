package org.eurekaclinical.standardapis.filter;

/*-
 * #%L
 * Eureka! Clinical Common
 * %%
 * Copyright (C) 2016 - 2017 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
import java.security.Principal;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletRequest;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.entity.RoleEntity;
import org.eurekaclinical.standardapis.entity.UserEntity;

/**
 * Filter that adds the user's roles from a {@link UserDao} to the request.
 * Users of this filter must bind {@link UserDao} in their Guice configuration.
 *
 * @author Andrew Post
 */
@Singleton
public class RolesFromDbFilter extends AbstractRolesFilter {
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    
    private final UserDao<? extends UserEntity<? extends RoleEntity>> userDao;

    @Inject
    public RolesFromDbFilter(UserDao<? extends UserEntity<? extends RoleEntity>> inUserDao) {
        this.userDao = inUserDao;
    }

    @Override
    protected String[] getRoles(Principal principal, ServletRequest inRequest) {
        UserEntity<? extends RoleEntity> user = this.userDao.getByPrincipal(principal);
        if (user != null) {
            List<? extends RoleEntity> roles = user.getRoles();
            if(roles.size() !=0){
	            String[] roleNames = new String[roles.size()];
	            int i = 0;
	            for (RoleEntity re : roles) {
	                roleNames[i++] = re.getName();
	            }
	            return roleNames;
            }
            else {
            	return EMPTY_STRING_ARRAY;
            }
        } else {
            return null;
        }

    }

}
