package com.rntgroup.testingtask.moviecatalog.domain.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ManyToManyRefResultSetExtractor implements ResultSetExtractor<Map<UUID, List<UUID>>> {

    @Override
    public Map<UUID, List<UUID>> extractData(ResultSet rs) throws SQLException {
        var primaryIdToOtherIds = new LinkedHashMap<UUID, List<UUID>>();
        while (rs.next()) {
            var metaData = rs.getMetaData();
            var primaryId = getIdOrNull(rs, metaData.getColumnName(1));
            primaryIdToOtherIds.putIfAbsent(primaryId, new LinkedList<>());
            var otherId = getIdOrNull(rs, metaData.getColumnName(2));
            primaryIdToOtherIds.get(primaryId).add(otherId);
        }
        return primaryIdToOtherIds;
    }

    private UUID getIdOrNull(ResultSet rs, String idName) {
        return Try.of(() -> rs.getString(idName))
                .map(UUID::fromString)
                .getOrNull();
    }
}
