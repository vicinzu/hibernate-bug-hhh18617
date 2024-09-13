package org.hibernate.bugs.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table
public class ChildEntity {

  @Id
  @UuidGenerator
  private UUID id;

  @TenantId
  private String tenant;

  @OneToOne
  @JoinColumn
  private RootEntity rootEntity;

  public ChildEntity() {
  }

  public ChildEntity(RootEntity rootEntity) {
    this.rootEntity = rootEntity;
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

  public RootEntity getRootEntity() {
    return rootEntity;
  }

  public void setRootEntity(RootEntity rootEntity) {
    this.rootEntity = rootEntity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    ChildEntity event = (ChildEntity) o;
    return Objects.equals(id, event.id);
  }

  @Override
  public int hashCode() {
    return 0;
  }
}
