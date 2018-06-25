package org.eurekaclinical.standardapis.entity;

/*-
 * #%L
 * Eureka! Clinical Standard APIs
 * %%
 * Copyright (C) 2016 Emory University
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
 * @author Andrew Post
 */
public interface UserTemplateEntity<R extends RoleEntity> extends Entity<Long> {
    /**
     * Get the user's unique identifier.
     *
     * @return A {@link Long} representing the user's unique identifier.
     */
    @Override
    Long getId();

    /**
     * Get all the roles assigned to the user.
     *
     * @return A list of roles assigned to the user.
     */
    List<R> getRoles();
    

    /**
     * Set the user's unique identifier.
     *
     * @param inId A {@link Long} representing the user's unique identifier.
     */
    @Override
    void setId(final Long inId);

    /**
     * Set the roles assigned to the current user.
     *
     * @param inRoles A list of roles to be assigned to the user.
     */
    void setRoles(final List<R> inRoles);
    
    void addRole(R role);
    
    void removeRole(R role);
    
    /**
	 * Sets criteria for triggering auto-authorization. May be 
	 * <code>null</code>, which means that auto-authorization will always be 
	 * triggered when requested.
	 * 
	 * @param criteria the criteria for triggering auto-authorization, 
	 * expressed as a Freemarker Template Language expression.
	 */
    void setCriteria(String criteria);
    
    /**
	 * Gets the criteria for triggering auto-authorization. May be 
	 * <code>null</code>, which means that auto-authorization will always be 
	 * triggered when requested. The criteria are expressed as Freemarker 
	 * Template Language expression
	 * @return the criteria expression string.
	 */
	String getCriteria();
    

    /**
     * Sets the name of the template.
     * 
     * @param name the template's name.
     */
    void setName(String name);
    
    /**
     * Gets the name of the template.
     * 
     * @return the template's name. 
     */
    String getName();
    
}
