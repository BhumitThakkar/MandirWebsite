package org.shreejalarammandir.service.calendar;

import org.shreejalarammandir.model.PujariSeva;
import org.springframework.stereotype.Component;

@Component
public class PujariCalendarDescriptionBuilder {

    public String build(PujariSeva seva) {
        StringBuilder sb = new StringBuilder();
        append(sb, "publicId", seva.getPublicId());
        if (seva.getStatus() != null) {
            append(sb, "status", seva.getStatus().name());
        }
        if (seva.getSelectedSevas() != null) {
            append(sb, "sevas", seva.getSelectedSevas());
        }
        if (seva.getSevaDate() != null) {
            append(sb, "date", seva.getSevaDate().toString());
        }
        if (seva.getStartTime() != null && seva.getEndTime() != null) {
            append(sb, "time", seva.getStartTime() + "–" + seva.getEndTime());
        }
        if (seva.getPublicId() != null) {
            append(sb, "admin", "/admin/pujari-seva/" + seva.getPublicId());
        }
        return sb.toString().trim();
    }

    private static void append(StringBuilder sb, String label, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        if (sb.length() > 0) {
            sb.append('\n');
        }
        sb.append(label).append(": ").append(value);
    }
}
