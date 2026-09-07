package dev.obscuria.aquamirae.client.screen.tooltip;

import dev.obscuria.aquamirae.api.common.ability.Ability;
import dev.obscuria.aquamirae.api.common.ability.AbilityHelper;
import dev.obscuria.aquamirae.api.common.ability.AbilitySet;
import dev.obscuria.aquamirae.api.common.ability.AbilitySetTooltip;
import dev.obscuria.aquamirae.api.common.ability.AbilityStyle;
import dev.obscuria.fragmentum.content.world.tooltip.TooltipOptions;
import dev.obscuria.fragmentum.content.world.tooltip.Tooltips;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClientAbilitySetTooltip implements ClientTooltipComponent {

    private final Component title;
    private final List<TierData> bakedTiers;
    private final ResourceLocation texture;

    private int maxNameWidth = -1;

    public static ClientAbilitySetTooltip create(AbilitySetTooltip tooltip) {
        var abilitySet = tooltip.abilitySet().value();
        var player = Objects.requireNonNull(Minecraft.getInstance().player);
        var pieces = AbilityHelper.countPiecesForTooltip(
                tooltip.abilitySet(),
                player,
                tooltip.stack()
        );

        var tiers = abilitySet.tiers();
        var bakedTiers = new ArrayList<TierData>(tiers.size());

        for (var tier : tiers) {
            if (!tier.shouldDisplay(tooltip.stack().getItem()))
                continue;

            bakedTiers.add(TierData.create(
                    player,
                    pieces,
                    tier.requiredPieces(),
                    abilitySet,
                    tier.ability()
            ));
        }

        for (int i = 0; i < bakedTiers.size() - 1; i++) {
            var current = bakedTiers.get(i);
            var next = bakedTiers.get(i + 1);

            current.setProgress(
                    pieces,
                    current.requiredPieces,
                    next.requiredPieces
            );
        }

        var displayName = abilitySet.displayName()
                .copy()
                .withStyle(ChatFormatting.GOLD);

        return new ClientAbilitySetTooltip(
                displayName,
                abilitySet.style(),
                bakedTiers
        );
    }

    private ClientAbilitySetTooltip(
            Component title,
            AbilityStyle style,
            List<TierData> bakedTiers
    ) {
        this.title = title;
        this.bakedTiers = bakedTiers;
        this.texture = style.texture();
    }

    @Override
    public int getHeight() {
        var height = 14;

        for (var tier : bakedTiers)
            height += tier.getHeight();

        return height;
    }

    @Override
    public int getWidth(Font font) {
        var maxName = 0;

        for (var tier : bakedTiers) {
            tier.cacheWidths(font);

            if (tier.nameWidth > maxName)
                maxName = tier.nameWidth;
        }

        maxNameWidth = maxName;

        var width = font.width(title);

        for (var tier : bakedTiers) {
            var tierWidth = maxName + 14 + tier.descriptionWidth;

            if (tierWidth > width)
                width = tierWidth;
        }

        return width;
    }

    @Override
    public void renderImage(
            Font font,
            int x,
            int y,
            GuiGraphics graphics
    ) {
        graphics.drawString(font, title, x, y, 0xFFFFFFFF);

        var offset = 14;

        for (int index = 0; index < bakedTiers.size(); index++) {
            var tier = bakedTiers.get(index);
            var last = index == bakedTiers.size() - 1;
            var nextActive = !last && bakedTiers.get(index + 1).active();

            tier.renderImage(
                    texture,
                    font,
                    x,
                    y + offset,
                    graphics,
                    last,
                    nextActive,
                    maxNameWidth
            );

            offset += tier.getHeight();
        }
    }

    private static final class TierData {

        private final Component name;
        private final List<Component> description;
        private final boolean active;
        private final int requiredPieces;

        private float progress;

        private int nameWidth = -1;
        private int descriptionWidth = -1;

        private TierData(
                Component name,
                List<Component> description,
                boolean active,
                int requiredPieces
        ) {
            this.name = name;
            this.description = description;
            this.active = active;
            this.requiredPieces = requiredPieces;
        }

        public static TierData create(
                LivingEntity entity,
                int pieces,
                int requiredPieces,
                AbilitySet abilitySet,
                Ability ability
        ) {
            var active = pieces >= requiredPieces;
            var style = Style.EMPTY.withColor(
                    active
                            ? ChatFormatting.GRAY
                            : ChatFormatting.DARK_GRAY
            );

            return new TierData(
                    abilitySet.counter().format(requiredPieces, style),
                    Tooltips.process(
                            ability.getFormattedDescription(entity, active),
                            null,
                            TooltipOptions.builder()
                                    .withMaxLineLength(32)
                                    .withDefaultStyle(style)
                                    .build()
                    ),
                    active,
                    requiredPieces
            );
        }

        public void setProgress(
                int pieces,
                int currentRequiredPieces,
                int nextRequiredPieces
        ) {
            if (nextRequiredPieces <= currentRequiredPieces) {
                progress = pieces >= nextRequiredPieces ? 1.0f : 0.0f;
                return;
            }

            progress = Math.clamp(
                    (float) (pieces - currentRequiredPieces)
                            / (nextRequiredPieces - currentRequiredPieces),
                    0.0f,
                    1.0f
            );
        }

        public boolean active() {
            return active;
        }

        public int getHeight() {
            return Math.max(10, description.size() * 10) + 3;
        }

        void cacheWidths(Font font) {
            if (nameWidth >= 0)
                return;

            nameWidth = font.width(name);

            var maxDesc = 0;

            for (var line : description) {
                var width = font.width(line);

                if (width > maxDesc)
                    maxDesc = width;
            }

            descriptionWidth = maxDesc;
        }

        public void renderImage(
                ResourceLocation texture,
                Font font,
                int x,
                int y,
                GuiGraphics graphics,
                boolean last,
                boolean nextActive,
                int maxNameWidth
        ) {
            cacheWidths(font);

            var nameX = x + maxNameWidth - nameWidth;
            graphics.drawString(font, name, nameX, y, 0xFFFFFFFF);

            var iconX = x + maxNameWidth;

            if (!last) {
                var lineHeight = getHeight();
                var lineTop = y + 3.5f;

                graphics.pose().pushPose();
                graphics.pose().translate(iconX - 1, lineTop, 0);
                graphics.pose().scale(1f, lineHeight / 15f, 1f);

                graphics.blit(
                        texture,
                        0,
                        0,
                        0,
                        0,
                        15,
                        15,
                        48,
                        32
                );

                graphics.pose().popPose();

                if (progress > 0.0f) {
                    var filledHeight = lineHeight * progress;

                    graphics.pose().pushPose();
                    graphics.pose().translate(iconX - 1, lineTop, 0);
                    graphics.pose().scale(1f, lineHeight / 15f, 1f);

                    graphics.enableScissor(
                            iconX - 1,
                            Math.round(lineTop),
                            iconX + 14,
                            Math.round(lineTop + filledHeight)
                    );

                    graphics.blit(
                            texture,
                            0,
                            0,
                            0,
                            16,
                            15,
                            15,
                            48,
                            32
                    );

                    graphics.disableScissor();
                    graphics.pose().popPose();
                }
            }

            graphics.blit(
                    texture,
                    iconX - 1,
                    y - 4,
                    active && !nextActive ? 32 : 16,
                    active && nextActive ? 16 : 0,
                    15,
                    15,
                    48,
                    32
            );

            var descX = iconX + 14;
            var offset = 0;

            for (var line : description) {
                graphics.drawString(
                        font,
                        line,
                        descX,
                        y + offset,
                        0xFFFFFFFF
                );

                offset += 10;
            }
        }
    }
}