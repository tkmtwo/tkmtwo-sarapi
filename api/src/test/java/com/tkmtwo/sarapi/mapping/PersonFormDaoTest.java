package com.tkmtwo.sarapi.mapping;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.bmc.arsys.api.Entry;
import com.bmc.arsys.api.Value;
import java.util.List;
import java.util.Map;
import com.tkmtwo.sarapi.ArsDataType;
//import com.tkmtwo.sarapi.InvalidDataAccessResourceUsageException;
import com.tkmtwo.sarapi.support.EntryUtil;
import org.springframework.dao.InvalidDataAccessResourceUsageException;



@ContextConfiguration
public final class PersonFormDaoTest
  extends AbstractJUnit4SpringContextTests
{
  
  @Autowired
  protected PersonFormDao personDao;
  
  
  @Test
  public void testCrud() {
    String id = null;
    String firstName = "FormDaoFirstName";
    String lastName = "FormDaoLastName";
    Integer age = Integer.valueOf(73);
    String gender = "Male";


    Person p = new Person();
    p.setFirstName(firstName);
    p.setLastName(lastName);

    System.out.println("Begin  : " + p.toString());
    p = personDao.save(p);
    System.out.println("Create : " + p.toString());


    assertNotNull(p);
    assertNotNull(p.getId());
    id = p.getId();
    assertEquals(firstName, p.getFirstName());
    assertEquals(lastName, p.getLastName());
    assertNull(p.getAge());
    assertNull(p.getGender());

    p.setAge(age);
    p = personDao.createOrSet(p);
    System.out.println("Set    : " + p.toString());
    assertNotNull(p);
    assertEquals(id, p.getId());
    assertEquals(firstName, p.getFirstName());
    assertEquals(lastName, p.getLastName());
    assertEquals(age, p.getAge());
    assertNull(p.getGender());

    p.setGender(gender);
    p = personDao.createOrMerge(p);
    System.out.println("Merge  : " + p.toString());
    assertNotNull(p);
    assertEquals(id, p.getId());
    assertEquals(firstName, p.getFirstName());
    assertEquals(lastName, p.getLastName());
    assertEquals(age, p.getAge());
    assertEquals(gender, p.getGender());
    
    p.setId(null);
    p = personDao.save(p);
    System.out.println("Dup    : " + p.toString());
    
  }


  @Test
  public void testFindFirstNameLike() {
    List<Person> persons = personDao.personsByFirstNameLike("SetFirst%");
    assertTrue(!persons.isEmpty());
    for (Person person : persons) {
      assertTrue(person.getFirstName().startsWith("SetFirst"));
    }
  }
  
  
  
}
