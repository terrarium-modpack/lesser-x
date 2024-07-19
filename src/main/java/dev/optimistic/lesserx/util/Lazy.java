package dev.optimistic.lesserx.util;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {
  private final Supplier<T> initialSupplier;
  // I don't really care properly implementing synchronization here, this shouldn't cause issues anyway
  private volatile @Nullable T value;

  public Lazy(Supplier<T> initialSupplier) {
    this.initialSupplier = initialSupplier;
  }

  @Override
  public T get() {
    if (value == null) value = initialSupplier.get();
    return value;
  }
}
