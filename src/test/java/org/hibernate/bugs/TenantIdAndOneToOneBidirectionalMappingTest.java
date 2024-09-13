package org.hibernate.bugs;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;
import java.util.UUID;
import org.hibernate.bugs.config.TenantIdentifierResolver;
import org.hibernate.bugs.model.ChildEntity;
import org.hibernate.bugs.model.RootEntity;
import org.hibernate.cfg.MultiTenancySettings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TenantIdAndOneToOneBidirectionalMappingTest {

  private EntityManagerFactory entityManagerFactory;

  @BeforeEach
  void init() {
    final var tenantIdentifierResolver = new TenantIdentifierResolver();
    entityManagerFactory = Persistence.createEntityManagerFactory("templatePU",
        Map.of(MultiTenancySettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver));
  }

  @AfterEach
  void destroy() {
    entityManagerFactory.close();
  }

  private UUID setupEntities(EntityManager entityManager, boolean withChild) {
    // start transaction
    entityManager.getTransaction().begin();

    // setup entities and persist
    final var childEntity = withChild ? new ChildEntity() : null;
    var rootEntity = new RootEntity(childEntity);
    entityManager.persist(rootEntity);

    // flush and commit transaction
    entityManager.flush();
    entityManager.getTransaction().commit();

    // clear caches
    entityManager.clear();

    // return entity id
    return rootEntity.getId();
  }

  // Entities are auto-discovered, so just add them anywhere on class-path
  // Add your tests, using standard JUnit.
  @Test
  void hhh18617_childExists() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // setup root entity including child entity
    final var rootEntityId = setupEntities(entityManager, true);

    // ensure root entity is found and contains a child
    assertThat(entityManager.find(RootEntity.class, rootEntityId))
        .isNotNull()
        .satisfies(rootEntity -> assertThat(rootEntity.getChildEntity())
            .isNotNull()
        );

    entityManager.close();
  }

  // Entities are auto-discovered, so just add them anywhere on class-path
  // Add your tests, using standard JUnit.
  @Test
  void hhh18617_childDoesNotExist() {
    EntityManager entityManager = entityManagerFactory.createEntityManager();

    // setup root entity without child
    final var rootEntityId = setupEntities(entityManager, false);

    // ensure root entity is found and does not contain a child
    assertThat(entityManager.find(RootEntity.class, rootEntityId))
        .isNotNull()
        .satisfies(rootEntity -> assertThat(rootEntity.getChildEntity())
            .isNull()
        );

    entityManager.close();
  }
}
