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
package com.tkmtwo.sarapi;

import static com.google.common.base.Preconditions.checkState;

import com.bmc.arsys.api.ARServerUser;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.impl.StackObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;



/**
 * Bean-based implementation of an <code>BasicArsUserSource</code>.
 *
 * @author Tom Mahaffey
 */
public final class BasicArsUserSource
  extends AbstractArsUserSource {
  
  public ARServerUser getARServerUser() {
    ARServerUser arsu = null;
    try {
      arsu = getUserPool().borrowObject();
    } catch (Exception ex) {
      arsu = null;
      throw new CannotGetARServerUserException("While borrowing from pool...", ex);
    }
    return arsu;
  }
  public void releaseARServerUser(ARServerUser arsu) {
    if (arsu != null) {
      try {
        getUserPool().returnObject(arsu);
      } catch (Exception ex) {
        throw new CannotGetARServerUserException("While returning to pool...", ex);
      }
    }
  }
  
  
}
