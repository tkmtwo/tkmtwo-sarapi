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
package com.tkmtwo.sarapi.mapping;


import static com.google.common.base.Preconditions.checkNotNull;

import com.bmc.arsys.api.Constants;
import com.bmc.arsys.api.Entry;
import com.google.common.collect.ImmutableList;
import com.tkmtwo.sarapi.support.EntryUtil;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;


/**
 * Remove Entry entries where the Value is $NULL$
 */
public class NullRemovingEntryHandler
  extends FormMappingOperation
  implements EntryHandler {

  protected final Logger logger = LoggerFactory.getLogger(getClass());  

  private List<String> fieldNames;
  private List<Integer> fieldIds;

  
  public List<String> getFieldNames() {
    if (fieldNames == null) {
      fieldNames = new ArrayList<String>();
    }
    return fieldNames;
  }
  public void setFieldNames(List<String> l) { fieldNames = l; }
  
  
  
  
  private List<Integer> getFieldIds() {
    if (fieldIds == null) {
      fieldIds = ImmutableList.of();
    }
    return fieldIds;
  }
  private void setFieldIds(List<Integer> l) {
    if (l != null) {
      fieldIds = ImmutableList.copyOf(l);
    }
  }
  
  
  
  public void handleEntry(Entry entry) {
    checkNotNull(entry, "Need an entry");
    int sizeBefore = entry.size();
    
    if (getFieldIds().isEmpty()) {
      EntryUtil.removeNullValues(entry);
    } else {
      EntryUtil.removeNullValues(entry, getFieldIds());
    }
    
    int sizeAfter = entry.size();
    logger.trace("Removed {} entries from value map", String.valueOf(sizeBefore - sizeAfter));
  }
  

  
  public void afterPropertiesSet() {
    super.afterPropertiesSet();
    
    ImmutableList.Builder<Integer> ilb = new ImmutableList.Builder<Integer>();
    for (String fieldName : getFieldNames()) {
      if (fieldName == null) { continue; }
      ilb.add(getSchemaHelper().getFieldId(getFormName(), fieldName));
    }
    fieldIds = ilb.build();
    
  }
  

  
  
}
