/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.base.config;

import java.util.ServiceLoader;

/**
 * Helper class for {@link ServiceLoader}.
 */
public final class ServiceHelper {

  private ServiceHelper() {

  }

  /**
   * @param <S> type of the service.
   * @param serviceLoader the {@link ServiceLoader} that has to be provided from the module declaring the service API
   *        and holds the {@code uses} statement in its {@code module-info}.
   * @return the requested service.
   */
  public static final <S> S singleton(ServiceLoader<S> serviceLoader) {

    return singleton(serviceLoader, true);
  }

  /**
   * @param <S> type of the service.
   * @param unique - {@code true} if an exception is thrown if the service is not unique, {@code false} otherwise (allow
   *        overriding default).
   * @param serviceLoader the {@link ServiceLoader} that has to be provided from the module declaring the service API
   *        and holds the {@code uses} statement in its {@code module-info}.
   * @return the requested service.
   */
  public static final <S> S singleton(ServiceLoader<S> serviceLoader, boolean unique) {

    S service = null;
    for (S currentService : serviceLoader) {
      if (service == null) {
        service = currentService;
      } else {
        if (unique) {
          String type = serviceLoader.toString();
          throw new IllegalStateException(type);
          // throw new DuplicateObjectException(currentService, type, service);
        } else if (!currentService.getClass().getName().startsWith("io.github.mmm.")) {
          service = currentService;
        }
      }
    }
    if (service == null) {
      String type = serviceLoader.toString();
      throw new IllegalStateException(type);
      // throw new ObjectNotFoundException(type);
    }
    return service;
  }

}
