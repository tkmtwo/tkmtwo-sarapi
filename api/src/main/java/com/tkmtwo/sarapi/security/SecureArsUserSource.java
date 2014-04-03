/*
 *
 * Copyright 2014 Tom Mahaffey
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tkmtwo.sarapi.security;

import com.bmc.arsys.api.ARException;
import com.bmc.arsys.api.ARServerUser;
import com.tkmtwo.sarapi.CannotImpersonateARServerUserException;
import com.tkmtwo.sarapi.ArsUserSource;
import com.tkmtwo.sarapi.support.ARExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;




/**
 * An <code>ArsUserSource</code> which wraps another source.
 *
 *
 *
 * @author Tom Mahaffey
 * @version 1.0
 */
public class SecureArsUserSource
  implements ArsUserSource,
             FactoryBean, InitializingBean, DisposableBean {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  protected ArsUserSource arsUserSource;
  
  
  
  
  /**
   * Gets the underlying <code>ArsUserSource</code>.
   *
   * @return an <code>ArsUserSource</code> value
   */
  public ArsUserSource getArsUserSource() {
    return this.arsUserSource;
  }
  
  
  /**
   * Sets the underlying <code>ArsUserSource</code>.
   *
   * The underlying source should have ARS administrative credentials.
   * 
   *
   * @param aus an <code>ArsUserSource</code> value
   */
  public void setArsUserSource(ArsUserSource aus) {
    this.arsUserSource = aus;
  }
  
  
  public String getArsEnvironmentName() {
    return this.arsUserSource.getArsEnvironmentName();
  }

  /*
  public ArsContext getArsContext()
  {
    return this.arsUserSource.getArsContext();
  }
  */


  /**
   * Gets an <code>ARServerUser</code>.
   *
   * Initially gets an <code>ARServerUser</code> from the underlying
   * <code>ArsUserSource</code>, and immediately impersonates the authenticated
   * user in the current <code>SecurityContext</code>.
   *
   * To impersonate, the underlying <code>ArsUserSource</code> must be
   * getting <code>ARServerUser</code> objects for an AR Administrator user.
   *
   * @return an <code>ARServerUser</code> value
   * @exception ARException if an error occurs
   * @exception AuthenticationCredentialsNotFoundException not authenticated
   *  via Spring Security
   */
  public ARServerUser getARServerUser()
    throws AuthenticationCredentialsNotFoundException {

    String authUserName;
    
    SecurityContext securityContext = SecurityContextHolder.getContext();
    
    Authentication authentication = securityContext.getAuthentication();
    if (authentication == null) {
      throw new AuthenticationCredentialsNotFoundException("No authentication found.");
    }
    
    Object principal = authentication.getPrincipal();
    if (principal == null) {
      throw new AuthenticationCredentialsNotFoundException("No principal found.");
    }
    
    if (principal instanceof UserDetails) {
      authUserName = ((UserDetails) principal).getUsername();
    } else {
      authUserName = principal.toString();
    }
    
    logger.debug("Authenticated username is {}.", authUserName);
    
    ARServerUser arsu = arsUserSource.getARServerUser();
    
    logger.debug("Setting impersonated user on ARServerUser {} to {}.",
                 arsu.getUser(),
                 authUserName);
    
    try {
      arsu.impersonateUser(authUserName);
      //arsu.verifyUser();
    } catch (ARException arex) {
      if (ARExceptions.hasStatusInfoMessageNum(arex, 623L)) {
        logger.warn("Could not set impersonated user on ARServerUser {} to {}. "
                    + "Is {} an AR Admin and does {} exist in AR?",
                    new Object[]
          {
            arsu.getUser(),
            authUserName,
            arsu.getUser(),
            authUserName
          });
        throw new CannotImpersonateARServerUserException("Can not impersonate username "
                                                         + authUserName
                                                         + " under SecureArsUserSource.  ",
                                                         arex);
      } else {
        throw new CannotImpersonateARServerUserException("While impersonating ...", arex);
      }
      
    }
    
    return arsu;
  }
  
  
  public void releaseARServerUser(ARServerUser arsu) {
    arsUserSource.releaseARServerUser(arsu);
  }  
  
  
  
  
  
  /**
   * @see org.springframework.beans.factory.InitializingBean
   */
  public void afterPropertiesSet() {
    Assert.notNull(arsUserSource, "Need a plain ArsUserSource.");
  }
  
  
  /**
   * @see org.springframework.beans.factory.FactoryBean
   */
  public Object getObject() {
    return this;
  }
  
  /**
   * @see org.springframework.beans.factory.FactoryBean
   */
  public Class getObjectType() {
    return SecureArsUserSource.class;
  }
  
  /**
   * @see org.springframework.beans.factory.FactoryBean
   */
  public boolean isSingleton() {
    return true;
  }
  
  /**
   * @see org.springframework.beans.factory.DisposableBean
   */
  public void destroy() {
    logger.info("Closing SecureArsUserSource.");
  }
  
  
  
}

