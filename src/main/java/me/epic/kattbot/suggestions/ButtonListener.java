package me.epic.kattbot.suggestions;

import me.epic.kattbot.KattBot;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.jetbrains.annotations.NotNull;

public class ButtonListener extends ListenerAdapter {
    private final KattBot plugin;

    public ButtonListener(KattBot bot) {
        this.plugin = bot;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {

        Role role = event.getGuild().getRoleById(990619550083067924L);
        if (event.getComponentId().equals("approve") && event.getMember().getRoles().contains(role)) {
            TextInput approveInt = TextInput.create("approve-int", "Number", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setMaxLength(5)
                    .setRequired(true)
                    .setLabel("Suggestion number")
                    .build();
            TextInput approveReason = TextInput.create("approve-reason", "Reason", TextInputStyle.SHORT)
                    .setMinLength(3)
                    .setMaxLength(200)
                    .setRequired(true)
                    .setValue("This is a good suggestion")
                    .build();
            Modal modal = Modal.create("approve-modal", "Approve a suggestion")
                    .addActionRows(ActionRow.of(approveInt), ActionRow.of(approveReason))
                    .build();
            event.replyModal(modal).queue();
            return;
        } else if (event.getComponentId().equals("deny") && event.getMember().getRoles().contains(role)) {
            TextInput denyInt = TextInput.create("deny-int", "Number", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setMaxLength(5)
                    .setRequired(true)
                    .setLabel("Suggestion number")
                    .build();
            TextInput denyReason = TextInput.create("deny-reason", "Reason", TextInputStyle.SHORT)
                    .setMinLength(3)
                    .setMaxLength(200)
                    .setRequired(true)
                    .setValue("This is a bad suggestion")
                    .build();
            Modal modal = Modal.create("deny-modal", "Deny a suggestion")
                    .addActionRows(ActionRow.of(denyInt), ActionRow.of(denyReason))
                    .build();
            event.replyModal(modal).queue();
        } else if (event.getComponentId().equals("implemented") && event.getMember().getRoles().contains(role)) {
            TextInput implementInt = TextInput.create("implement-int", "Number", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setMaxLength(5)
                    .setRequired(true)
                    .setLabel("Suggestion number")
                    .build();
            Modal modal = Modal.create("implement-modal", "Mark a suggestion as implemented")
                    .addActionRow(implementInt)
                    .build();
            event.replyModal(modal).queue();
        } else {
            event.reply("You don't have the permission to do that").setEphemeral(true).queue();
        }
    }
}
