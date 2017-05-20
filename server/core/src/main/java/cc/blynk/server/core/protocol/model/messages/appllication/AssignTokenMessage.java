package cc.blynk.server.core.protocol.model.messages.appllication;

import cc.blynk.server.core.protocol.model.messages.StringMessage;

import static cc.blynk.server.core.protocol.enums.Command.ASSIGN_TOKEN;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 2/1/2015.
 */
public class AssignTokenMessage extends StringMessage {

    public AssignTokenMessage(int messageId, String body) {
        super(messageId, ASSIGN_TOKEN, body.length(), body);
    }

    @Override
    public String toString() {
        return "AssignTokenMessage{" + super.toString() + "}";
    }
}
