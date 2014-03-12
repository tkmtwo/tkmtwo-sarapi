package com.tkmtwo.sarapi.mapping;


import com.bmc.arsys.api.Entry;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Map;
import java.util.Set;
import com.tkmtwo.sarapi.InvalidEntryAccessException;
import org.springframework.beans.BeanWrapper;



public final class PersonMapper
  extends AbstractEntryMapper<Person> {
  
  
  private static final Map<String, String> PROPERTY_TO_FIELDNAME = new ImmutableMap.Builder<String, String>()
    .put("firstName", "First Name")
    .put("lastName", "Last Name")
    .put("age", "Age")
    .put("status", "Status")
    .put("createDateTime", "Create Date")
    .put("modifyDateTime", "Modified Date")
    .build();
  
  private static final Map<String, String> PROPERTY_TO_ENUMFIELDNAME = new ImmutableMap.Builder<String, String>()
    .put("gender", "Gender")
    .build();
  
  private static final Set<String> FIELD_NAMES = new ImmutableSet.Builder<String>()
    .addAll(PROPERTY_TO_FIELDNAME.values())
    .addAll(PROPERTY_TO_ENUMFIELDNAME.values())
    .build();
  
  
  protected Map<String, String> propertyToFieldNames() { return PROPERTY_TO_FIELDNAME; }
  protected Map<String, String> propertyToEnumFieldNames() { return PROPERTY_TO_ENUMFIELDNAME; }
  public Set<String> getMappedFieldNames() { return FIELD_NAMES; }
  
  
  
  
  
  
  
  
  
  public Entry mapObject(Person source)
    throws InvalidEntryAccessException {
    
    if (source == null) { return null; }
    Entry entry = new Entry();
    
    if (source.getId() != null) {
      entry.setEntryId(source.getId());
    }
    
    BeanWrapper bw = newBeanWrapper(source);
    setEntryValues(bw, entry, propertyToFieldNames());
    setEntryEnumValues(bw, entry, propertyToEnumFieldNames());
    
    return entry;
  }
  
  
  
  
  
  
  
  public Person mapEntry(Entry entry)
    throws InvalidEntryAccessException {
    
    if (entry == null) { return null; }
    
    Person p = new Person();
    p.setId(entry.getEntryId());
    
    BeanWrapper bw = newBeanWrapper(p);
    setPropertyValues(bw, entry, propertyToFieldNames());
    setPropertyEnumValues(bw, entry, propertyToEnumFieldNames());
    
    return p;
  }
  
  
  
}
