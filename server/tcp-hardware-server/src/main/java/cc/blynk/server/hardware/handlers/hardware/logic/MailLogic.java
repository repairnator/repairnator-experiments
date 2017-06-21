package cc.blynk.server.hardware.handlers.hardware.logic;

import cc.blynk.server.core.BlockingIOProcessor;
import cc.blynk.server.core.model.DashBoard;
import cc.blynk.server.core.model.auth.User;
import cc.blynk.server.core.model.widgets.notifications.Mail;
import cc.blynk.server.core.processors.NotificationBase;
import cc.blynk.server.core.protocol.exceptions.IllegalCommandException;
import cc.blynk.server.core.protocol.exceptions.NotAllowedException;
import cc.blynk.server.core.protocol.model.messages.StringMessage;
import cc.blynk.server.core.session.HardwareStateHolder;
import cc.blynk.server.notifications.mail.MailWrapper;
import cc.blynk.utils.validators.BlynkEmailValidator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static cc.blynk.utils.BlynkByteBufUtil.notificationError;
import static cc.blynk.utils.BlynkByteBufUtil.ok;

/**
 * Sends email from received from hardware. Via google smtp server.
 *
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 2/1/2015.
 *
 */
public class MailLogic extends NotificationBase {

    private static final Logger log = LogManager.getLogger(MailLogic.class);

    private final BlockingIOProcessor blockingIOProcessor;
    private final MailWrapper mailWrapper;

    public MailLogic(BlockingIOProcessor blockingIOProcessor, MailWrapper mailWrapper, long notificationQuotaLimit) {
        super(notificationQuotaLimit);
        this.blockingIOProcessor = blockingIOProcessor;
        this.mailWrapper = mailWrapper;
    }

    public void messageReceived(ChannelHandlerContext ctx, HardwareStateHolder state, StringMessage message) {
        final User user = state.user;

        DashBoard dash = user.profile.getDashByIdOrThrow(state.dashId);

        Mail mail = dash.getWidgetByType(Mail.class);

        if (mail == null || !dash.isActive) {
            throw new NotAllowedException("User has no mail widget or active dashboard.");
        }

        if (message.body.isEmpty()) {
            throw new IllegalCommandException("Invalid mail notification body.");
        }

        user.checkDailyEmailLimit();

        String[] bodyParts = message.body.split("\0");

        if (bodyParts.length < 2) {
            throw new IllegalCommandException("Invalid mail notification body.");
        }

        String to;
        String subj;
        String body;

        if (bodyParts.length == 3) {
            to = bodyParts[0];
            subj = bodyParts[1];
            body = bodyParts[2];
        } else {
            to = (mail.to == null || mail.to.isEmpty()) ? user.email : mail.to;
            subj = bodyParts[0];
            body = bodyParts[1];
        }

        checkIfNotificationQuotaLimitIsNotReached();

        //minimal validation for receiver.
        if (BlynkEmailValidator.isNotValidEmail(to)) {
            throw new IllegalCommandException("Invalid mail receiver.");
        }

        log.trace("Sending Mail for user {}, with message : '{}'.", user.email, message.body);
        mail(ctx.channel(), user.email, to, subj, body, message.id);
        user.emailMessages++;
    }

    private void mail(Channel channel, String email, String to, String subj, String body, int msgId) {
        blockingIOProcessor.execute(() -> {
            try {
                mailWrapper.sendHtml(to, subj, body);
                channel.writeAndFlush(ok(msgId), channel.voidPromise());
            } catch (Exception e) {
                log.error("Error sending email from hardware. From user {}, to : {}. Reason : {}",  email, to, e.getMessage());
                channel.writeAndFlush(notificationError(msgId), channel.voidPromise());
            }
        });
    }

}
