package awa.Aether_254.create_regex_filter;

import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public final class RegexFilterItem extends FilterItem {
    public RegexFilterItem(Properties properties) {
        super(properties);
    }

    @Override
    public List<Component> makeSummary(ItemStack filter) {
        RegexFilterData data = RegexFilterData.read(filter);
        return List.of(Component.literal((data.tagMode() ? "# " : "") + data.namespacePattern()
            + ":" + data.pathPattern()).withStyle(ChatFormatting.GOLD));
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return null;
    }

    @Override
    public DataComponentType<?> getComponentType() {
        return DataComponents.CUSTOM_DATA;
    }

    @Override
    public FilterItemStack makeStackWrapper(ItemStack filter) {
        return new RegexFilterStack(filter);
    }

    @Override
    public ItemStack[] getFilterItems(ItemStack stack) {
        return new ItemStack[0];
    }

    private static final class RegexFilterStack extends FilterItemStack {
        private final RegexFilterData data;
        private final Pattern namespace;
        private final Pattern path;

        private RegexFilterStack(ItemStack filter) {
            super(filter);
            data = RegexFilterData.read(filter);
            int flags = RegexFilterConfig.get().caseSensitive ? 0 : Pattern.CASE_INSENSITIVE;
            namespace = compile(data.namespacePattern(), flags);
            path = compile(data.pathPattern(), flags);
        }

        @Override
        public boolean test(Level level, ItemStack stack, boolean matchData) {
            if (!RegexFilterConfig.get().enabled || stack.isEmpty() || namespace == null || path == null)
                return false;
            if (data.tagMode())
                return stack.getTags().anyMatch(key -> matches(key.location()));
            return matches(net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()));
        }

        @Override
        public boolean test(Level level, FluidStack stack, boolean matchData) {
            return false;
        }

        private boolean matches(ResourceLocation id) {
            return matches(namespace, id.getNamespace()) && matches(path, id.getPath());
        }

        private static boolean matches(Pattern pattern, String input) {
            return RegexFilterConfig.get().fullMatch ? pattern.matcher(input).matches()
                : pattern.matcher(input).find();
        }

        private static Pattern compile(String expression, int flags) {
            try {
                return Pattern.compile(expression, flags);
            } catch (PatternSyntaxException ignored) {
                return null;
            }
        }
    }
}
