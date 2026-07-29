package awa.Aether_254.create_regex_filter.client;

import awa.Aether_254.create_regex_filter.RegexFilterData;
import awa.Aether_254.create_regex_filter.network.RegexFilterPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public final class RegexFilterScreen extends Screen {
    private boolean tagMode;
    private EditBox namespace;
    private EditBox path;
    private Button mode;

    public RegexFilterScreen(ItemStack stack) {
        super(Component.literal("Create: Regex Filter"));
        RegexFilterData data = RegexFilterData.read(stack);
        tagMode = data.tagMode();
        namespace = null;
        path = null;
        initialNamespace = data.namespacePattern();
        initialPath = data.pathPattern();
    }

    private final String initialNamespace;
    private final String initialPath;

    @Override
    protected void init() {
        int left = width / 2 - 110;
        int top = height / 2 - 45;
        mode = addRenderableWidget(Button.builder(modeText(), button -> {
            tagMode = !tagMode;
            mode.setMessage(modeText());
        }).bounds(left, top, 42, 20).build());
        namespace = addRenderableWidget(new EditBox(font, left + 47, top, 70, 20,
            Component.literal("mod id regex")));
        path = addRenderableWidget(new EditBox(font, left + 122, top, 98, 20,
            Component.literal("path regex")));
        namespace.setMaxLength(128);
        path.setMaxLength(128);
        namespace.setValue(initialNamespace);
        path.setValue(initialPath);
        addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
            .bounds(width / 2 - 50, top + 42, 100, 20).build());
    }

    private Component modeText() {
        return Component.literal(tagMode ? "Tag #" : "Item");
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 72, 0xFFFFFFFF);
        graphics.drawString(font, ":", width / 2 + 8, height / 2 - 39, 0xFFFFFFFF, true);
    }

    @Override
    public void onClose() {
        if (namespace != null && path != null)
            PacketDistributor.sendToServer(new RegexFilterPayload(tagMode, namespace.getValue(), path.getValue()));
        super.onClose();
    }
}
