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

import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.Value;
import com.tkmtwo.sarapi.ArsField;
import com.tkmtwo.sarapi.InvalidEntryAccessException;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;



/**
 *
 * @param <T> object type
 */
public abstract class AbstractEntryMapper<T>
  extends FormMappingOperation
  implements EntryMapper<T> {
  
  
  public abstract Entry mapObject(T source)
    throws InvalidEntryAccessException;
  public abstract T mapEntry(Entry entry)
    throws InvalidEntryAccessException;

  public abstract Set<String> getMappedFieldNames();
  
  protected abstract Map<String, String> propertyToFieldNames();
  protected abstract Map<String, String> propertyToEnumFieldNames();
  

  protected BeanWrapper newBeanWrapper(Object target) {
    BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(target);
    bw.setConversionService(getConversionService());
    return bw;
  }
  
  
  
  
  protected ArsField getField(String fieldName) {
    return getSchemaHelper().getField(getFormName(), fieldName);
  }
  
  protected void setPropertyValues(BeanWrapper bw,
                                   Entry entry,
                                   Map<String, String> m) {
    for (Map.Entry<String, String> me : m.entrySet()) {
      bw.setPropertyValue(me.getKey(),
                          entry.get(getSchemaHelper().getField(getFormName(), me.getValue()).getId()));
    }
  }


  protected void setPropertyEnumValues(BeanWrapper bw,
                                       Entry entry,
                                       Map<String, String> m) {
    for (Map.Entry<String, String> me : m.entrySet()) {
      ArsField af = getField(me.getValue());
      Integer enumNumber = getConversionService().convert(entry.get(af.getId()), Integer.class);

      bw.setPropertyValue(me.getKey(),
                          getSchemaHelper().getEnumName(getFormName(), me.getValue(), enumNumber));
    }
  }
  

  /*
  
  protected void put(Entry entry,
                     String fieldName,
                     Object fieldValue) {
    ArsField af = getSchemaHelper().getField(getFormName(), fieldName);
    entry.put(af.getId(),
              af.getDataType().convert(fieldValue));
  }
  

    

  */
  
  
  protected void setEntryValues(BeanWrapper bw,
                                Entry entry,
                                Map<String, String> m) {
    for (Map.Entry<String, String> me : m.entrySet()) {
      entry.put(getSchemaHelper().getField(getFormName(), me.getValue()).getId(),
                bw.getConversionService().convert(bw.getPropertyValue(me.getKey()),
                                                  Value.class));
    }
  }

  protected void setEntryEnumValues(BeanWrapper bw,
                                    Entry entry,
                                    Map<String, String> m) {
    for (Map.Entry<String, String> me : m.entrySet()) {

      Integer fieldId = getSchemaHelper().getField(getFormName(), me.getValue()).getId();
      String enumName = bw.getConversionService().convert(bw.getPropertyValue(me.getKey()),
                                                          String.class);
      if (enumName == null) {
        entry.put(fieldId, new Value());
      } else {
        entry.put(fieldId,
                  new Value(getSchemaHelper().getEnumNumber(getFormName(), me.getValue(), enumName)));
      }

      /***      
      Integer enumNumber =
        getSchemaHelper()
        .getEnumNumber(getFormName(), me.getValue(),
                       bw.getConversionService().convert(bw.getPropertyValue(me.getKey()),
                                                         String.class));
      */

      /*      
      entry.put(getSchemaHelper().getField(getFormName(), me.getValue()).getId(),
                bw.getConversionService().convert(bw.getPropertyValue(me.getKey()),
                                                  Value.class));
      */

      /***
      entry.put(getSchemaHelper().getField(getFormName(), me.getValue()).getId(),
                new Value(enumNumber));
      */
      
    }
  }


                        
  
  public void afterPropertiesSet() {
    super.afterPropertiesSet();
  }



}
