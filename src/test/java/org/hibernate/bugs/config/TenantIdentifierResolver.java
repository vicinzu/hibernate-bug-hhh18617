package org.hibernate.bugs.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {

  @Override
  public String resolveCurrentTenantIdentifier() {
    return "TENANT";
  }

  @Override
  public boolean validateExistingCurrentSessions() {
    return true;
  }
}
