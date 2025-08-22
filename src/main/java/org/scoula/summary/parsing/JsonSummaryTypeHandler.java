package org.scoula.summary.parsing;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.scoula.lh.danzi.dto.JsonSummaryDTO;

import java.sql.*;
import java.util.Collections;
import java.util.List;


@MappedTypes({List.class})
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR, JdbcType.LONGNVARCHAR, JdbcType.CLOB, JdbcType.OTHER})
public class JsonSummaryTypeHandler extends BaseTypeHandler<List<JsonSummaryDTO.SummaryItem>> {
    private static final ObjectMapper M = new ObjectMapper();
    private static final JavaType LIST_OF_ITEM =
            M.getTypeFactory().constructCollectionType(List.class, JsonSummaryDTO.SummaryItem.class);

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i,
                                    List<JsonSummaryDTO.SummaryItem> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, M.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Failed to write SummaryItem list JSON", e);
        }
    }

    @Override
    public List<JsonSummaryDTO.SummaryItem> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public List<JsonSummaryDTO.SummaryItem> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public List<JsonSummaryDTO.SummaryItem> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    private List<JsonSummaryDTO.SummaryItem> parse(String json) throws SQLException {
        if (json == null || json.isBlank()) return Collections.emptyList();
        try {
            return M.readValue(json, LIST_OF_ITEM);
        } catch (Exception e) {
            throw new SQLException("Failed to parse SummaryItem list JSON", e);
        }
    }
}
