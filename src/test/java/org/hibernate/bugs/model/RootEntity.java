package org.hibernate.bugs.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
public class RootEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @TenantId
  private String tenant;

  @OneToOne(mappedBy = "rootEntity", cascade = CascadeType.PERSIST)
  private ChildEntity childEntity;

  public RootEntity() {
  }

  public RootEntity(ChildEntity childEntity) {
    setChildEntity(childEntity);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTenant() {
    return tenant;
  }

  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  public ChildEntity getChildEntity() {
    return childEntity;
  }

  public void setChildEntity(ChildEntity childEntity) {
    this.childEntity = childEntity;
    if (childEntity != null) {
      childEntity.setRootEntity(this);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    RootEntity event = (RootEntity) o;
    return Objects.equals(id, event.id);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
