package awa.Aether_254.create_copycat_configuration.client;

import awa.Aether_254.create_copycat_configuration.CopycatSettingsData;
import awa.Aether_254.create_copycat_configuration.network.CopycatSettingsPayload;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;

public final class CopycatSettingsScreen extends Screen {
    private final BlockPos pos;
    private boolean collision;
    private boolean occlusion;
    private int brightness;
    private Button collisionButton;
    private Button occlusionButton;
    private Button brightnessButton;

    public CopycatSettingsScreen(BlockPos pos, CopycatSettingsData data) {
        super(Component.literal("Copycat Configuration"));
        this.pos = pos.immutable();
        collision = data.copycatConfig$collision();
        occlusion = data.copycatConfig$lightOcclusion();
        brightness = data.copycatConfig$brightness();
    }

    @Override
    protected void init() {
        int x = width / 2 - 85;
        int y = height / 2 - 48;
        collisionButton = addRenderableWidget(Button.builder(collisionText(), button -> {
            collision = !collision;
            collisionButton.setMessage(collisionText());
        }).bounds(x, y, 170, 20).build());
        occlusionButton = addRenderableWidget(Button.builder(occlusionText(), button -> {
            occlusion = !occlusion;
            occlusionButton.setMessage(occlusionText());
        }).bounds(x, y + 24, 170, 20).build());
        brightnessButton = addRenderableWidget(Button.builder(brightnessText(), button -> {
            brightness = (brightness + 1) % 16;
            brightnessButton.setMessage(brightnessText());
        }).bounds(x, y + 48, 170, 20).build());
        addRenderableWidget(Button.builder(Component.literal("Done"), button -> onClose())
            .bounds(x + 35, y + 78, 100, 20).build());
    }

    private Component collisionText() {
        return Component.literal("Collision: " + (collision ? "Vanilla" : "Pass through"));
    }

    private Component occlusionText() {
        return Component.literal("Light occlusion: " + (occlusion ? "Vanilla" : "Transparent"));
    }

    private Component brightnessText() {
        return Component.literal("Brightness: " + brightness);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(font, title, width / 2, height / 2 - 76, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        PacketDistributor.sendToServer(new CopycatSettingsPayload(pos, collision, occlusion, brightness));
        super.onClose();
    }
}
