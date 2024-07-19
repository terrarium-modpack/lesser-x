package dev.optimistic.lesserx.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.optimistic.lesserx.LesserX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
  @Final
  @Shadow
  @Mutable
  private Item item;

  @WrapOperation(method = "<init>(Lnet/minecraft/item/ItemConvertible;I)V", at = @At(
    value = "FIELD",
    target = "Lnet/minecraft/item/ItemStack;item:Lnet/minecraft/item/Item;",
    opcode = Opcodes.PUTFIELD)
  )
  private void setItem(ItemStack instance, Item value, Operation<Void> original) {
    Item migration = LesserX.getItemMigrationFor(value);
    original.call(instance, migration == null ? value : migration);
  }
}
