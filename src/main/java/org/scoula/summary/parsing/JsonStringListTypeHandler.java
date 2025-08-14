package org.scoula.summary.parsing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR) // MySQL JSON 컬럼도 VARCHAR로 다뤄도 OK
public class JsonStringListTypeHandler extends BaseTypeHandler<List<String>> {
    private static final ObjectMapper om = new ObjectMapper();

    @Override public void setNonNullParameter(PreparedStatement ps, int i, List<String> param, JdbcType jdbcType) throws SQLException {
        try { ps.setString(i, om.writeValueAsString(param)); }
        catch (JsonProcessingException e) { throw new SQLException(e); }
    }
    @Override public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String s = rs.getString(columnName);
        if (s == null) return new ArrayList<>();
        try { return om.readValue(s, new TypeReference<List<String>>(){}); }
        catch (IOException e) { throw new SQLException(e); }
    }
    @Override public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String s = rs.getString(columnIndex);
        if (s == null) return new ArrayList<>();
        try { return om.readValue(s, new TypeReference<List<String>>(){}); }
        catch (IOException e) { throw new SQLException(e); }
    }
    @Override public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String s = cs.getString(columnIndex);
        if (s == null) return new ArrayList<>();
        try { return om.readValue(s, new TypeReference<List<String>>(){}); }
        catch (IOException e) { throw new SQLException(e); }
    }
}