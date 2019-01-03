package org.school.app.model;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

	private LocalDateTime createdAt = LocalDateTime.now();
}
