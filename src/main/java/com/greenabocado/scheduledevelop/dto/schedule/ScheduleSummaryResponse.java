package com.greenabocado.scheduledevelop.dto.schedule;

import java.time.LocalDateTime;

public record ScheduleSummaryResponse(Long id,
                                      String title,
                                      LocalDateTime updatedAt,
                                      String writerName) { }