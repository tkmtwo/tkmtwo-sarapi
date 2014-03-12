package com.tkmtwo.sarapi.mapping;


import com.google.common.base.Objects;
import org.joda.time.DateTime;

public class Person {
  private String id;
  private Integer status;
  private String firstName;
  private String lastName;
  private Integer age;
  private String gender;
  
  private DateTime createDateTime;
  private DateTime modifyDateTime;

  
  public String getId() { return id; }
  public void setId(String s) { id = s; }
  
  public Integer getStatus() { return status; }
  public void setStatus(Integer i) { status = i; }
  
  public String getFirstName() { return firstName; }
  public void setFirstName(String s) { firstName = s; }
  
  public String getLastName() { return lastName; }
  public void setLastName(String s) { lastName = s; }
  
  public Integer getAge() { return age; }
  public void setAge(Integer i) { age = i; }
  
  public String getGender() { return gender; }
  public void setGender(String s) { gender = s; }
  
  public DateTime getCreateDateTime() { return createDateTime; }
  public void setCreateDateTime(DateTime dt) { createDateTime = dt; }
  
  public DateTime getModifyDateTime() { return modifyDateTime; }
  public void setModifyDateTime(DateTime dt) { modifyDateTime = dt; }
  
  public String toString() {
    return Objects.toStringHelper(this)
      .add("id", getId())
      .add("status", getStatus())
      .add("firstName", getFirstName())
      .add("lastName", getLastName())
      .add("age", getAge())
      .add("gender", getGender())
      .add("createDateTime", getCreateDateTime())
      .add("modifyDateTime", getModifyDateTime())
      .toString();
  }
  
}
