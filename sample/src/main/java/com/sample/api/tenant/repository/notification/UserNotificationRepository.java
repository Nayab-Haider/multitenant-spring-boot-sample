package com.sample.api.tenant.repository.notification;

import com.sample.api.tenant.domain.notification.UserNotifications;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserNotificationRepository extends CrudRepository<UserNotifications, Long> {

    List<UserNotifications> findByNotificationRecipient(String notificationRecipient);

    Long countByNotificationRecipientAndNotificationReadStatus(String notificationRecipient, Boolean
            notificationReadStatus);
}
