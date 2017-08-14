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
import javax.servlet.http.HttpSession;
import org.eurekaclinical.standardapis.dao.UserDao;
import org.eurekaclinical.standardapis.entity.RoleEntity;
import org.eurekaclinical.standardapis.entity.UserEntity;

/**
 * Filter that adds the user's roles from a database to the 
 * request by accessing the {@link UserDao}. Users of this filter must bind
 * {@link UserDao} in their Guice configuration.
 *
 * @author Andrew Post
 */
@Singleton
public abstract class RolesFromDbFilter extends AbstractRolesFilter {

    private final UserDao<? extends UserEntity<? extends RoleEntity>> userDao;

    @Inject
    public RolesFromDbFilter(UserDao<? extends UserEntity<? extends RoleEntity>> inUserDao) {
        this.userDao = inUserDao;
    }

    @Override
    protected String[] getRoles(HttpSession session, Principal principal) {
        UserEntity<? extends RoleEntity> user = this.userDao.getByPrincipal(principal);
        List<? extends RoleEntity> roles = user.getRoles();
        String[] roleNames = new String[roles.size()];
        int i = 0;
        for (RoleEntity re : roles) {
            roleNames[i++] = re.getName();
        }
        return roleNames;
    }

    @Override
    public void destroy() {
    }
}
