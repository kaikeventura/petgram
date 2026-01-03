package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Notification;
import com.kaikeventura.petgram.dto.NotificationResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toNotificationResponse(Notification notification);
}
