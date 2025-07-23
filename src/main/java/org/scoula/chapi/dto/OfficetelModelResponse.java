package org.scoula.chapi.dto;

import java.util.List;
import lombok.Data;

@Data
public class OfficetelModelResponse {
	private int perPage;
	private List<CHOfficetelModelDTO> data;
	private int currentCount;
	private int matchCount;
	private int page;
	private int totalCount;
}