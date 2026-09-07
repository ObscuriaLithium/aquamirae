package dev.obscuria.aquamirae.client.screen;

import dev.obscuria.aquamirae.common.component.LogBookContents;
import dev.obscuria.aquamirae.registry.AquamiraeDataComponents;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class LogBookViewScreen extends Screen {

    private static final ResourceLocation BOOK_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/book.png");

    private static final int IMAGE_WIDTH = 192;
    private static final int IMAGE_HEIGHT = 192;
    private static final int TEXT_WIDTH = 114;
    private static final int TEXT_HEIGHT = 144;
    private static final int LINE_HEIGHT = 9;
    private static final int TEXT_MARGIN_X = 36;
    private static final int TEXT_MARGIN_Y = 16;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_GAP = 18;
    private static final int MIN_THUMB_HEIGHT = 16;

    private final Component content;

    private List<FormattedCharSequence> lines = Collections.emptyList();

    private int leftPos;
    private int topPos;

    private double scrollAmount;
    private int maxScroll;
    private boolean scrollbarGrabbed;
    private double scrollbarDragOffset;

    public LogBookViewScreen(ItemStack stack) {
        this(
                stack.getOrDefault(AquamiraeDataComponents.LOGBOOK_CONTENTS, LogBookContents.EMPTY).topic(),
                stack.getOrDefault(AquamiraeDataComponents.LOGBOOK_CONTENTS, LogBookContents.EMPTY).content());
    }

    public LogBookViewScreen(Component title, Component content) {
        super(title);
        this.content = content;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - IMAGE_WIDTH) / 2;
        this.topPos = (this.height - IMAGE_HEIGHT) / 2;

        this.lines = this.font.split(this.content, TEXT_WIDTH);

        int contentHeight = this.lines.size() * LINE_HEIGHT;
        this.maxScroll = Math.max(0, contentHeight - TEXT_HEIGHT);
        this.scrollAmount = Mth.clamp(this.scrollAmount, 0, this.maxScroll);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.blit(BOOK_LOCATION, this.leftPos, this.topPos, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        this.renderPage(graphics, mouseX, mouseY);
        this.renderScrollbar(graphics);
    }

    private void renderPage(GuiGraphics graphics, int mouseX, int mouseY) {
        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;

        graphics.enableScissor(textX, textY, textX + TEXT_WIDTH, textY + TEXT_HEIGHT);

        for (int i = 0; i < this.lines.size(); i++) {
            int lineY = (int) (textY + i * LINE_HEIGHT - this.scrollAmount);
            if (lineY + LINE_HEIGHT < textY || lineY > textY + TEXT_HEIGHT) {
                continue;
            }
            graphics.drawString(this.font, this.lines.get(i), textX, lineY, 0x000000, false);
        }

        graphics.disableScissor();

        Style style = this.getStyleAt(mouseX, mouseY, textX, textY);
        if (style != null) {
            graphics.renderComponentHoverEffect(this.font, style, mouseX, mouseY);
        }
    }

    private void renderScrollbar(GuiGraphics graphics) {
        if (this.maxScroll <= 0) {
            return;
        }

        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackX = textX + TEXT_WIDTH + SCROLLBAR_GAP;

        graphics.fill(trackX, textY, trackX + SCROLLBAR_WIDTH, textY + TEXT_HEIGHT, 0x33000000);

        int thumbHeight = this.getThumbHeight();
        int thumbY = this.getThumbY(thumbHeight);

        graphics.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH, thumbY + thumbHeight, 0xFF8B8B8B);
        graphics.fill(trackX, thumbY, trackX + SCROLLBAR_WIDTH - 1, thumbY + thumbHeight - 1, 0xFFD0D0D0);
    }

    @Nullable
    private Style getStyleAt(int mouseX, int mouseY, int textX, int textY) {
        if (this.lines.isEmpty()) {
            return null;
        }
        if (mouseX < textX || mouseX >= textX + TEXT_WIDTH) {
            return null;
        }
        if (mouseY < textY || mouseY >= textY + TEXT_HEIGHT) {
            return null;
        }

        int relativeY = (int) (mouseY - textY + this.scrollAmount);
        int line = relativeY / LINE_HEIGHT;
        if (line < 0 || line >= this.lines.size()) {
            return null;
        }

        FormattedCharSequence sequence = this.lines.get(line);
        return this.font.getSplitter().componentStyleAtWidth(sequence, mouseX - textX);
    }

    private boolean isOverScrollbar(double mouseX, double mouseY) {
        if (this.maxScroll <= 0) {
            return false;
        }
        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackX = textX + TEXT_WIDTH + SCROLLBAR_GAP;
        return mouseX >= trackX && mouseX < trackX + SCROLLBAR_WIDTH
                && mouseY >= textY && mouseY < textY + TEXT_HEIGHT;
    }

    private int getThumbHeight() {
        return Math.max(MIN_THUMB_HEIGHT, (TEXT_HEIGHT * TEXT_HEIGHT) / (TEXT_HEIGHT + this.maxScroll));
    }

    private int getThumbY(int thumbHeight) {
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackRange = TEXT_HEIGHT - thumbHeight;
        if (trackRange <= 0 || this.maxScroll <= 0) {
            return textY;
        }
        return textY + (int) (trackRange * (this.scrollAmount / this.maxScroll));
    }

    private void setScrollFromThumbY(double thumbY, int thumbHeight) {
        int textY = this.topPos + TEXT_MARGIN_Y;
        int trackRange = TEXT_HEIGHT - thumbHeight;
        if (trackRange <= 0) {
            this.scrollAmount = 0;
            return;
        }
        double relative = (thumbY - textY) / (double) trackRange;
        this.scrollAmount = Mth.clamp(relative * this.maxScroll, 0, this.maxScroll);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && this.isOverScrollbar(mouseX, mouseY)) {
            this.scrollbarGrabbed = true;

            int thumbHeight = this.getThumbHeight();
            int thumbY = this.getThumbY(thumbHeight);

            if (mouseY >= thumbY && mouseY < thumbY + thumbHeight) {
                this.scrollbarDragOffset = mouseY - thumbY;
            } else {
                this.scrollbarDragOffset = thumbHeight / 2.0;
                this.setScrollFromThumbY(mouseY - this.scrollbarDragOffset, thumbHeight);
            }
            return true;
        }

        int textX = this.leftPos + TEXT_MARGIN_X;
        int textY = this.topPos + TEXT_MARGIN_Y;
        Style style = getStyleAt((int) mouseX, (int) mouseY, textX, textY);
        if (style != null && this.handleComponentClicked(style)) {
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (this.scrollbarGrabbed) {
            int thumbHeight = this.getThumbHeight();
            this.setScrollFromThumbY(mouseY - this.scrollbarDragOffset, thumbHeight);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.scrollbarGrabbed = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (this.maxScroll > 0) {
            this.scrollAmount = Mth.clamp(this.scrollAmount - scrollY * LINE_HEIGHT * 3, 0, this.maxScroll);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.maxScroll > 0) {
            if (keyCode == GLFW.GLFW_KEY_UP) {
                this.scrollAmount = Mth.clamp(this.scrollAmount - LINE_HEIGHT, 0, this.maxScroll);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_DOWN) {
                this.scrollAmount = Mth.clamp(this.scrollAmount + LINE_HEIGHT, 0, this.maxScroll);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_PAGE_UP) {
                this.scrollAmount = Mth.clamp(this.scrollAmount - TEXT_HEIGHT, 0, this.maxScroll);
                return true;
            }
            if (keyCode == GLFW.GLFW_KEY_PAGE_DOWN) {
                this.scrollAmount = Mth.clamp(this.scrollAmount + TEXT_HEIGHT, 0, this.maxScroll);
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}