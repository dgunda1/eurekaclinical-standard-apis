package org.eurekaclinical.standardapis.entity;

/*-
 * #%L
 * Eureka! Clinical Standard APIs
 * %%
 * Copyright (C) 2016 - 2018 Emory University
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


import java.util.List;
/**
*
* @author Dileep Gunda
* @param <r> a group type.
*/
public interface UserGroupEntity <r extends GroupEntity> extends Entity<Long> {

    /**
     * Get the user's unique identifier.
     *
     * @return A {@link Long} representing the user's unique identifier.
     */
    @Override
    Long getId();

    /**
     * Get all the groups assigned to the user.
     *
     * @return A list of groups assigned to the user.
     */
    List<r> getGroups();

    /**
     * Get the user's unique username.
     *
     * @return the username.
     */
    String getUsername();

    /**
     * Set the user's unique identifier.
     *
     * @param inId A {@link Long} representing the user's unique identifier.
     */
    @Override
    void setId(final Long inId);

    /**
     * Set the groups assigned to the current user.
     *
     * @param ingroups A list of groups to be assigned to the user.
     */
    void setGroups(final List<r> ingroups);
    
    void addGroup(r component);
    
    void removeGroup(r component);

    /**
     * Set the user's unique username.
     *
     * @param inUsername the usernames.
     */
    void setUsername(final String inUsername);
    
}
