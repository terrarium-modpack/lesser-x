package dev.optimistic.lesserx;

import dev.optimistic.lesserx.util.Lazy;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LesserX implements ModInitializer {
  private static final Map<Item, Supplier<Item>> ITEM_MIGRATIONS;
  private static List<Runnable> registryRunnables = new ObjectArrayList<>();

  static {
    var itemPair = createItemMigrations(
      new Pair<>(Identifier.of("betternether", "nether_reed"), getVanillaItemSupplier(Items.STICK)),

      new Pair<>(Identifier.of("betternether", "cincinnasite"), getVanillaItemSupplier(Items.RAW_IRON)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_ingot"), getVanillaItemSupplier(Items.IRON_INGOT)),

      new Pair<>(Identifier.of("betternether", "cincinnasite_sword"), getVanillaItemSupplier(Items.IRON_SWORD)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_axe"), getVanillaItemSupplier(Items.IRON_AXE)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_shovel"), getVanillaItemSupplier(Items.IRON_SHOVEL)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_pickaxe"), getVanillaItemSupplier(Items.IRON_PICKAXE)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_hoe"), getVanillaItemSupplier(Items.IRON_HOE)),

      new Pair<>(Identifier.of("betternether", "cincinnasite_helmet"), getVanillaItemSupplier(Items.IRON_HELMET)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_chestplate"), getVanillaItemSupplier(Items.IRON_CHESTPLATE)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_leggings"), getVanillaItemSupplier(Items.IRON_LEGGINGS)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_boots"), getVanillaItemSupplier(Items.IRON_BOOTS)),

      new Pair<>(Identifier.of("betternether", "cincinnasite_sword_diamond"), getVanillaItemSupplier(Items.DIAMOND_SWORD)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_axe_diamond"), getVanillaItemSupplier(Items.DIAMOND_AXE)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_shovel_diamond"), getVanillaItemSupplier(Items.DIAMOND_SHOVEL)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_pickaxe_diamond"), getVanillaItemSupplier(Items.DIAMOND_PICKAXE)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_hoe_diamond"), getVanillaItemSupplier(Items.DIAMOND_HOE)),

      new Pair<>(Identifier.of("betternether", "cincinnasite_helmet_diamond"), getVanillaItemSupplier(Items.DIAMOND_HELMET)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_chestplate_diamond"), getVanillaItemSupplier(Items.DIAMOND_CHESTPLATE)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_leggings_diamond"), getVanillaItemSupplier(Items.DIAMOND_LEGGINGS)),
      new Pair<>(Identifier.of("betternether", "cincinnasite_boots_diamond"), getVanillaItemSupplier(Items.DIAMOND_BOOTS)),

      new Pair<>(Identifier.of("betternether", "nether_ruby"), getItemTechRebornSupplier("ruby_gem")),

      new Pair<>(Identifier.of("betternether", "nether_ruby_sword"), getItemTechRebornSupplier("ruby_sword")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_axe"), getItemTechRebornSupplier("ruby_axe")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_shovel"), getItemTechRebornSupplier("ruby_shovel")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_pickaxe"), getItemTechRebornSupplier("ruby_pickaxe")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_hoe"), getItemTechRebornSupplier("ruby_hoe")),

      new Pair<>(Identifier.of("betternether", "nether_ruby_helmet"), getItemTechRebornSupplier("ruby_helmet")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_chestplate"), getItemTechRebornSupplier("ruby_chestplate")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_leggings"), getItemTechRebornSupplier("ruby_leggings")),
      new Pair<>(Identifier.of("betternether", "nether_ruby_boots"), getItemTechRebornSupplier("ruby_boots")),

      new Pair<>(Identifier.of("betternether", "flaming_ruby_sword"), getItemTechRebornSupplier("ruby_sword")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_axe"), getItemTechRebornSupplier("ruby_axe")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_shovel"), getItemTechRebornSupplier("ruby_shovel")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_pickaxe"), getItemTechRebornSupplier("ruby_pickaxe")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_hoe"), getItemTechRebornSupplier("ruby_hoe")),

      new Pair<>(Identifier.of("betternether", "flaming_ruby_helmet"), getItemTechRebornSupplier("ruby_helmet")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_chestplate"), getItemTechRebornSupplier("ruby_chestplate")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_leggings"), getItemTechRebornSupplier("ruby_leggings")),
      new Pair<>(Identifier.of("betternether", "flaming_ruby_boots"), getItemTechRebornSupplier("ruby_boots"))
    );

    ITEM_MIGRATIONS = itemPair.getLeft();
    registryRunnables.addAll(itemPair.getRight());
  }

  @SafeVarargs
  private static <T> Pair<Map<T, Supplier<T>>, List<Runnable>> createMigrations(Registry<T> registry, Supplier<T> blankSupplier, Pair<Identifier, Supplier<T>>... migrations) {
    Map<T, Supplier<T>> migrationMap = new IdentityHashMap<>(migrations.length);
    List<Runnable> registryRunnables = new ObjectArrayList<>(migrations.length);

    for (Pair<Identifier, Supplier<T>> migration : migrations) {
      T blank = blankSupplier.get();
      registryRunnables.add(() -> Registry.register(registry, migration.getLeft(), blank));
      migrationMap.put(blank, migration.getRight());
    }

    return new Pair<>(Collections.unmodifiableMap(migrationMap), Collections.unmodifiableList(registryRunnables));
  }

  @SafeVarargs
  private static Pair<Map<Item, Supplier<Item>>, List<Runnable>> createItemMigrations(Pair<Identifier, Supplier<Item>>... migrations) {
    return createMigrations(Registries.ITEM, () -> new Item(new Item.Settings()), migrations);
  }

  public static @Nullable Item getItemMigrationFor(Item item) {
    var migration = ITEM_MIGRATIONS.get(item);
    return migration == null ? null : migration.get();
  }

  private static Supplier<Item> getItemTechRebornSupplier(String name) {
    return new Lazy<>(() -> Registries.ITEM.get(Identifier.of("techreborn", name)));
  }

  private static Supplier<Item> getVanillaItemSupplier(Item item) {
    return () -> item;
  }

  @Override
  public void onInitialize() {
    registryRunnables.forEach(Runnable::run);
    registryRunnables = null;
  }
}
