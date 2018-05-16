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

/**
 *
 * @author arpost
 */
public interface GroupEntity extends Entity<Long> {

    /**
     * Get the role's identification number.
     *
     * @return A {@link Long} representing the role's id.
     */
    @Override
    Long getId();

    /**
     * Get the role's name.
     *
     * @return A String containing the role's name.
     */
    String getName();

    /**
     * Set the role's identification number.
     *
     * @param inId The number representing the role's id.
     */
    @Override
    void setId(Long inId);

    /**
     * Set the role's name.
     *
     * @param inName A string containing the role's name.
     */
    void setName(String inName);
    
}